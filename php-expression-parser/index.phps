<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<meta name="author" content="Tim Mullin" />
	<title>PHP Expression Parser</title>
	<link rel="stylesheet" type="text/css" href="style.css" />
	<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/redmond/jquery-ui.css" />
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
	<script type="text/javascript" src="jquery.cookie.js"></script>
	<script type="text/javascript" src="expression_parser.js"></script>
</head>

<body>
	<div id="container">
		<ul>
			<li><a href="#demo">Demo</a></li>
			<li><a href="#source">Source</a></li>
		</ul>
		
		<div id="demo">
			<h1>Expression Parser</h1>
			<form>
				<input type="text" name="expression" placeholder="Enter an expression" />
				<br>
				<input type="submit" value="Parse" />
			</form>
			<p id="result" class="ui-state-highlight ui-corner-all">
				The parser can handle +, -, *, /, and parentheses.
			</p>
		</div>
		
		<div id="source">
			<ul>
				<li><a href="index.phps">index.php</a></li>
				<li><a href="ajax.phps">ajax.php</a></li>
				<li><a href="expression_parser.phps">expression_parser.php</a></li>
				<li><a href="expression_parser.js">expression_parser.js</a></li>
				<li><a href="style.css">style.css</a></li>
			</ul>
		</div>
	</div>
</body>
</html>