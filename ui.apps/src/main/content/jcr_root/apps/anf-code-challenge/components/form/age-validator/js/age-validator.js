$(document).ready(function() {

	$("button[name='submitForm']").click(function(event) {

        //Fetching Age from input field
		let currentAge = $("input[name=currentAge]").val();

        //Ajax call for servlet for validation
		$.ajax({
			type: 'GET',
			url: '/bin/saveUserDetails',
			data: {
				"inputAge": currentAge
			},
			contentType: 'text/plain',
			success: function(resp) {
				if (resp == "false") {
				    //Showing warning for non eligible age
					event.preventDefault();
					alert("You are not eligible");
				} else {
				    //Redirecting to Submission Page
					window.location.href = "/content/anf-code-challenge/us/en/thank-you-for-submission.html";
				}
			}
		});
	});
});