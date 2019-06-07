package cl.forum.api.ef.operations.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.config.Properties;
import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.util.DateTimeUtil;
import cl.forum.api.ef.operations.util.JdbcTemplateUtils;
import cl.forum.api.ef.operations.util.ServiceUtil;
import cl.forum.api.ef.operations.exception.StatusResponseEnum;
import cl.forum.api.ef.operations.pojo.AmountsEntity;
import cl.forum.api.ef.operations.pojo.OperationPrepagoEntity;
import cl.forum.api.ef.operations.pojo.OperationStatusEntity;
import cl.forum.api.ef.operations.pojo.TotalAmountsEntity;

@Component
public class OperationsRepository {

	private JdbcTemplateUtils jdbcUtils;
	private static final String SPFIND = "spfind";
	private static final String SPFINDPROC = "spfindproc";
	private static final String SPFINDINTER = "spfindinter";
	private static final String SPEAF = "speaf";
	private static final String SPFINDDOCSOL = "spdocumensoli";
	private static final String SPFINDDOCPAG = "spdocpago";
	private static final String SPUPDATE = "spupdate";
	private static final String SPINSERTPROC = "spinsertproc";
	private static final String SPUPDATEDOCU = "spupdatedocu";
	private static final String SPUPDATESTORBOX = "spstorboxact";
	private static final String SPAMOUNTS = "spamounts";
	private static final String SPDOCPARAM = "spdocparam";
	private static final String ERROR = "cErrorNumber";
	private static final String DESERROR = "cErrorMessage";
	private static final String RESULTSET = "#result-set-1";
	private static final String RUTDEALER = "cRutDistribuidor";
	private static final String EST01 = "EST01";
	private static final String EST02 = "EST02";
	private static final String EST03 = "EST03";
	private static final String EST04 = "EST04";
	private static final String EST06 = "EST06";
	private static final String EST08 = "EST08";
	private static final String DFECHAPAGO = "dFechaPago";
	private static final String NDIAS = "nDias";
	private static final String DESCEST = "descripcion_estado";
	private static final String PKCONTRAT = "pkNumeroContrato";
	private static final String RUTCLIE = "cRutCliente";
	private static final String NMONSP = "nMontoSaldoPrecio";
	private static final String NMONTC = "nMontoTotalCheque";
	private static final String CUSER = "cUsuario";
	private static final String FECPAGO = "fecha_pago";
	private static final String CDESESTOP = "cDescEstadoOperacion";
	private static final String PKCODDOC = "pkCodTipoDocumento";
	private static final String NOMASIG = "nombre_asignacion";
	private static final String USERASIG = "usuario_asignacion";
	private static final String FECBLOQ = "fecha_bloqueo";
	private static final String FECVENC = "fecha_vencimiento";
	private static final String DIASPEND = "dias_pendientes";
	private static final String CNOMUSR = "cNombreUsuario";
	private static final String STATUSURL = "statusurl";
	private static final String NSTATUSOPE = "nCodEstadoOperacion";
	private static final String NOMDISTI = "cNombreDistribuidor";
	private static final String SKIP = "iSkip";
	private static final String LIMIT = "iLimit";

	@Autowired
	private void setJdbcUtils(JdbcTemplateUtils jdbcUtils) {
		this.jdbcUtils = jdbcUtils;
	}

	@Autowired
	private Properties properties;

