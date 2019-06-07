package cl.forum.api.ef.operations.validate;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.pojo.OperationStatusEntity;
import cl.forum.api.ef.operations.util.DateTimeUtil;
import cl.forum.api.ef.operations.util.ValidateUtil;

public class ValidateUpdatePayment {

	private static final String EXPRECOM = "[^A-Za-z0-9]";
	private static final String NUMBER   = "[^0-9]";
	private static final String LETTERS  = "[^A-Za-z]";
	private static final String CONTRACT = "pkNumeroContrato";
	private static final String CODCAT   = "nCodCategoria";
	private static final String DESCAT   = "cDescCategoria";
	private static final String PKCODEST = "pkCodEstadoOperacion";
	private static final String CODEST   = "nCodEstadoOperacion";
	private static final String DESEST   = "cDescEstadoOperacion";
	private static final String DIAS     = "nDias";
	private static final String FECPAG   = "dFechaPago";
	private static final String FECVEN   = "dFechaVencimiento";
	private static final String PREPAGO  = "nMontoDescuentoPrepago";
	private static final String TOTAL    = "nMontoTotal";
	private static final String USERMOD  = "cUsuarioModificacion";
	private static final String NOMUSERPROC  = "cNombreUsuarioProceso";
	private static final String USERPROC = "cUsuarioProceso";
	private static final String USER     = "cUsuario";
	private static final String NOMUSER  = "cNombreUsuario";
	private static final String ERROR    = "cErrorNumber";
	private static final String DESERROR = "cErrorMessage";
	private static final String CONTPREPAGO  = "cContratoDescuentoPrepago";

	public static Map<String, Object> validaData(OperationStatusEntity body)
			throws  ResponseException {

		Map<String, Object> params = new HashMap<>();
		ValidateUtil.validaBody(new ObjectMapper().convertValue(body, ObjectNode.class));
		// Campos Requeridos
		ValidateUtil.validaData(CONTRACT, body.getContractNumber(), 8, EXPRECOM, true);
		ValidateUtil.validaData(DIAS, body.getDias(), 3, NUMBER, true);
		ValidateUtil.validaData(CODEST, body.getCodigoEstadoOperacion(), 10, NUMBER, true);
		ValidateUtil.validaData(DESEST, body.getDescripcionOperacion(), 30, LETTERS, true);
		ValidateUtil.validaData(USERMOD, body.getUsuarioModificacion(), 8, EXPRECOM, true);
		
		// Put
		params.put(CONTRACT, body.getContractNumber());
		params.put(CODCAT, null);
		params.put(DESCAT, null);
		params.put(PKCODEST, body.getCodigoEstadoOperacion());
		params.put(CODEST, body.getCodigoEstadoOperacion());
		params.put(DESEST, body.getDescripcionOperacion());
		params.put(FECPAG, DateTimeUtil.getDay());
		params.put(FECVEN, DateTimeUtil.calculaVencimiento(Integer.parseInt(body.getDias())));
		params.put(DIAS, null);
		params.put(PREPAGO, null);
		params.put(TOTAL, null);
		params.put(CONTPREPAGO, null);
		params.put("crMarcaDescarte", null);
		params.put("cMotivoDescarte", null);
		params.put(USERPROC, body.getUsuarioModificacion());
		params.put(USERMOD, body.getUsuarioModificacion());
		params.put(USER, body.getUsuarioModificacion());
		params.put(NOMUSERPROC, body.getNombreUsuarioModificacion());	
		params.put(NOMUSER, body.getNombreUsuarioModificacion());	
		params.put(DESERROR, null);
		params.put(ERROR, null);

		return params;
	}

	private ValidateUpdatePayment() {

	}
}
