package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase encargada de gestionar a las entidades
 */

public class Entity implements IEntity {
	private List<Card> hand;
	private EntityStatus status;
	private String nickname;
	private int score;

	/**
	 * Constructor de la clase que se encarga de instanciarla
	 * 
	 * @param nickname Apodo de la entidad
	 */

	public Entity(String nickname) {
		setStatus(EntityStatus.INSIDE);
		hand = new ArrayList<>();
		this.nickname = nickname;
		score = 0;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public List<Card> getHand() {
		return hand;
	}

	/**
	 * Método que indica el estado de la entidad
	 * 
	 * @return Estado de la entidad
	 */

	public EntityStatus getStatus() {
		return status;
	}

	/**
	 * Método que cambia el estado de la entidad
	 * 
	 * @param status Estado de la entidad
	 */

	public void setStatus(EntityStatus status) {
		this.status = status;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public void draw(Card drawCard) {
		hand.add(drawCard);
		orderDeckCard();
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public Card discard(int index) {
		orderDeckCard();
		return hand.remove(index);
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public void clearHand() {
		hand.clear();
	}

	/**
	 * @inheritDoc
	 */

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

	/**
	 * Método para ordenar las cartas de la mano de los jugadores
	 */

	private void orderDeckCard() {
		Collections.sort(hand);
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public boolean validateCombination(List<Card> cards, int option) {
		return switch (option) {
		case 1 -> isGroup(cards);
		case 2 -> isStraight(cards);
		case 3 -> isChichon(cards);
		default -> false;
		};
	}

	/**
	 * Método que comprueba si la combinación es un trío con todos el mismo palo o
	 * más
	 * 
	 * @param cards Cartas combinadas
	 * @return true/false si la combinación es correcta
	 */

	private boolean isGroup(List<Card> cards) {
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

	/**
	 * Método que comprueba si la combinación corresponde con la de una escalera
	 * 
	 * @param cards Cartas combinadas
	 * @return true/false si la combinación es correcta
	 */

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

	/**
	 * Método que comprueba si la combinación corresponde con la de un Chinchón, es
	 * decir, una escalera de 7
	 * 
	 * @param cards Cartas combinadas
	 * @return true/false si la combinación es correcta
	 */

	private boolean isChichon(List<Card> cards) {
		return cards.size() == 7 && isStraight(cards);
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public void isInside() {
		setStatus(EntityStatus.INSIDE);
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public void isOut() {
		setStatus(EntityStatus.OUT);
	}

	/**
	 * @inheritDoc
	 */

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

	@Override
	public String toString() {
		return String.format("%s - %d", nickname, score);
	}

}
