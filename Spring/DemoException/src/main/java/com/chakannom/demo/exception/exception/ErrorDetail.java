package com.chakannom.demo.exception.exception;



public class ErrorDetail {
	private Error error;
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
	
	public class Error {
		private int status;
		private String message;
		private String requestUrl;
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getRequestUrl() {
			return requestUrl;
		}
		public void setRequestUrl(String requestUrl) {
			this.requestUrl = requestUrl;
		}	
	}
}
