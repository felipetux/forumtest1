package cl.forum.api.ef.operations.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cl.forum.api.ef.operations.exception.ResponseException;
import cl.forum.api.ef.operations.exception.StatusResponseEnum;

public class ServiceUtil {

	public static final Logger LOG_ERROR = LoggerFactory.getLogger("error");
	public static final String FORMAT_DB_DATE = "yyyy-MM-dd";
	private static final String SERVICIO = "Servicio ";
	private static final String MSGNORSP = " no responde debido a ";
	private static final String NUMCONTRATO = "cNumeroContrato";
	private static final String IDTCABCOTIZA = "lIdtplCabezaCotiza";
	private static final String ERRORENSP = "Error en SP: ";
	private static final String RESULTSET = "#result-set-1";

	public static Integer getStatusId(String url, String statusCode, String usuario)
			throws ResponseException, IOException {
		Integer statusId = 0;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("usuario", usuario);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		} catch (Exception ex) {
			throw new ResponseException(SERVICIO + url + MSGNORSP + ex, StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode body = mapper.readTree(response.getBody());

		Iterator<JsonNode> iterator = body.findValue("data").elements();
		while (iterator.hasNext()) {
			JsonNode element = iterator.next();
			if (element.findValue("ValorCodigo").asText().equals(statusCode)) {
				statusId = element.findValue("ValorCatalogoId").asInt();
			}
		}

		return statusId;

	}

	public static String getStatusCode(String url, Integer statusId, String user)
			throws ResponseException, IOException {
		String statuscode = "";
		HttpHeaders paramHeaders = new HttpHeaders();
		paramHeaders.setAccept(Arrays.asList());
		paramHeaders.setContentType(MediaType.APPLICATION_JSON);
		paramHeaders.set("usuario", user);
		HttpEntity<String> httpEntity = new HttpEntity<>(paramHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
		} catch (Exception ex) {
			throw new ResponseException(SERVICIO + url + MSGNORSP + ex, StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode body = mapper.readTree(response.getBody());

		Iterator<JsonNode> iterator = body.findValue("data").elements();
		while (iterator.hasNext()) {
			JsonNode element = iterator.next();
			if (element.findValue("ValorCatalogoId").asInt() == (statusId)) {
				statuscode = element.findValue("ValorCodigo").asText();
			}
		}

		return statuscode;
	}

	public static Integer getDays(String fecha) {

		String[] dateParts = fecha.split("-");
		String year = dateParts[0];
		String month = dateParts[1];
		String day = dateParts[2];
		Integer yyyy = Integer.parseInt(year);
		Integer mm = Integer.parseInt(month);
		Integer dd = Integer.parseInt(day);
		LocalDate fechaVencimiento = LocalDate.of(yyyy, mm, dd);
		LocalDate fechaActual = LocalDate.now();
		long diferenciaDias = ChronoUnit.DAYS.between(fechaActual, fechaVencimiento);
		return (int) diferenciaDias;

	}

	public static String getExpirationDate(String fechaPago, Integer dias) throws java.text.ParseException {

		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(FORMAT_DB_DATE);
		java.util.Date fecha = null;
		fecha = formatoDelTexto.parse(fechaPago);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return new SimpleDateFormat(FORMAT_DB_DATE).format(calendar.getTime());

	}

	public static String getStatus(String usuario, String urlSecurity, String obj)
			throws IOException, ResponseException {

		String status = "N";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Lidtplusuario", usuario);
		headers.add("lNombreObjeto", obj);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(urlSecurity, HttpMethod.GET, entity, String.class);
		} catch (Exception ex) {
			LOG_ERROR.error("Error en servicio : ", urlSecurity, ex);
			throw new ResponseException(SERVICIO + urlSecurity + MSGNORSP + ex, StatusResponseEnum.PRECONDITION_FAILED,
					true, "");
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode body = mapper.readTree(response.getBody());
		JsonNode bod = body.get("body");
		if (!bod.isEmpty(null)) {
			JsonNode detalle = bod.get("DetallePermisos");
			if (detalle.findValue("permiso").asText().equals("S")) {
				status = "S";
			} else {
				status = "N";
			}
		}

		return status;

	}

	public static JsonNode leerContrato(String contrato, String storeProcedure, String url) throws ResponseException {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put(NUMCONTRATO, contrato);
		JsonNode response;
		try {
			response = callStoreProcedureSybase(storeProcedure, url, params).get(RESULTSET);
			if (response.isEmpty(null) || response.get(0).findValue(IDTCABCOTIZA) == null) {
				throw new ResponseException("Contrato " + contrato + " no existe en Sybase",
						StatusResponseEnum.PRECONDITION_FAILED, true, "");
			}
		} catch (Exception e) {
			throw new ResponseException(ERRORENSP + storeProcedure + e, StatusResponseEnum.PRECONDITION_FAILED, true,
					"");
		}
		return response;

	}

	public static ObjectNode callStoreProcedureSybase(String procedureName, String url, Map<String, Object> parameters)
			throws ResponseException {

		ObjectNode node = null;

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(url + procedureName);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			mapper.setDateFormat(df);
			String jsonInString = mapper.writeValueAsString(parameters);
			StringEntity params = new StringEntity(jsonInString, "UTF-8");
			httpPost.setEntity(params);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			CloseableHttpResponse response = client.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				node = new ObjectMapper().readValue(respuestaAString(response).toString(), ObjectNode.class);
			} else {
				node = new ObjectMapper().readValue(respuestaAString(response).toString(), ObjectNode.class);
				throw new ResponseException(SERVICIO + url + MSGNORSP + node,
						StatusResponseEnum.PRECONDITION_FAILED, true, "");
			}
		} catch (Exception ex) {
			throw new ResponseException(SERVICIO + url + MSGNORSP + ex,
					StatusResponseEnum.PRECONDITION_FAILED, true, "");
		}
		return node;
	}

	private static StringBuilder respuestaAString(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent()), StandardCharsets.UTF_8));
		String output;
		StringBuilder result = new StringBuilder();
		while ((output = br.readLine()) != null) {
			result.append(output);
		}
		return result;
	}

	private ServiceUtil() {

	}

}
