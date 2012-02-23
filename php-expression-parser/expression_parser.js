$(function() {
	$('#container').tabs({
		cookie: {expires: 1}
	});
	
	// make the submit button look nicer
	$('input:submit').button();
	
	var
		$textField = $('input[type=text]'),
		$result    = $('#result');
	
	$('form').submit(function(event) {
		var expression = $.trim($textField.val());
		
		// no point in sending an empty expression
		if ('' == expression) {
			$textField.addClass('error');
			$result.addClass('ui-state-error');
			$result.text('Please enter an expression first.');
			return false;
		}
		
		$textField.removeClass('error');
		$result.removeClass('ui-state-error');
		
		$.post('ajax.php',
			{expression: expression},
			function(data, textStatus, jqXHR) {
				if ('success' == data.status) {
					$textField.removeClass('error');
					$result.removeClass('ui-state-error');
					$result.html(
						'Result: <span class="value">' + 
						data.value + '</span>');
				} else {
					$textField.addClass('error');
					
					var html =
						'Error: <span class="error">' +
						data.message + '</span>';
					
					if ('undefined' !== typeof(data.charIndex)) {
						html += ' at character ' + (data.charIndex + 1);
					}
					
					html += '.';
					
					$result.addClass('ui-state-error');
					$result.html(html);
				}
			}
		);
		
		
		return false; // prevent form from submitting
	});
});
