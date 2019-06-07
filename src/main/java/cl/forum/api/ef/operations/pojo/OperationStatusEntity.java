package cl.forum.api.ef.operations.pojo;

public class OperationStatusEntity {

	private String contractNumber;
	private String codigoEstadoOperacion;
	private String descripcionOperacion;
	private String usuarioModificacion;
	private String nombreUsuarioModificacion;
	private String dias;
	
	
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getCodigoEstadoOperacion() {
		return codigoEstadoOperacion;
	}
	public void setCodigoEstadoOperacion(String codigoEstadoOperacion) {
		this.codigoEstadoOperacion = codigoEstadoOperacion;
	}
	public String getDescripcionOperacion() {
		return descripcionOperacion;
	}
	public void setDescripcionOperacion(String descripcionOperacion) {
		this.descripcionOperacion = descripcionOperacion;
	}
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public String getNombreUsuarioModificacion() {
		return nombreUsuarioModificacion;
	}
	public void setNombreUsuarioModificacion(String nombreUsuarioModificacion) {
		this.nombreUsuarioModificacion = nombreUsuarioModificacion;
	}
	public String getDias() {
		return dias;
	}
	public void setDias(String dias) {
		this.dias = dias;
	}
	

	
}
