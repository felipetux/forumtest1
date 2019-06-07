package cl.forum.api.ef.operations.validate;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.pojo.OperationDocuEntity;
import cl.forum.api.ef.operations.util.ValidateUtil;

public class ValidateUpdateDocu {

	private static final String EXPRECOM = "[^A-Za-z0-9]";
	private static final String LETTERS  = "[^A-Za-z]";
	private static final String NUMBER   = "[^0-9]";
	private static final String CONTRACT = "pkNumeroContrato";
	private static final String CODDOCU  = "pkCodTipoDocumento";
	private static final String RECIBIDO = "crRecepcionadoUsuario";
	private static final String RECISTOR = "crRecepcionadoStorBox";
	private static final String INVALIDO = "crInvalido";
	private static final String USERMOD  = "cUsuarioModificacion";
	private static final String ERROR    = "cErrorNumber";
	private static final String DESERROR = "cErrorMessage";

	public static Map<String, Object> validaData(OperationDocuEntity body)
			throws  ResponseException {

		Map<String, Object> params = new HashMap<>();
		ValidateUtil.validaBody(new ObjectMapper().convertValue(body, ObjectNode.class));
		// Campos Requeridos
		ValidateUtil.validaData(CONTRACT, body.getContractNumber(), 8, EXPRECOM, true);
		ValidateUtil.validaData(CODDOCU, body.getDocumentType(), 10, NUMBER, true);
		ValidateUtil.validaData(RECIBIDO, body.getRecepcionadoUsuario(), 2, LETTERS, true);
		ValidateUtil.validaData(INVALIDO, body.getDocInvalido(), 2, LETTERS, true);
		ValidateUtil.validaData(USERMOD, body.getUsuarioModificacion(), 8, EXPRECOM, true);
		//Valida y obtiene codigo de estado
		
		// Put
		params.put(CONTRACT, body.getContractNumber());
		params.put(CODDOCU, body.getDocumentType());
		params.put(RECIBIDO, body.getRecepcionadoUsuario());
		params.put(RECISTOR, null);
		params.put(INVALIDO, body.getDocInvalido());
		params.put(USERMOD, body.getUsuarioModificacion());
		params.put(DESERROR, null);
		params.put(ERROR, null);

		return params;
	}

	private ValidateUpdateDocu() {

	}
	
}
