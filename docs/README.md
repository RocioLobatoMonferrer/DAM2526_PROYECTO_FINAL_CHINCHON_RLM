# PROYECTO FINAL DE PROGRAMACIÓN 25/26 - CHINCHÓN

![Imagen de título sobre el Chinchón](/assets/titulo.png)

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

[Aquí podrás encontrar las clases que se utilizaron para el proyecto](indiceClases.md)

## DIAGRAMA DE CLASE (UML)

![Imagen UML del proyecto](/assets/UML_Chinchón.drawio%20(1).png)

## ESTRUCTURA DEL PROYECTO

Este proyecto está compuesto por las siguientes carpetas: 

- assets: Carpeta en la que se almacenan las imágenes que se utilizarán en este proyecto, como lo son, el diagrama de clase, entre otros...

- docs: Carpeta en la que se almacenan documentos de información importante para el proyecto, el readme, entre otros...

- src: Carpeta en la que se encuentra el códgio fuente del juego. Dentro de ella encontramos:
    - app: Aquí encontraremos las clases que llevan el flujo de la partida y funciones para la comunicación y la visualización del usuario
    - dominio: Aquí encontraremos las clases fundamentales para la partida, es decir, la baraja, los jugadores...
    - tests: Aquí encontraremos las distintas pruebas que se han realizado a las clases del proyecto

El resto de carpetas y archivos son fundamentales para el funcionamiento del programa, a excepción de .gitignore

## PRUEBAS UNITARIAS CON JUnit 5

En este proyecto hemos realizado cierta pruebas para comprobar el comportamiento de un programa para detectar errores y aumentar la confianza en su calidad. Como estamos programando en Java, utilizaremos JUnit 5, que es uno de los frameworks más conocidos para hacer pruebas en Java. En este apartado tendremos en cuenta las pruebas de *caja negra* y *caja blanca*.

Para estas pruebas, nos centraremos en la clase Entity, así que dentro de la carpeta test del proyecto hemos creado la clase "EntityParameterizedTest":

Para facilitar la selección de cartas, hemos creado un método auxiliar privado dentro de la clase de pruebas para que valide los índices introducidos no superen el tamaño de la mano antes de llamar al método validateCombination:

```java
private boolean validateIndexes(Entity e, String indexes) {
		
	    String[] parts = indexes.trim().split("\\s+");
	    int val;
	    if (parts.length < 3) return false;
	    for (String p : parts) {
	        try {
	             val = Integer.parseInt(p);
	            if (val < 1 || val > e.getHand().size()) return false;
	        } catch (NumberFormatException ex) {
	            return false;
	        }
	    }
	    return true;
	}
```

A continuación, vamos a ver pruebas de caja blanca, que nos sirven para conocer los requisitos internos del programa para diseñar los casos correctamente, como por ejemplo saber que SEVEN y JACK son consecutivos en las escaleras:

- isSeriesTest:

Prueba que valida que el método isSeries detecta correctamente una combinación de cartas del mismo tipo. La mano se compone de 4 cartas del mismo tipo con distintos palos y una quinta carta de tipo diferente para romper la serie:

```java
@ParameterizedTest
	@CsvSource({ 
		"1 2 3, true",  
	    "1 2 3 4 5, true",  
	    "1 2 3 4 5 6, true", 
	    "1 2 3 4 5 6 7, true",  
	    "1 2 3 4 5 6 7 8, false", 
	    "1 2, false", 
	    "1 2 9, false", 
	    "1 2 10, false" 
		})
	
	void isSeriesTest(String indexes, boolean expected) {
		Entity e = new Entity("Test");
		boolean result;
		for (int i = 1; i <=8; i++) {
			e.draw(new Card(i, CardType.KING, Suit.COINS));
		}
		
		if (!validateIndexes(e,indexes)) {
			result = false;
		} else {
			List<Card> cards = Arrays.stream(indexes.trim().split("\\s+"))
					.map(i -> e.getHand().get(Integer.parseInt(i) -1))
					.toList();
			
			result = e.validateCombination(cards, 1);
		}
		
		assertEquals(expected, result);
	}
```

- isStraightTest:

Prueba que valida que el método isStraight detecta correctamente una escalera. La mano se compone de 8 cartas consecutivas del mismo palo, siendo la carta 8 un JACK que es consecutivo al SEVEN:

