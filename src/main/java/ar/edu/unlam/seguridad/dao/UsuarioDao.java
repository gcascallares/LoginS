package ar.edu.unlam.seguridad.dao;

import java.util.List;

import ar.edu.unlam.seguridad.modelo.Texto;
import ar.edu.unlam.seguridad.modelo.Usuario;
import ar.edu.unlam.seguridad.modelo.UsuarioAuditoria;
import ar.edu.unlam.seguridad.modelo.UsuarioSalt;

public interface UsuarioDao {
	
	Usuario consultarUsuarioLogin (Usuario usuario);
	public Usuario consultarUsuarioPreguntaSecreta(Usuario usuario);
	Usuario ObtenerUsuarioPorId (Long idUsuario);
	Usuario ObtenerUsuarioPorToken (String token);
	List<Usuario> verSiExisteUsuario(String nick);
	Usuario guardarUsuario (Usuario usuario);
	public Texto getTextoUsuario(Long idUsuario);
	public void crearTexto(Texto texto);
	public void ActualizarTextoUsuario(Long idUsuario, String texto);
	public void CrearUsuarioAuditoria(UsuarioAuditoria usuarioAuditoria);
	public List<UsuarioAuditoria> ListarAuditoriasUsuario(Usuario usuario);
	public List<UsuarioAuditoria> ListarAuditoriasUsuarios();
	public void ActualizarUsuario(Usuario usuario);
	List<Usuario> GetUsuarios(Boolean estado);
	List<Usuario> GetUsuarios();
	Usuario ConsultarUsuarioPorNick(String nick);
	public void GuardarUsuarioSalt(UsuarioSalt usuarioSalt);
	Usuario ObtenerUsuarioPorGuid(String guid);
}
