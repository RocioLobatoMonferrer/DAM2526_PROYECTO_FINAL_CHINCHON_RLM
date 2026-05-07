# CONSOLEINPUT

## FUNCIÓN

Clase encargada de la entrada y salida por consola.

## ATRIBUTOS

- Atributo que indica a la clase Scanner.

```java
private Scanner kb;
```

- Atributo estático para crear la instancia de la consola. 

```java
private static ConsoleInput instance;
```

## CONSTRUCTOR

- Constructor de la clase que la instancia.

```java
public ConsoleInput() {
		kb = new Scanner(System.in);
	}
```

## MÉTODOS

- Método que inicializa la instancia de la clase para que sea única.
    Este método devuelve la instancia de la clase.

```java
public static ConsoleInput getInstance() {
		if (instance == null) {
			instance = new ConsoleInput();
		}
		return instance;
	}
```

- Método que cierra la consola.

```java
private void cleanInput() {
		kb.nextLine();
	}
```

- Método que lee un entero introducido por un usuario.
Podemos encontrar los siguientes parametros:

	- message: Mensaje para el usuario.

    Este método devuelve el valor que confirma que es un número.

```java
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
```

- Método que lee un entero introducido por un usuario.
Podemos encontrar los siguientes parametros:

    - lowerBound: Valor mínimo.
	- message: Mensaje para el usuario.

    Este método devuelve el valor que cumple con las condiciones.

```java
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
```

- Método que lee un entero introducido por un usuario.
Podemos encontrar los siguientes parametros:

    - lowerBound: Valor del rango mínimo.
    - upperBound: Valor del rango máximo.
	- message: Mensaje para el usuario.

    Este método devuelve el valor que cumple con las condiciones.

```java
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
```

- Método que lee el carácter introducido.
Podemos encontrar los siguientes parametros:

	- message: Mensaje para el usuario.

    Este método devuelve el carácter introducido.

```java
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
```

- Método que lee la cadena de texto introducida.
Podemos encontrar los siguientes parametros:

	- message: Mensaje para el usuario.

    Este método devuelve la cadena de texto introducida.

```java
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
```

- Método que lee si el carácter introducido es correcto o no.
Podemos encontrar los siguientes parametros:

    - affirmativeValue: Carácter correcto.
    - negativeValue: Carácter incorrecto.
	- error: Mensaje de error para el usuario.

    Este método devuelve true/false dependiendo de la entrada del usuario. 

```java
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
```

- Método que lee una cadena de texto vacía.
    Este método devuelve la cadena de texto introducida.

```java
public String readEmptyString() {
		String value = "";
		value = kb.nextLine();
		return value;
	}
```

- Método que cierra la consola.

```java
public void close() {
		kb.close();
	}
```

- Método que imprime mensajes.
Podemos encontrar los siguientes parametros:

    - text: Mensaje concreto para el usuario.

```java
public void writeLine(String text) {
		System.out.println(text);
	}
```

- Método que imprime mensajes de error.
Podemos encontrar los siguientes parametros:

    - text: Mensaje de error concreto para el usuario.

```java
public void writeError(String text) {
		System.out.printf("%s%s%s\n", "\u001B[31m", text, "\u001B[0m");
	}
```

## RELACIONES

ConsoleInput es utilizada por la clase Menu que lo usa para mostrar los mensajes y pedir los datos necesarios al usuario.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)