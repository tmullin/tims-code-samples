<?php
/**
 * Associative array that will be converted to json.
 * 
 * The 'status' key will be 'success' or 'failure' indicating
 * whether the parse was successful.
 * 
 * Upon success, the 'value' key will contain the numeric result
 * of parsing and evaluating the equation.
 * 
 * Upon failure, the 'message' key will indicate what the problem
 * was. If the 'charIndex' key is set, that indicates the specific
 * character where the problem was encountered.
 */ 
$result = array();

if (!empty($_REQUEST['expression'])) {
	require 'expression_parser.php';
	$parser = new ExpressionParser();
	
	try {
		$value = $parser->parse($_REQUEST['expression']);
		$result['status'] = 'success';
		$result['value']  = $value;
	} catch (Exception $e) {
		$result['status']  = 'failure';
		$result['message'] = $e->getMessage();
		
		if (isset($e->charIndex)) {
			$result['charIndex'] = $e->charIndex;
		}
	}
} else {
	$result['status']  = 'failure';
	$result['message'] = 'no expression given'; 
}

header('Content-type: application/json');
die(json_encode($result, JSON_FORCE_OBJECT));
?>