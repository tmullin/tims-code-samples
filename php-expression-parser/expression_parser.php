<?php
/**
 * Represents an error while trying to evaluate the
 * parsed string, such as dividing by zero. It includes a 
 * field for the character index indicating the operator that
 * was being executed when the error occured.
 */
class ArithmeticException extends Exception {
	public $charIndex;
	
	public function __construct(
			$message, $charIndex, $code = 0, $previous = null) {
		parent::__construct($message, $code, $previous);
		$this->charIndex = $charIndex;
	}
}

/**
 * Represents a syntax error in the string
 * given to the parser. It includes a field for the character index
 * where the error occured.
 */
class ParserException extends Exception {
	public $charIndex;
	
	public function __construct(
			$message, $charIndex, $code = 0, $previous = null) {
		parent::__construct($message, $code, $previous);
		$this->charIndex = $charIndex;
	}
}

/**
 * Represents a token read from the source string.
 */
class Token {
	/**
	 * The index in the source string where this token started
	 */
	public $startIndex;
	
	/**
	 * The substring from the source string this token represents,
	 * i.e. the content of the token
	 */
	public $string;
	
	public function __construct($startIndex = -1, $string = '') {
		$this->startIndex = $startIndex;
		$this->string     = $string;
	}
	
	/**
	 * @return int The number of characters this token contains
	 */
	public function length() {
		return strlen($this->string);
	}
	
	public function isOperator() {
		return ExpressionParser::isOperator($this->string);
	}
	
	// should only be called after checking isOperator()
	public function operatorPrecedence() {
		return ExpressionParser::operatorPrecedence($this->string);
	}
	
	public function isOpenParen() {
		return '(' == $this->string;
	}
	
	public function isCloseParen() {
		return ')' == $this->string;
	}
	
	public function isNumber() {
		return ExpressionParser::isNumber($this->string);
	}
}

/**
 * This class parses a string representing a mathematical expression
 * via my implementation of Dijkstra's shunting-yard algorithm and
 * then evaluates the resultant Reverse Polish Notation representation
 * into the final value.
 */
class ExpressionParser {
	const OPERATORS = "*/+-";
	
	/**
	 * Determines if a character represents an operator.
	 * 
	 * @param string $char A one-character string to check
	 * @return boolean True if $char is an operator, false otherwise
	 */
	public static function isOperator($char) {
		return false !== strpos(ExpressionParser::OPERATORS, $char);
	}
	
	/**
	 * Determines the order of operation for operators.
	 * 
	 * @param string $operator The operator to check
	 * @return int A relative value that can be used to compare the
	 * 		given operator's precedence to another's
	 */
	public static function operatorPrecedence($operator) {
		switch ($operator) {
			case '*': case '/':
				return 2;
				
			case '+': case '-':
				return 1;
		}
		
		throw new InvalidArgumentException('unknown operator: ' . $operator);
	}
	
	/**
	 * Determines if a string represents a number.
	 * 
	 * @param string $string The string to check
	 * @return boolean True if $string is a number, false otherwise
	 */
	public static function isNumber($string) {
		// 123 or 123.456 or .123
		return preg_match('/^(\d+|(\d*\.\d+))?$/', $string);
	}
	
	
	/**
	 * The complete input string
	 * 
	 * @var string
	 */
	private $string;
	
	/**
	 * The index within the input string of the current character
	 * being processed
	 * 
	 * @var int
	 */
	private $currentCharIndex;
	
	/**
	 * The Token object to which characters will be appended so long
	 * as they are part of that token type
	 * 
	 * @var Token
	 */
	private $currentToken;
	
	/**
	 * The previously parsed Token object
	 * 
	 * @var Token
	 */
	private $lastToken;
	
	/**
	 * The stack to hold pending operators during the parsing phase
	 * 
	 * @var SplStack
	 */
	private $operatorStack;
	
	/**
	 * The queue that contains the parsed input string in Reverse
	 * Polish Notation upon a successful parse
	 * 
	 * @var SplQueue
	 */
	private $outputQueue;
	
	/**
	 * Contains the final value of the input string upon a successful
	 * parse and successful evaluation
	 * 
	 * @var number
	 */
	private $value;
	
	public function __construct() {
		
	}
	
	/**
	 * Resets the internal state of the parser in preparation for
	 * parsing a new string.
	 */
	private function reset() {
		$this->currentCharIndex = null;
		$this->currentToken     = null;
		$this->lastToken        = null;
		$this->operatorStack    = new SplStack();
		$this->outputQueue      = new SplQueue();
		$this->value            = null;
	}
	
