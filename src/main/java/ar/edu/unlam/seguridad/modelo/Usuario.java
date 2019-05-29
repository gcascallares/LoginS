package ar.edu.unlam.seguridad.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nick;
	private String password;
	private String rol;
	private Boolean estado;
	private String respuestaSeguridad;
	private String token;
	private String guid;
	private String mail;
	
	@OneToMany
	private List<UsuarioAuditoria> listaAuditorias;
	
	@OneToOne
	private Texto texto;
	
	@OneToOne
	private UsuarioSalt usuarioSalt;
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public UsuarioSalt getUsuarioSalt() {
		return usuarioSalt;
	}
	public void setUsuarioSalt(UsuarioSalt usuarioSalt) {
		this.usuarioSalt = usuarioSalt;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public Texto getTexto() {
		return texto;
	}
	public void setTexto(Texto texto) {
		this.texto = texto;
	}
	public List<UsuarioAuditoria> getListaAuditorias() {
		return listaAuditorias;
	}
	public void setListaAuditorias(List<UsuarioAuditoria> listaAuditorias) {
		this.listaAuditorias = listaAuditorias;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getRespuestaSeguridad() {
		return respuestaSeguridad;
	}
	public void setRespuestaSeguridad(String respuestaSeguridad) {
		this.respuestaSeguridad = respuestaSeguridad;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
