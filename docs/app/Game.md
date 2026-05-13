# GAME

## FUNCIÓN

Clase encargada de gestionar la partida al completo.

## ATRIBUTOS

- Atributo que alamcena en una lista los jugadores de la partida.

```java
private List<IEntity> players;
```

- Atributo que tiene tanto la baraja principal como la de descarte.

```java
private IDeckCard deckCard;
```

- Atributo que mostrará los mensajes necesarios para el usuario.

```java
private Menu menu;
```

- Atributo factoria que creará una entidad o una CPU dependiendo de una elección.

```java
private FactoryEntity factory;
```

- Atributo que indica la puntuación máxima de la partida.

```java
private int maxPoints;
```

- Atributo que indica si se puede cerrar la partida o no.

```java
private boolean canClose;
```

- Atributo que indica si la partida ha finalizado por un Chinchón o no.

```java
private boolean winForChinchon;
```

- Método que indica cuando la partida ha finalizado o no.

```java
private boolean gameOver;
```

## CONSTRUCTOR

- Constructor de la clase que se encarga de instanciarla.

```java
public Game() {
		players = new ArrayList<>();
		menu = Menu.getInstance();
		factory = new FactoryEntity();
		deckCard = new DeckCard();
	}
```

## MÉTODOS

- Método que muestra el menú principal y gestiona la configuración inicial de la partida, permitiendo al usuario iniciarla o salirse de esta.

```java
@Override
	public void startConfiguration() {
		int option;

		do {
			option = menu.mainMenu();
			switch (option) {
			case 1 -> startGame();
			}
		} while (option != 2);
		menu.closeConsole();
	}
```

- Método que añade la entidad a la lista de jugadores de la partida.
Podemos encontrar los siguientes parametros:

	- entity: Entidad de la partida.

```java
private void addEntity(IEntity entity) {
		players.add(entity);
	}
```

- Método que prepara a los jugadores preguntale los puntos máximos, cuántos serán y sus apodos.

```java
private void preparePlayer() {
		int numberPlayers;
		maxPoints = menu.howManyPoints();

		numberPlayers = menu.numberOfPlayers();
		for (int i = 0; i < numberPlayers; i++) {
			addEntity(factory.createEntity(menu.typeOfEntity(), menu.nicknameOfPlayers()));
		}
	}
```

- Método que prepara la baraja creándola según el número de jugadores.
Podemos encontrar los siguientes parametros:

	- times: Cantidad de veces que va a tener que crearlo.

```java
private void prepareDeck(int times) {
		deckCard.createDeck(times);
	}
```

- Método que inicia la partida preparando jugadores y las barajas, se reparten las cartas y ejecuta las rondas hasta que la partida termine.

```java
private void startGame() {
		players.clear();
		preparePlayer();

		gameOver = false;

		while (!gameOver) {
			winForChinchon = false;
			prepareDeck(players.size());
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getStatus() == EntityStatus.INSIDE) {
					players.get(i).clearHand();
					for (int j = 0; j < 7; j++) {
						players.get(i).draw(deckCard.drawDeck());
					}
				}
			}
			deckCard.addCardInDiscard(deckCard.drawDeck());
			startRound();
		}

	}
```

- Método que gestiona una ronda completa, ejecutandose los turnos de los jugadores hasta que alguno cierre.

```java
private void startRound() {
		int turn = 1, closerIndex = -1;

		canClose = false;
		gameOver = false;

		while (closerIndex == -1) {
			if (turn >= 2) {
				canClose = true;
			}
			closerIndex = playTurn(turn);
			turn++;
		}

		roundScore(closerIndex);
		menu.showRoundEnd();
		checkGameOver(closerIndex);

	}
```

- Método que ejecuta el turno de cada jugador que siga dentro de la partida.
Podemos encontrar los siguientes parametros:

	- turn: Número del turno actual.

	Este método devuelve el índice del jugador que haya cerrado la ronda, o -1 si no es nadie.


```java
private int playTurn(int turn) {
		IEntity player;
		for (int i = 0; i < players.size(); i++) {

			player = players.get(i);

			if (player.getStatus() == EntityStatus.INSIDE && startTurn(player, turn)) {
				return i;
			}
		}
		return -1;
	}
```

- Método que comprueba si la partida ha terminado, ya sea por chinchón o por quedar solo un jugador dentro de la partida. Podemos encontrar los siguientes parametros:

	- closerIndex: Índice del jugador que ha cerrado la ronda.

```java
private void checkGameOver(int closerIndex) {
		long playersLeft;
		playersLeft = players.stream().filter(p -> p.getStatus() == EntityStatus.INSIDE).count();

		if (winForChinchon || playersLeft <= 1) {
			gameOver = true;
			if (winForChinchon) {
				menu.showWinnerForChinchon(getWinner(closerIndex));
			} else {
				menu.showWinner(getWinner(closerIndex));
			}
		}
	}
```

