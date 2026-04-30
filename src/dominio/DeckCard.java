package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase encargada de controlar las barajas
 */

public class DeckCard implements IDeckCard {
	private List<Card> deckInCards;
	private List<Card> discardCards;

	/**
	 * Constructor de la clase que instancia la clase
	 */

	public DeckCard() {
		deckInCards = new ArrayList<>();
		discardCards = new ArrayList<>();
	}

	/**
	 * Método que añade una carta a la baraja principal
	 * 
	 * @param c Carta que será añadida
	 */

	private void addCard(Card c) {
		deckInCards.add(c);
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public Card getFirstDiscardCard() {
		return discardCards.get(discardCards.size() - 1);
	}

	/**
	 * @inheritDoc
	 */

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

	/**
	 * @inheritDoc
	 */

	@Override
	public Card drawDiscardCard() {
		Card drawCard = discardCards.get(discardCards.size() - 1);
		discardCards.remove(discardCards.size() - 1);
		return drawCard;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public void addCardInDiscard(Card c) {
		discardCards.add(c);
	}

	/**
	 * Método que baraja el mazo y si detecta que esta vacío la pila de descartes,
	 * las añade a la baraja principal
	 */

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

	/**
	 * @inheritDoc
	 */

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

}