```java
@ParameterizedTest
	@CsvSource({ 
		"1 2 3, true",
		"1 2 3 4 5, true",
		"1 2 3 4 5 6, true",
		"1 2, false", 
		"1 2 4, false",
		"1 2 8, false",
		"1 1 2, false",
		"1 2 10, false",
		"2 3 10, false"
		})
	
	void isStraightTest(String indexes, boolean expected) {
		Entity e = new Entity("Test");
		Suit suit = Suit.CLUBS;
		CardType type;
		boolean result;
		
		for (int i = 1; i<=8;i++){
			type = switch(i) {
			case 1 -> CardType.ONE;
			case 2 -> CardType.TWO;
			case 3 -> CardType.THREE;
			case 4 -> CardType.FOUR;
			case 5 -> CardType.FIVE;
			case 6 -> CardType.SIX;
			case 7 -> CardType.SEVEN;
			case 8 -> CardType.JACK;
			default -> CardType.ERROR;
			};
				e.draw(new Card(i, type, suit));
			}
		
		if (!validateIndexes(e,indexes)) {
			result = false;
		} else {
			List<Card> cards = Arrays.stream(indexes.trim().split("\\s+"))
					.map(i -> e.getHand().get(Integer.parseInt(i) -1))
					.toList();
			
			result = e.validateCombination(cards, 2);
		}
		
		assertEquals(expected, result);
	}
```

- isChinchonTest:

Prueba que valida que el método isChichon detecta correctamente un chinchón. La mano se compone de 7 cartas consecutivas del mismo palo y una octava carta JACK que junto al SEVEN es consecutiva.

```java
@ParameterizedTest
	@CsvSource({
		"1 2 3 4 5 6 7, true",  
	    "1 2 3 4 5 6, false", 
	    "1 2 3 4 5 6 8, false", 
	    "1 2, false"
	})
	
	void isChinchonTest(String indexes, boolean expected) {
		Entity e = new Entity("Test");
		Suit suit = Suit.CLUBS;
		CardType type;
		boolean result;
		
		for (int i = 1; i<=8;i++){
			type = switch(i) {
			case 1 -> CardType.ONE;
			case 2 -> CardType.TWO;
			case 3 -> CardType.THREE;
			case 4 -> CardType.FOUR;
			case 5 -> CardType.FIVE;
			case 6 -> CardType.SIX;
			case 7 -> CardType.SEVEN;
			case 8 -> CardType.JACK;
			default -> CardType.ERROR;
			};
				e.draw(new Card(i, type, suit));
			}
		
		if (!validateIndexes(e,indexes)) {
			result = false;
		} else {
			List<Card> cards = Arrays.stream(indexes.trim().split("\\s+"))
					.map(i -> e.getHand().get(Integer.parseInt(i) -1))
					.toList();
			
			result = e.validateCombination(cards, 3);
		}
		
		assertEquals(expected, result);
	}
```

Por último, vamos a ver una prueba de caja negra, que nos sirven para pruebas que solo conocemos las reglas del juego: cada carta suma su valor en puntos y si no sobra ninguna carta el resultado es -10. No miramos la implementación interna de calculateScore:

- calculateScoreTest:

```java
@ParameterizedTest
	@CsvSource({
		"0, -10",  
	    "1, 1", 
	    "2, 3", 
	    "3, 6"
	})
	
	void calculateScoreTest(int numCards, int expected) {
		Entity e = new Entity("Test");
		List<Card> remaining = new ArrayList<>();
		CardType[] types = {CardType.ONE, CardType.TWO, CardType.THREE};
		
	    for (int i = 0; i < numCards; i++) {
	        remaining.add(new Card(i + 1, types[i], Suit.COINS));
	    }

	    int result = e.calculateScore(remaining);
	    assertEquals(expected, result);
		
	}
```

Por último, podemos observar en esta imagen que todas las pruebas han sido existosas:

![Imagen de las pruebas pasadas](/assets/pruebas.png)

## PATRONES DE DISEÑOS 

Para este proyecto, se han utilizado patrones de diseño para facilitar la reutilización, hacerlo más mantenible y reducir el acomplamiento del código. 

Antes de ver cuales son los patrones de diseño utilizados, debemos saber que un patrón de diseño es una solución reutilizable y general a un problema común en el desarrollo de software, por lo tanto, es muy útil usarlo a la hora de entender el proyecto.

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

