package ar.edu.unlam.seguridad.dao;

import ar.edu.unlam.seguridad.modelo.Texto;
import ar.edu.unlam.seguridad.modelo.Usuario;
import ar.edu.unlam.seguridad.modelo.UsuarioAuditoria;
import ar.edu.unlam.seguridad.modelo.UsuarioSalt;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("unchecked")
@Repository("usuarioDao")
public class UsuarioDaoImpl implements UsuarioDao {

	@Inject
    private SessionFactory sessionFactory;

	@Override
	public Usuario consultarUsuarioLogin(Usuario usuario) {
		final Session session = sessionFactory.getCurrentSession();
		Usuario usuarioBuscado = (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("nick", usuario.getNick()))
				.add(Restrictions.eq("password", usuario.getPassword()))
				.uniqueResult();
		return usuarioBuscado;
	}
	
	@Override
	public List<Usuario> verSiExisteUsuario(String nick) {
		final Session session = sessionFactory.getCurrentSession();
		List<Usuario> existeUsuario = session.createCriteria(Usuario.class)
				.add(Restrictions.eq("nick", nick))
				.list();
		return existeUsuario;
	}
	
	@Override
	public Usuario guardarUsuario(Usuario usuario) {
		final Session session = sessionFactory.getCurrentSession();
		session.save(usuario);
		return usuario;
	}

	@Override
	public Texto getTextoUsuario(Long idUsuario) {
		final Session session = sessionFactory.getCurrentSession();
		Texto texto = (Texto)session.createCriteria(Texto.class)
				.add(Restrictions.eq("usuario.id", idUsuario)).uniqueResult();
		return texto;
	}

	@Override
	public Usuario ObtenerUsuarioPorId(Long idUsuario) {
		final Session session = sessionFactory.getCurrentSession();
		Usuario usuarioBuscado = (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("id", idUsuario))
				.uniqueResult();	
		return usuarioBuscado;
	}

	@Override
	public void crearTexto(Texto texto) {
		final Session session = sessionFactory.getCurrentSession();
		session.save(texto);
	}

	@Override
	public void ActualizarTextoUsuario(Long idUsuario, String texto) {
		final Session session = sessionFactory.getCurrentSession();
		Texto textoUsuario = (Texto)session.createCriteria(Texto.class)
				.add(Restrictions.eq("usuario.id", idUsuario)).uniqueResult();
		textoUsuario.setDescripcion(texto);
		session.update(textoUsuario);
	}

	@Override
	public void CrearUsuarioAuditoria(UsuarioAuditoria usuarioAuditoria) {
		final Session session = sessionFactory.getCurrentSession();
		session.save(usuarioAuditoria);	
	}

	@Override
	public List<UsuarioAuditoria> ListarAuditoriasUsuario(Usuario usuario) {
		final Session session = sessionFactory.getCurrentSession();
		List<UsuarioAuditoria> auditorias = session.createCriteria(UsuarioAuditoria.class)
				.add(Restrictions.eq("usuario", usuario))
				.list();
		return auditorias;
	}

	@Override
	public List<UsuarioAuditoria> ListarAuditoriasUsuarios() {
		final Session session = sessionFactory.getCurrentSession();
		List<UsuarioAuditoria> auditorias = session.createCriteria(UsuarioAuditoria.class)
				.list();
		return auditorias;
	}

	@Override
	public Usuario consultarUsuarioPreguntaSecreta(Usuario usuario) {
		
		final Session session = sessionFactory.getCurrentSession();
		Usuario usuarioBuscado = (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("nick", usuario.getNick()))
				.add(Restrictions.eq("respuestaSeguridad", usuario.getRespuestaSeguridad()))
				.uniqueResult();
		
		return usuarioBuscado;
	}

	@Override
	public void ActualizarUsuario(Usuario usuario) {
		final Session session = sessionFactory.getCurrentSession();
		session.update(usuario);
		
	}

	@Override
	public List<Usuario> GetUsuarios(Boolean estado) {
		final Session session = sessionFactory.getCurrentSession();
		List<Usuario> usuarios = session.createCriteria(Usuario.class)
				.add(Restrictions.eq("estado", estado))
				.add(Restrictions.eq("rol", "user"))
				.list();
		return usuarios;
	}

	@Override
	public List<Usuario> GetUsuarios() {
		final Session session = sessionFactory.getCurrentSession();
		List<Usuario> usuarios = session.createCriteria(Usuario.class)
				.list();
		return usuarios;
	}

	@Override
	public Usuario ObtenerUsuarioPorToken(String token) {
		final Session session = sessionFactory.getCurrentSession();
		Usuario usuarioBuscado = (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("token", token))
				.uniqueResult();	
		return usuarioBuscado;
	}

	@Override
	public Usuario ConsultarUsuarioPorNick(String nick) {
		final Session session = sessionFactory.getCurrentSession();
		Usuario usuarioBuscado = (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("nick", nick))
				.uniqueResult();	
		return usuarioBuscado;
	}

	@Override
	public void GuardarUsuarioSalt(UsuarioSalt usuarioSalt) {
		final Session session = sessionFactory.getCurrentSession();
		session.save(usuarioSalt);
	}

	@Override
	public Usuario ObtenerUsuarioPorGuid(String guid) {
		final Session session = sessionFactory.getCurrentSession();
		return (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("guid", guid))
				.uniqueResult();
	}
}
