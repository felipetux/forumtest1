package cl.forum.api.ef.operations.pojo;

public class OperationStatusValidationEntity {
	
	boolean responseValidationStatus;
	String messageValidationStatus;
	
	public boolean isResponseValidationStatus() {
		return responseValidationStatus;
	}
	public void setResponseValidationStatus(boolean responseValidationStatus) {
		this.responseValidationStatus = responseValidationStatus;
	}
	public String getMessageValidationStatus() {
		return messageValidationStatus;
	}
	public void setMessageValidationStatus(String messageValidationStatus) {
		this.messageValidationStatus = messageValidationStatus;
	}

}
