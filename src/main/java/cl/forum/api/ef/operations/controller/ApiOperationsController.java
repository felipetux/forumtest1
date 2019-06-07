package cl.forum.api.ef.operations.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.util.ServiceUtil;
import cl.forum.api.ef.operations.util.ValidateUtil;
import cl.forum.api.ef.operations.validate.ValidateFindFilter;
import cl.forum.api.ef.operations.validate.ValidateUpdateDocu;
import cl.forum.api.ef.operations.validate.ValidateUpdatePayment;
import cl.forum.api.ef.operations.validate.ValidateUpdatePrepago;
import cl.forum.api.ef.operations.validate.ValidateUpdateStatus;
import cl.forum.api.ef.operations.config.Properties;
import cl.forum.api.ef.operations.exception.ErrorResponseJson;
import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.exception.StatusResponseEnum;
import cl.forum.api.ef.operations.pojo.AmountsEntity;
import cl.forum.api.ef.operations.pojo.OperationDocuEntity;
import cl.forum.api.ef.operations.pojo.OperationPrepagoEntity;
import cl.forum.api.ef.operations.pojo.OperationStatusEntity;
import cl.forum.api.ef.operations.pojo.OutputAmountsEntity;
import cl.forum.api.ef.operations.pojo.TotalAmountsEntity;
import cl.forum.api.ef.operations.repository.OperationsRepository;
import cl.forum.api.ef.operations.util.ResponseUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiOperationsController {

	public static final Logger LOG_SERVICE = LoggerFactory.getLogger("restservice");
	public static final Logger LOG_ERROR = LoggerFactory.getLogger("error");
	private static final String URL = "securityurl";
	private static final String OBJ = "obj";
	private static final String N = "N";
	private static final String MESSAGE_SECURITY = "Usuario no tiene permisos";
	private static final String TOTAL = "nMontoTotal";
	private static final String USER = "usuario";
	private static final String URLMACHI = "spurlmachi";
	private static final String SPCOTIZA = "spcotiza";

	@Autowired
	private OperationsRepository operationsRepository;

	@Autowired
	private Properties properties;

	@ControllerAdvice
	public class ErrorHandler {
		@ExceptionHandler(ResponseException.class)
		public ResponseEntity<ErrorResponseJson> methodCatchResponseException(HttpServletRequest request,
				ResponseException e) {
			LOG_ERROR.error("Error info ", e);
			ErrorResponseJson errorInfo = new ErrorResponseJson(e);
			return new ResponseEntity<>(errorInfo,
					HttpStatus.valueOf(Integer.parseInt(e.getStatusResponseEnum().getStatusCode())));
		}

		@ExceptionHandler(Exception.class)
		public ResponseEntity<ErrorResponseJson> methodCatchException(HttpServletRequest request, Exception e) {
			ErrorResponseJson errorInfo = new ErrorResponseJson(e);
			LOG_ERROR.error("BAD REQUEST : ", e);
			return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * Find Filter Operations
	 * 
	 */
	@GetMapping(value = "/ef/operations/${info.version}/", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView findAll(@RequestParam(required = false) String categoryCode,
			@RequestParam(required = false) String clientDocumentNumber,
			@RequestParam(required = false) String dealerDocumentNumber,
			@RequestParam(required = false) String dealerName, @RequestParam(required = false) String contractNumber,
			@RequestParam(required = true) String operationStatusCode,
			@RequestParam(required = false) String assignedUser, @RequestParam(required = false) String payDateFrom,
			@RequestParam(required = false) String payDateTo, @RequestParam(required = false) String pagareDateFrom,
			@RequestParam(required = false) String pagareDateTo, @RequestParam(required = true) String pageNumber,
			@RequestParam(required = true) String maxRows, @RequestHeader(value = USER) String usuario)
			throws IOException, ResponseException, ParseException {

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		ArrayNode responseOperations = null;
		ObjectMapper mapper = new ObjectMapper();

		String url = properties.getProperties().get("statusurl");
		Integer statusId = ServiceUtil.getStatusId(url, operationStatusCode, usuario);
		if (statusId <= 0) {
			throw new ResponseException("Estado no existe", StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("pkNumeroContrato", contractNumber);
		parametros.put("nCodEstadoOperacion", statusId);
		parametros.put("nCodCategoria", categoryCode);
		parametros.put("cRutCliente", clientDocumentNumber);
		parametros.put("cRutDistribuidor", dealerDocumentNumber);
		parametros.put("cNombreDistribuidor", dealerName);
		parametros.put("cUsuario", assignedUser);
		parametros.put("dFechaPagoDesde", payDateFrom);
		parametros.put("dFechaPagoHasta", payDateTo);
		parametros.put("dFechaPagareDesde", pagareDateFrom);
		parametros.put("dFechaPagareHasta", pagareDateTo);
		Map<String, Object> params = ValidateFindFilter.validaData(parametros, maxRows, pageNumber);
		responseOperations = operationsRepository.findOperations(params, operationStatusCode, statusId, usuario);

		if (responseOperations.size() <= 0) {
			throw new ResponseException("No existen registros", StatusResponseEnum.NO_CONTENT, true, "");
		}
		return ResponseUtil.genericResponseFind(mapper.writeValueAsString(responseOperations));
	}

	/*
	 * 
	 * Find Detail Operation
	 * 
	 */
	@GetMapping(value = "/ef/operations/${info.version}/operation/{contractNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView findDetalle(@PathVariable("contractNumber") String contractNumber,
			@RequestHeader(value = USER) String usuario) throws IOException, ResponseException, ParseException {

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("pkNumeroContrato", contractNumber);
		parametros.put("nCodEstadoOperacion", 1);
		parametros.put("nCodCategoria", null);
		parametros.put("cRutCliente", null);
		parametros.put("cRutDistribuidor", null);
		parametros.put("cNombreDistribuidor", null);
		parametros.put("cUsuario", null);
		parametros.put("dFechaPagoDesde", null);
		parametros.put("dFechaPagoHasta", null);
		parametros.put("dFechaPagareDesde", null);
		parametros.put("dFechaPagareHasta", null);
		parametros.put("iSkip", 0);
		parametros.put("iLimit", 10);
		parametros.put("cErrorMessage", null);
		parametros.put("cErrorNumber", null);

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		// Obtiene Detalle
		ObjectNode responseDetailOperations = operationsRepository.findDetailOperations(parametros);

		if (responseDetailOperations.size() <= 0) {
			throw new ResponseException("Contrato no existe", StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}
		return ResponseUtil.genericResponseFind(mapper.writeValueAsString(responseDetailOperations));

	}

	/*
	 * 
	 * Update Status Operation
	 * 
	 */
	@PutMapping(value = "/ef/operations/${info.version}/status/{contractNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView updateOperation(@RequestBody(required = true) OperationStatusEntity body,
			@PathVariable("contractNumber") String contractNumber, @RequestHeader(value = USER) String usuario)
			throws IOException, ResponseException, ParseException {

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		body.setContractNumber(contractNumber);
		// Valida Estados
		if (operationsRepository.validStatusOperations(body)) {
			Map<String, Object> params = ValidateUpdateStatus.validaData(body);
			ObjectMapper mapper = new ObjectMapper();
			int retorno = operationsRepository.updateStatus(params);
			return ResponseUtil.genericResponsePostPut(mapper.writeValueAsString(retorno));
		} else {
			throw new ResponseException("Estado invalido para actualizar el contrato",
					StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}
	}

	/*
	 * 
	 * Update StorBox Documents
	 * 
	 */
	@PutMapping(value = "/ef/operations/${info.version}/document/{contractNumber}/{documentType}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView updateOperation(@RequestBody(required = true) OperationDocuEntity body,
			@PathVariable("contractNumber") String contractNumber, @PathVariable("documentType") String documentType,
			@RequestHeader(value = USER) String usuario) throws IOException, ResponseException, ParseException {

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		body.setContractNumber(contractNumber);
		body.setDocumentType(documentType);
		Map<String, Object> params = ValidateUpdateDocu.validaData(body);
		ObjectMapper mapper = new ObjectMapper();
		int retorno = operationsRepository.saveUpdateDoc(params);

		return ResponseUtil.genericResponsePostPut(mapper.writeValueAsString(retorno));

	}

	/*
	 * 
	 * Update Status Payment Operation
	 * 
	 */
	@PutMapping(value = "/ef/operations/${info.version}/payment/{contractNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView payOperation(@RequestBody(required = true) OperationStatusEntity body,
			@PathVariable("contractNumber") String contractNumber, @RequestHeader(value = USER) String usuario)
			throws IOException, ResponseException, ParseException {

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		body.setContractNumber(contractNumber);
		Map<String, Object> params = ValidateUpdatePayment.validaData(body);
		ObjectMapper mapper = new ObjectMapper();
		int retorno = operationsRepository.updateStatus(params);

		return ResponseUtil.genericResponsePostPut(mapper.writeValueAsString(retorno));

	}

	/*
	 * 
	 * Get Amounts
	 * 
	 */
	@GetMapping(value = "/ef/operations/${info.version}/amounts/{dealerDocumentNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView getAmounts(@PathVariable("dealerDocumentNumber") String dealerDocumentNumber,
			@RequestHeader(value = USER) String usuario) throws IOException, ResponseException, ParseException {

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}
		String rutDealer = ValidateUtil.validacionRut(dealerDocumentNumber);
		TotalAmountsEntity totalAmounts = operationsRepository.findAmounts(rutDealer, usuario);
		AmountsEntity montosTramo = operationsRepository.findAmountsTramo(rutDealer, "EST08", usuario);

		// salida
		ObjectMapper mapper = new ObjectMapper();
		OutputAmountsEntity salida = new OutputAmountsEntity();
		salida.setMontoTotalDisponibles(totalAmounts.getMontoTotalDisponible());
		salida.setMontoTotalBloqueados(totalAmounts.getMontoTotalBloqueados());
		salida.setMontoTotalVencidos(totalAmounts.getMontoTotalVencidos());
		salida.setMontoTramo1(montosTramo.getMontoTramo0dias());
		salida.setMontoTramo2(montosTramo.getMontoTramo30dias());
		salida.setMontoTramo3(montosTramo.getMontoTramo60dias());

		return ResponseUtil.genericResponseFind(mapper.writeValueAsString(salida));

	}

	/*
	 * 
	 * Update prepago Operation
	 * 
	 */
	@PutMapping(value = "/ef/operations/${info.version}/prepay/{contractNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin
	public ModelAndView updateOperationPrepago(@RequestBody(required = true) OperationPrepagoEntity body,
			@PathVariable("contractNumber") String contractNumber, @RequestHeader(value = USER) String usuario)
			throws IOException, ResponseException, ParseException {

		// Seguridad
		String urlSecurity = properties.getProperties().get(URL);
		String obj = properties.getProperties().get(OBJ);
		String permiso = ServiceUtil.getStatus(usuario, urlSecurity, obj);
		if (permiso.equals(N)) {
			LOG_ERROR.error(MESSAGE_SECURITY);
			throw new ResponseException(MESSAGE_SECURITY, StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}
		
		//Valido Contrato Prepago en Sybase
		String urlMachi = properties.getProperties().get(URLMACHI);
		String storeProcedure = properties.getProperties().get(SPCOTIZA);		
		JsonNode detalleContrato = ServiceUtil.leerContrato(body.getContractPrepago(), storeProcedure, urlMachi);
		Integer lIdtplCabezaCotiza = detalleContrato.get(0).findValue("lIdtplCabezaCotiza").asInt();
		if(lIdtplCabezaCotiza <= 0) {
			throw new ResponseException("Contrato de prepago inválido, no existe en Sybase",
					StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}

		body.setContractNumber(contractNumber);
		Map<String, Object> params = ValidateUpdatePrepago.validaData(body);
		Integer montoTotal = operationsRepository.getTotalAmountPrepago(body);
		params.put(TOTAL, montoTotal);
		ObjectMapper mapper = new ObjectMapper();
		int retorno = operationsRepository.updatePrepago(params);

		return ResponseUtil.genericResponsePostPut(mapper.writeValueAsString(retorno));

	}

}
