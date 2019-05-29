package Helpers;

import java.util.Date;

import ar.edu.unlam.seguridad.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenHelper {

	public final static String key = "grupo1";
	
    public static String GenerarTokenUsuario(Usuario usuario) {
    	return Jwts.builder()
                .claim("nick", usuario.getNick())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30000 * 10)) //le agrego 5 minutos a la sesion
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
	}
    
    public static boolean ComprobarTokenExpirado(String token){
    	try {
    		Claims claims;
    		claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
    		
    		if(claims.getExpiration().after(new Date(System.currentTimeMillis()))){
    			return true;
    		}else{
    			return false;
    		}
		} catch (Exception e) {
			return false;
		}
    }
}
