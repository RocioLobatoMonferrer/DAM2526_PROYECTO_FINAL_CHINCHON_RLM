# MENU 
*W I P*

## FUNCIÓN

Clase encargada de mostrar todo tipo de menús y mensajes al usuario.

## ATRIBUTOS

- Atributo de la consola.

```java
private ConsoleInput ci;
```

- Atributo estático para crear la instancia del menu.

```java
private static Menu instance;
```

## CONSTRUCTOR

- Constructor de la clase que la instancia.

```java
public Menu() {
		ci = ConsoleInput.getInstance();
	}
```

## MÉTODOS

- Método que inicializa la instancia de la clase para que sea única.
    Este método devuelve la instancia de la clase.

```java
public static Menu getInstance() {
		if (instance == null) {
			instance = new Menu();
		}
		return instance;
	}
```

- Método que cierra la consola.

```java
public void closeConsole() {
		ci.close();
	}
```

- Método que muestra el menú principal de la partida.
    Este método devuelve una de las siguientes opciones disponibles para el usuario:

        - Opción 1: Iniciar la partida.
        - Opción 2: Salir del programa.

```java
public int mainMenu() {
		ci.writeLine("      CHINCHÓN\n   ______________\n");
		return ci.readIntInRange(1, 2, "1. Iniciar Partida\n2. Salir\n");
	}
```

- Método que pide al usuario los puntos que quiere para la partida
    Este método devuelve la opción disponible para el usuario, mínimo debe ser un punto 1.

```java
public int howManyPoints() {
		return ci.readIntGreaterOrEqualThan(1, "Introduce cuantos puntos deseas para tu partida.");
	}
```

- Método que pide cuantos jugadores quiere el usuario.
    Este método devuelve una de las siguientes opciones para el usuario, siendo mínimo deben ser 2 jugadores y como máximo 5.

```java
public int numberOfPlayers() {
		return ci.readIntInRange(2, 5, "Introduce el número de jugadores.");
	}
```

- Método que pide al usuario que introduzca su apodo para la partida.
    Este método devuelve la introducción de apodo de los jugadores. 

```java
public String nicknameOfPlayers() {
		ci.writeLine("Introduce el nombre para este jugador:");
		return ci.readEmptyString();
	}
```

- Método que le pide al usuario si desea un jugador o una CPU.
    Este método devuelve una de las siguientes opciones disponibles para el usuario:

        - Opción J: Jugador.
        - Opción C: CPU.

```java
public boolean typeOfEntity() {
		ci.writeLine("Introduce J si lo que quieres es un jugador o C para una CPU.");
		return ci.readBooleanUsingChar('J', 'C',
				"Debes introducir una J si lo que quieres es un jugador o C para una CPU.");
	}
```

- Método que muestra que turno se encuentran los jugadores.
Podemos encontrar los siguientes parametros:

	- turn: El número del turno en cuestión.

```java
public void showTurn(int turn) {
		ci.writeLine(String.format("        TURNO %d\n   ______________\n", turn));
	}
```

- Método que muestra la mano del jugador.
Podemos encontrar los siguientes parametros:

	- player: Jugador en concreto.

```java
public void showHand(IEntity player) {
		ci.writeLine(player.showHand());
	}

```

- Método que enseña la primera carta en la pila de descartes
Podemos encontrar los siguientes parametros:

	- deck: Baraja de descarte. 

```java
public void showFirstDiscardCard(IDeckCard deck) {
		ci.writeLine(deck.getFirstDiscardCard().toString());
	}
```

- Método que muestra de que jugador es el turno.
Podemos encontrar los siguientes parametros:

	- nickname: Apodo del jugador.

```java
public void showPlayerTurn(String nickname) {
		ci.writeLine(String.format("TURNO DE %s:\n   ", nickname));
	}
```

- Método que le pide al usuario de que baraja quiere robar.
Podemos encontrar los siguientes parametros:

	- discardCard: Carta descartada.

     Este método devuelve una de las siguientes opciones disponibles para el usuario:

        - Opción 1: Robar de la baraja principal.
        - Opción 2: Robar de la baraja de descartes.

```java
public int turnMenu(String discardCard) {
		ci.writeLine("Seleccione de donde deseas robar tu siguiente carta:");
		return ci.readIntInRange(1, 2, String.format("1. Robar del mazo\n2. Robar del descarte [%s]\n", discardCard));
	}
```

