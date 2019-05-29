package ar.edu.unlam.seguridad.servicios;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Helpers.CustomException;
import Helpers.EmailClient;
import Helpers.TokenHelper;
import ar.edu.unlam.seguridad.dao.UsuarioDao;
import ar.edu.unlam.seguridad.modelo.Texto;
import ar.edu.unlam.seguridad.modelo.Usuario;
import ar.edu.unlam.seguridad.modelo.UsuarioAuditoria;
import ar.edu.unlam.seguridad.modelo.UsuarioSalt;
import ar.edu.unlam.seguridad.modelo.UsuarioViewModel;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario{

	private Logger LOGGER = LoggerFactory.getLogger(ServicioUsuarioImpl.class);

	@Inject
	private UsuarioDao usuarioDao;
	
	@Override
	public Boolean registrarUsuario(Usuario usuario) throws CustomException {
		try {
			List<Usuario> lista = usuarioDao.verSiExisteUsuario(usuario.getNick());
			if (lista.size() == 0) {
				Usuario usuarioNuevo = new Usuario();
				String saltNuevo = GenerarSaltRandom();
				UsuarioSalt usuarioSalt = new UsuarioSalt();
				usuarioSalt.setSalt(saltNuevo);
				usuarioNuevo.setNick(usuario.getNick());
				usuarioNuevo.setPassword(GetPassMd5ConSalt(usuario.getPassword(), saltNuevo));
				usuarioNuevo.setRol("user");
				usuarioNuevo.setMail(usuario.getMail());
				usuarioNuevo.setEstado(false);
				usuarioNuevo.setRespuestaSeguridad(GetRespuestaSeguridadHash(usuario.getRespuestaSeguridad()));
				usuarioNuevo.setToken(TokenHelper.GenerarTokenUsuario(usuario));
				UUID guid = UUID.randomUUID();
				usuarioNuevo.setGuid(guid.toString());
				try {
					EmailClient.EnviarMail(guid.toString(), usuario.getMail());
					LOGGER.info("EMAIL - Se envia mail de activación exitosamente.");
				} catch (MessagingException e) {
					LOGGER.error("EMAILERROR - " + e.getMessage());
					throw new CustomException();
				}
				usuarioDao.guardarUsuario(usuarioNuevo);
				Usuario usuarioGuardado = usuarioDao.ObtenerUsuarioPorToken(usuarioNuevo.getToken());
				usuarioSalt.setUsuario(usuarioGuardado);
				usuarioGuardado.setUsuarioSalt(usuarioSalt);
				usuarioDao.GuardarUsuarioSalt(usuarioSalt);
				LOGGER.info("REGISTRO - Se registra usuario exitosamente.");
				return true;	
			} else {
				LOGGER.info("REGISTRO FALLIDO - Usuario ya existente.");
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("REGISTRO FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
		
	}

	@Override
	public Texto getTextoUsuario(String token) throws CustomException {
		try {
			Usuario usuario = usuarioDao.ObtenerUsuarioPorToken(token);
			LOGGER.info("TEXTO - Se obtiene texto de usuario por token.");
			return usuarioDao.getTextoUsuario(usuario.getId());
		} catch (Exception e) {
			LOGGER.error("TEXTO OBTENCIONFALLIDA - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public void AsignarTextoNuevoAUsuario(String token) throws CustomException {
		try {
			Usuario usuario = usuarioDao.ObtenerUsuarioPorToken(token);
			Texto texto = new Texto();
			texto.setDescripcion("");
			texto.setUsuario(usuario);
			usuarioDao.crearTexto(texto);
			UsuarioAuditoria usuarioAuditoria = new UsuarioAuditoria();
			usuarioAuditoria.setUsuario(usuario);
			usuarioAuditoria.setDescripcion("Se crea texto vacio para el usuario " + usuario.getNick());
			usuarioDao.CrearUsuarioAuditoria(usuarioAuditoria);
			LOGGER.info("TEXTO - Texto asignado correctamente a usuario.");
		} catch (Exception e) {
			LOGGER.error("TEXTO ASIGNACIONFALLIDA - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public void ActualizarTextoUsuario(String token, String texto, char accion) throws CustomException {
		try {
			Usuario usuario = usuarioDao.ObtenerUsuarioPorToken(token);
			Texto textoAnterior = usuarioDao.getTextoUsuario(usuario.getId());
			String textoAnt = textoAnterior.getDescripcion();
			usuarioDao.ActualizarTextoUsuario(usuario.getId(), texto);
			UsuarioAuditoria usuarioAuditoria = new UsuarioAuditoria();
			usuarioAuditoria.setUsuario(usuario);
			switch (accion) {
				case 'm':
					usuarioAuditoria.setDescripcion("El usuario " + usuario.getNick() + " modifica el texto " 
													+ textoAnt + " por " + texto);
					break;
				case 'e':
					usuarioAuditoria.setDescripcion("El usuario " + usuario.getNick() + " elimina el texto " 
							+ textoAnt);
					break;
			}
			usuarioDao.CrearUsuarioAuditoria(usuarioAuditoria);
			LOGGER.info("TEXTO - Texto actualizado correctamente a usuario.");
		} catch (Exception e) {
			LOGGER.error("TEXTO ACTUALIZACIONFALLIDA - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public List<UsuarioAuditoria> ListarAuditorias(String token, String rol) throws CustomException {
		try {
			List<UsuarioAuditoria> auditorias;
			Usuario usuario = usuarioDao.ObtenerUsuarioPorToken(token);
			auditorias = usuarioDao.ListarAuditoriasUsuario(usuario);
			LOGGER.info("AUDITORIAS - Lista de auditorias obtenida con exito.");
		return auditorias;
		} catch (Exception e) {
			LOGGER.error("AUDITORIAS OBTENCIONFALLIDA - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public List<UsuarioAuditoria> ListarAuditorias(Long idUsuario, String rol) throws CustomException {
		try {
			List<UsuarioAuditoria> auditorias;
			Usuario usuario = usuarioDao.ObtenerUsuarioPorId(idUsuario);
			auditorias = usuarioDao.ListarAuditoriasUsuario(usuario);
			LOGGER.info("AUDITORIAS - Lista de auditorias por usuario obtenida con exito.");
		return auditorias;
		} catch (Exception e) {
			LOGGER.error("AUDITORIAS OBTENCIONFALLIDA - " + e.getMessage());
			throw new CustomException();
		}
	}
	
	@Override
	public Boolean consultarUsuarioPreguntaSecreta(Usuario usuario) throws CustomException {
		try {
			usuario.setRespuestaSeguridad(GetRespuestaSeguridadHash(usuario.getRespuestaSeguridad()));
			Usuario usuarioBuscado = usuarioDao.consultarUsuarioPreguntaSecreta(usuario);
			if(usuarioBuscado != null){
				usuarioBuscado.setPassword(GetPassMd5ConSalt(usuario.getPassword(), usuarioBuscado.getUsuarioSalt().getSalt()));
				usuarioBuscado.setToken(TokenHelper.GenerarTokenUsuario(usuarioBuscado));
				usuarioDao.ActualizarUsuario(usuarioBuscado);
				UsuarioAuditoria usuarioAuditoria = new UsuarioAuditoria();
				usuarioAuditoria.setUsuario(usuarioBuscado);
				usuarioAuditoria.setDescripcion("El usuario " + usuario.getNick() + " ha recuperado su contraseña mediante pregunta secreta.");
				usuarioDao.CrearUsuarioAuditoria(usuarioAuditoria);
				LOGGER.info("USUARIOCREDENCIALES - Usuario recuperó su contraseña con exito.");
				return true;
			}
			else{
				LOGGER.info("USUARIOCREDENCIALES FALLIDO - Usuario falló al recuperar su contraseña.");
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("USUARIOCREDENCIALES FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public Boolean consultarUsuarioCambioPassword(String nickUsuario, UsuarioViewModel usuario) throws CustomException {
		try {
			Usuario usuarioSalt = usuarioDao.ConsultarUsuarioPorNick(nickUsuario);
			Usuario usuarioBusqueda = new Usuario();
			usuarioBusqueda.setNick(nickUsuario);
			usuarioBusqueda.setPassword(GetPassMd5ConSalt(usuario.getPasswordAntiguo(), usuarioSalt.getUsuarioSalt().getSalt()));
			Usuario usuarioBuscado = usuarioDao.consultarUsuarioLogin(usuarioBusqueda);
			if(usuarioBuscado != null){
				usuarioBuscado.setPassword(GetPassMd5ConSalt(usuario.getPasswordNuevo(), usuarioSalt.getUsuarioSalt().getSalt()));
				usuarioBuscado.setToken(TokenHelper.GenerarTokenUsuario(usuarioBuscado));
				usuarioDao.ActualizarUsuario(usuarioBuscado);
				UsuarioAuditoria usuarioAuditoria = new UsuarioAuditoria();
				usuarioAuditoria.setUsuario(usuarioBuscado);
				usuarioAuditoria.setDescripcion("El usuario " + usuarioBuscado.getNick() + " ha cambiado su contraseña.");
				usuarioDao.CrearUsuarioAuditoria(usuarioAuditoria);
				LOGGER.info("USUARIOCREDENCIALES - Usuario cambió su contraseña con exito.");
				return true;
			}
			else{
				LOGGER.info("USUARIOCREDENCIALES FALLIDO - Usuario falló al cambiar su contraseña.");
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("USUARIOCREDENCIALES FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public List<Usuario> GetUsuarios(String accion) throws CustomException {		
		try {
			switch (accion) {
			case "Activar":
				LOGGER.info("USUARIO - Se obtiene lista de usuarios inactivos con exito.");
				return usuarioDao.GetUsuarios(false);
			case "Desactivar":
				LOGGER.info("USUARIO - Se obtiene lista de usuarios activos con exito.");
				return usuarioDao.GetUsuarios(true);
			default:
				LOGGER.info("USUARIO FALLIDO - Obtener lista de usuarios por estado falla.");
				return null;
			}		
		} catch (Exception e) {
			LOGGER.error("USUARIO FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public void GestionarUsuarios(Usuario usuario, Boolean accion) throws CustomException {
		try {
			Usuario usuarioBuscado = usuarioDao.ObtenerUsuarioPorId(usuario.getId());
			usuarioBuscado.setEstado(accion);
			if(accion)
				usuarioBuscado.setGuid(null);
			usuarioDao.ActualizarUsuario(usuarioBuscado);
			LOGGER.info("USUARIO - Se actualizó estado de usuario con exito.");
		} catch (Exception e) {
			LOGGER.error("USUARIO FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public List<Usuario> GetUsuarios() throws CustomException {
		try {
			List<Usuario> usuarios = usuarioDao.GetUsuarios();
			LOGGER.info("USUARIO - Se listan usuarios con exito.");
			return usuarios;
		} catch (Exception e) {
			LOGGER.error("USUARIO FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}	
	
	@Override
	public Usuario consultarUsuarioLogin (Usuario usuario) throws CustomException {
		try {
			Usuario usuarioSalt = usuarioDao.ConsultarUsuarioPorNick(usuario.getNick());
			usuario.setPassword(GetPassMd5ConSalt(usuario.getPassword(), usuarioSalt.getUsuarioSalt().getSalt()));
			Usuario usuarioBuscado = usuarioDao.consultarUsuarioLogin(usuario);
			if(usuarioBuscado!= null){
				usuarioBuscado.setToken(TokenHelper.GenerarTokenUsuario(usuarioBuscado));
				usuarioDao.ActualizarUsuario(usuarioBuscado);
			}
			LOGGER.info("USUARIOLOGIN - Consulta realizada con exito.");
			return usuarioBuscado;
		} catch (Exception e) {
			LOGGER.error("USUARIOLOGIN FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}
	
	public static String GetPassMd5ConSalt(String password, String salt) {

        //Referencia de codigo
        //http://www.qualityinfosolutions.com/metodos-para-encriptar-y-desencriptar-en-java/
		
		String factorExtra = "grupo1" + salt;//Llave secreta que usamos como factor extra según verificación
		String passEnBase64 = "";
		
        try {
 
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(factorExtra.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);
 
            byte[] plainTextBytes = password.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            passEnBase64 = new String(base64Bytes);
            
        } catch (Exception ex) {
        }
        return passEnBase64;
	}
	
	public static String GetRespuestaSeguridadHash(String respuesta) {

        //Referencia de codigo
        //http://www.qualityinfosolutions.com/metodos-para-encriptar-y-desencriptar-en-java/
		
		String factorExtra = "respuesta";//Llave secreta que usamos como factor extra según verificación
		String respuestaEnBase64 = "";
		
        try {
 
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(factorExtra.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);
 
            byte[] plainTextBytes = respuesta.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            respuestaEnBase64 = new String(base64Bytes);
            
        } catch (Exception ex) {
        }
        return respuestaEnBase64;
	}
	
	public String GenerarSaltRandom(){
		    byte[] array = new byte[8]; // length is bounded by 7
		    new Random().nextBytes(array);
		    String stringRandom = new String(array, Charset.forName("UTF-8"));
		    return stringRandom;
	}

	@Override
	public boolean activarCuentaUsuario(String guid) throws CustomException {
		try {
			Usuario usuarioAActivar = usuarioDao.ObtenerUsuarioPorGuid(guid);
			if(usuarioAActivar!=null){
				usuarioAActivar.setEstado(true);
				usuarioAActivar.setGuid(null);
				usuarioDao.ActualizarUsuario(usuarioAActivar);
				LOGGER.info("USUARIOACTIVACION - Se activa cuenta de usuario con éxito.");
				return true;
			}else{
				LOGGER.info("USUARIOACTIVACION - Error al activar cuenta de usuario, solo se activa una vez.");
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("USUARIOACTIVACION FALLIDO - " + e.getMessage());
			throw new CustomException();
		}
	}

	@Override
	public void BloquearUsuario(String nick) {
		try {
			Usuario usuario = usuarioDao.ConsultarUsuarioPorNick(nick);
			usuario.setEstado(false);
			usuarioDao.ActualizarUsuario(usuario);
			LOGGER.info("USUARIOACTIVACION - Se da de baja usuario por reiterados intentos de login fallidos.");
		} catch (Exception e) {
			LOGGER.error("USUARIOACTIVACION FALLIDO - " + e.getMessage());
		}
	}
}