- Método que devuelve el ganador de la partida, ya sea porque alguien ha hecho un chinchón o el jugador con menos puntos. Podemos encontrar los siguientes parametros:

	- closerIndex: Índice del jugador que ha cerrado la ronda.

	Este método devuelve el apodo del jugador ganador.	

```java
private String getWinner(int closerIndex) {
		if (winForChinchon) {
			return players.get(closerIndex).getNickname();
		}
		return players
				.stream().filter(p -> p.getStatus() == EntityStatus.INSIDE).findFirst().orElse(players.stream()
						.min((a, b) -> Integer.compare(a.getScore(), b.getScore())).orElse(players.get(closerIndex)))
				.getNickname();
	}
```

- Método que gestiona el turno de un jugador, dividiéndose en fase de robo y fase de decidir si descartar o cerrar la ronda. Podemos encontrar los siguientes parametros:

	- player: Jugador que jugará ese turno.
	- turn: Número del turno actual.

	Este método devuelve true/false dependiendo de si el jugador cierra la ronda o no.

```java
private boolean startTurn(IEntity player, int turn) {
		if (!(player instanceof ICpu)) {
			menu.fakeClearConsole();
			menu.showTurn(turn);
			menu.showPlayerTurn(player.getNickname());
		}

		drawPhase(player);

		if (decisionPhase(player)) {
			return true;
		}

		discardPhase(player);
		return false;
	}
```

- Método que verifica si el jugador puede o no cerrar en ese turno y lo gestiona dependiendo si es un jugador o una CPU. Podemos encontrar los siguientes parametros:

	- player: Jugador que va a cerrar en el turno.

	Este método devuelve true/false si el jugador cierra de manera correcta o no.	

```java
private boolean closeTurn(IEntity player) {

		if (player instanceof ICpu cpu) {
			return closeTurnCpu(cpu);
		}

		if (combinationLoop(player)) {
			if (!winForChinchon) {
				menu.showPerfectClosing();
			}
			player.clearHand();
			player.endTempMode();
			return true;
		}

		return resolveClosing(player);
	}
```

- Método que gestiona el cierre de la ronda para la CPU, declarando sus combinaciones y descartando la carta con mayor valor permitido. Podemos encontrar los siguientes parametros:

	- cpu: CPU que cerrará la ronda.

	Este método devuelve true/false si la CPU cierra correctamente o no.

```java
private boolean closeTurnCpu(ICpu cpu) {
		cpu.declareCombinations();

		int val;
		List<Card> handCpu = cpu.getHand();
		int worstIndex = -1;
		for (int i = 0; i < handCpu.size(); i++) {
			val = handCpu.get(i).type().getScoreValue();
			if (val <= 5) {
				if (worstIndex == -1 || val > handCpu.get(worstIndex).type().getScoreValue()) {
					worstIndex = i;
				}
			}
		}
		if (worstIndex == -1) {
			cpu.clearHand();
			return true;
		}
		deckCard.addCardInDiscard(cpu.discard(worstIndex));
		return true;
	}
```

- Método que resuelve el cierre de la ronda para un jugador humano, comprobando las cartas sobrantes y gestionando los distintos casos de cierre. Podemos encontrar los siguientes parametros:

	- player: Jugador que cerrará la ronda.

	Este método devuelve true/false si el jugador cierra correctamente o no.

```java
private boolean resolveClosing(IEntity player) {
		List<Card> remaining = player.getTempHand();
		int originalSize = player.getHand().size();

		// Caso 1: Cierre perfecto, -10 puntos
		if (remaining.isEmpty()) {
			menu.showPerfectClosing();
			player.clearHand();
			player.endTempMode();
			return true;
		}

		// Caso 2: Cierre con más de dos cartas en mano
		if (remaining.size() > 2) {
			player.endTempMode();
			if (remaining.size() != originalSize) {
				menu.errorCombination();
			}

			return false;
		}

		return closeWithLeftover(player, remaining);
	}
```

- Método que gestiona el cierre de la ronda cuando al jugador le sobran una o dos cartas tras realizar sus combinaciones. Podemos encontrar los siguientes parametros:

	- player: Jugador que cerrará la ronda.
	- remaining: Cartas sobrantes tras las combinaciones.

	Este método devuelve true/false si el jugador cierra correctamente o no.

