package dominio;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EntityParameterizedTest {

	private boolean validateIndexes(Entity e, String indexes) {
		
	    String[] parts = indexes.trim().split("\\s+");
	    int val;
	    if (parts.length < 3) return false;
	    for (String p : parts) {
	        try {
	             val = Integer.parseInt(p);
	            if (val < 1 || val > e.getHand().size()) return false;
	        } catch (NumberFormatException ex) {
	            return false;
	        }
	    }
	    return true;
	}
	
	
	@ParameterizedTest
	@CsvSource({ 
		"1 2 3, true",  
	    "1 2 3 4 5, true",  
	    "1 2 3 4 5 6, true", 
	    "1 2 3 4 5 6 7, true",  
	    "1 2 3 4 5 6 7 8, false", 
	    "1 2, false", 
	    "1 2 9, false", 
	    "1 2 10, false" 
		})
	
	void isSeriesTest(String indexes, boolean expected) {
		Entity e = new Entity("Test");
		boolean result;
		for (int i = 1; i <=8; i++) {
			e.draw(new Card(i, CardType.KING, Suit.COINS));
		}
		
		if (!validateIndexes(e,indexes)) {
			result = false;
		} else {
			List<Card> cards = Arrays.stream(indexes.trim().split("\\s+"))
					.map(i -> e.getHand().get(Integer.parseInt(i) -1))
					.toList();
			
			result = e.validateCombination(cards, 1);
		}
		
		assertEquals(expected, result);
	}

	@ParameterizedTest
	@CsvSource({ 
		"1 2 3, true",
		"1 2 3 4 5, true",
		"1 2 3 4 5 6, true",
		"1 2, false", 
		"1 2 4, false",
		"1 2 8, false",
		"1 1 2, false",
		"1 2 10, false",
		"2 3 10, false"
		})
	
	void isStraightTest(String indexes, boolean expected) {
		Entity e = new Entity("Test");
		Suit suit = Suit.CLUBS;
		CardType type;
		boolean result;
		
		for (int i = 1; i<=8;i++){
			type = switch(i) {
			case 1 -> CardType.ONE;
			case 2 -> CardType.TWO;
			case 3 -> CardType.THREE;
			case 4 -> CardType.FOUR;
			case 5 -> CardType.FIVE;
			case 6 -> CardType.SIX;
			case 7 -> CardType.SEVEN;
			case 8 -> CardType.JACK;
			default -> CardType.ERROR;
			};
				e.draw(new Card(i, type, suit));
			}
		
		if (!validateIndexes(e,indexes)) {
			result = false;
		} else {
			List<Card> cards = Arrays.stream(indexes.trim().split("\\s+"))
					.map(i -> e.getHand().get(Integer.parseInt(i) -1))
					.toList();
			
			result = e.validateCombination(cards, 2);
		}
		
		assertEquals(expected, result);
	}

	@ParameterizedTest
	@CsvSource({
		"1 2 3 4 5 6 7, true",  
	    "1 2 3 4 5 6, false", 
	    "1 2 3 4 5 6 8, false", 
	    "1 2, false"
	})
	
	void isChinchonTest(String indexes, boolean expected) {
		Entity e = new Entity("Test");
		Suit suit = Suit.CLUBS;
		CardType type;
		boolean result;
		
		for (int i = 1; i<=8;i++){
			type = switch(i) {
			case 1 -> CardType.ONE;
			case 2 -> CardType.TWO;
			case 3 -> CardType.THREE;
			case 4 -> CardType.FOUR;
			case 5 -> CardType.FIVE;
			case 6 -> CardType.SIX;
			case 7 -> CardType.SEVEN;
			case 8 -> CardType.JACK;
			default -> CardType.ERROR;
			};
				e.draw(new Card(i, type, suit));
			}
		
		if (!validateIndexes(e,indexes)) {
			result = false;
		} else {
			List<Card> cards = Arrays.stream(indexes.trim().split("\\s+"))
					.map(i -> e.getHand().get(Integer.parseInt(i) -1))
					.toList();
			
			result = e.validateCombination(cards, 3);
		}
		
		assertEquals(expected, result);
	}

	@ParameterizedTest
	@CsvSource({
		"0, -10",  
	    "1, 1", 
	    "2, 3", 
	    "3, 6"
	})
	
	void calculateScoreTest(int numCards, int expected) {
		Entity e = new Entity("Test");
		List<Card> remaining = new ArrayList<>();
		CardType[] types = {CardType.ONE, CardType.TWO, CardType.THREE};
		
	    for (int i = 0; i < numCards; i++) {
	        remaining.add(new Card(i + 1, types[i], Suit.COINS));
	    }

	    int result = e.calculateScore(remaining);
	    assertEquals(expected, result);
		
	}
	
	
}
