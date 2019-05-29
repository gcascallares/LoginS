package ar.edu.unlam.seguridad.controladores;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Helpers.CustomException;
import Helpers.SesionHelper;
import ar.edu.unlam.seguridad.modelo.Texto;
import ar.edu.unlam.seguridad.modelo.Usuario;
import ar.edu.unlam.seguridad.modelo.UsuarioAuditoria;
import ar.edu.unlam.seguridad.modelo.UsuarioViewModel;
import ar.edu.unlam.seguridad.servicios.ServicioUsuario;

@Controller
public class ControladorUsuario {
	
	private Logger LOGGER = LoggerFactory.getLogger(ControladorUsuario.class);	
	@Inject
	private ServicioUsuario servicioUsuario;
	
	@RequestMapping(path = "/registrar", method = RequestMethod.GET)
	public ModelAndView irARegistrar() {
		Usuario usuario = new Usuario();
		ModelMap modelo = new ModelMap();
		modelo.put("usuario", usuario);
		return new ModelAndView("registrar", modelo);
	}
	
	@RequestMapping(path = "/registrarusuario", method = RequestMethod.POST)
	public ModelAndView registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {
		ModelMap modelo = new ModelMap();

		if (SesionHelper.VerificarNickProhibido(usuario.getNick())) {
			return new ModelAndView("error");
		}
		
		if (SesionHelper.VerificarPasswordInseguro(usuario.getPassword())) {
			modelo.put("error", "El password es inseguro, por favor elija otro");
			return new ModelAndView("registrar", modelo);
		}
		
		if(!SesionHelper.ValidarPass(usuario.getPassword()) || !SesionHelper.ValidarEmail(usuario.getMail()) || !SesionHelper.ValidarNick(usuario.getNick())|| !SesionHelper.ValidarNick(usuario.getRespuestaSeguridad())){
			modelo.put("error", "Registro fallido. Alguno de los campos no cumple con el formato pedido.");
			return new ModelAndView("registrar", modelo);
		}
		
		try {
			Boolean resultado = servicioUsuario.registrarUsuario(usuario);
			if (resultado == true) {
				modelo.put("respuesta", "El registro fue exitoso, debes activar tu cuenta para logear.");
				return new ModelAndView("login", modelo);
			}else {
				modelo.put("respuesta", "El nombre de usuario ya existe.");
				return new ModelAndView("registrar", modelo);
			}	
		} catch (CustomException e) {
			return new ModelAndView("error");
		}		
			
	}
	
	@RequestMapping(path = "/texto", method = RequestMethod.GET)
	public ModelAndView irAMiTexto(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
			return new ModelAndView("redirect:/login");

		String token = GetToken(request);
		Texto texto = null;
		try {
			texto = servicioUsuario.getTextoUsuario(token);
			if(texto == null){
				servicioUsuario.AsignarTextoNuevoAUsuario(token);
				texto = servicioUsuario.getTextoUsuario(token);
			}
		} catch (Exception e) {
			return new ModelAndView("error");
		}
		
		ModelMap modelo = new ModelMap();
		Texto textoNuevo = new Texto();
		textoNuevo.setDescripcion(texto.getDescripcion());
		modelo.put("texto", textoNuevo);
		return new ModelAndView("texto", modelo);
	}
	
	@RequestMapping(path = "texto-actualizar", method = RequestMethod.POST)
	public ModelAndView actualizarTextoUsuario(@ModelAttribute("texto") Texto texto, HttpServletRequest request) {
		ModelMap modelo = new ModelMap();

		if(!SesionHelper.ValidarTexto(texto.getDescripcion())){
			Texto textoerr = new Texto();
			modelo.put("texto", textoerr);
			modelo.put("error", "El texto contiene caracteres invalidos.");
			return new ModelAndView("texto", modelo);
		}
	
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		try {
			servicioUsuario.ActualizarTextoUsuario(GetToken(request), texto.getDescripcion(), 'm');
		} catch (CustomException e) {
			return new ModelAndView("error");
		}
		
		Texto textoActualizado = new Texto();
		textoActualizado.setDescripcion(texto.getDescripcion());
		modelo.put("texto", textoActualizado);
	return new ModelAndView("texto", modelo);
	}
	
	@RequestMapping(path = "texto-eliminar", method = RequestMethod.GET)
	public ModelAndView eliminarTextoUsuario(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		try {
			servicioUsuario.ActualizarTextoUsuario(GetToken(request), "", 'e');
		} catch (CustomException e) {
			return new ModelAndView("error");
		}
		ModelMap modelo = new ModelMap();
		Texto textoActualizado = new Texto();
		textoActualizado.setDescripcion("");
		modelo.put("texto", textoActualizado);
	return new ModelAndView("texto", modelo);
	}
	
