package dominio;

/**
 * Enum que presenta los palos de la carta y su color.<br>
 * Representa a los 4 palos de baraja española.
 */

public enum Suit {
	COINS("\u001B[33m" + "\uD83D\uDCB0"), CUPS("\u001B[31m" + "\uD83C\uDF77"), SWORDS("\u001B[34m" + "\u2694"),
	CLUBS("\u001B[32m" + "\uD83C\uDF3F"), ERROR("?");

	private String symbol;

	Suit(String symbol) {
		this.symbol = symbol + "\u001B[0m";
	}

	public String getSymbol() {
		return symbol;
	}

}
