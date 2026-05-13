# DECKCARD

## FUNCIÓN

Clase encargada de controlar las barajas.

## ATRIBUTOS

- Atributo que indica las cartas de baraja principal.

```java
private List<Card> deckInCards;
```

- Atributo que indica las cartas de la baraja de descartes.

```java
private List<Card> discardCards;
```

## CONSTRUCTOR

- Constructor de la clase que instancia la clase.

```java
public DeckCard() {
		deckInCards = new ArrayList<>();
		discardCards = new ArrayList<>();
	}
```

## MÉTODOS

- Método que añade una carta a la baraja principal.
Podemos encontrar los siguientes parametros:

	- card: Carta que será añadida.

```java
private void addCard(Card card) {
		deckInCards.add(card);
	}
```

- Método que muestra la primera carta de la pila de descartes.
    Este método devuelve la primera carta de la pila de descartes.

```java
@Override
	public Card getFirstDiscardCard() {
		return discardCards.get(discardCards.size() - 1);
	}
```

- Método que roba del mazo principal.
    Este método devuelve la carta robada.

```java
@Override
	public Card drawDeck() {
		Card drawCard;

		if (deckInCards.size() == 0) {
			shuffle();
		}

		drawCard = deckInCards.get(0);
		deckInCards.remove(0);
		return drawCard;
	}
```

- Método que roba del mazo de descartes.
    Este método devuelve la carta robada.

```java
@Override
	public Card drawDiscardCard() {
		Card drawCard = discardCards.get(discardCards.size() - 1);
		discardCards.remove(discardCards.size() - 1);
		return drawCard;
	}
```

- Método que añade una carta a la baraja de descartes.
Podemos encontrar los siguientes parametros:

	- card: Carta que será añadida.

```java
@Override
	public void addCardInDiscard(Card c) {
		discardCards.add(c);
	}
```

- Método que baraja el mazo y si detecta que esta vacío la pila de descartes, las añade a la baraja principal.

```java
private void shuffle() {
		Card aux;
		if (discardCards.size() != 0) {
			aux = discardCards.get(discardCards.size() - 1);
			discardCards.remove(aux);
			deckInCards.addAll(discardCards);
			discardCards.clear();
			discardCards.add(aux);
		}

		Collections.shuffle(deckInCards);
	}
```

- Método que crea las cartas dependiendo de la cantidad de jugadores y las añade al mazo principal.
Podemos encontrar los siguientes parametros:

	- numberOfPlayers: Cantidad de jugadores que jugaran la partida.

```java
@Override
	public void createDeck(int numberOfPlayers) {
		deckInCards.clear();
		discardCards.clear();
		CardType type;
		Suit suit;
		int id = 0;

		for (int i = 0; i < (numberOfPlayers < 3 ? 1 : 2); i++) {
			for (int j = 1; j <= 4; j++) {

				suit = switch (j) {
				case 1 -> Suit.COINS;
				case 2 -> Suit.CUPS;
				case 3 -> Suit.SWORDS;
				case 4 -> Suit.CLUBS;
				default -> Suit.ERROR;
				};

				for (int k = 1; k <= 10; k++) {

					type = switch (k) {
					case 1 -> CardType.ONE;
					case 2 -> CardType.TWO;
					case 3 -> CardType.THREE;
					case 4 -> CardType.FOUR;
					case 5 -> CardType.FIVE;
					case 6 -> CardType.SIX;
					case 7 -> CardType.SEVEN;
					case 8 -> CardType.JACK;
					case 9 -> CardType.KNIGHT;
					case 10 -> CardType.KING;
					default -> CardType.ERROR;
					};

					addCard(new Card(++id, type, suit));
				}
			}
		}

		shuffle();

	}
```

## RELACIONES

DeckCard es utilizado por el Menu para mostrar la primera carta descartada y Game para tener todas las cartas de la partida.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)