	public ArrayNode findOperations(Map<String, Object> params, String operationStatusCode, Integer statusId,
			String usuario) throws ParseException, ResponseException, IOException {

		Map<String, Object> retornoFind = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFIND), params);
		//
		if (Integer.parseInt(retornoFind.get(ERROR).toString()) != 0) {
			throw new ResponseException(retornoFind.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED,
					true, "");
		}
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode listOperationsFind = nodeFactory.arrayNode();
		ObjectNode operationsFind = null;
		Integer contReg = 0;

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultSetFind = (List<Map<String, Object>>) retornoFind.get(RESULTSET);

		if (!resultSetFind.isEmpty()) {
			for (int i = 0; i < resultSetFind.size(); i++) {
				Map<String, Object> catalogoBD = resultSetFind.get(i);
				contReg++;
				operationsFind = nodeFactory.objectNode();
				String fechaVencimiento = "";
				switch (operationStatusCode) {
				case EST02:
					operationsFind.put(NOMASIG, catalogoBD.get(CNOMUSR).toString().trim());
					operationsFind.put(USERASIG, catalogoBD.get(CUSER).toString());
					operationsFind.put(FECBLOQ, "");
					operationsFind.put(FECPAGO, "");
					operationsFind.put(FECVENC, fechaVencimiento);
					operationsFind.put(DIASPEND, "");
					break;
				case EST03:
					fechaVencimiento = DateTimeUtil.getExpirationDate(catalogoBD.get(DFECHAPAGO).toString(),
							Integer.parseInt(catalogoBD.get(NDIAS).toString()));
					operationsFind.put(NOMASIG, "");
					operationsFind.put(USERASIG, "");
					operationsFind.put(FECBLOQ, "");
					operationsFind.put(FECPAGO, catalogoBD.get(DFECHAPAGO).toString());
					operationsFind.put(FECVENC, fechaVencimiento);
					operationsFind.put(DIASPEND, "");
					break;
				case EST04:
				case EST08:
					fechaVencimiento = DateTimeUtil.getExpirationDate(catalogoBD.get(DFECHAPAGO).toString(),
							Integer.parseInt(catalogoBD.get(NDIAS).toString()));
					operationsFind.put(NOMASIG, catalogoBD.get(CNOMUSR).toString().trim());
					operationsFind.put(USERASIG, catalogoBD.get(CUSER).toString());
					operationsFind.put(FECBLOQ, "");
					operationsFind.put(FECPAGO, catalogoBD.get(DFECHAPAGO).toString());
					operationsFind.put(FECVENC, fechaVencimiento);
					operationsFind.put(DIASPEND, ServiceUtil.getDays(fechaVencimiento));
					operationsFind.put("cantidad_documentos", cantDocumentosPago(catalogoBD.get(PKCONTRAT).toString()));
					break;
				case EST06:
					operationsFind.put(NOMASIG, "");
					operationsFind.put(USERASIG, "");
					String fechaBloqueo = getFechaBloqueo(catalogoBD.get(PKCONTRAT).toString(), statusId);
					operationsFind.put(FECBLOQ, fechaBloqueo);
					operationsFind.put(FECPAGO, "");
					operationsFind.put(FECVENC, fechaVencimiento);
					operationsFind.put(DIASPEND, "");
					break;
				default:
					break;
				}
				operationsFind.put("descripcion_categoria", catalogoBD.get("cDescCategoria").toString());
				operationsFind.put("descripcion_distribuidor", catalogoBD.get("cRazonSocialDistribuidor").toString());
				operationsFind.put("rut_distribuidor", catalogoBD.get(RUTDEALER).toString());
				operationsFind.put(DESCEST, catalogoBD.get(CDESESTOP).toString());
				operationsFind.put("numero_contrato", catalogoBD.get(PKCONTRAT).toString());
				operationsFind.put("rut_cliente", catalogoBD.get(RUTCLIE).toString());
				operationsFind.put("monto_total", Integer.parseInt(catalogoBD.get(NMONTC).toString()));
				operationsFind.put("fecha_curse", catalogoBD.get("dFechaCurse").toString());
				operationsFind.put("fecha_pagare", catalogoBD.get("dFechaPagare").toString());
				operationsFind.put("marca_descarte", catalogoBD.get("crMarcaDescarte").toString());
				// Calcula codigo de estado
				String url = properties.getProperties().get(STATUSURL);
				Integer statusIdOperation = Integer.parseInt(catalogoBD.get(NSTATUSOPE).toString());
				String statuscode = ServiceUtil.getStatusCode(url, statusIdOperation, usuario);
				operationsFind.put("codigo_estado", statuscode);
				//
				listOperationsFind.add(operationsFind);
			}

			//
			String imax = params.get("maxReg").toString();
			String pag = params.get("pagina").toString();
			//
			if (contReg > Integer.parseInt(imax)) {
				listOperationsFind.remove(contReg - 1);
			}

			ObjectNode node = JsonNodeFactory.instance.objectNode();
			if (contReg > Integer.parseInt(imax)) {
				node.put("paginacion", true);
				node.put("paginaSiguiente", Integer.parseInt(pag) + 1);
			} else {
				node.put("paginacion", false);
				node.put("paginaSiguiente", 0);
			}

			listOperationsFind.add(node);
		}
		return listOperationsFind;
	}

	public String getFechaBloqueo(String contrato, Integer statusId)
			throws ParseException, ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		params.put(PKCONTRAT, contrato);
		params.put(NSTATUSOPE, statusId);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFINDPROC), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		String fecha = "";

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);

		if (!salida.isEmpty()) {
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> catalogoBD = salida.get(i);
				fecha = catalogoBD.get("pkFechaProceso").toString();
			}
		}
		return fecha;

	}

	public ObjectNode findDetailOperations(Map<String, Object> params)
			throws ParseException, ResponseException, IOException {

		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFIND), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode detailOperation = nodeFactory.arrayNode();
		ArrayNode listaIntervinientes = nodeFactory.arrayNode();
		ArrayNode listaElementosCarrito = nodeFactory.arrayNode();
		ArrayNode listaDocumentosPago = nodeFactory.arrayNode();
		ArrayNode listaDocumentosSolicitados = nodeFactory.arrayNode();
		ObjectNode salidaDetalle = nodeFactory.objectNode();
		ObjectNode operation = null;

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);

		if (!salida.isEmpty()) {
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> catalogoBD = salida.get(i);
				operation = nodeFactory.objectNode();
				operation.put("numero_contrato", catalogoBD.get(PKCONTRAT).toString());
				operation.put("descripcion_categoria", catalogoBD.get("cDescCategoria").toString());
				operation.put(DESCEST, catalogoBD.get(CDESESTOP).toString());
				operation.put("numero_operacion", catalogoBD.get("nNumOperacion").toString());
				operation.put("numero_detalle_operacion", catalogoBD.get("nNumDetalleOperacion").toString());
				operation.put("rut_cliente", catalogoBD.get(RUTCLIE).toString());
				operation.put("nombre_cliente", catalogoBD.get("cNombreCliente").toString().trim());
				operation.put("email_cliente", catalogoBD.get("cEmailCliente").toString().trim());
				operation.put("fono_cliente", catalogoBD.get("cFonoCliente").toString());
				operation.put("direccion_cliente", catalogoBD.get("cDireccionCliente").toString());
				operation.put("rut_distribuidor", catalogoBD.get(RUTDEALER).toString());
				operation.put("razon_social_distribuidor", catalogoBD.get("cRazonSocialDistribuidor").toString());
				operation.put("nombre_distribuidor", catalogoBD.get(NOMDISTI).toString());
				operation.put("descripcion_marca_vehiculo", catalogoBD.get("cDescMarcaVehiculo").toString());
				operation.put("descripcion_modelo_vehiculo", catalogoBD.get("cDescModeloVehiculo").toString());
				operation.put("anio_vehiculo", Integer.parseInt(catalogoBD.get("nAnoVehiculo").toString()));
				operation.put("descripcion_version_vehiculo", catalogoBD.get("cDescVersionVehiculo").toString());
				operation.put("descripcion_estado_vehiculo", catalogoBD.get("cEstadoVehiculo").toString());
				operation.put("dias", catalogoBD.get(NDIAS).toString());
				operation.put(FECPAGO, catalogoBD.get(DFECHAPAGO).toString());
				operation.put("fecha_curse", catalogoBD.get("dFechaCurse").toString());
				operation.put("fecha_pagare", catalogoBD.get("dFechaPagare").toString());
				operation.put("fecha_primera_cuota", catalogoBD.get("dFechaPrimeraCuota").toString());
				operation.put("fecha_ultima_cuota", catalogoBD.get("dFechaUltimaCuota").toString());
				operation.put("tasa", catalogoBD.get("nTasa").toString());
				operation.put("numero_cuota", ((BigDecimal) catalogoBD.get("nNumeroCuota")).intValue());
				operation.put("monto_cuota_base", ((BigDecimal) catalogoBD.get("nMontoCuotaBase")).intValue());
				operation.put("monto_total_cheque", ((BigDecimal) catalogoBD.get(NMONTC)).intValue());
				operation.put("monto_gastos_inscripcion",((BigDecimal) catalogoBD.get("nMontoGastoInscripcion")).intValue());
				operation.put("monto_saldo_precio", ((BigDecimal) catalogoBD.get(NMONSP)).intValue());
				operation.put("monto_descuento_prepago",((BigDecimal) catalogoBD.get("nMontoDescuentoPrepago")).intValue());
				String contratoPrepago = "";
				if(catalogoBD.get("cContratoDescuentoPrepago") != null) {
					contratoPrepago = catalogoBD.get("cContratoDescuentoPrepago").toString();			
				}
				operation.put("contrato_descuento_prepago", contratoPrepago);
				operation.put("producto", catalogoBD.get("cTipoProducto").toString().trim());
				operation.put("elementos_adicionales", catalogoBD.get("crElementosAdicionales").toString());
				operation.put("marca_descarte", catalogoBD.get("crMarcaDescarte").toString());
				operation.put("motivo_descarte", catalogoBD.get("cMotivoDescarte").toString().trim());
				operation.put("monto_paridad_uf", catalogoBD.get("nMontoParidadUF").toString());
				operation.put("codigo_tipo_distribuidor", catalogoBD.get("nTipoDistribuidor").toString());
				Integer cantBienes = ((BigDecimal) catalogoBD.get("nCantidadBienes")).intValue();				
				operation.put("cantidad_bienes", cantBienes);
				Integer montoBien = 0;
				if(catalogoBD.get("nMontoBien") != null) {
					montoBien = ((BigDecimal) catalogoBD.get("nMontoBien")).intValue();			
				}						
				operation.put("precio_venta_unitario", montoBien);
				operation.put("precio_venta_total", montoBien*cantBienes);
				operation.put("usuario", catalogoBD.get(CUSER).toString().trim());
				operation.put("nombre_usuario", catalogoBD.get(CNOMUSR).toString().trim());
				//
				detailOperation.add(operation);
				listaIntervinientes = interviniente(catalogoBD.get(PKCONTRAT).toString());
				if (catalogoBD.get("crElementosAdicionales").toString().equals("SI")) {
					listaElementosCarrito = elemenCarrito(catalogoBD.get(PKCONTRAT).toString());
				}
				listaDocumentosSolicitados = documentosSolicitados(catalogoBD.get(PKCONTRAT).toString());
				listaDocumentosPago = documentosPago(catalogoBD.get(PKCONTRAT).toString());
			}
		}
		//
		// Graba detalle
		salidaDetalle.putArray("detalle").addAll(detailOperation);
		// Graba intervinientes
		salidaDetalle.putArray("intervinientes").addAll(listaIntervinientes);
		// Graba elementos carrito
		salidaDetalle.putArray("elementosCarrito").addAll(listaElementosCarrito);
		// Graba documentos solicitados
		salidaDetalle.putArray("documentosSolicitados").addAll(listaDocumentosSolicitados);
		// Graba documentos pagos
		salidaDetalle.putArray("documentosPagos").addAll(listaDocumentosPago);
		// Retorno
		return salidaDetalle;
	}

	public ArrayNode interviniente(String contrato) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		params.put(PKCONTRAT, contrato);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFINDINTER), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode listaIntervinientes = nodeFactory.arrayNode();
		//
		if (!salida.isEmpty()) {
			//
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> paramDisBD = salida.get(i);
				ObjectNode interviniente = nodeFactory.objectNode();
				interviniente.put("rut_interviniente", paramDisBD.get("pkRutInterviniente").toString().trim());
				interviniente.put("nombre_interviniente", paramDisBD.get("cNombreInterviniente").toString().trim());
				interviniente.put("tipo_interviniente", paramDisBD.get("cTipoInterviniente").toString().trim());
				listaIntervinientes.add(interviniente);
			}
		}
		return listaIntervinientes;
	}

	public ArrayNode elemenCarrito(String contrato) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		params.put(PKCONTRAT, contrato);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPEAF), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode listaElementos = nodeFactory.arrayNode();
		//
		if (!salida.isEmpty()) {
			//
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> elementBD = salida.get(i);
				ObjectNode elemento = nodeFactory.objectNode();
				elemento.put("elemento", elementBD.get("cElemento").toString());
				elemento.put("valor_elemento", elementBD.get("nValorElemento").toString());
				listaElementos.add(elemento);
			}
		}
		return listaElementos;
	}

	public ArrayNode documentosSolicitados(String contrato) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		params.put(PKCONTRAT, contrato);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFINDDOCSOL), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultSetDoc = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode listaDocSolicit = nodeFactory.arrayNode();
		//
		if (!resultSetDoc.isEmpty()) {
			//
			for (int i = 0; i < resultSetDoc.size(); i++) {
				Map<String, Object> docBD = resultSetDoc.get(i);
				ObjectNode docSol = nodeFactory.objectNode();
				docSol.put("nombre_documento", docBD.get("cNombreDocumento").toString());
				docSol.put("marca_enviado", docBD.get("crIndEnviado").toString());
				docSol.put("marca_aprobado", docBD.get("crIndAprobado").toString());
				docSol.put("observacion", docBD.get("cObservacion").toString());
				listaDocSolicit.add(docSol);
			}
		}
		return listaDocSolicit;
	}

	public ArrayNode documentosPago(String contrato) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		params.put(PKCONTRAT, contrato);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFINDDOCPAG), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode listaDocusSoli = nodeFactory.arrayNode();
		//
		if (!salida.isEmpty()) {
			//
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> elementBD = salida.get(i);
				ObjectNode docSol = nodeFactory.objectNode();
				docSol.put("codigo_documento", elementBD.get(PKCODDOC).toString());
				docSol.put("nombre_documento", elementBD.get("cDescTipoDocumento").toString());
				docSol.put("requerido",
						getRequerido(elementBD.get("fkCodCategoria").toString(), elementBD.get(PKCODDOC).toString()));
				docSol.put("marca_enviado", elementBD.get("crRecepcionadoStorBox").toString());
				docSol.put("marca_valido", elementBD.get("crRecepcionadoUsuario").toString());
				docSol.put("marca_invalido", elementBD.get("crInvalido").toString());
				listaDocusSoli.add(docSol);
			}
		}
		return listaDocusSoli;
	}

	public Integer cantDocumentosPago(String contrato) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		Integer cantDoc = 0;
		params.put(PKCONTRAT, contrato);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFINDDOCPAG), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		if (!salida.isEmpty()) {
			//
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> elementBD = salida.get(i);
				if (elementBD.get("crRecepcionadoStorBox").toString().equalsIgnoreCase("SI")
						&& elementBD.get("crRecepcionadoUsuario").toString().equalsIgnoreCase("NO")) {
					cantDoc++;
				}
			}
		}
		return cantDoc;
	}

	public String getRequerido(String categoryCode, String documentType) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		params.put("pkCodCategoria", categoryCode);
		params.put(PKCODDOC, documentType);
		params.put("pkFechaHasta", "2100-12-31 23:59:59");
		params.put(SKIP, 0);
		params.put(LIMIT, 10);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPDOCPARAM), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		String requerido = "";
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		if (!salida.isEmpty()) {
			//
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> elementBD = salida.get(i);
				requerido = elementBD.get("crRequerido").toString();
			}
		}
		return requerido;
	}

	public int updateStatus(Map<String, Object> params) throws ResponseException, IOException {

		Integer retorno = 1;
		Map<String, Object> retornoUpdate = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPUPDATE),
				params);
		if (Integer.parseInt(retornoUpdate.get(ERROR).toString()) == 0) {
			Map<String, Object> retornoCreate = jdbcUtils
					.callStoreProcedure(properties.getProperties().get(SPINSERTPROC), params);
			if (Integer.parseInt(retornoCreate.get(ERROR).toString()) == 0) {
				retorno = Integer.parseInt(retornoCreate.get(ERROR).toString());
			} else {
				throw new ResponseException(retornoCreate.get(DESERROR).toString(),
						StatusResponseEnum.PRECONDITION_FAILED, true, "");
			}
		} else {
			throw new ResponseException(retornoUpdate.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED,
					true, "");
		}

		return retorno;
	}

	public int saveUpdateDoc(Map<String, Object> params) throws ResponseException, IOException {

		Map<String, Object> dataBD = null;
		dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPUPDATEDOCU), params);
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		} else {
			Map<String, Object> dataBD2 = null;
			dataBD2 = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPUPDATESTORBOX), params);
			if (Integer.parseInt(dataBD2.get(ERROR).toString()) != 0) {
				throw new ResponseException(dataBD2.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED,
						true, "");
			} else {
				return Integer.parseInt(dataBD2.get(ERROR).toString());
			}
		}
	}

	public Integer getTotalAmount(String rut, String estado, String usuario) throws ResponseException, IOException {

		Map<String, Object> params = new HashMap<>();
		//
		String url = properties.getProperties().get(STATUSURL);
		Integer statusId = ServiceUtil.getStatusId(url, estado, usuario);
		if (statusId <= 0) {
			throw new ResponseException("Estado no existe", StatusResponseEnum.OK, true, "");
		}
		//
		params.put("rut", rut);
		params.put("estado", statusId);
		params.put(DESERROR, null);
		params.put(ERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPAMOUNTS), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		//
		Integer montoTotal = 0;
		//
		if (!salida.isEmpty()) {
			//
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> elementBD = salida.get(i);
				if (elementBD.get("MontoTotal") == null) {
					montoTotal = 0;
				} else {
					montoTotal = Integer.parseInt(elementBD.get("MontoTotal").toString());
				}
			}
		}
		return montoTotal;
	}

	public TotalAmountsEntity findAmounts(String rut, String usuario) throws ResponseException, IOException {

		TotalAmountsEntity totalAmounts = new TotalAmountsEntity();

		Integer montoTotalDisponible = getTotalAmount(rut, EST01, usuario);
		Integer montoTotalBloqueados = getTotalAmount(rut, EST06, usuario);
		Integer montoTotalVencidos = getTotalAmount(rut, EST08, usuario);

		totalAmounts.setMontoTotalDisponible(montoTotalDisponible);
		totalAmounts.setMontoTotalBloqueados(montoTotalBloqueados);
		totalAmounts.setMontoTotalVencidos(montoTotalVencidos);

		return totalAmounts;

	}

	public AmountsEntity findAmountsTramo(String rut, String estado, String usuario)
			throws ResponseException, IOException {

		AmountsEntity montosTramo = new AmountsEntity();
		Integer montoTramo1 = 0;
		Integer montoTramo2 = 0;
		Integer montoTramo3 = 0;
		Integer cantReg = 0;
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

		try {
			Integer skip = 0;
			Integer limit = 30;
			ArrayNode listOperationsTramo = nodeFactory.arrayNode();
			while (listOperationsTramo.size() >= 0) {
				cantReg = 0;
				listOperationsTramo = leeSpTramo(rut, estado, usuario, skip, limit);
				if (listOperationsTramo.size() != 0) {
					int monto1 = Integer.parseInt(listOperationsTramo.findValue("MontoTramo1").toString());
					int monto2 = Integer.parseInt(listOperationsTramo.findValue("MontoTramo2").toString());
					int monto3 = Integer.parseInt(listOperationsTramo.findValue("MontoTramo3").toString());
					cantReg = Integer.parseInt(listOperationsTramo.findValue("CantidadReg").toString());
					montoTramo1 = (montoTramo1 + monto1);
					montoTramo2 = (montoTramo2 + monto2);
					montoTramo3 = (montoTramo3 + monto3);

				}
				skip = skip + limit;
				if (cantReg <= 0) {
					montosTramo.setMontoTramo0dias(montoTramo1);
					montosTramo.setMontoTramo30dias(montoTramo2);
					montosTramo.setMontoTramo60dias(montoTramo3);
					break;
				}
			}

		} catch (Exception e) {
			throw new ResponseException("Error en mostrar montos de tramos", StatusResponseEnum.PRECONDITION_FAILED,
					true, "");
		}
		return montosTramo;
	}

	public ArrayNode leeSpTramo(String rut, String estado, String usuario, Integer skip, Integer limit)
			throws ResponseException, IOException, ParseException {

		int montoTramo1 = 0;
		int montoTramo2 = 0;
		int montoTramo3 = 0;
		Map<String, Object> params = new HashMap<>();
		String url = properties.getProperties().get(STATUSURL);
		Integer statusId = ServiceUtil.getStatusId(url, estado, usuario);
		if (statusId <= 0) {
			throw new ResponseException("Estado no existe", StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}
		params.put(PKCONTRAT, null);
		params.put(NSTATUSOPE, statusId);
		params.put("nCodCategoria", null);
		params.put(RUTCLIE, null);
		params.put(RUTDEALER, rut);
		params.put(NOMDISTI, null);
		params.put(CUSER, "TRAMO");
		params.put("dFechaPagoDesde", "2100-12-10 00:00:00");
		params.put("dFechaPagoHasta", "2100-12-31 23:59:59");
		params.put("dFechaPagareDesde", null);
		params.put("dFechaPagareHasta", null);
		params.put(SKIP, skip);
		params.put(LIMIT, limit);
		//
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFIND), params);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode listOperations = nodeFactory.arrayNode();
		ObjectNode operations = nodeFactory.objectNode();
		Integer contReg = 0;

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);

		if (!salida.isEmpty()) {
			montoTramo1 = 0;
			montoTramo2 = 0;
			montoTramo3 = 0;
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> catalogoBD = salida.get(i);
				contReg++;

				operations = nodeFactory.objectNode();
				String fechaPago = catalogoBD.get(DFECHAPAGO).toString();
				Integer nDias = Integer.parseInt(catalogoBD.get(NDIAS).toString());
				String fechaVencimiento = ServiceUtil.getExpirationDate(fechaPago, nDias);
				int diasPendientes = ServiceUtil.getDays(fechaVencimiento);
				operations.put("nDias Pendientes", diasPendientes);
				diasPendientes = diasPendientes * -1;

				if (diasPendientes < 30) {
					montoTramo1 = montoTramo1 + Integer.parseInt(catalogoBD.get(NMONTC).toString());
				} else if (diasPendientes > 30 && diasPendientes < 60) {
					montoTramo2 = montoTramo2 + Integer.parseInt(catalogoBD.get(NMONTC).toString());
				} else if (diasPendientes > 60) {
					montoTramo3 = montoTramo3 + Integer.parseInt(catalogoBD.get(NMONTC).toString());
				}
				operations.put("MontoTramo1", montoTramo1);
				operations.put("MontoTramo2", montoTramo2);
				operations.put("MontoTramo3", montoTramo3);

			}
			operations.put("CantidadReg", contReg);
			listOperations.add(operations);

		}

		return listOperations;

	}

	public Integer getTotalAmountPrepago(OperationPrepagoEntity prepagoEntity) throws ResponseException, IOException {

		Integer montoCarrito = 0;

		String marca = prepagoEntity.getMarcaCarrito();
		if (marca.equals("SI")) {
			montoCarrito = getAmountCarrito(prepagoEntity);
		} else {
			montoCarrito = 0;
		}
		Integer montoSaldoPrecio = prepagoEntity.getMontoSaldoPrecio();
		Integer montoGastos = prepagoEntity.getMontoGastoInscripcion();
		Integer montoPrepago = prepagoEntity.getAmountPrepago();
		return ((montoSaldoPrecio - montoPrepago) + (montoGastos + montoCarrito));
	}

	public int updatePrepago(Map<String, Object> params) throws ResponseException, IOException {

		Map<String, Object> retornoUpdate = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPUPDATE),
				params);
		if (Integer.parseInt(retornoUpdate.get(ERROR).toString()) != 0) {
			throw new ResponseException(retornoUpdate.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED,
					true, "");
		}
		return Integer.parseInt(retornoUpdate.get(ERROR).toString());
	}

	@SuppressWarnings("unchecked")
	public Integer getAmountCarrito(OperationPrepagoEntity prepagoEntity) throws ResponseException, IOException {

		Integer montoCarrito = 0;
		Map<String, Object> params = new HashMap<>();
		params.put(PKCONTRAT, prepagoEntity.getContractNumber());
		params.put(ERROR, null);
		params.put(DESERROR, null);
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPEAF), params);
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);
		if (!salida.isEmpty()) {
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> catalogoBD = salida.get(i);
				montoCarrito = (montoCarrito + Integer.parseInt(catalogoBD.get("nValorElemento").toString()));
			}
		}

		return montoCarrito;
	}

	public Boolean validStatusOperations(OperationStatusEntity body)
			throws ParseException, ResponseException, IOException {

		Integer codigoEstado = 0;
		Boolean retorno = false;
		// Setea parametros para buscar el contrato
		Map<String, Object> parametros = new HashMap<>();
		parametros.put(PKCONTRAT, body.getContractNumber());
		parametros.put(NSTATUSOPE, 1);
		parametros.put("nCodCategoria", null);
		parametros.put(RUTCLIE, null);
		parametros.put(RUTDEALER, null);
		parametros.put(NOMDISTI, null);
		parametros.put(CUSER, null);
		parametros.put("dFechaPagoDesde", null);
		parametros.put("dFechaPagoHasta", null);
		parametros.put("dFechaPagareDesde", null);
		parametros.put("dFechaPagareHasta", null);
		parametros.put(SKIP, 0);
		parametros.put(LIMIT, 10);
		parametros.put(DESERROR, null);
		parametros.put(ERROR, null);

		// Ejecuta SP que busca el contrato
		Map<String, Object> dataBD = jdbcUtils.callStoreProcedure(properties.getProperties().get(SPFIND), parametros);
		//
		if (Integer.parseInt(dataBD.get(ERROR).toString()) != 0) {
			throw new ResponseException(dataBD.get(DESERROR).toString(), StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		//

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> salida = (List<Map<String, Object>>) dataBD.get(RESULTSET);

		// Obtiene respuesta del SP y obtiene estado actual de la operacion.
		if (!salida.isEmpty()) {
			for (int i = 0; i < salida.size(); i++) {
				Map<String, Object> catalogoBD = salida.get(i);
				codigoEstado = ((BigDecimal) catalogoBD.get(NSTATUSOPE)).intValue();
			}
		}else {
			throw new ResponseException("Contrato "+body.getContractNumber()+" no existe en BD GFE", StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}

		// Retorno
		if(codigoEstado <= 21){
			retorno = validStatusCode1(codigoEstado, body);
		}else {
			retorno = validStatusCode2(codigoEstado, body);
		}		
		return retorno;
	}
	
	public Boolean validStatusCode1(Integer codigoEstado, OperationStatusEntity body) {
		
		Boolean validationStatus = false;
		
		switch (codigoEstado) {
		case 15:
		case 21:
			// Valida Cargado
			// Valida Bloqueado
			if (body.getCodigoEstadoOperacion().equals("16") || body.getCodigoEstadoOperacion().equals("22")) {
				validationStatus = true;
			}
			break;
		case 16:
			// Valida Disponible
			if (body.getCodigoEstadoOperacion().equals("17") || body.getCodigoEstadoOperacion().equals("20")
					|| body.getCodigoEstadoOperacion().equals("21") || body.getCodigoEstadoOperacion().equals("22")) {
				validationStatus = true;
			}
			break;
		case 17:
			// Valida Asignado
			if (body.getCodigoEstadoOperacion().equals("16") || body.getCodigoEstadoOperacion().equals("18")
					|| body.getCodigoEstadoOperacion().equals("19") || body.getCodigoEstadoOperacion().equals("20")
					|| body.getCodigoEstadoOperacion().equals("22")) {
				validationStatus = true;
			}
			break;
		case 19:
			// Valida Anticipado
			if (body.getCodigoEstadoOperacion().equals("18") || body.getCodigoEstadoOperacion().equals("23")) {
				validationStatus = true;
			}
			break;
		default:
			break;
		}
		
		return validationStatus;
		
	}
	
	public Boolean validStatusCode2(Integer codigoEstado, OperationStatusEntity body) {
		
		Boolean validationStatus = false;
		
		switch (codigoEstado) {
		case 23:
			// Valida Descursado
			if (body.getCodigoEstadoOperacion().equals("15")) {
				validationStatus = true;
			}
			break;
		case 22:
			// Valida Vencidos
			if (body.getCodigoEstadoOperacion().equals("18")) {
				validationStatus = true;
			}
			break;
		default:
			break;
		}
		
		return validationStatus;
		
	}

} // fin class CatalogoRepository
