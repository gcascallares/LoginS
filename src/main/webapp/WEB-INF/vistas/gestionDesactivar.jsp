
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
			          <h4>Desactivar Usuarios</h4>
				          <form:form class="text-center" method="post" action="desactivar" modelAttribute="usuario">							
						    <div class="form-group input-size">
							    <form:select required="true" path="id" id="id" name="id" Class="form-control">
							 		<option value="" selected> Seleccione Usuario..
							 		<c:forEach items= "${listaUsuarios}" var="usuario">
							 			<option value="${usuario.id}">${usuario.nick}	
							 		</c:forEach>
						  		</form:select>
  							</div>						    
			    						    
						    <button type="submit" id="btn-modificar" value="modificar" class="btn btn-primary">Desactivar</button>
						</form:form>
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
