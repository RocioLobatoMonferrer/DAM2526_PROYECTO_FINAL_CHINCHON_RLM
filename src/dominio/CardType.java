package dominio;

/**
 * Enum que almacena el valor de la carta<br>
 * Representa sus distintos valores y su puntuación
 */

public enum CardType {
	ONE(1, 1), TWO(2, 2), THREE(3, 3), FOUR(4, 4), FIVE(5, 5), SIX(6, 6), SEVEN(7, 7), JACK(8, 10), KNIGHT(9, 11),
	KING(10, 12), ERROR(-1, 0);

	private int straightValue;
	private int scoreValue;

	CardType(int straightValue, int scoreValue) {
		this.straightValue = straightValue;
		this.scoreValue = scoreValue;
	}

	public int getStraightValue() {
		return straightValue;
	}

	public int getScoreValue() {
		return scoreValue;
	}

}
