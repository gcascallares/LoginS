var passreg = document.getElementsByClassName("passreg")
var chevent = document.getElementsByClassName("chevent")

passreg[0].addEventListener("keyup", CompletarCambioPass, true);
passreg[0].addEventListener("keydown", CompletarCambioPass, true);

chevent[0].addEventListener("click", MostrarOcultarPassNueva, true);