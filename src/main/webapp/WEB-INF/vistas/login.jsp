<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>
	<head>
		<%@include file="styles/styles.jsp"%> 
		<script src="https://www.google.com/recaptcha/api.js"></script>
	</head>
	<body>
		<div id="wrapper">
			<%@include file="menusl.jsp"%>
			<section id="content">
		      <div class="container">
	
		        <div class="row">
		          <div class="span12">
			          <div class="aligncenter"> 
				        <h5>Login</h5>
						<form:form method="POST" action="home" modelAttribute="usuario">
							<form:label path="nick">Usuario</form:label>
							<div class="form-group input-size">
								<form:input path="nick" type="text" id="nick" class="form-control" pattern="^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$" required="true"/>  								
							</div>
							<form:label path="password">Contraseña</form:label>
							<div class="form-group input-size">
								<form:input path="password" type="password" id="password" class="form-control" autocomplete="off" required="true"/>								
							</div>
							<div class="form-group input-size">
								<div class="g-recaptcha" data-sitekey="6LdaLqUUAAAAACa1WvqxBPRAfqLCgve0cIdTxjtj"></div>
							</div>
							<Span class="resp"><a href="reestablecerpassword">Olvide mi contraseña</a></Span><br>							
							<button type="submit" class="btn btn-primary">Login</button>
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
			<%@include file="footer.jsp"%>
			<%@include file="scripts/scripts.jsp"%> 
		</div>
	</body>
</html>