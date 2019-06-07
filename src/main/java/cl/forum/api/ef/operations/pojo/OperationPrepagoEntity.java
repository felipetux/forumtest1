package cl.forum.api.ef.operations.pojo;

public class OperationPrepagoEntity {

	private String contractNumber;
	private Integer amountPrepago;
	private String usuarioModificacion;
	private String nombreUsuarioModificacion;
	private Integer montoSaldoPrecio;
	private Integer montoGastoInscripcion;
	private String marcaCarrito;
	private String contractPrepago;
	
	
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Integer getAmountPrepago() {
		return amountPrepago;
	}
	public void setAmountPrepago(Integer amountPrepago) {
		this.amountPrepago = amountPrepago;
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
	public Integer getMontoSaldoPrecio() {
		return montoSaldoPrecio;
	}
	public void setMontoSaldoPrecio(Integer montoSaldoPrecio) {
		this.montoSaldoPrecio = montoSaldoPrecio;
	}
	public Integer getMontoGastoInscripcion() {
		return montoGastoInscripcion;
	}
	public void setMontoGastoInscripcion(Integer montoGastoInscripcion) {
		this.montoGastoInscripcion = montoGastoInscripcion;
	}
	public String getMarcaCarrito() {
		return marcaCarrito;
	}
	public void setMarcaCarrito(String marcaCarrito) {
		this.marcaCarrito = marcaCarrito;
	}
	public String getContractPrepago() {
		return contractPrepago;
	}
	public void setContractPrepago(String contractPrepago) {
		this.contractPrepago = contractPrepago;
	}
	
	
}