```java
private boolean closeWithLeftover(IEntity player, List<Card> remaining) {
		int points, index, valueCard;
		Card leftover, keptCard;

		// Caso 3: Cierre con 1 o 2 cartas
		if (remaining.size() == 1) {
			index = 0;
			leftover = remaining.get(index);
			menu.showClosingCard(leftover.toString());
		} else {
			menu.showHand(player);
			index = menu.selectClosingCard() - 1;
			leftover = remaining.get(index);
		}

		valueCard = leftover.type().getScoreValue();

		if (valueCard < 1 || valueCard > 5) {
			player.endTempMode();
			menu.errorCloseCard();
			return false;
		}

		keptCard = remaining.size() == 1 ? null : remaining.get(index == 0 ? 1 : 0);
		points = keptCard == null ? 0 : player.calculateScore(List.of(keptCard));

			// Solo se puede cerrar si no se supera maxPoints
		if (player.getScore() + points >= maxPoints) { 
			player.endTempMode();
			menu.errorPoints();
			return false;
		}

		deckCard.addCardInDiscard(leftover);
		player.clearHand();
		if (keptCard != null) {
			player.getHand().add(keptCard);
		}
		player.endTempMode();
		return true;
	}
```

- Método que obtiene las cartas de la mano del jugador a partir de sus índices.
Podemos encontrar los siguientes parametros:

	- hand: Mano del jugador.
	- indexes: Índices de las cartas seleccionadas.

	Este método devuelve la lista de cartas correspondientes a los índices proporcionados.

```java
private List<Card> getCardsFromIndexes(List<Card> hand, List<Integer> indexes) {
		List<Card> cards = new ArrayList<>();

		for (int i : indexes) {
			if (i >= 0 && i < hand.size()) {
				cards.add(hand.get(i));
			}
		}

		return cards;
	}
```

- Método que solicita al jugador los índices de las cartas que desea combinar.
Podemos encontrar los siguientes parametros:

	- player: Jugador al que se le piden los índices.

	Este método devuelve la lista de índices seleccionados por el jugador.

```java
private List<Integer> askForIndexes(IEntity player) {
		menu.showHand(player);
		String input = menu.listCards();

		if (input.trim().isEmpty()) {
			return new ArrayList<>();
		}

		return parseIndexes(input.trim().split("\\s+"), player.getTempHand().size());
	}
```

- Método que parsea y valida los índices introducidos por el jugador respecto al tamaño de la mano temporal. Podemos encontrar los siguientes parametros:

	- parts: Array de cadenas con los índices introducidos.
	- handSize: Tamaño de la mano temporal del jugador.

	Este método devuelve la lista de índices válidos introducidos por el jugador.

```java
private List<Integer> parseIndexes(String[] parts, int handSize) {
		List<Integer> indexes = new ArrayList<>();
		boolean valid = true;
		int value;

		for (String p : parts) {
			try {
				value = Integer.parseInt(p);
				if (value < 1 || value > handSize || indexes.contains(value - 1)) {
					valid = false;
				} else {
					indexes.add(value - 1);
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		if (!valid) {
			indexes.clear();
			menu.errorCombination();
		}

		return indexes;
	}
```

- Método que gestiona el bucle de combinaciones del jugador, permitiéndole introducir combinaciones hasta que no pueda o no quiera continuar. Podemos encontrar los siguientes parametros:

	- player: Jugador que realizará las combinaciones.

	Este método devuelve true/false si el jugador ha realizado un chinchón o no.

```java
private boolean combinationLoop(IEntity player) {
		player.startTempMode();
		boolean canContinue = true, combinationValid;

		while (canContinue && player.getTempHand().size() > 2) {
			combinationValid = false;

			while (!combinationValid) {
				List<Integer> indexes = askForIndexes(player);

				if (indexes.isEmpty()) {
					combinationValid = true;
					canContinue = false;
				} else {
					combinationValid = processCombination(player, indexes);
					if (winForChinchon) {
						return true;
					}
				}
			}

			if (player.getTempHand().size() <= 1) {
				canContinue = false;
			}
		}
		return false;
	}
```

- Método que procesa una combinación introducida por el jugador, validándola y eliminando las cartas combinadas de la mano temporal. Podemos encontrar los siguientes parametros:

	- player: Jugador que realiza la combinación.
	- indexes: Índices de las cartas seleccionadas para la combinación.

	Este método devuelve true/false si la combinación es válida o no.

```java
private boolean processCombination(IEntity player, List<Integer> indexes) {
		int combination;
		List<Card> selectedCards;

		selectedCards = getCardsFromIndexes(player.getTempHand(), indexes);

		if (selectedCards.size() >= player.getTempHand().size() && player.getTempHand().size() != 7) {
			menu.errorCombination();
			return false;
		}

		combination = detectCombination(player, selectedCards);

		if (combination == 0) {
			menu.errorCombination();
			return false;
		}

		List<Integer> sortedIndexes = new ArrayList<>(indexes);
		sortedIndexes.sort(Collections.reverseOrder());
		for (int i : sortedIndexes) {
			player.getTempHand().remove(i);
		}

		if (combination == 3 && selectedCards.size() == 7) {
			winForChinchon = true;
		}

		return true;
	}
```

