package dominio;

import java.util.List;

/**
 * Interfaz que se encarga de controlar a la entidad
 */

public interface IEntity {

	/**
	 * Método que permite a la entidad robar una carta
	 * 
	 * @param drawCard Carta robada
	 */

	void draw(Card drawCard);

	/**
	 * Método que limpia la mano
	 */

	void clearHand();

	/**
	 * Método que muestra la mano al completo de la entidad
	 * 
	 * @return Mano de la entidad
	 */

	String showHand();

	/**
	 * Método que calcula la puntuación de la entidad dependiendo de cuantas cartas
	 * no combine. Si no tiene ninguna que le sobre, se le restarán 10 puntos
	 * 
	 * @param remaining Cartas sobrantes
	 * @return Puntuación de la entidad
	 */

	int calculateScore(List<Card> remaining);

	/**
	 * Método que cambia el estado del jugador a que siga dentro de la partida
	 */

	void isInside();

	/**
	 * Método que cambia el estado del jugador a que no esta dentro de la partida
	 */

	void isOut();

	/**
	 * Método que elimina de la mano de la entidad una carta
	 * 
	 * @param index Índice de la carta
	 * @return Carta descartada
	 */

	Card discard(int index);

	/**
	 * Método que comprueba cual de las combinaciones es la correcta
	 * 
	 * @param cards  Cartas que forman la combinación
	 * @param option Opción entre las 3 posibilidades que ahí
	 * @return true/false si la combinación es válida o no
	 */

	boolean validateCombination(List<Card> cards, int option);

	/**
	 * Método que devuelve la mano de la entidad
	 * 
	 * @return Mano de la entidad
	 */

	List<Card> getHand();

	/**
	 * Método que devuelve la mano temporal de la entidad
	 * 
	 * @return Mano temporal de la entidad
	 */

	List<Card> getTempHand();

	/**
	 * Método que activa el modo temporal, copiando la mano actual de la entidad
	 * en una mano temporal para realizar combinaciones
	 */

	void startTempMode();

	/**
	 * Método que desactiva el modo temporal eliminando la mano temporal
	 */

	void endTempMode();

	/**
	 * Método que añade puntos a la puntuación acumulada de la entidad
	 * 
	 * @param points Puntos a añadir
	 */
	void addScore(int points);

	/**
	 * Método que devuelve la puntuación acumulada de la entidad
	 * 
	 * @return Puntuación acumulada de la entidad
	 */
	int getScore();

	/**
	 * Método que indica el estado de la entidad
	 * 
	 * @return Estado de la entidad
	 */
	EntityStatus getStatus();

	/**
	 * Método que devuelve el apodo de la entidad
	 * 
	 * @return Apodo de la entidad
	 */
	String getNickname();
}
