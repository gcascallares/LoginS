package ar.edu.unlam.seguridad.controladores;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import Helpers.SesionHelper;
import Helpers.VerificarRecaptcha;
import ar.edu.unlam.seguridad.modelo.Usuario;
import ar.edu.unlam.seguridad.servicios.ServicioUsuario;

@Controller
public class ControladorLogin {
	
	private int intentos = 3;
	private String usuarioTemp;
	private Logger LOGGER = LoggerFactory.getLogger(ControladorLogin.class);
	
	@Inject
	private ServicioUsuario servicioUsuario;
	
	@RequestMapping("/login")
	public ModelAndView irALogin(HttpServletRequest request) {		
		SetearSesion(request, null);
		ModelMap modelo = new ModelMap();
		Usuario usuario = new Usuario();
		modelo.put("usuario", usuario);
		return new ModelAndView("login", modelo);
	}

	@RequestMapping(path = "/home", method = RequestMethod.POST)
	public ModelAndView validarLogin(@ModelAttribute("usuario") Usuario usuario, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		
		if(!SesionHelper.ValidarNick(usuario.getNick())){
			model.put("error", "Error. Nick invalido.");
			return new ModelAndView("login", model);
		}
			
		boolean verificado;
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		try {
			verificado = VerificarRecaptcha.verificar(gRecaptchaResponse);
		} catch (IOException e) {
			return new ModelAndView("error");
		}
		
		if(!verificado){
			model.put("error", "Capcha no verificado");
			return new ModelAndView("login", model);
		}
		
		if(intentos == 3)
			usuarioTemp = usuario.getNick();
		
		if(intentos <= 0){
			servicioUsuario.BloquearUsuario(usuario.getNick());
			intentos = 3;
			usuarioTemp = "";
			model.put("error", "Se ha desactivado el usuario " + usuario.getNick() + " por fallar al ingresar tres veces seguidas.");
			return new ModelAndView("login", model);
		}
		try {
			Usuario usuarioBuscado = servicioUsuario.consultarUsuarioLogin(usuario);
			
			if (usuarioBuscado != null) 
			{
				intentos = 3;
				usuarioTemp = "";
				ModelMap modelo = new ModelMap();
				if(usuarioBuscado.getEstado()==true){
					SetearSesion(request, usuarioBuscado);
					modelo.put("nombre", (String)request.getSession().getAttribute("nick"));
					LOGGER.info("USUARIOLOGIN - Logeó con con exito en el sistema.");
					return new ModelAndView("home", modelo);
				}
				else{
					model.put("error", "Usuario no activado");
					LOGGER.info("USUARIOLOGIN - Usuario inactivo intentó logear en el sistema.");
					return new ModelAndView("login", model);
				}
			} 
			else {
				
				if(usuarioTemp.equals(usuario.getNick())){
					intentos --;
				}else{
					intentos = 3;
				}
				LOGGER.info("USUARIOLOGIN - El usuario ingresó credenciales erroneas.");
				model.put("error", "Usuario o clave incorrecta");
			}
			return new ModelAndView("login", model);
		} catch (Exception e) {
			return new ModelAndView("error");
		}
	}

	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public ModelAndView irAHome(HttpServletRequest request) {
		if(!SesionHelper.ValidarSesion(request))
			return new ModelAndView("redirect:/login");
		ModelMap modelo = new ModelMap();
		modelo.put("nombre", (String)request.getSession().getAttribute("nick"));
		return new ModelAndView("home", modelo);
	}
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView inicio(HttpServletRequest request) {
		SetearSesion(request, null);
		return new ModelAndView("redirect:/login");
	}
	
	private void SetearSesion(HttpServletRequest request, Usuario usuario){
		if(usuario != null){
			request.getSession().setAttribute("rol", usuario.getRol());
			request.getSession().setAttribute("nick", usuario.getNick());
			request.getSession().setAttribute("token", usuario.getToken());
		}else{
			request.getSession().setAttribute("rol", "");
			request.getSession().setAttribute("nick", "");
			request.getSession().setAttribute("token", "");
		}
	}
}
