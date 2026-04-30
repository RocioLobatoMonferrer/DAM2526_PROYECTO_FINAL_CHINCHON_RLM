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
	 * @return Opciones disponibles para el usuario
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
	 *         Opción 2: Cerrar la ronda con una combinación de cartas
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
	 * Método que le pide al usuario que combinación desea hacer
	 * 
	 * @return Opciones disponibles para el usuario<br>
	 *         Opción 1: Grupos del mismo número. Ej: 3-3-3 <br>
	 *         Opción 2: Escalera. Ej: 1 2 3 <br>
	 *         Opción 3: Chinchón. Ej: 1 2 3 4 5 6 7
	 */

	public int selectCombination() {
		ci.writeLine("Seleccione que combinación deseas hacer:");
		return ci.readIntInRange(1, 3, "1. Grupos\n2. Escalera\n3. Chinchón\n");
	}

	/**
	 * Método que le pide el usuario que introduzca la combinación que desea
	 * 
	 * @return Combinación realizada por el usuario
	 */

	public String listCards() {
		ci.writeLine("Introduzca los índices de las cartas con espacios que deseas combinar.\nEjemplo: (1 2 3) ");
		return ci.readEmptyString();
	}

	/**
	 * Método que le dice al usuario que no puede cerrar
	 */

	public void errorClose() {
		ci.writeError("No puedes cerrar en el primer turno.");
	}

	/**
	 * Método que le dice al usuario que la combinación es incoreccta
	 */

	public void errorCombination() {
		ci.writeError("Combinación incorrecta.");
	}

	/**
	 * Método que le dice al usuario que no puede cerrar por pasarse de puntos
	 */

	public void errorPoints() {
		ci.writeError("No puedes cerrar ya que vas a sobrepasar el límite de puntos.");
	}
}
