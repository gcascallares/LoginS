package ar.edu.unlam.seguridad.servicios;

import java.util.List;

import Helpers.CustomException;
import ar.edu.unlam.seguridad.modelo.Texto;
import ar.edu.unlam.seguridad.modelo.Usuario;
import ar.edu.unlam.seguridad.modelo.UsuarioAuditoria;
import ar.edu.unlam.seguridad.modelo.UsuarioViewModel;

public interface ServicioUsuario {
	
	Usuario consultarUsuarioLogin(Usuario usuario) throws CustomException;
	Boolean registrarUsuario(Usuario usuario) throws CustomException;
	public Texto getTextoUsuario(String token) throws CustomException;
	public void AsignarTextoNuevoAUsuario(String token) throws CustomException;
	public void ActualizarTextoUsuario(String token, String texto, char accion) throws CustomException;
	List<UsuarioAuditoria> ListarAuditorias(String token, String rol) throws CustomException;
	List<UsuarioAuditoria> ListarAuditorias(Long idUsuario, String rol) throws CustomException;
	public Boolean consultarUsuarioPreguntaSecreta(Usuario usuario) throws CustomException;
	public Boolean consultarUsuarioCambioPassword(String nickUsuario, UsuarioViewModel usuario) throws CustomException;
	List<Usuario> GetUsuarios(String accion) throws CustomException;
	public void GestionarUsuarios(Usuario usuario, Boolean accion) throws CustomException;
	List<Usuario> GetUsuarios() throws CustomException;
	public boolean activarCuentaUsuario(String guid) throws CustomException;
	public void BloquearUsuario(String nick);
}
