    <!-- start header -->
    <header>
       <div class="top">
        <div class="container">
          <div class="row">
            <div class="span6 titulomateria">
              Seguridad y calidad en Aplicaciones Web
            </div>
            <div class="span6"> <ul class="social-network">
                <li class="colormenutop">Usuario <span>${sessionScope.nick}</span></li>
              </ul></div>
          </div>
        </div>
      </div>
      <div class="container">


        <div class="row nomargin">
          <div class="span4">
            <div class="logo">
              <h1><i class="icon-bug"></i> Seguridad</h1>
            </div>
          </div>
          <div class="span8">
            <div class="navbar navbar-static-top">
              <div class="navigation">
                <nav>
                  <ul class="nav topnav">
                    <li><a href="home">Home</a></li>
                    <li><a href="texto">Texto</a></li>
                    <c:if test="${sessionScope.rol.equals('admin')}">
						<li class="dropdown">
	                      <a href="#">Gestion Usuarios <i class="icon-angle-down"></i></a>
	                      <ul class="dropdown-menu">
	                        <li><a href="activarusuario">Activar</a></li>
	                        <li><a href="desactivarusuario">Desactivar</a></li>
	                      </ul>
	                    </li>   
					</c:if>
                    <li><a href="auditoria">Historial</a></li>      
                    <li class="dropdown">
                      <a href="#">Perfil <i class="icon-angle-down"></i></a>
                      <ul class="dropdown-menu">
                        <li><a href="cambiarpass">Cambiar contraseña</a></li>
                        <li><a href="detalleGrupo">Detalle Grupo</a></li>
                      </ul>
                    </li>
                    <li>
                      <a href="salir">Salir </a>
                    </li>
                  </ul>
                </nav>
              </div>
              <!-- end navigation -->
            </div>
          </div>
        </div>
      </div>
    </header>