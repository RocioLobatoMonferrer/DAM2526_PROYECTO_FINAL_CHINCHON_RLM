# CPU 

## FUNCIÓN

Interfaz que gestiona la CPU.

## CONSTRUCTOR

- Constructor de la clase que lo instancia. Podemos encontrar los siguientes parametros:

    - nickname Apodo de la entidad.

```java
public Cpu(String nickname) {
		super(nickname);
	}
```

## MÉTODOS

- Método que permite a la CPU elegir dónde robará su siguiente carta. 
    Este método devuelve una de las siguientes opciones disponibles para la CPU:

        - Opción 1: Robar del mazo principal.
        - Opción 2: Robar de la pila de descartes.

```java
@Override
	public int choosePlay() {
		return (int) (Math.random() * 2) + 1;
	}
```

- Método que elige que carta va descartar la CPU. 
    Este método devuelve el índice de la carta a descartar.

```java
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
```

- Método que comprueba que la CPU puede cerrar la ronda en base a sus cartas.
    Este método devuelve true/false si la CPU puede cerrar o no.

```java
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
```

- Método que le permite realizar combinaciones a la CPU y eliminando las cartas combinadas.

```java
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
```

## RELACIONES

Cpu es utilizada por la clase Game para permitir a la IA jugar la partida, pudiendo cerrar, descartar o elegir donde robar. Además, Cpu es subclase de Entity.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)