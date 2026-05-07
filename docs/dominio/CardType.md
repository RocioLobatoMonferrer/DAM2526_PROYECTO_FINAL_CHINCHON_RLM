# CARDTYPE

## FUNCIÓN

Enum que almacena el valor de la carta.
Representa sus distintos valores y su puntuación.

## ATRIBUTOS

- Atributos constantes que consiste en los valores de la carta .

```java
ONE(1, 1), TWO(2, 2), THREE(3, 3), FOUR(4, 4), FIVE(5, 5), SIX(6, 6), SEVEN(7, 7), JACK(8, 10), KNIGHT(9, 11), KING(10, 12), ERROR(-1, 0);
```

- Atributo que indica el valor de la carta.

```java
private int straightValue;
```

- Atributo que indica lo que vale la carta.

```java
private int scoreValue;
```

### CONSTRUCTOR

- Constructor de la clase que permite la instancia del valor de la carta. 
Podemos encontrar los siguientes parametros:

    - straightValue: Valor estándar de cada carta.
	- scoreValue: Puntuación de cada carta.

```java
CardType(int straightValue, int scoreValue) {
		this.straightValue = straightValue;
		this.scoreValue = scoreValue;
	}
```

## MÉTODOS

- Método que devuelve el valor de la carta.

```java
public int getStraightValue() {
		return straightValue;
	}

```

- Método que devuelve la puntuación de la carta.

```java
public int getScoreValue() {
		return scoreValue;
	}
```

## RELACIONES

CardType es utilizada por las clases DeckCard y Card para formar respectivamente la baraja y las cartas.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)