package ar.edu.unlam.seguridad.modelo;

public class UsuarioViewModel {
	private String passwordAntiguo;
	private String passwordNuevo;
	
	public String getPasswordAntiguo() {
		return passwordAntiguo;
	}
	public void setPasswordAntiguo(String passwordAntiguo) {
		this.passwordAntiguo = passwordAntiguo;
	}
	public String getPasswordNuevo() {
		return passwordNuevo;
	}
	public void setPasswordNuevo(String passwordNuevo) {
		this.passwordNuevo = passwordNuevo;
	}
}
