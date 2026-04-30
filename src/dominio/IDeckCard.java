package dominio;

/**
 * Interfaz que gestiona las barajas
 */

public interface IDeckCard {
	/**
	 * Método que roba del mazo principal
	 * 
	 * @return Carta robada
	 */

	Card drawDeck();

	/**
	 * Método que crea las cartas dependiendo de la cantidad de jugadores y las
	 * añade al mazo principal
	 * 
	 * @param numberOfPlayers Cantidad de jugadores que jugaran la partida
	 */

	void createDeck(int numberOfPlayers);

	/**
	 * Método que roba del mazo de descartes
	 * 
	 * @return Carta robada
	 */

	Card drawDiscardCard();

	/**
	 * Método que añade una carta a la baraja de descartes
	 * 
	 * @param c Carta que será añadida
	 */

	void addCardInDiscard(Card c);

	/**
	 * Método que muestra la primera carta de la pila de descartes
	 * 
	 * @return Primera carta de la pila de descartes
	 */

	Card getFirstDiscardCard();
}
