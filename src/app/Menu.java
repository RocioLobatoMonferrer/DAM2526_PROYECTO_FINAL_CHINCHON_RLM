package app;

import dominio.IDeckCard;
import dominio.IEntity;

/**
 * Clase encargada de mostrar todo tipo de menús y mensajes al usuario
 */

public class Menu {
	private ConsoleInput ci;
	private static Menu instance;

	/**
	 * Constructor de la clase que la instancia
	 */

	public Menu() {
		ci = ConsoleInput.getInstance();
	}

	/**
	 * Método que inicializa la instancia de la clase para que sea única
	 * 
	 * @return Instancia de la clase
	 */

	public static Menu getInstance() {
		if (instance == null) {
			instance = new Menu();
		}
		return instance;
	}

	/**
	 * Método que cierra la consola
	 */

	public void closeConsole() {
		ci.close();
	}

	/**
	 * Método que muestra el menú principal de la partida
	 * 
	 * @return Opciones disponibles para el usuario<br>
	 *         Opción 1: Iniciar la partida<br>
	 *         Opción 2: Salir del programa
	 */

	public int mainMenu() {
		ci.writeLine("      CHINCHÓN\n   ______________\n");
		return ci.readIntInRange(1, 2, "1. Iniciar Partida\n2. Salir\n");
	}

	/**
	 * Método que pide al usuario los puntos que quiere para la partida
	 * 
	 * @return Opción disponible para el usuario, mínimo debe ser un punto 1
	 */

	public int howManyPoints() {
		return ci.readIntGreaterOrEqualThan(1, "Introduce cuantos puntos deseas para tu partida.");
	}

	/**
	 * Método que pide cuantos jugadores quiere el usuario
	 * 
	 * @return Opciones disponibles para el usuario, siendo mínimo deben ser 2
	 *         jugadores y como máximo 5
	 */

	public int numberOfPlayers() {
		return ci.readIntInRange(2, 5, "Introduce el número de jugadores.");
	}

	/**
	 * Método que pide al usuario que introduzca su apodo para la partida
	 * 
	 * @return Introducción de apodo de los jugadores
	 */

	public String nicknameOfPlayers() {
		ci.writeLine("Introduce el nombre para este jugador:");
		return ci.readEmptyString();
	}

	/**
	 * Método que le pide al usuario si desea un jugador o una CPU
	 * 
	 * @return Opciones disponibles para el usuario<br>
	 *         Opción J: Jugador<br>
	 *         Opción C: CPU
	 */

	public boolean typeOfEntity() {
		ci.writeLine("Introduce J si lo que quieres es un jugador o C para una CPU.");
		return ci.readBooleanUsingChar('J', 'C',
				"Debes introducir una J si lo que quieres es un jugador o C para una CPU.");
	}

	/**
	 * Método que muestra que turno se encuentran los jugadores
	 * 
	 * @param turn El número del turno en cuestión
	 */

	public void showTurn(int turn) {
		ci.writeLine(String.format("        TURNO %d\n   ______________\n", turn));
	}

	/**
	 * Método que muestra la mano del jugador
	 * 
	 * @param player Jugador en concreto
	 */

	public void showHand(IEntity player) {
		ci.writeLine(player.showHand());
	}

	/**
	 * Método que enseña la primera carta en la pila de descartes
	 * 
	 * @param deck Baraja de descarte
	 */

	public void showFirstDiscardCard(IDeckCard deck) {
		ci.writeLine(deck.getFirstDiscardCard().toString());
	}

	/**
	 * Método que muestra de que jugador es el turno
	 * 
	 * @param nickname Apodo del jugador
	 */

	public void showPlayerTurn(String nickname) {
		ci.writeLine(String.format("TURNO DE %s:\n   ", nickname));
	}

	/**
	 * Método que le pide al usuario de que baraja quiere robar
	 * 
	 * @param discardCard Carta descartada
	 * @return Opciones disponibles para el usuario<br>
	 *         Opción 1: Robar de la baraja principal<br>
	 *         Opción 2: Robar de la baraja de descartes
	 */

	public int turnMenu(String discardCard) {
		ci.writeLine("Seleccione de donde deseas robar tu siguiente carta:");
		return ci.readIntInRange(1, 2, String.format("1. Robar del mazo\n2. Robar del descarte [%s]\n", discardCard));
	}

	/**
	 * Método que le pide al usuario si desea descartar o cerrar
	 * 
	 * @return Opciones disponibles para el usuario<br>
	 *         Opción 1: Descartar una carta<br>
	 *         Opción 2: Cerrar la ronda con combinaciones de cartas
	 */

