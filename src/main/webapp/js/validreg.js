var passreg = document.getElementsByClassName("passreg")
var nickreg = document.getElementsByClassName("nickreg")
var chevent = document.getElementsByClassName("chevent")

passreg[0].addEventListener("keyup", CompletarPass, true);
passreg[0].addEventListener("keydown", CompletarPass, true);
nickreg[0].addEventListener("keyup", CompletarNick, true);
nickreg[0].addEventListener("keydown", CompletarNick, true);
chevent[0].addEventListener("click", MostrarOcultarPass, true);