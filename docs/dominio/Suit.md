# SUIT

## FUNCIÓN

Enum que presenta los palos de la carta y su color.
Representa a los 4 palos de baraja española.

## ATRIBUTOS

- Atributos constantes que consisten en los palos de las cartas y su color

	```java
	COINS("\u001B[33m" + "\uD83D\uDCB0"), CUPS("\u001B[31m" + "\uD83C\uDF77"), SWORDS("\u001B[34m" + "\u2694"), CLUBS("\u001B[32m" + "\uD83C\uDF3F"), ERROR("?");
	```

- Atributo del palo

	```java
	private String symbol;
	```

## CONSTRUCTOR

- Constructor de la clase que permite la instancia de un palo:

```java
Suit (String symbol) {
		this.symbol = symbol + "\u001B[0m";
	}
```

## MÉTODOS

- Método que devuelve el palo con su color:

```java
public String getSymbol() {
		return symbol;
	}
```

## RELACIONES

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)