- Método que le pide al usuario de que baraja quiere robar.
    Este método devuelve una de las siguientes opciones disponibles para el usuario:

        - Opción 1: Descartar una carta.
        - Opción 2: Cerrar la ronda con combinaciones de cartas.

```java
public int turnMenu2() {
		ci.writeLine("Seleccione una de las siguientes opciones:");
		return ci.readIntInRange(1, 2, "1. Descartar\n2. Cerrar\n");
	}
```

- Método que le pide al usuario que carta desea descartar de las 8 que tiene en mano.
    Este método devuelve las opciones disponibles para el usuario.

```java
public int selectToDiscard() {
		return ci.readIntInRange(1, 8, "Seleccione cual de sus cartas desea descartar:\n");
	}
```

- *W I P*

```java
public int selectCombination() {
		ci.writeLine("Seleccione que combinación deseas hacer:");
		return ci.readIntInRange(1, 3, "1. Grupos\n2. Escalera\n3. Chinchón\n");
	}
```

- Método que le pide el usuario que introduzca la combinación que desea.
    Este método devuelve la combinación realizada por el usuario.

```java
public String listCards() {
		ci.writeLine("Introduzca los índices de las cartas con espacios que deseas combinar.\nSi no desea introducir ninguna combinación, pulse ENTER\nEjemplo: (1 2 3) ");
		return ci.readEmptyString();
	}
```

- Método que te pide seleccionar cual de tus cartas deseas descartar.
    Este método devuelve la carta seleccionada por el usuario.

```java
public int selectClosingCard() {
		return ci.readIntInRange(1, 2, "Seleccione si desea descartar la primera o la segunda carta para cerrar:");
	}
```

- Método que indica el fin de la ronda.

```java
public void showRoundEnd() {
		ci.writeLine("\n        FIN DE LA RONDA\n");
	}
```

- Método que indica que el cierre ha sido con todas sus cartas y se le restarán puntos.

```java
public void showPerfectClosing() {
		ci.writeLine("¡Cierre perfecto!\nSe te resatrán 10 puntos.\n");
	}
```

- Método que muestra la puntuación actual del usuario y los puntos que ha ganado en esa ronda.
Podemos encontrar los siguientes parametros:

	- player: Jugador en cuestión.
    - points: Cantidad de puntos obtenidos.


```java
public void showRoundScore(IEntity player, int points) {
		ci.writeLine(String.format("%s (Has recibido %s%d en esta ronda)", player.toString(), points < 0 "" : "+", points));
	}
```

- Método que indica que el jugador ha sido eliminado de la partida.
Podemos encontrar los siguientes parametros:

	- nickname: Apodo del jugador.

```java
public void showPlayerOut(String nickname) {
		ci.writeLine(String.format("\nEl jugador %s ha sido eliminado.\n", nickname));
	}
```

- Método que muestra quien es el ganador de la partida.
Podemos encontrar los siguientes parametros:

	- nickname: Apodo del jugador.

```java
public void showWinner(String nickname) {
		ci.writeLine(String.format("¡El ganador es %s! ¡Felicidades!\n", nickname));
	}
```

- Método que muestra quien es el ganador de la partida por hacer un Chinchón.
Podemos encontrar los siguientes parametros:

	- nickname: Apodo del jugador.

```java
public void showWinnerForChinchon(String nickname) {
		ci.writeLine(
				String.format("¡Felicidades, %s!\nHas realizado un Chinchón, por lo tanto has ganado.\n", nickname));
	}
```

- Método que le dice al usuario que no puede cerrar.

```java
public void errorClose() {
		ci.writeError("No puedes cerrar en el primer turno.");
	}
```

- Método que le dice al usuario que la combinación es incoreccta.

```java
public void errorCombination() {
		ci.writeError("Combinación incorrecta.");
	}
```

- Método que le dice al usuario que no puede cerrar por pasarse de puntos.

```java
public void errorPoints() {
		ci.writeError("No puedes cerrar ya que vas a sobrepasar el límite de puntos.");
	}
```

- Método que pinta 50 líneas vacías para separar los turnos de los jugadores.

```java
public void fakeClearConsole() {
		for (int i = 0; i < 50; i++) {
			ci.writeLine("");
		}
	}
```

## RELACIONES

Menu es utilizada por la clase Game para mostrar los mensajes al usuario durante su partida.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)