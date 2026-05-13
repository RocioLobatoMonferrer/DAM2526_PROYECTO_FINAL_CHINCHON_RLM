package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dominio.Card;
import dominio.Cpu;
import dominio.DeckCard;
import dominio.EntityStatus;
import dominio.FactoryEntity;
import dominio.ICpu;
import dominio.IDeckCard;
import dominio.IEntity;

/**
 * Clase encargada de gestionar la partida al completo
 */

public class Game implements IGame {
	private List<IEntity> players;
	private IDeckCard deckCard;
	private Menu menu;
	private FactoryEntity factory;
	private int maxPoints;
	private boolean canClose;
	private boolean winForChinchon;
	private boolean gameOver;

	/**
	 * Constructor de la clase que se encarga de instanciarla
	 *
	 */

	public Game() {
		players = new ArrayList<>();
		menu = Menu.getInstance();
		factory = new FactoryEntity();
		deckCard = new DeckCard();
	}

	/**
	 * @inheritDoc
	 */

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

	/**
	 * Método que añade la entidad a la lista de jugadores de la partida
	 * 
	 * @param entity Entidad de la partida
	 */

	private void addEntity(IEntity entity) {
		players.add(entity);
	}

	/**
	 * Método que prepara a los jugadores preguntale los puntos máximos, cuántos
	 * serán y sus apodos
	 */

	private void preparePlayer() {
		int numberPlayers;
		maxPoints = menu.howManyPoints();

		numberPlayers = menu.numberOfPlayers();
		for (int i = 0; i < numberPlayers; i++) {
			addEntity(factory.createEntity(menu.typeOfEntity(), menu.nicknameOfPlayers()));
		}
	}

	/**
	 * Método que prepara la baraja creándola según el número de jugadores
	 * 
	 * @param times Cantidad de veces que va a tener que crearlo
	 */

	private void prepareDeck(int times) {
		deckCard.createDeck(times);
	}

	/**
	 * Método que inicia la partida preparando jugadores y las barajas, se reparten
	 * las cartas y ejecuta las rondas hasta que la partida termine
	 */

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

	/**
	 * Método que gestiona una ronda completa, ejecutandose los turnos de los
	 * jugadores hasta que alguno cierre
	 */

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

	/**
	 * Método que ejecuta el turno de cada jugador que siga dentro de la partida
	 * 
	 * @param turn Número del turno actual
	 * @return Índice del jugador que haya cerrado la ronda, o -1 si no es nadie
	 */

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

	/**
	 * Método que comprueba si la partida ha terminado, ya sea por chinchón o por
	 * quedar solo un jugador dentro de la partida
	 * 
	 * @param closerIndex Índice del jugador que ha cerrado la ronda
	 */

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

	/**
	 * Método que devuelve el ganador de la partida, ya sea porque alguien ha hecho
	 * un chinchón o el jugador con menos puntos
	 * 
	 * @param closerIndex Índice del jugador que ha cerrado la ronda
	 * @return Apodo del jugador ganador
	 */

	private String getWinner(int closerIndex) {
		if (winForChinchon) {
			return players.get(closerIndex).getNickname();
		}
		return players
				.stream().filter(p -> p.getStatus() == EntityStatus.INSIDE).findFirst().orElse(players.stream()
						.min((a, b) -> Integer.compare(a.getScore(), b.getScore())).orElse(players.get(closerIndex)))
				.getNickname();
	}

	/**
	 * Método que gestiona el turno de un jugador, dividiéndose en fase de robo y
	 * fase de decidir si descartar o cerrar la ronda
	 * 
	 * @param player Jugador que jugará ese turno
	 * @param turn   Número del turno actual
	 * @return true/false dependiendo de si el jugador cierra la ronda o no
	 */

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

	/**
	 * Método que verifica si el jugador puede o no cerrar en ese turno y lo
	 * gestiona dependiendo si es un jugador o una CPU
	 * 
	 * @param player Jugador que va a cerrar en el turno
	 * @return true/false si el jugador cierra de manera correcta o no
	 */

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

	/**
	 * Método que gestiona el cierre de la ronda para la CPU, declarando sus
	 * combinaciones y descartando la carta con mayor valor permitido
	 * 
	 * @param cpu CPU que cerrará la ronda
	 * @return true/false si la CPU cierra correctamente o no
	 */

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

