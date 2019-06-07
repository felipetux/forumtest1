package cl.forum.api.ef.operations.pojo;

public class OperationDocuEntity {
	
	private String contractNumber;
	private String documentType;
	private String usuarioModificacion;
	private String recepcionadoStorbox;
	private String recepcionadoUsuario;
	private String docInvalido;
		
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public String getRecepcionadoStorbox() {
		return recepcionadoStorbox;
	}
	public void setRecepcionadoStorbox(String recepcionadoStorbox) {
		this.recepcionadoStorbox = recepcionadoStorbox;
	}
	public String getRecepcionadoUsuario() {
		return recepcionadoUsuario;
	}
	public void setRecepcionadoUsuario(String recepcionadoUsuario) {
		this.recepcionadoUsuario = recepcionadoUsuario;
	}
	public String getDocInvalido() {
		return docInvalido;
	}
	public void setDocInvalido(String docInvalido) {
		this.docInvalido = docInvalido;
	}
	

}
