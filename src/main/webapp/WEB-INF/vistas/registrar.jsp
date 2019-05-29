<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>
	<head>
		<%@include file="styles/styles.jsp"%> 
	</head>
	<body>
		<div id="wrapper">
			<%@include file="menusl.jsp"%>
			<section id="content">
		      <div class="container">
	
		        <div class="row">
		          <div class="span12">
			          <div class="aligncenter"> 
				        <h5>Registrate</h5>
						<form:form method="POST" action="registrarusuario" modelAttribute="usuario">
							<form:label path="nick">Usuario</form:label>
							<div class="form-group input-size">
								<form:input path="nick" id="nick" type="text" class="form-control nickreg" required="true" pattern="^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$" placeholder="Ingrese un nick"/>							
							</div>
							<div class="form-group input-size" id="divTextoNick" hidden="true">
								<form:label path="nick" id="textoNick">El nick debe tener entre 4 y 20 caracteres</form:label>  							
							</div>
							
							<form:label path="password">Contraseña</form:label> 
							<div class="form-group input-size">
								<form:input path="password" type="password" id="password" class="form-control passreg" autocomplete="off" required="true" pattern="(?=.*\d)(?=.*[a-záéíóúüñ]).*[A-ZÁÉÍÓÚÜÑ].*" placeholder="Ingrese una contraseña"/> 							
							</div>
							
							<input type="checkbox" class="chevent"/> Ver contraseña
							
							<label>Nivel de seguridad de la contraseña. Recuerde que debe estar compuesta por mayusculas, minusculas y numeros.</label>
							<div class="nivelSeguridad">
							    <div class="nivelesColores">
							      <div id="spanNivelesColores" class="spanNivelesColores"></div>
							    </div>
							  
							  <div class="NivelesColores"></div>
							</div>
							
							<div class="form-group input-size" id="divTextoPass" hidden="true">
								<form:label path="password" id="textoPass">La logitud no puede ser menor a 12 caracteres ni mayor a 72 caracteres</form:label>  							
							</div>
							<form:label path="respuestaSeguridad">¿Cual es el nombre de tu mascota?</form:label> 
							<div class="form-group input-size">
								<form:input path="respuestaSeguridad" autocomplete="off" type="text" id="respuestaSeguridad" class="form-control" required="true" placeholder="Ingrese su respuesta"/> 							
							</div>
							<form:label path="mail">Email</form:label> 
							<div class="form-group input-size">
								<form:input path="mail" type="email" id="mail" class="form-control" required="true" pattern="^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$" placeholder="example@example.com"/> 							
							</div>
							<button type="submit" class="btn btn-primary" id="btnSubm">Registrar</button>
						</form:form>	

						<c:if test="${not empty error}">
					        <span>${error}</span>
					        <br>
				        </c:if>
				        <c:if test="${not empty respuesta}">
					        <span>${respuesta}</span>
					        <br>
				        </c:if>						
			          </div>
		          </div>
		        </div>
		      </div>
		    </section>
 			<%-- <%@include file="footer.jsp"%> --%>
			<%@include file="scripts/scripts.jsp"%> 
			<script src="/seguridad/js/validreg.js"></script>
		</div>
	</body>
</html>