package cl.forum.api.ef.operations.validate;

import java.text.ParseException;
import java.util.Map;

import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.util.ValidateUtil;

public class ValidateFindFilter {

	public static Map<String, Object> validaData(Map<String, Object> parametros, String maxPagina, String paginacion) throws ResponseException, ParseException {
				
		String pagina = ValidateUtil.validaPaginacion(paginacion);
		String maxReg = ValidateUtil.validaMaxPagina(maxPagina);
		Integer valskip = (Integer.parseInt(pagina) - 1);
		Integer skip = (Integer.parseInt(maxReg) * valskip);
		Integer limit = (Integer.parseInt(maxReg) + 1);
		//
		parametros.put("iSkip", skip);
		parametros.put("iLimit", limit);
		parametros.put("cErrorMessage", null);
		parametros.put("cErrorNumber", null);
		parametros.put("pagina", pagina);
		parametros.put("maxReg", maxReg);
		//
		return parametros;			
	}
	
	private ValidateFindFilter () {
		
	}

}