	/**
	 * Attempts to parse and evaluate a mathematical expression.
	 * 
	 * @param string $string The string to parse
	 * @return number The evaluated value
	 * @throws ParserException If the string contains a lexical error
	 * @throws ArithmeticException If the string was parsed successfully
	 * 		but there was an error while evaluating it, such as division
	 * 		by zero
	 */
	public function parse($string) {
		if (null === $string) {
			throw new InvalidArgumentException('$string cannot be null');
		}
		
		if (!is_string($string)) {
			throw new InvalidArgumentException('$string must be a string');
		}
		
		$string = trim($string);
		
		if ('' == $string) {
			throw new InvalidArgumentException('$string must not be empty');
		}
		
		$this->reset();
		$this->string = $string;
		
		$this->tokenize();
		$this->evaluate();
		
		return $this->value;
	}
	
	/**
	 * @return string A string representing the parsed value in
	 * 		Reverse Polish Notation
	 */
	public function toRPN() {
		$str = '';
		
		foreach ($this->outputQueue as $token) {
			$str .= $token->string . ' ';
		}
		
		return $str;
	}
	
	/**
	 * @return number The value that the input string evaluated to
	 */
	public function value() {
		if (null === $this->value) {
			throw new RuntimeException(
				'value() must only be called after a successful parse');
		}
		
		return $this->value;
	}
	
	
	/**
	 * Throws a ParserException with the given message and the
	 * given character index.
	 * 
	 * @param string $message A string indicating the error
	 * @param int $charIndex The index in the input string where
	 * 		the error occured, may be null to use the parser's
	 * 		internal state instead or -1 to ignore
	 */
	private function parseError($message, $charIndex = null) {
		if (null === $charIndex) {
			$charIndex = $this->currentCharIndex;
		}
		
		throw new ParserException($message, $charIndex);
	}
	
	/**
	 * Throws a ParserException with the given message and the
	 * given character index.
	 * 
	 * @param string $message A string indicating the error
	 * @param int $charIndex The index in the input string where
	 * 		the error occured, may be -1 to ignore
	 */
	private function arithmeticError($message, $charIndex = -1) {
		throw new ArithmeticException($message, $charIndex);
	}
	
	
	/**
	 * Parses the input string for individual tokens.
	 */
	private function tokenize() {
		$chars = str_split($this->string);
		$charCount = count($chars);
		$this->currentToken = new Token(0);
		
		for ($i = 0; $i < $charCount; $i++) {
			$this->currentCharIndex = $i;
			$char = $chars[$i];
			
			// Whitespace is always a token delimiter.
			// If the previous character was also whitespace,
			// currentToken will be blank and will be handled
			// in handleToken().
			if (preg_match('/\s/', $char)) {
				$this->handleToken();
				continue;
			}
			
			// Check for tokens that are only ever one character in length.
			if (ExpressionParser::isOperator($char) ||
				'(' == $char || ')' == $char) {
				
				// Handle the previous (numeric) token if necessary.
				$this->handleToken();
				
				$this->currentToken->startIndex = $i;
				$this->currentToken->string = $char;
				$this->handleToken();
				continue;
			}
			
			if ('' == $this->currentToken->string ||
				preg_match('/^\d+$/', $this->currentToken->string)) {
				// If the token is empty or only contains digits 
				// we can expect either a digit or a decimal point.
				if (preg_match('/[\d.]/', $char)) {
					$this->currentToken->string .= $char;
				} else {
					$this->parseError('unknown character');
				}
			} elseif (preg_match('/\.\d*$/', $this->currentToken->string)) {
				// If the token ends with a decimal point and possibly
				// digits we can expect digits for the fractional part
				// of the number.
				if (preg_match('/\d/', $char)) {
					$this->currentToken->string .= $char;
				} else {
					$this->parseError('unknown character');
				}
			} else {
				$this->parseError('unknown character');
			}
		}
		
		// One last call to handleToken() in case the string ended
		// with a number.
		$this->handleToken();
		
		// The last token can only be a number or close paren.
		if (null === $this->lastToken ||
			!($this->lastToken->isCloseParen() ||
			  $this->lastToken->isNumber())
		) {
			$this->parseError(
				'unexpected token',
				$this->lastToken->startIndex);
		}
		
		// Now handle any remaining tokens on the operatorStack.
		while (!$this->operatorStack->isEmpty()) {
			$popped = $this->operatorStack->pop();
			
			if ($popped->isOpenParen() || $popped->isCloseParen()) {
				$this->parseError('mismatched paren', $popped->startIndex);
			}
			
			$this->outputQueue->push($popped);
		}
	}
	
