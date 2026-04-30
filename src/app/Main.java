package app;

/**
 * Clase que ejecutará el flujo del programa
 */

public class Main {

	/**
	 * Método donde se desarrolla todo el flujo del programa
	 */

	public void main() {
		IGame game = new Game();

		game.startConfiguration();

	}

	public static void main(String[] args) {
		new Main().main();
	}

}