	@RequestMapping(path = "/auditoria", method = RequestMethod.GET)
	public ModelAndView irAAuditoria(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		String rolUsuario = (String)request.getSession().getAttribute("rol");

		try {
			ModelMap modelo = new ModelMap();
			if(SesionHelper.ValidarRol(request)){
				Usuario usuario = new Usuario();
				modelo.put("usuario", usuario);
				modelo.put("listaUsuarios", servicioUsuario.GetUsuarios());
			} 
			List<UsuarioAuditoria> listaAuditorias = servicioUsuario.ListarAuditorias(GetToken(request), rolUsuario);
			modelo.put("listaAuditorias", listaAuditorias);
			return new ModelAndView("auditoria", modelo);
		} catch (CustomException e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "/auditoriaporusuario", method = RequestMethod.POST)
	public ModelAndView irAAuditoriaPorUsuario(HttpServletRequest request, @ModelAttribute("usuario") Usuario usuario) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");

		try {
			ModelMap modelo = new ModelMap();
			if(SesionHelper.ValidarRol(request)){
				Usuario usuarioNuevo = new Usuario();
				modelo.put("usuario", usuarioNuevo);
				modelo.put("listaUsuarios", servicioUsuario.GetUsuarios());
			} 
			String rolUsuario = (String)request.getSession().getAttribute("rol");
			List<UsuarioAuditoria> listaAuditorias = servicioUsuario.ListarAuditorias(usuario.getId(), rolUsuario);
			modelo.put("listaAuditorias", listaAuditorias);
			return new ModelAndView("auditoria", modelo);
		} catch (CustomException e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "/reestablecerpassword", method = RequestMethod.GET)
	public ModelAndView irARecuperarContraseña(HttpServletRequest request) {
		Usuario usuario = new Usuario();
		ModelMap modelo = new ModelMap();
		modelo.put("usuario", usuario);
		return new ModelAndView("recuperar", modelo);
	}
	
	@RequestMapping(path = "/reestablecerpassword-validar", method = RequestMethod.POST)
	public ModelAndView validarPregunta(@ModelAttribute("usuario") Usuario usuario) {
		ModelMap model = new ModelMap();
		
		if(!SesionHelper.ValidarPass(usuario.getPassword()) || !SesionHelper.ValidarNick(usuario.getNick())){
			model.put("error", "Error. Alguno de los campos no cumple con el formato pedido.");
			return new ModelAndView("recuperar", model);
		}
		
		if (SesionHelper.VerificarPasswordInseguro(usuario.getPassword())) {
			model.put("error", "El password es inseguro, por favor elija otro");
			return new ModelAndView("recuperar", model);
		}

		try {
			Boolean usuarioBuscado = servicioUsuario.consultarUsuarioPreguntaSecreta(usuario);
			if (usuarioBuscado) {
				model.put("respuesta", "Se ha cambiado la contraseña con éxito.");
				return new ModelAndView("login", model);
			} 
			else {
				model.put("error", "Usuario no existe o la respuesta incorrecta");
				return new ModelAndView("recuperar", model);
			}
		} catch (CustomException e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "/cambiarpass", method = RequestMethod.GET)
	public ModelAndView irACambiarPassword(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		UsuarioViewModel usuario = new UsuarioViewModel();
		ModelMap modelo = new ModelMap();
		modelo.put("usuarioviewmodel", usuario);
		return new ModelAndView("cambiarPass", modelo);
	}
	
	@RequestMapping(path = "/cambiarpass-validar", method = RequestMethod.POST)
	public ModelAndView validarNuevaPass(@ModelAttribute("usuario") UsuarioViewModel usuario, HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		ModelMap model = new ModelMap();
		UsuarioViewModel usuariovm = new UsuarioViewModel();
		model.put("usuarioviewmodel", usuariovm);
		
		if(!SesionHelper.ValidarPass(usuario.getPasswordNuevo())){
			model.put("respuesta", "La logitud de la password no puede ser menor a 12 caracteres ni mayor a 72 caracteres");
			return new ModelAndView("cambiarPass", model);
		}
		
		if (SesionHelper.VerificarPasswordInseguro(usuario.getPasswordNuevo())) {
			model.put("respuesta", "El password es inseguro, por favor elija otro");
			return new ModelAndView("cambiarPass", model);
		}
		
		String nickUsuario = (String)request.getSession().getAttribute("nick");

		try {
			Boolean cambioExitoso = servicioUsuario.consultarUsuarioCambioPassword(nickUsuario, usuario);
			if (cambioExitoso) {
				model.put("respuesta", "Se ha cambiado la contraseña con éxito.");
				return new ModelAndView("cambiarPass", model);
			} 
			else {
				model.put("respuesta", "Error. La contraña actual es incorrecta.");
				return new ModelAndView("cambiarPass", model);
			}
		} catch (CustomException e) {
			return new ModelAndView("error");
		}	
	}
	
	@RequestMapping(path = "activarusuario", method = RequestMethod.GET)
	public ModelAndView irAActivarUsuarios(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		if(!SesionHelper.ValidarRol(request))
		return new ModelAndView("redirect:/home");
		
		Usuario usuario = new Usuario();
		ModelMap modelo = new ModelMap();
		try {
			List<Usuario> listaUsuarios = servicioUsuario.GetUsuarios("Activar");
			modelo.put("listaUsuarios", listaUsuarios);
			modelo.put("usuario", usuario);
			return new ModelAndView("gestionActivar", modelo);
		} catch (Exception e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "desactivarusuario", method = RequestMethod.GET)
	public ModelAndView irADesactivarUsuarios(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		if(!SesionHelper.ValidarRol(request))
		return new ModelAndView("redirect:/home");
		
		Usuario usuario = new Usuario();
		ModelMap modelo = new ModelMap();
		
		try {
			List<Usuario> listaUsuarios = servicioUsuario.GetUsuarios("Desactivar");
			modelo.put("listaUsuarios", listaUsuarios);
			modelo.put("usuario", usuario);
			return new ModelAndView("gestionDesactivar", modelo);
		} catch (Exception e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "desactivar", method = RequestMethod.POST)
	public ModelAndView desactivarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		if(!SesionHelper.ValidarRol(request))
		return new ModelAndView("redirect:/home");

		try {
			servicioUsuario.GestionarUsuarios(usuario, false);
			Usuario usuarioVacio = new Usuario();
			ModelMap modelo = new ModelMap();
			List<Usuario> listaUsuarios = servicioUsuario.GetUsuarios("Desactivar");
			modelo.put("listaUsuarios", listaUsuarios);
			modelo.put("usuario", usuarioVacio);
			modelo.put("buttonText", "Desactivar");
			return new ModelAndView("gestionDesactivar", modelo);
		} catch (Exception e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "activar", method = RequestMethod.POST)
	public ModelAndView activarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		if(!SesionHelper.ValidarRol(request))
		return new ModelAndView("redirect:/home");

		try {
			servicioUsuario.GestionarUsuarios(usuario, true);
			Usuario usuarioVacio = new Usuario();
			ModelMap modelo = new ModelMap();
			List<Usuario> listaUsuarios = servicioUsuario.GetUsuarios("Activar");
			modelo.put("listaUsuarios", listaUsuarios);
			modelo.put("usuario", usuarioVacio);
			modelo.put("buttonText", "Activar");
			return new ModelAndView("gestionActivar", modelo);
		} catch (Exception e) {
			return new ModelAndView("error");
		}
	}
	
	@RequestMapping(path = "/detalleGrupo", method = RequestMethod.GET)
	public ModelAndView irADetalleGrupo(HttpServletRequest request) {
		
		if(!SesionHelper.ValidarSesion(request))
		return new ModelAndView("redirect:/login");
		
		return new ModelAndView("detalleGrupo");
	}
	
	@RequestMapping(path = "/salir", method = RequestMethod.GET)
	public ModelAndView irASalir(HttpServletRequest request) {
		request.getSession().setAttribute("rol", "");
		request.getSession().setAttribute("nick", "");
		request.getSession().setAttribute("token", "");
		ModelMap modelo = new ModelMap();
		Usuario usuario = new Usuario();
		modelo.put("usuario", usuario);
		LOGGER.info("SESION - El usuario salió de su cuenta.");
		return new ModelAndView("login", modelo);
		//redirect:/
	}
	
	private String GetToken(HttpServletRequest request){
		return (String)request.getSession().getAttribute("token");
	}
	
	@RequestMapping(path = "activarcuenta", method = RequestMethod.GET)
	public ModelAndView irAModificarSucursal(@RequestParam(value="guid") String guid) {
		ModelMap model = new ModelMap();
		Usuario usuario = new Usuario();
		model.put("usuario", usuario);
		if(!SesionHelper.ValidarGuid(guid)){
			return new ModelAndView("error");
		}
		
		try {
			if(servicioUsuario.activarCuentaUsuario(guid)){
				model.put("respuesta", "Cuenta activada exitosamente, ya puede logear");
				return new ModelAndView("login", model);
			}else{
				model.put("error", "Error! La activación sólo se puede realizar 1 vez");
				return new ModelAndView("login", model);
			}
		} catch (Exception e) {
			return new ModelAndView("error");
		}
	}
}
