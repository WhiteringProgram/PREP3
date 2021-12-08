package view;

import controller.ControladorDeArquivos;

public class Principal {

	public static void main(String[] args) {
		ControladorDeArquivos controller = new ControladorDeArquivos ();
		controller.dadosArquivo();
		controller.filtraArquivo();
		controller.controleDeArquivo();
	}
}
