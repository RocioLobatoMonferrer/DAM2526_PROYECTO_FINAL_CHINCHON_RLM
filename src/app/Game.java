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
	 * Método que añade los jugadores
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
	 * Método que prepara el deck creandolo
	 * 
	 * @param times Cantidad de veces que va a tener que crearlo
	 */

	private void prepareDeck(int times) {
		deckCard.createDeck(times);
	}

	/**
	 * 
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
	 * Método que prepara la ronda para los jugadores
	 */

	private void startRound() { /* TODO: Decidir quien es el ganador */
		int turn = 1, closerIndex = -1;
		IEntity player;
		canClose = false;
		gameOver = false;
		long playersLeft;

		while (closerIndex == -1) {
			menu.showTurn(turn);
			if (turn >= 2) {
				canClose = true;
			}

			for (int i = 0; i < players.size(); i++) {

				player = players.get(i);

				if (player.getStatus() == EntityStatus.INSIDE && startTurn(player)) {
					closerIndex = i;
					i = players.size();
				}
			}
			turn++;
		}

		roundScore(closerIndex);
		menu.showRoundEnd();

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
	 * 
	 * @param closerIndex
	 * @return
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
	 * Método que define los turnos de un jugador, diviendose en robó, decidir si
	 * descartar o cerrar la ronda
	 * 
	 * @param player Jugador que jugará ese turno
	 * @return true/false dependiendo de la acción del jugador
	 */

	private boolean startTurn(IEntity player) {
		menu.showPlayerTurn(player.getNickname());
		drawPhase(player);

		if (decisionPhase(player)) {
			return true;
		}

		discardPhase(player);
		return false;
	}

	/**
	 * Método que verifica si el jugador puede o no cerrar en ese turno
	 * 
	 * @param player Jugador que va a cerrar en el turno
	 * @return true/false si el jugador cierra de manera correcta o no
	 */

	// TODO: Terminar esta madre

	private boolean closeTurn(IEntity player) {

		if (combinationLoop(player)) {
			if (!winForChinchon) {
				menu.showPerfectClosing();
			}
			player.getHand().clear();
			player.endTempMode();
			return true;
		}

		List<Card> remaining = player.getTempHand();
		int points, index, valueCard;
		Card leftover, keptCard;

		// Caso 1: Cierre perfecto, -10 puntos
		if (remaining.isEmpty()) {
			menu.showPerfectClosing();
			player.getHand().clear();
			player.endTempMode();
			return true;
		}

		// Caso 2: Cierre con más de dos cartas en mano
		if (remaining.size() > 2) {
			player.endTempMode();
			if (remaining.size() == player.getHand().size()) {

			} else {
				menu.errorCombination();
			}

			return false;
		}

		// Caso 3: Cierre con 1 o 2 cartas
		if (remaining.size() == 1) {
			index = 0;
			leftover = remaining.get(index);
		} else {
			menu.showHand(player);
			index = menu.selectClosingCard() - 1;
			leftover = remaining.get(index);
		}

		valueCard = leftover.type().getScoreValue();

		if (valueCard < 1 || valueCard > 5) {
			player.endTempMode();
			menu.errorCombination();
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
		player.getHand().clear();
		if (keptCard != null) {
			player.getHand().add(keptCard);
		}
		player.endTempMode();
		return true;

	}

	/**
	 * Método que busca las cartas combinadas del jugador dependiendo de su índice
	 * 
	 * @param hand    Mano del jugador
	 * @param indexes Índices
	 * @return
	 */

	// TODO: Terminar esta madre

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
	 * Método
	 * 
	 * @param player
	 * @return
	 */

	// TODO: Terminar esta madre

	private List<Integer> askForIndexes(IEntity player) {
		List<Integer> indexes = new ArrayList<>();
		int index;
		boolean valid;
		String input;
		String[] parts;

		menu.showHand(player);
		input = menu.listCards();

		if (input.trim().isEmpty()) {
			return indexes;
		}

		parts = input.trim().split("\\s+");
		valid = true;

		for (String p : parts) {

			try {

				int value = Integer.parseInt(p);

				if (value < 1 || value > player.getTempHand().size()) {
					valid = false;
				} else {
					index = value - 1;

					if (!indexes.contains(index)) {
						indexes.add(index);
					} else {
						valid = false;
					}
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
	 * 
	 * @param player
	 */

	private boolean combinationLoop(IEntity player) {
		player.startTempMode();
		List<Integer> indexes, sortedIndexes;
		List<Card> selectedCards;
		int combination;
		boolean canContinue = true, combinationValid;

		while (canContinue) {

			combinationValid = false;

			while (!combinationValid) {

				indexes = askForIndexes(player);

				if (indexes.isEmpty()) {
					combinationValid = true;
					canContinue = false;

				} else {

					combination = menu.selectCombination();
					selectedCards = getCardsFromIndexes(player.getTempHand(), indexes);

					if (!player.validateCombination(selectedCards, combination)) {
						menu.errorCombination();

					} else {

						sortedIndexes = new ArrayList<>(indexes);
						sortedIndexes.sort(Collections.reverseOrder());

						for (int i : sortedIndexes) {
							player.getTempHand().remove(i);
						}

						if (combination == 3 && selectedCards.size() == 7) {
							winForChinchon = true;
							return true;
						}

						combinationValid = true;
					}
				}
			}

			if (player.getTempHand().size() <= 2) {
				canContinue = false;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param player
	 */

	private void declarePhase(IEntity player) {
		combinationLoop(player);

		player.getHand().clear();
		player.getHand().addAll(player.getTempHand());
		player.endTempMode();
	}

	/**
	 * 
	 * @param closerIndex
	 * @param player
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
	 * 
	 * @param player
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
	 * 
	 * @param player
	 * @return
	 */

	// TODO: Terminar esta madre

	private boolean decisionPhase(IEntity player) {
		int option;
		boolean decisionMade;

		if (player instanceof ICpu) {
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
	 * 
	 * @param player
	 */

	private void discardPhase(IEntity player) {
		int index;
		Card cardToDiscard;

		if (player instanceof Cpu cpu) { // CPU
			index = cpu.chooseDiscard(); // Método Provisional
			cardToDiscard = cpu.discard(index); // Método Provisional2

		} else { // JUGADOR
			menu.showHand(player);
			index = menu.selectToDiscard() - 1;
			cardToDiscard = player.discard(index);
		}
		deckCard.addCardInDiscard(cardToDiscard);
	}

}
