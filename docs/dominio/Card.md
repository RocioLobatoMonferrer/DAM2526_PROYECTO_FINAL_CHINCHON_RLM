# CARD

## FUNCIÓN

- Record que almacena la carta de la baraja española. Podemos encontrar los siguientes parametros: 

    - id: Representa un identificador único para cada carta
    - type: Representa el valor de la carta (1-7, SOTA, CABALLO y REY)
    - suit: Representa el palo de la carta (OROS, COPAS, ESPADAS y BASTOS)

```java
public record Card(int id, CardType type, Suit suit) implements Comparable<Card> {
```

## MÉTODOS

- Método estándar toString que imprime el número y el palo de la carta

```java
@Override
	public String toString() {
		return String.format("%s%s", type.getScoreValue(), suit.getSymbol());
	}
```

- Método estándar compareTo para comparar los valores de la carta para ordenarlos

```java
@Override
	public int compareTo(Card o1) {
		int result = Integer.compare(type.getScoreValue(), o1.type().getScoreValue());
		return result;
	}
```

## RELACIONES

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)