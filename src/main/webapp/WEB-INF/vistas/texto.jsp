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
				          <h4>Mi Texto</h4>
							<form:form method="POST" action="texto-actualizar" modelAttribute="texto">	
								<div class="form-group input-size">
									<form:input path="descripcion" type="text" class="form-control formpropio" id="descripcion"  name="descripcion" value="${texto.descripcion}" pattern="^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$"></form:input>
								</div><br>
							    <button type="submit" class="btn btn-primary">Modificar</button><span>   </span><a href="texto-eliminar"><button type="button" class="btn btn-primary">Eliminar</button></a>
							</form:form>						
							<c:if test="${not empty error}">
					        <span>${error}</span>
					        <br>
				        </c:if>
			          </div>
		          </div>
		        </div>
		      </div>
		    </section>
			<%@include file="footer.jsp"%>
			<%@include file="scripts/scripts.jsp"%> 
		</div>
	</body>
</html>