# ENTITY 
WIP

## FUNCIÓN

Clase encargada de gestionar a las entidades.

## ATRIBUTOS

- Atributo de la mano de la entidad

```java
private List<Card> hand;
```

- Atributo que indica si la entidad sigue dentro o no de la partida

```java
private EntityStatus status;
```

- Atributo que indica el apodo de la entidad

```java
private String nickname;
```

- Atributo de la puntuación de la entidad

```java
private int score;
```

## CONSTRUCTOR

- Constructor de la clase que se encarga de instanciarla. Podemos encontrar los siguientes parametros:

    - nickname Apodo de la entidad

```java
public Entity(String nickname) {
		setStatus(EntityStatus.INSIDE);
		hand = new ArrayList<>();
		this.nickname = nickname;
		score = 0;
	}
```

## MÉTODOS

- Método que muestra la mano de la entidad
    Este método devuelve la mano de la entidad

```java
@Override
	public List<Card> getHand() {
		return hand;
	}
```

- Método que indica el estado de la entidad
     Este método devuelve la mano de la entidad

```java
public EntityStatus getStatus() {
		return status;
	}
```

- Método que cambia el estado de la entidad
     Este método devuelve el estado de la entidad

```java
public void setStatus(EntityStatus status) {
		this.status = status;
	}
```

- Método que permite a la entidad robar una carta
     Este método devuelve la carta robada

```java
@Override
	public void draw(Card drawCard) {
		hand.add(drawCard);
		orderDeckCard();
	}
```

- Método que comprueba cual de las combinaciones es la correcta
Podemos encontrar los siguientes parametros:

	- cards: Cartas que forman la combinación
	- option: Opción entre las 3 posibilidades que ahí

    Este método devuelve true/false si la combinación es válida o no

```java
@Override
	public Card discard(int index) {
		orderDeckCard();
		return hand.remove(index);
	}
```

- Método que limpia la mano

```java
@Override
	public void clearHand() {
		hand.clear();
	}
```

- Método que calcula la puntuación de la entidad dependiendo de cuantas cartas no combine. Si no tiene ninguna que le sobre, se le restarán 10 puntos. Podemos encontrar los siguientes parametros:

	- remaining: Cartas sobrantes

    Este método devuelve la puntuación de la entidad

```java
@Override
	public int calculateScore(List<Card> remaining) {
		int total = 0;

		if (remaining.size() == 0) {
			total = -10;
		}

		for (Card c : remaining) {
			total += c.type().getScoreValue();
		}

		return total;
	}
```

- Método para ordenar las cartas de la mano de las entidades

```java
private void orderDeckCard() {
		Collections.sort(hand);
	}
```

- Método que comprueba cual de las combinaciones es la correcta
Podemos encontrar los siguientes parametros:

	- cards: Cartas que forman la combinación
    - option: Opción entre las 3 posibilidades que ahí

    Este método devuelve true/false si la combinación es válida o no

```java
@Override
	public boolean validateCombination(List<Card> cards, int option) {
		return switch (option) {
		case 1 -> isGroup(cards);
		case 2 -> isStraight(cards);
		case 3 -> isChichon(cards);
		default -> false;
		};
	}
```

- Método que comprueba si la combinación es un trío con todos el mismo palo o más
Podemos encontrar los siguientes parametros:

	- cards: Cartas combinadas

    Este método devuelve true/false si la combinación es correcta

```java
private boolean isSeries(List<Card> cards) {
		CardType type = cards.get(0).type();
		if (cards.size() < 3) {
			return false;
		}

		for (Card c : cards) {
			if (c.type() != type) {
				return false;
			}
		}
		return true;
	}
```

- Método que comprueba si la combinación corresponde con la de una escalera
Podemos encontrar los siguientes parametros:

	- cards: Cartas combinadas

    Este método devuelve true/false si la combinación es correcta

```java
private boolean isStraight(List<Card> cards) {
		List<Card> sortCard = new ArrayList<>(cards);
		Suit suit = cards.get(0).suit();

		if (cards.size() < 3) {
			return false;
		}

		for (Card c : cards) {
			if (c.suit() != suit) {
				return false;
			}
		}

		sortCard.sort((a, b) -> a.type().getStraightValue() - b.type().getStraightValue());

		for (int i = 0; i < sortCard.size() - 1; i++) {
			int next = sortCard.get(i + 1).type().getStraightValue();
			int current = sortCard.get(i).type().getStraightValue();
			if (next != current + 1) {
				return false;
			}
		}
		return true;
	}
```

- Método que comprueba si la combinación corresponde con la de un Chinchón, es decir, una escalera de 7
Podemos encontrar los siguientes parametros:

	- cards: Cartas combinadas

    Este método devuelve true/false si la combinación es correcta

```java
private boolean isChichon(List<Card> cards) {
		return cards.size() == 7 && isStraight(cards);
	}
```

- Método que cambia el estado del jugador a que siga dentro de la partida

```java
@Override
	public void isInside() {
		setStatus(EntityStatus.INSIDE);
	}
```

- Método que cambia el estado del jugador a que no esta dentro de la partida

```java
@Override
	public void isOut() {
		setStatus(EntityStatus.OUT);
	}
```

- Método que muestra la mano al completo de la entidad
    Este método devuelve la mano de la entidad

```java
@Override
	public String showHand() {
		StringBuilder sb = new StringBuilder("");
		sb.append(String.format("%s - ", nickname));
		for (int i = 0; i < hand.size(); i++) {
			if (i != 0 && i != hand.size() - 1) {
				sb.append(", ");
			} else if (i == hand.size() - 1) {
				sb.append(" y ");
			}
			sb.append(String.format("[%d]%s", i + 1, hand.get(i)));
		}
		sb.append(".");
		return sb.toString();
	}
```

- WIP

```java
@Override
	public String toString() {
		return String.format("%s - %d", nickname, score);
	}
```

```java
```

## RELACIONES

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)