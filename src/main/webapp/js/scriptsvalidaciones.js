var password = document.getElementById("password");
var divTextoPass = document.getElementById("divTextoPass");
var btnSubm= document.getElementById("btnSubm");
var passwordNuevo = document.getElementById("passwordNuevo");
var nick = document.getElementById("nick");
var divTextoNick = document.getElementById("divTextoNick");

var nivelesColores = document.getElementById("nivelesColores");
var nivel;
var espanNivelesColores = document.getElementById("spanNivelesColores");

var nivelBajo = 12;
var nivelMedio = 14;
var nivelAlto = 16;

function MostrarOcultarPass() {

	if (password.type == "password") {
		password.type = "text";
	}
	else{
		password.type = "password";
	}
}

function CompletarNick(){
	if(nick.value.length < 4 || nick.value.length > 20){
		divTextoNick.removeAttribute("hidden");
		btnSubm.disabled = true;
	}
	else{
		divTextoNick.setAttribute("hidden", "hidden")
		btnSubm.disabled = false;
	}		  
}

function CompletarPass(){
	numCaracteres = password.value.length;
	if (numCaracteres > 0 && numCaracteres < nivelBajo) {
        nivel = "bajo";
                espanNivelesColores.className = '';
                espanNivelesColores.classList.add("bajo","spanNivelesColores");  
      } 
      else if (numCaracteres >= nivelBajo && numCaracteres < nivelMedio) {
        nivel = "medio";
        espanNivelesColores.className = '';
		        espanNivelesColores.classList.add("medio","spanNivelesColores");  
      } 
      else if (numCaracteres >= nivelMedio && numCaracteres < nivelAlto) {
        nivel = "alto";
        espanNivelesColores.className = '';
		        espanNivelesColores.classList.add("alto","spanNivelesColores");  
      } 
      else if (numCaracteres >= nivelAlto) {
        nivel = "muy alto";
        espanNivelesColores.className = '';
		        espanNivelesColores.classList.add("muyAlto","spanNivelesColores");  
      }
      if (numCaracteres === 0) {
    	  espanNivelesColores.className = '';
    	  espanNivelesColores.classList.add("bajo","spanNivelesColores"); 
      }
	
	if(password.value.length < 12 || password.value.length > 72){
		divTextoPass.removeAttribute("hidden");
		btnSubm.disabled = true;
	}
	else{
		divTextoPass.setAttribute("hidden", "hidden")
		btnSubm.disabled = false;
	}		  
}

function MostrarOcultarPassNueva() {

	if (passwordNuevo.type == "password") {
		passwordNuevo.type = "text";
	}
	else{
		passwordNuevo.type = "password";
	}
}

function CompletarCambioPass(){
	numCaracteres = passwordNuevo.value.length;
	if (numCaracteres > 0 && numCaracteres < nivelBajo) {
        nivel = "bajo";
                espanNivelesColores.className = '';
                espanNivelesColores.classList.add("bajo","spanNivelesColores");  
      } 
      else if (numCaracteres >= nivelBajo && numCaracteres < nivelMedio) {
        nivel = "medio";
        espanNivelesColores.className = '';
		        espanNivelesColores.classList.add("medio","spanNivelesColores");  
      } 
      else if (numCaracteres >= nivelMedio && numCaracteres < nivelAlto) {
        nivel = "alto";
        espanNivelesColores.className = '';
		        espanNivelesColores.classList.add("alto","spanNivelesColores");  
      } 
      else if (numCaracteres >= nivelAlto) {
        nivel = "muy alto";
        espanNivelesColores.className = '';
		        espanNivelesColores.classList.add("muyAlto","spanNivelesColores");  
      }
      if (numCaracteres === 0) {
    	  espanNivelesColores.className = '';
    	  espanNivelesColores.classList.add("bajo","spanNivelesColores"); 
      }
	
	if(passwordNuevo.value.length < 12 || passwordNuevo.value.length > 72){
		divTextoPass.removeAttribute("hidden");
		btnSubm.disabled = true;
	}
	else{
		divTextoPass.setAttribute("hidden", "hidden")
		btnSubm.disabled = false;
	}		  
}
