package cl.forum.api.ef.operations.validate;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.pojo.OperationStatusEntity;
import cl.forum.api.ef.operations.util.ValidateUtil;

public class ValidateUpdateStatus {

	private static final String EXPRECOM     = "[^A-Za-z0-9]";
	private static final String CONTRACT     = "pkNumeroContrato";
	private static final String CODCAT       = "nCodCategoria";
	private static final String DESCAT       = "cDescCategoria";
	private static final String PKCODEST     = "pkCodEstadoOperacion";
	private static final String CODEST       = "nCodEstadoOperacion";
	private static final String DESEST       = "cDescEstadoOperacion";
	private static final String FECPAG       = "dFechaPago";
	private static final String FECVEN       = "dFechaVencimiento";
	private static final String DIAS         = "nDias";
	private static final String PREPAG       = "nMontoDescuentoPrepago";
	private static final String TOT          = "nMontoTotal";
	private static final String USRMOD       = "cUsuarioModificacion";
	private static final String NOMUSRPRO    = "cNombreUsuarioProceso";
	private static final String USRPRO       = "cUsuarioProceso";
	private static final String USR          = "cUsuario";
	private static final String NOMUSR       = "cNombreUsuario";
	private static final String ERR          = "cErrorNumber";
	private static final String DESERR       = "cErrorMessage";
	private static final String CONTPREPAGO  = "cContratoDescuentoPrepago";

	public static Map<String, Object> validaData(OperationStatusEntity body)
			throws  ResponseException {

		Map<String, Object> params = new HashMap<>();
		ValidateUtil.validaBody(new ObjectMapper().convertValue(body, ObjectNode.class));
		// Campos Requeridos
		ValidateUtil.validaData(CONTRACT, body.getContractNumber(), 8, EXPRECOM, true);
		ValidateUtil.validaData(USRMOD, body.getUsuarioModificacion(), 8, EXPRECOM, true);
		ValidateUtil.validaData(DESCAT, body.getDescripcionOperacion(), 8, EXPRECOM, true);
		//Valida y obtiene codigo de estado
		
		// Put
		params.put(CONTRACT, body.getContractNumber());
		params.put(CODCAT, null);
		params.put(DESCAT, null);
		params.put(PKCODEST, body.getCodigoEstadoOperacion());
		params.put(CODEST, body.getCodigoEstadoOperacion());
		params.put(DESEST, body.getDescripcionOperacion());
		params.put(FECPAG, null);
		params.put(FECVEN, null);
		params.put(DIAS, null);
		params.put(PREPAG, null);
		params.put(TOT, null);
		params.put(CONTPREPAGO, null);
		params.put("crMarcaDescarte", null);
		params.put("cMotivoDescarte", null);
		params.put(USRPRO, body.getUsuarioModificacion());
		params.put(USR, body.getUsuarioModificacion());
		params.put(USRMOD, body.getUsuarioModificacion());
		params.put(NOMUSRPRO, body.getNombreUsuarioModificacion());
		params.put(NOMUSR, body.getNombreUsuarioModificacion());	
		params.put(DESERR, null);
		params.put(ERR, null);

		return params;
	}

	private ValidateUpdateStatus() {

	}
}
