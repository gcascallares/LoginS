package Helpers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class SesionHelper {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SesionHelper.class);
	private static List<String> passwordsQueNoQuiero = Arrays.asList("rivercapo", "bocacampeonbocacampeon", "river","boca", "Bocacampeon123");
	private static List<String> nicksProhibidos = Arrays.asList("admin", "root", "sa");
	
	public static boolean VerificarPasswordInseguro(String password){
		if(passwordsQueNoQuiero.contains(password)){
			return true;
		}
		return false;
	}
	
	public static boolean VerificarNickProhibido(String nick){
		if(nicksProhibidos.contains(nick)){
			return true;
		}
		return false;
	}
	
	public static boolean ValidarSesion(HttpServletRequest request){
		String token = (String) request.getSession().getAttribute("token");
		
		try {
    		Claims claims;
    		claims = Jwts.parser()
                    .setSigningKey(TokenHelper.key)
                    .parseClaimsJws(token)
                    .getBody();
    		
    		if(claims.getExpiration().after(new Date(System.currentTimeMillis())) && !token.equals("")){
    			return true;
    		}else{
    			request.getSession().setAttribute("rol", "");
    			request.getSession().setAttribute("nick", "");
    			request.getSession().setAttribute("token", "");
    			LOGGER.info("SESION - La sesion del usuario ha expirado.");
    			return false;
    		}
		} catch (Exception e) {
			request.getSession().setAttribute("rol", "");
			request.getSession().setAttribute("nick", "");
			request.getSession().setAttribute("token", "");
			LOGGER.info("SESION - " + e.getMessage());
			return false;
		}
	}
	
	public static boolean ValidarRol(HttpServletRequest request){
		String rol = (String)request.getSession().getAttribute("rol");
		if(rol.equals("admin")){
			return true;
		}else{
			LOGGER.info("AUTORIZACION - Un usuario normal intentó acceder a una funcionalidad de administrador.");
			return false;
		}
	}
	
	public static boolean ValidarPass(String password) {
		if(password.length()<12 || password.length()>72 || !ValidarSeguridadPass(password)) {
			LOGGER.info("CREDENCIALES - El usuario omitió la validación de credenciales de js.");
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean ValidarNick(String nick) {
		if(nick.length()<4 || nick.length()>20 || !ValidarString(nick)) {
			LOGGER.info("CREDENCIALES - El usuario omitió la validación de credenciales de js.");
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean ValidarEmail(String email) {
		Pattern validEmail = 
			    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = validEmail.matcher(email);
        return matcher.find();
	}
	
	public static boolean ValidarTexto(String texto) {
		if(texto.length()>100 || !ValidarString(texto)) {
			LOGGER.info("CREDENCIALES - El usuario omitió la validación de credenciales de js.");
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean ValidarString(String string) {
		Pattern validString = 
			    Pattern.compile("^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = validString.matcher(string);
		boolean res = matcher.find();
		if(!res)
			LOGGER.info("USUARIOLOGIN FALLIDO - Se intenta ingresar con un nick compuesto por caracteres prohibidos.");
        return res;
	}
	
	public static boolean ValidarGuid(String guid) {
		Pattern validGuid = 
			    Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})", Pattern.CASE_INSENSITIVE);
		Matcher matcher = validGuid.matcher(guid);
        return matcher.find();
	}
	
	public static boolean ValidarSeguridadPass(String guid) {
		Pattern validGuid = 
			    Pattern.compile("(?=.*\\d)(?=.*[a-záéíóúüñ]).*[A-ZÁÉÍÓÚÜÑ].*", Pattern.CASE_INSENSITIVE);
		Matcher matcher = validGuid.matcher(guid);
        return matcher.find();
	}
}
