package cl.forum.api.ef.operations.validate;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.pojo.OperationPrepagoEntity;
import cl.forum.api.ef.operations.util.ValidateUtil;

public class ValidateUpdatePrepago {

	private static final String EXPRECOM     = "[^A-Za-z0-9]";
	private static final String NUMBER       = "[^0-9]";
	private static final String CONTRACT     = "pkNumeroContrato";
	private static final String CODCAT       = "nCodCategoria";
	private static final String DESCAT       = "cDescCategoria";
	private static final String CODEST       = "nCodEstadoOperacion";
	private static final String DESEST       = "cDescEstadoOperacion";
	private static final String FECPAG       = "dFechaPago";
	private static final String DIAS         = "nDias";
	private static final String PREPAGO      = "nMontoDescuentoPrepago";
	private static final String CONTPREPAGO  = "cContratoDescuentoPrepago";
	private static final String USER         = "cUsuario";
	private static final String NOMUSER      = "cNombreUsuario";
	private static final String ERROR        = "cErrorNumber";
	private static final String DESERROR     = "cErrorMessage";
	
	public static Map<String, Object> validaData(OperationPrepagoEntity body)
			throws  ResponseException {

		Map<String, Object> params = new HashMap<>();
		ValidateUtil.validaBody(new ObjectMapper().convertValue(body, ObjectNode.class));
		// Campos Requeridos
		ValidateUtil.validaData(CONTRACT, body.getContractNumber(), 8, EXPRECOM, true);
		ValidateUtil.validaData(CONTPREPAGO, body.getContractPrepago(), 8, EXPRECOM, true);
		ValidateUtil.validaData(PREPAGO, body.getAmountPrepago().toString(), 15, NUMBER, true);
		ValidateUtil.validaData(USER, body.getUsuarioModificacion(), 8, EXPRECOM, true);
		
		// Put
		params.put(CONTRACT, body.getContractNumber());
		params.put(CODCAT, null);
		params.put(DESCAT, null);
		params.put(CODEST, null);
		params.put(DESEST, null);
		params.put(FECPAG, null);
		params.put(DIAS, null);
		params.put(PREPAGO, body.getAmountPrepago());
		params.put(CONTPREPAGO, body.getContractPrepago());
		params.put("crMarcaDescarte", null);
		params.put("cMotivoDescarte", null);
		params.put(USER, body.getUsuarioModificacion());
		params.put(NOMUSER, body.getNombreUsuarioModificacion());	
		params.put(DESERROR, null);
		params.put(ERROR, null);

		return params;
	}

	private ValidateUpdatePrepago() {

	}
}