	public int turnMenu2() {
		ci.writeLine("Seleccione una de las siguientes opciones:");
		return ci.readIntInRange(1, 2, "1. Descartar\n2. Cerrar\n");
	}

	/**
	 * Método que le pide al usuario que carta desea descartar de las 8 que tiene en
	 * mano
	 * 
	 * @return Opciones disponibles para el usuario
	 */

	public int selectToDiscard() {
		return ci.readIntInRange(1, 8, "Seleccione cual de sus cartas desea descartar:\n");
	}

	/**
	 * Método que le pide el usuario que introduzca la combinación que desea
	 * 
	 * @return Combinación realizada por el usuario
	 */

	public String listCards() {
		ci.writeLine(
				"Introduzca los índices de las cartas con espacios que deseas combinar.\nSi no desea introducir ninguna combinación, pulse ENTER\nEjemplo: (1 2 3) ");
		return ci.readEmptyString();
	}

	/**
	 * Método que te pide seleccionar cual de tus cartas deseas descartar
	 * 
	 * @return Carta seleccionada por el usuario
	 */

	public int selectClosingCard() {
		return ci.readIntInRange(1, 2, "Seleccione si desea descartar la primera o la segunda carta para cerrar:\n");
	}

	/**
	 * Método que le indica al usuario que carta ha sido descartar para cerrar la
	 * ronda
	 * 
	 * @param card Carta descartada para cerrar
	 */

	public void showClosingCard(String card) {
		ci.writeLine(String.format("\nDescartas %s para cerrar la ronda.\n", card));
	}

	/**
	 * Método que indica el fin de la ronda
	 */

	public void showRoundEnd() {
		ci.writeLine("\n        FIN DE LA RONDA\n");
	}

	/**
	 * Método que indica que el cierre ha sido con todas sus cartas y se le restarán
	 * puntos
	 */

	public void showPerfectClosing() {
		ci.writeLine("¡Cierre perfecto!\nSe te restarán 10 puntos.\n");
	}

	/**
	 * Método que muestra la puntuación actual del usuario y los puntos que ha
	 * ganado en esa ronda
	 * 
	 * @param player Jugador en cuestión
	 * @param points Cantidad de puntos obtenidos
	 */

	public void showRoundScore(IEntity player, int points) {
		ci.writeLine(String.format("%s (Has recibido %s%d en esta ronda)", player.toString(), points < 0 ? "" : "+",
				points));
	}

	/**
	 * Método que indica que el jugador ha sido eliminado de la partida
	 * 
	 * @param nickname Apodo del jugador
	 */

	public void showPlayerOut(String nickname) {
		ci.writeLine(String.format("\nEl jugador %s ha sido eliminado.\n", nickname));
	}

	/**
	 * Método que muestra quien es el ganador de la partida
	 * 
	 * @param nickname Apodo del jugador
	 */

	public void showWinner(String nickname) {
		ci.writeLine(String.format("¡El ganador es %s! ¡Felicidades!\n", nickname));
	}

	/**
	 * Método que muestra quien es el ganador de la partida por hacer un Chinchón
	 * 
	 * @param nickname Apodo del jugador
	 */

	public void showWinnerForChinchon(String nickname) {
		ci.writeLine(
				String.format("¡Felicidades, %s!\nHas realizado un Chinchón, por lo tanto has ganado.\n", nickname));
	}

	/**
	 * Método que le dice al usuario que no puede cerrar
	 */

	public void errorClose() {
		ci.writeError("No puedes cerrar en el primer turno.\n");
	}

	/**
	 * Método que le dice al usuario que la combinación es incoreccta
	 */

	public void errorCombination() {
		ci.writeError("Combinación incorrecta, por favor, intentelo de nuevo.\n");
	}

	/**
	 * Método que le dice al usuario que no puede cerrar por pasarse de puntos
	 */

	public void errorPoints() {
		ci.writeError("No puedes cerrar ya que vas a sobrepasar el límite de puntos.\n");
	}

	/**
	 * Método que le dice al usuario que no puede cerrar con una carta superior que
	 * 5
	 */

	public void errorCloseCard() {
		ci.writeError("No puedes cerrar con una carta que es mayor que 5.\n");
	}

	/**
	 * Método que pinta 50 líneas vacías para separar los turnos de los jugadores
	 */

	public void fakeClearConsole() {
		for (int i = 0; i < 50; i++) {
			ci.writeLine("");
		}
	}
}