	private function handleToken() {
		// Token could be empty when handling whitespace.
		if ('' == $this->currentToken->string) {
			// adjust the startIndex since we just consumed a whitespace char
			$this->currentToken->startIndex = $this->currentCharIndex;
			return;
		}
		
		if (preg_match('/\.$/', $this->currentToken->string)) {
			// Didn't have a fractional part or just a plain decimal
			// in the input string
			$this->parseError(
				'misplaced decimal',
				$this->currentToken->startIndex +
				$this->currentToken->length() - 1);
		} elseif ($this->currentToken->isNumber()) {
			// Can't have two numbers in a row
			if (null !== $this->lastToken &&
				$this->lastToken->isNumber()) {
				
				$this->parseError(
					'unexpected number',
					$this->currentToken->startIndex);
			}
			
			$this->outputQueue->enqueue($this->currentToken);
		} elseif ($this->currentToken->isOperator()) {
			// Can only have operator after number or close paren
			if (null === $this->lastToken ||
				!($this->lastToken->isCloseParen() ||
				  $this->lastToken->isNumber())
			) {	
				$this->parseError(
					'unexpected operator',
					$this->currentToken->startIndex);
			}
			
			while (
				// while the top element is an operator
				!$this->operatorStack->isEmpty() &&
				$this->operatorStack->top()->isOperator() &&
				
				$this->currentToken->operatorPrecedence() <=
				$this->operatorStack->top()->operatorPrecedence()
			) {
				$this->outputQueue->enqueue(
					$this->operatorStack->pop());
			}
			
			$this->operatorStack->push($this->currentToken);
		} elseif ($this->currentToken->isOpenParen()) {
			// open paren can only be at the beginning, after an operator
			// or after another open paren
			if (null !== $this->lastToken &&
				!($this->lastToken->isOpenParen() ||
				  $this->lastToken->isOperator())
			) {
				$this->parseError(
					'unexpected open paren',
					$this->currentToken->startIndex);
			}
			
			$this->operatorStack->push($this->currentToken);
		} elseif ($this->currentToken->isCloseParen()) {
			// close paren can only be after a number or after another
			// close paren
			if (null === $this->lastToken ||
				!($this->lastToken->isCloseParen() ||
				  $this->lastToken->isNumber())
			) {
				$this->parseError(
					'unexpected close paren',
					$this->currentToken->startIndex);
			}
			
			while (
				// while the top element is not an open paren
				!$this->operatorStack->isEmpty() &&
				!$this->operatorStack->top()->isOpenParen()
			) {
				$this->outputQueue->enqueue(
					$this->operatorStack->pop());
			}
			
			if ($this->operatorStack->isEmpty()) {
				$this->parseError(
					'mismatched paren',
					$this->currentToken->startIndex);
			}
			
			// Top element must be an open paren so discard it
			$this->operatorStack->pop();
		}
		
		// Reset in order to parse next token.
		$this->lastToken = $this->currentToken;
		$this->currentToken = new Token($this->currentCharIndex + 1);
	}

	/**
	 * Calculates the value of the input string.
	 */
	private function evaluate() {
		$operandStack = new SplStack();
		
		// outputQueue only contains numbers and operators already
		// in the correct order for evaluation.
		foreach ($this->outputQueue as $next) {
			if ($next->isOperator()) {
				if ($operandStack->count() < 2) {
					throw new RuntimeException(
						'assert: too many operands, ' .
						'parser should have caught this');
				}
				
				$right = $operandStack->pop();				
				$left  = $operandStack->pop();
				$value = 0;
				
				// As we pop Tokens and evaluate them, we will be pushing
				// plain numbers back onto the stack, which is why these
				// checks are necessary.
				if (is_a($right, 'Token')) {
					$right = $right->string;
				}
				
				if (is_a($left, 'Token')) {
					$left = $left->string;
				}
				
				switch ($next->string) {
					case '*':
						$value = $left * $right;
						break;
						
					case '/':
						if (0 == $right) {
							$this->arithmeticError(
								'division by 0', $next->startIndex);
						}
						
						$value = $left / $right;
						break;
						
					case '+':
						$value = $left + $right;
						break;
						
					case '-':
						$value = $left - $right;
						break;
						
					default:
						throw new InvalidArgumentException(
							'unknown operator: ' . $next->string);
				}
				
				$operandStack->push($value);
			} else {
				$operandStack->push($next);
			}
		}
		
		// A single value on the stack is the final answer.
		if (1 == $operandStack->count()) {
			$this->value = $operandStack->pop();
			
			// This could happen if the input string only consisted
			// of a single number.
			if (is_a($this->value, 'Token')) {
				$this->value = (float) $this->value->string;
			}
			
			return;
		}
		
		if ($operandStack->count() > 1) {
			throw new RuntimeException(
				'assert: too many operands, parser should have caught this');
		}

		throw new RuntimeException(
			'assert: operandStack should not be empty');
	}
}
?>