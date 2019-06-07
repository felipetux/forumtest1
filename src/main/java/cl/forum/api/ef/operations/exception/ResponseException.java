package cl.forum.api.ef.operations.exception;

public class ResponseException extends Exception {

	private static final long serialVersionUID = 1L;
	protected final StatusResponseEnum statusResponseEnum;
	protected final String field;
	protected final boolean handled;
	protected final boolean result;

	/**
	 * ResponseException Respuesta generica cuando se produce una excepcion
	 * 
	 * @autor Arquitectura
	 * @param message
	 * @param StatusResponseEnum
	 * @return field
	 */
	public ResponseException(String message, StatusResponseEnum responseStatusEnum, String field) {
		super(message);
		this.statusResponseEnum = responseStatusEnum;
		this.handled = false;
		this.field = field;
		this.result = false;
	}

	/**
	 * ResponseException Respuesta controlada generica cuando se produce una
	 * excepcion
	 * 
	 * @autor Arquitectura
	 * @param message
	 * @param StatusResponseEnum
	 * @param handled
	 * @return field
	 */
	public ResponseException(String message, StatusResponseEnum responseStatusEnum, boolean handled, String field) {
		super(message);
		this.statusResponseEnum = responseStatusEnum;
		this.handled = handled;
		this.field = field;
		this.result = false;
	}

	public StatusResponseEnum getStatusResponseEnum() {
		return statusResponseEnum;
	}

	public String getField() {
		return field;
	}

	public boolean isHandled() {
		return handled;
	}

	public boolean isResult() {
		return result;
	}
}