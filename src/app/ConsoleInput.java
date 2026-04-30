package app;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase encargada de la entrada y salida por consola.
 */

public class ConsoleInput {

	private Scanner kb;
	private static ConsoleInput instance;

	/**
	 * Constructor de la clase que la instancia
	 */

	public ConsoleInput() {
		kb = new Scanner(System.in);
	}

	/**
	 * Método que inicializa la instancia de la clase para que sea única
	 * 
	 * @return Instancia de la clase
	 */

	public static ConsoleInput getInstance() {
		if (instance == null) {
			instance = new ConsoleInput();
		}
		return instance;
	}

	/**
	 * Método que cierra la consola
	 */

	private void cleanInput() {
		kb.nextLine();
	}

	/**
	 * Método que lee un entero introducido por un usuario
	 * 
	 * @param message Mensaje para el usuario
	 * @return Valor que confirma que es un número
	 */

	public int readInt(String message) {
		int value = 0;
		boolean error;
		writeLine(message);
		do {
			try {
				value = kb.nextInt();
				error = false;
			} catch (InputMismatchException e) {
				System.out.printf(
						"%sEl valor del integer debe ser de tipo númerico entero y comprendido entre el rango %d - %d.%s\n",
						"\u001B[31m", Integer.MIN_VALUE, Integer.MAX_VALUE, "\u001B[0m");
				error = true;
			} finally {
				cleanInput();
			}
		} while (error);
		return value;
	}

	/**
	 * Método que lee el entero introducido por el usuario que será mayor o igual al
	 * mínimo
	 * 
	 * @param lowerBound Valor mínimo
	 * @param message    Mensaje para el usuario
	 * @return Valor que cumple con las condiciones
	 */

	public int readIntGreaterOrEqualThan(int lowerBound, String message) {
		int value = 0;

		do {
			value = readInt(message);
			if (value < lowerBound) {
				System.out.printf("%sEl valor del integer debe ser mayor o igual que %d.%s\n", "\u001B[31m", lowerBound,
						"\u001B[0m");
			}
		} while (value < lowerBound);
		return value;
	}

	/**
	 * Método que lee el entero introducido por el usuario en un rango en concreto
	 * 
	 * @param lowerBound Valor del rango mínimo
	 * @param upperBound Valor del rango máximo
	 * @param message    Mensaje para el usuario
	 * @return Valor que cumple con las condiciones de rango
	 */

	public int readIntInRange(int lowerBound, int upperBound, String message) {
		int value = 0;
		do {
			value = readInt(message);
			if (value < lowerBound || value > upperBound) {
				System.out.printf("%sEl valor del integer debe estar comprendido entre %d y %d (ambos incluidos).%s\n",
						"\u001B[31m", lowerBound, upperBound, "\u001B[0m");
			}
		} while (value < lowerBound || value > upperBound);
		return value;
	}

	/**
	 * Método que lee el carácter introducido
	 * 
	 * @param message Mensaje para el usuario
	 * @return Carácter introducido
	 */

	public char readChar(String message) {
		String value = "";

		if (!message.isEmpty()) {
			writeLine(message);
		}

		do {
			value = readString(message).toLowerCase();
			if (value.trim().length() != 1) {
				System.out.printf("%sEl valor del char debe ser de un único caracter.%s\n", "\u001B[31m", "\u001B[0m");
			}
		} while (value.trim().length() != 1);
		return value.trim().charAt(0);
	}

	/**
	 * Método que lee la cadena de texto introducida
	 * 
	 * @param message Mensaje para el usuario
	 * @return Cadena de texto introducida
	 */

	public String readString(String message) {
		String value = "";

		if (!message.isEmpty()) {
			writeLine(message);
		}

		do {
			value = kb.nextLine();
			if (value.trim().isEmpty()) {
				System.out.printf("%sNo puedes introducir una cadena vacía, debe contener al menos un caracter.%s\n",
						"\u001B[31m", "\u001B[0m");
			}
		} while (value.trim().isEmpty());
		return value;
	}

	/**
	 * Método que lee si el carácter introducido es correcto o no
	 * 
	 * @param affirmativeValue Carácter correcto
	 * @param negativeValue    Carácter incorrecto
	 * @param error            Mensaje de error para el usuario
	 * @return true/false dependiendo de la entrada del usuario
	 */
	public boolean readBooleanUsingChar(char affirmativeValue, char negativeValue, String error) {
		char value = '¬';
		String message = "";
		do {
			value = readChar(message);
			if (value != Character.toLowerCase(affirmativeValue) && value != Character.toLowerCase(negativeValue)) {
				System.out.printf("%s%s%s\n", "\u001B[31m", error, "\u001B[0m");
			}
		} while (value != Character.toLowerCase(affirmativeValue) && value != Character.toLowerCase(negativeValue));
		if (value == Character.toLowerCase(affirmativeValue)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Método que lee una cadena de texto vacía
	 * 
	 * @return Cadena de texto introducida
	 */

	public String readEmptyString() {
		String value = "";
		value = kb.nextLine();
		return value;
	}

	/**
	 * Método que cierra la consola
	 */

	public void close() {
		kb.close();
	}

	/**
	 * Método que imprime mensajes
	 * 
	 * @param text Mensaje concreto para el usuario
	 */

	public void writeLine(String text) {
		System.out.println(text);
	}

	/**
	 * Método que imprime mensajes de error
	 * 
	 * @param text Mensaje de error concreto para el usuario
	 */

	public void writeError(String text) {
		String.format("%s%s%s", "\u001B[31m", text, "\u001B[0m");
	}

}