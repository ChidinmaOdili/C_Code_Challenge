package com.example.codingchallenge.helper;

public enum Messages {
	
	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
	SUCCESS("Operation was a Success"),
	INTERNAL_SERVER_ERROR("An Error has Occurred"),
	VALIDATION_ERRORS("Validation Errors"),
	ERROR_WRITING_JSON_RESPONSE("Error writing JSON output"),
	MEDIA_TYPE_NOT_SUPPORTED("Media type is not supported. Supported media types are "),
	METHOD_NOT_SUPPORTED("Method not Supported, Supported Method are:"),
	MALFORMED_JSON_REQUEST("Malformed JSON Request"),
	NOT_EQUAL("Record is not Equal");

    private String message;
	Messages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}