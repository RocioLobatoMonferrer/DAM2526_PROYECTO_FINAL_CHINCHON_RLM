package dominio;

/**
 * Record que almacena la carta de la baraja española.
 * 
 * @param id   Representa un identificador único para cada carta
 * @param type Representa el valor de la carta (1-7, SOTA, CABALLO y REY)
 * @param suit Representa el palo de la carta (OROS, COPAS, ESPADAS y BASTOS)
 *
 */

public record Card(int id, CardType type, Suit suit) implements Comparable<Card> {

	@Override
	public String toString() {
		return String.format("%s%s", type.getScoreValue(), suit.getSymbol());
	}

	@Override
	public int compareTo(Card o1) {
		int result = Integer.compare(type.getScoreValue(), o1.type().getScoreValue());
		return result;
	}

}
