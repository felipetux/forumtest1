package cl.forum.api.ef.operations.exception;

public enum StatusResponseEnum {

	OK("200"), NO_CONTENT("204"), BAD_REQUEST("400"), UNAUTHORIZED("401"), FORBIDDEN("403"), NOT_FOUND(
			"404"), METHOD_NOT_ALLOWED("405"), REQUEST_TIMEOUT(
					"408"), PRECONDITION_FAILED("412"), INTERNAL_SERVER_ERROR("500"), SERVICE_UNAVAILABLE("503");

	String code;

	public static final String RESPONSE_STATUS = "status";
	public static final String RESPONSE_STATUS_CODE = "code";
	public static final String RESPONSE_STATUS_MESSAGE = "message";
	public static final String RESPONSE_RESULT = "result";
	public static final String RESPONSE_DATA = "data";
	public static final String RESPONSE_ERROR = "error";
	public static final String RESPONSE_ERROR_FIELD = "field";
	public static final String RESPONSE_ERROR_MESSAGE = "message";

	public static StatusResponseEnum getMessageByCode(String code) {
		try {
			return StatusResponseEnum.valueOf(code);
		} catch (Exception ignored) {
			return null;
		}
	}

	StatusResponseEnum(String code) {
		this.code = code;
	}

	public String getStatusCode() {
		return code;
	}
}