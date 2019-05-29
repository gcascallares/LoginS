<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>
	<head>
		<%@include file="styles/styles.jsp"%> 
	</head>
	<body>
		<div id="wrapper">
			<%@include file="menu.jsp"%>
			<section id="content">
		      <div class="container">
	
		        <div class="row">
		          <div class="span12">
			          <div class="aligncenter"> 
				        <h5>Cambiar Contraseña</h5>
						<form:form method="POST" action="cambiarpass-validar" modelAttribute="usuarioviewmodel">
							<form:label path="passwordAntiguo">Ingrese su contraseña actual</form:label>
							<div class="form-group input-size">
								<form:input path="passwordAntiguo" type="password" id="passwordAntiguo" class="form-control" required="true" autocomplete="off"/>  								
							</div>
							<form:label path="passwordNuevo">Ingrese nueva contraseña</form:label> 
							<div class="form-group input-size">
								<form:input path="passwordNuevo" type="password" id="passwordNuevo" autocomplete="off" class="form-control passreg" required="true" pattern="(?=.*\d)(?=.*[a-záéíóúüñ]).*[A-ZÁÉÍÓÚÜÑ].*"/>								
							</div>
							<input type="checkbox" class="chevent"/> Ver contraseña
							<label>La contraseña debe estar compuesta por mayusculas, minusculas y numeros.</label>
							<label>Nivel de seguridad de la contraseña. Recuerde que debe estar compuesta por mayusculas, minusculas y numeros.</label>
							<div class="nivelSeguridad">
							    <div class="nivelesColores">
							      <div id="spanNivelesColores" class="spanNivelesColores"></div>
							    </div>
							 </div>   
							<div class="form-group input-size" id="divTextoPass" hidden="true">
								<form:label path="passwordNuevo" id="textoPass">La logitud no puede ser menor a 12 caracteres ni mayor a 72 caracteres</form:label>  							
							</div>
							<button type="submit" class="btn btn-primary" id="btnSubm">Modificar</button>
						</form:form>	
						<c:if test="${not empty respuesta}">
					        <span>${respuesta}</span>
					        <br>
				        </c:if>						
			          </div>
		          </div>
		        </div>
		      </div>
		    </section>
			<%@include file="footer.jsp"%>
			<%@include file="scripts/scripts.jsp"%> 
			<script src="/seguridad/js/validcam.js"></script>
		</div>
	</body>
</html>