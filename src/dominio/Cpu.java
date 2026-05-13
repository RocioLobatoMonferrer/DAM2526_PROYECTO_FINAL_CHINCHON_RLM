package dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de la gestión de las CPUs
 */

public class Cpu extends Entity implements ICpu {

	/**
	 * Constructor de la clase que lo instancia
	 * 
	 * @param nickname Apodo de la CPU
	 */

	public Cpu(String nickname) {
		super(nickname);
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public int choosePlay() {
		return (int) (Math.random() * 2) + 1;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public int chooseDiscard() {
		int maxIndex = 0;
		for (int i = 0; i < getHand().size(); i++) {
			if (getHand().get(i).type().getScoreValue() > getHand().get(maxIndex).type().getScoreValue()) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public boolean canClose() {
		Card discarded;
		List<Card> hand = getHand(), remaining;
		boolean found;

		if (hand.size() < 7) {
			return false;
		}

		if (validateCombination(hand, 3)) {
			return true;
		}

		found = false;
		for (int i = 0; i < hand.size() && !found; i++) {
			discarded = hand.get(i);

			if (discarded.type().getScoreValue() <= 5) {
				remaining = new ArrayList<>(hand);
				remaining.remove(i);

				if (validateCombination(remaining, 1) || validateCombination(remaining, 2)) {
					found = true;
				}
			}
		}

		return found;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public void declareCombinations() {
		List<Card> hand = getHand();
		List<Card> best = new ArrayList<>();
		List<Card> same = new ArrayList<>();
		List<Card> subset;

		for (int i = 0; i < hand.size() && best.isEmpty(); i++) {
			for (int j = hand.size(); j > i + 2 && best.isEmpty(); j--) {
				subset = new ArrayList<>(hand.subList(i, j));
				if (validateCombination(subset, 2)) {
					best = subset;
				}
			}
		}

		if (best.isEmpty()) {
			for (int i = 0; i < hand.size() && best.isEmpty(); i++) {
				for (int j = 0; j < hand.size(); j++) {
					if (hand.get(j).type() == hand.get(i).type()) {
						same.add(hand.get(j));
					}
				}
				if (same.size() >= 3) {
					best = same;
				}
			}
		}

		for (Card c : best) {
			hand.remove(c);
		}
	}

}