	/**
	 * Método que resuelve el cierre de la ronda para un jugador humano, comprobando
	 * las cartas sobrantes y gestionando los distintos casos de cierre
	 * 
	 * @param player Jugador que cerrará la ronda
	 * @return true/false si el jugador cierra correctamente o no
	 */

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

	/**
	 * Método que gestiona el cierre de la ronda cuando al jugador le sobran una o
	 * dos cartas tras realizar sus combinaciones
	 * 
	 * @param player    Jugador que cerrará la ronda
	 * @param remaining Cartas sobrantes tras las combinaciones
	 * @return true/false si el jugador cierra correctamente o no
	 */

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

		if (player.getScore() + points >= maxPoints) { // Solo se puede cerrar si no se supera maxPoints
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

	/**
	 * Método que obtiene las cartas de la mano del jugador a partir de sus índices
	 * 
	 * @param hand    Mano del jugador
	 * @param indexes Índices de las cartas seleccionadas
	 * @return Lista de cartas correspondientes a los índices proporcionados
	 */

	private List<Card> getCardsFromIndexes(List<Card> hand, List<Integer> indexes) {
		List<Card> cards = new ArrayList<>();

		for (int i : indexes) {
			if (i >= 0 && i < hand.size()) {
				cards.add(hand.get(i));
			}
		}

		return cards;
	}

	/**
	 * Método que solicita al jugador los índices de las cartas que desea combinar
	 * 
	 * @param player Jugador al que se le piden los índices
	 * @return Lista de índices seleccionados por el jugador
	 */

	private List<Integer> askForIndexes(IEntity player) {
		menu.showHand(player);
		String input = menu.listCards();

		if (input.trim().isEmpty()) {
			return new ArrayList<>();
		}

		return parseIndexes(input.trim().split("\\s+"), player.getTempHand().size());
	}

	/**
	 * Método que parsea y valida los índices introducidos por el jugador respecto
	 * al tamaño de la mano temporal
	 *
	 * @param parts    Array de cadenas con los índices introducidos
	 * @param handSize Tamaño de la mano temporal del jugador
	 * @return Lista de índices válidos introducidos por el jugador
	 */

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

	/**
	 * Método que gestiona el bucle de combinaciones del jugador, permitiéndole
	 * introducir combinaciones hasta que no pueda o no quiera continuar
	 * 
	 * @param player Jugador que realizará las combinaciones
	 * @return true/false si el jugador ha realizado un chinchón o no
	 */

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

	/**
	 * Método que procesa una combinación introducida por el jugador, validándola y
	 * eliminando las cartas combinadas de la mano temporal
	 * 
	 * @param player  Jugador que realiza la combinación
	 * @param indexes Índices de las cartas seleccionadas para la combinación
	 * @return true/false si la combinación es válida o no
	 */

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

	/**
	 * Método que detecta el tipo de combinación formada por las cartas
	 * seleccionadas, comprobando si es un chinchón, escalera o serie
	 *
	 * @param player Jugador que realiza la combinación
	 * @param cards  Cartas seleccionadas para la combinación
	 * @return Número que indica el tipo de combinación:<br>
	 *         Opción 1: Serie<br>
	 *         Opción 2: Escalera<br>
	 *         Opción 3: Chinchón<br>
	 *         Opción 0: Combinación inválida
	 */

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

	/**
	 * Método que gestiona la fase de declaración de combinaciones al final de la
	 * ronda, tanto para jugadores humanos como para la CPU
	 * 
	 * @param player Jugador que declarará sus combinaciones
	 */

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

	/**
	 * Método que calcula y asigna la puntuación de cada jugador al finalizar la
	 * ronda, comprobando si alguno ha sido eliminado por superar el límite de
	 * puntos
	 * 
	 * @param closerIndex Índice del jugador que ha cerrado la ronda
	 */

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

	/**
	 * Método que gestiona la fase de robo del turno, permitiendo al jugador robar
	 * del mazo principal o de la pila de descartes
	 * 
	 * @param player Jugador que robará la carta
	 */

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

	/**
	 * Método que gestiona la fase de decisión del turno, permitiendo al jugador
	 * decidir si descartar o cerrar la ronda
	 * 
	 * @param player Jugador que tomará la decisión
	 * @return true/false si el jugador decide cerrar la ronda o no
	 */

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

	/**
	 * Método que gestiona la fase de descarte del turno, eliminando una carta de la
	 * mano del jugador y añadiéndola a la pila de descartes
	 * 
	 * @param player Jugador que descartará la carta
	 */

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

}