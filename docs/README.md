# PROYECTO FINAL DE PROGRAMACIÓN 25/26 - CHINCHÓN

## ¿QUÉ ES EL CHINCHÓN?

El Chinchón es un juego español de la extensa familia de juegos de cartas en el cual debes combinar las cartas de tu mano antes que el resto de los jugadores para poder elaborar la mejor estrategia a la hora de tener la menor cantidad de puntos posibles. 

Para este juego, se utiliza la baraja española (compuesta por oros, espadas, copas y bastos) compuesta por 40 o 48 cartas y son repartidas hasta 7 cartas entre los jugadores, que deben ser como mínimo 2 y no hay un máximo de jugadores, pero contra más sean, seguramente se necesite más de una baraja. El orden de las cartas en este juego va desde el 1 hasta 7 y después lo que sería 10 como sota, 11 como caballo y 12 como rey y como se puede notar, no se juega con 8 ni 9, así que técnicamente sota sería como '8', y así con el resto.

## REGLAS DEL CHINCHÓN

Cada partida del chinchón se compone de distintas rondas, en el que cada jugador puede hacer lo siguiente:

- Primero, se le deja al jugador robar una carta de la baraja o una carta que hay en el centro que se conoce como "pila de descarte"

- Después, el jugador tiene la opción de soltar una de sus cartas en la pila de descartes para que un futuro alguien robe está y acabando su turno, o cerrar la ronda, pero, esto no es posible si se encuentra en el primer turno

Si el jugador desea cerrar la ronda, debe realizar combinaciones mínimo con 6 de sus 7 cartas, entre las cuales, están permitidas las siguientes:

- Series (Ejemplo: 3🍷 3⚔️ 3💰)
- Escaleras del mismo palo y deben ser mínimo 3 cartas (Ejemplo: 1💰 2💰 3💰)
- Chinchón, que consiste en una escalera de hasta 7 cartas, es decir, de toda tu mano

Al cerrar la ronda, se deben tener en cuenta las siguientes observaciones: 

- Las cartas que no fueron combinadas serán sumadas a tu puntuación y si la superas, serás eliminado de la partida
- No puedes cerrar la ronda si la suma a tu puntuación supera la que hayáis elegido entre los jugadores. 
- Debes cerrar la partida con una carta que sea menor o igual que 5
- Si cierras con tus 7 cartas combinadas, se te restarán hasta 10 puntos
- Si el jugador realiza un chinchón, la partida finalizará llevandose la victoria

Solo ganará el último que quede en pie

## FUNCIONAMIENTO DEL PROGRAMA

[Aquí podrás encontrar las clases que se utilizaron para el proyecto]()

## DIAGRAMA DE CLASE (UML)

## ESTRUCTURA DEL PROYECTO

Este proyecto está compuesto por las siguientes carpetas: 

- docs: Carpeta en la que se almacenan documentos de información importante para el proyecto, como el diagrama de clase, el readme, entre otros...

- src: Carpeta en la que se encuentra el códgio fuente del juego. Dentro de ella encontramos:
    - app: Aquí encontraremos las clases que llevan el flujo de la partida y funciones para la comunicación y la visualización del usuario
    - dominio: Aquí encontraremos las clases fundamentales para la partida, es decir, la baraja, los jugadores...
    - tests: Aquí encontraremos las distintas pruebas que se han realizado a las clases del proyecto

El resto de carpetas y archivos son fundamentales para el funcionamiento del programa, a excepción de .gitignore

## PRUEBAS UNITARIAS CON JUnit 5

## PATRONES DE DISEÑOS 

Para este proyecto, se han utilizado patrones de diseño para facilitar la reutilización, hacerlo más mantenible y reducir el acomplamiento del código. 

Antes de ver cuales son los patrones de diseño utilizados, debemos saber que un patrón de diseño es uns solución reutilizable y general a un problema común en el desarrollo de software, por lo tanto, es muy útil usarlo a la hora de entender el proyecto.

Existen distintos patrones de diseño, pero para este proyecto encontraremos dos de estos: Singleton y Factory, los cuales son utilizados a la hora de la creación de objetos

El patrón Singleton es conocido por garantizar que la clase tenga una sola instancia y proporcionar acceso global y en este proyecto lo podemos encontrar tanto en la clase "Menu" como en la clase "ConsoleInput":


ConsoleInput:

```java
public class ConsoleInput {
private Scanner kb;
private static ConsoleInput instance;

	public ConsoleInput() {
		kb = new Scanner(System.in);
	}

	public static ConsoleInput getInstance() {
		if (instance == null) {
			instance = new ConsoleInput();
		}
		return instance;
	}
}
```

Menu: 

```java
public class Menu {
private ConsoleInput ci;
private static Menu instance;

	public Menu() {
		ci = ConsoleInput.getInstance();
	}

	public static Menu getInstance() {
		if (instance == null) {
			instance = new Menu();
		}
		return instance;
	}
}
```

Y así se vería a la hora de crear de instanciar la clase "Menu" en otra clase, en este caso, "Game":

Game: 

```java
public class Game implements IGame {
    
public Game() {
		players = new ArrayList<>();
		menu = Menu.getInstance(); // Menu
		ci = ConsoleInput.getInstance(); // ConsoleInput 
		factory = new FactoryEntity();
		deckCard = new DeckCard();
	}
}
```


Con respecto al patrón Factory, es conocido por ser un patrón que delega la creación de objetos en una clase fábrica para que el cliente no decida qué objeto crear y en este proyecto lo podemos encontrar con la clase "Entity":

FactoryEntity:

```java
public class FactoryEntity {

	public Entity createEntity(boolean type, String nickname) {
		if (type) {
			return new Entity(nickname);
		} else {
			return new Cpu(nickname);
		}
	}
}
```

Y así se vería a la hora de crear de instanciar la clase "FactoryEntity" en otra clase, en este caso, "Game":

Game: 

```java
public class Game implements IGame {
    
public Game() {
		players = new ArrayList<>();
		menu = Menu.getInstance(); 
		ci = ConsoleInput.getInstance();
		factory = new FactoryEntity(); // FactoryEntity
		deckCard = new DeckCard();
	}
}
```