- Método que detecta el tipo de combinación formada por las cartas seleccionadas, comprobando si es un chinchón, escalera o serie. Podemos encontrar los siguientes parametros:

	- player: Jugador que realiza la combinación.
	- cards: Cartas seleccionadas para la combinación.

     Este método devuelve una de las siguientes opciones disponibles para el usuario:

        - Opción 1: Serie.
	    - Opción 2: Escalera.
	    - Opción 3: Chinchón.
	    - Opción 0: Combinación inválida.

```java
private int detectCombination(IEntity player, List<Card> cards) {

		if (player.validateCombination(cards, 3)) {
			return 3;
		} else if (player.validateCombination(cards, 2)) {
			return 2;
		} else if (player.validateCombination(cards, 1)) {
			return 1;
		} else {
			return 0;
		}
	}
```

- Método que gestiona la fase de declaración de combinaciones al final de la ronda, tanto para jugadores humanos como para la CPU. Podemos encontrar los siguientes parametros:

	- player: Jugador que declarará sus combinaciones.

```java
private void declarePhase(IEntity player) {
		if (player instanceof ICpu cpu) {
			cpu.declareCombinations();
		} else {
			combinationLoop(player);

			player.clearHand();
			player.getHand().addAll(player.getTempHand());
			player.endTempMode();
		}
	}
```

- Método que calcula y asigna la puntuación de cada jugador al finalizar la ronda, comprobando si alguno ha sido eliminado por superar el límite de puntos. Podemos encontrar los siguientes parametros:

	- closerIndex: Índice del jugador que ha cerrado la ronda.

```java
private void roundScore(int closerIndex) {
		int points;
		for (int i = 0; i < players.size(); i++) {
			IEntity player = players.get(i);
			if (i != closerIndex && !winForChinchon && player.getStatus() == EntityStatus.INSIDE) {
				declarePhase(player);
			}
		}
		for (int i = 0; i < players.size(); i++) {
			IEntity player = players.get(i);
			if (player.getStatus() == EntityStatus.INSIDE) {
				points = player.calculateScore(player.getHand());
				player.addScore(points);

				if (!winForChinchon) {
					menu.showRoundScore(player, points);
					if (player.getScore() >= maxPoints) {
						player.isOut();
						menu.showPlayerOut(player.getNickname());
					}
				}
			}

		}
	}
```

- Método que gestiona la fase de robo del turno, permitiendo al jugador robar del mazo principal o de la pila de descartes. Podemos encontrar los siguientes parametros:

	- player: Jugador que robará la carta.

```java
private void drawPhase(IEntity player) {
		int option;

		if (player instanceof ICpu cpu) {
			option = cpu.choosePlay();
		} else {
			menu.showHand(player);
			option = menu.turnMenu(deckCard.getFirstDiscardCard().toString());
		}

		if (option == 1) {
			player.draw(deckCard.drawDeck());
		} else {
			player.draw(deckCard.drawDiscardCard());
		}
	}
```

- Método que gestiona la fase de decisión del turno, permitiendo al jugador decidir si descartar o cerrar la ronda. Podemos encontrar los siguientes parametros:

	- player: Jugador que tomará la decisión.

	Este método devuelve true/false si el jugador decide cerrar la ronda o no. 

```java
private boolean decisionPhase(IEntity player) {
		int option;
		boolean decisionMade;

		if (player instanceof ICpu cpu) {
			if (canClose && cpu.canClose()) {
				return closeTurn(player);
			}
			return false;
		}

		decisionMade = false;

		while (!decisionMade) {

			menu.showHand(player);
			option = menu.turnMenu2();

			if (option == 2) {
				if (!canClose) {
					menu.errorClose();
				} else {
					if (closeTurn(player)) {
						return true;
					}
				}
			} else {
				decisionMade = true;
			}
		}
		return false;
	}
```

- Método que gestiona la fase de descarte del turno, eliminando una carta de la mano del jugador y añadiéndola a la pila de descartes. Podemos encontrar los siguientes parametros:

	- player: Jugador que descartará la carta.

```java
private void discardPhase(IEntity player) {
		int index;
		Card cardToDiscard;

		if (player instanceof Cpu cpu) {
			index = cpu.chooseDiscard();
			cardToDiscard = cpu.discard(index);

		} else {
			menu.showHand(player);
			index = menu.selectToDiscard() - 1;
			cardToDiscard = player.discard(index);
		}
		deckCard.addCardInDiscard(cardToDiscard);
	}
```

## RELACIONES

Game es utilizado por la clase Main para que lleve el flujo de la partida.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)