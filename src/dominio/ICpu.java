package dominio;

/**
 * Interfaz que gestiona la CPU
 */

public interface ICpu extends IEntity {

	/**
	 * Método que permite a la CPU elegir dónde robará su siguiente carta
	 * 
	 * @return Opción elegida por la CPU<br>
	 *         Opción 1: Robar del mazo principal<br>
	 *         Opción 2: Robar de la pila de descartes
	 */

	int choosePlay();

	/**
	 * Método que elige que carta va descartar la CPU
	 * 
	 * @return Índice de la carta a descartar
	 */

	int chooseDiscard();

	/**
	 * Método que comprueba que la CPU puede cerrar la ronda en base a sus cartas
	 * 
	 * @return true/false si la CPU puede cerrar o no
	 */

	boolean canClose();

	/**
	 * Método que le permite realizar combinaciones a la CPU y eliminando las cartas
	 * combinadas
	 */

	void declareCombinations();
}
