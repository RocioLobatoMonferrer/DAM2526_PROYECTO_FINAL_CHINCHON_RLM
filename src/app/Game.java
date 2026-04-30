package app;

import java.util.ArrayList;
import java.util.List;

import dominio.Card;
import dominio.Cpu;
import dominio.DeckCard;
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
	private ConsoleInput ci;
	private FactoryEntity factory;
	private int maxPoints;
	private boolean canClose;

	/**
	 * Constructor de la clase que se encarga de instanciarla
	 *
	 */

	public Game() {
		players = new ArrayList<>();
		menu = Menu.getInstance();
		ci = ConsoleInput.getInstance();
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
	 * @inheritDoc
	 */

	@Override
	public void startGame() {
		preparePlayer();
		prepareDeck(players.size());

		for (int i = 0; i < players.size(); i++) {
			for (int j = 0; j < 7; j++) {
				players.get(i).draw(deckCard.drawDeck());
			}
		}
		deckCard.addCardInDiscard(deckCard.drawDeck());
		startRound();
	}

	/**
	 * Método que prepara la ronda para los jugadores
	 */

	private void startRound() { /* TODO: Decidir quien es el ganador */
		int turn = 1;
		IEntity player;
		boolean gameOver = false;
		canClose = false;

		while (!gameOver) {
			ci.writeLine(String.format("        TURNO %d\n   ______________\n", turn));
			for (int i = 0; i < players.size(); i++) {
				player = players.get(i);

				startTurn(player);

				if (turn >= 2) {
					canClose = true;
				}
			}
			turn++;
		}

	}

	/**
	 * Método que define los turnos de un jugador, diviendose en robó, decidir si
	 * descartar o cerrar la ronda
	 * 
	 * @param player Jugador que jugará ese turno
	 * @return true/false dependiendo de la acción del jugador
	 */

	private boolean startTurn(IEntity player) {
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
		List<Integer> indexes;
		List<Card> selectedCards, remaining;
		int combination, points, valueCard;
		Card leftover;

		indexes = askForIndexes(player);
		combination = menu.selectCombination();

		if (indexes.isEmpty()) {
			menu.errorCombination();
			return false;
		}

		selectedCards = getCardsFromIndexes(player, indexes);

		if (!player.validateCombination(selectedCards, combination)) {
			menu.errorCombination();
			return false;
		}

		if (combination == 3 && indexes.size() == 7) {
			return true;
		}

		remaining = getRemainingCards(player, indexes);
		points = player.calculateScore(remaining);

		if (points >= maxPoints) {
			menu.errorPoints();
			return false;
		}

		if (remaining.isEmpty()) {
			return true;
		}

		if (remaining.size() == 1) {
			leftover = remaining.get(0);
			valueCard = leftover.type().getScoreValue();

			if (valueCard >= 1 && valueCard <= 5) {
				return true;
			}
		}

		menu.errorCombination();
		return false;

	}

	/**
	 * Método que busca las cartas combinadas del jugador dependiendo de su índice
	 * 
	 * @param player  Jugador
	 * @param indexes Índices
	 * @return
	 */

	// TODO: Terminar esta madre

	private List<Card> getCardsFromIndexes(IEntity player, List<Integer> indexes) {
		List<Card> cards = new ArrayList<>();

		for (int i : indexes) {
			cards.add(player.getHand().get(i));
		}

		return cards;
	}

	/**
	 * Método que busca las cartas sobrantes que no fueron combinadas por el jugador
	 * dependiendo de su índice
	 * 
	 * @param player  Jugador
	 * @param indexes Índices
	 * @return
	 */

	// TODO: Terminar esta madre

	private List<Card> getRemainingCards(IEntity player, List<Integer> indexes) {
		List<Card> remaining = new ArrayList<>(player.getHand());
		List<Card> selected = getCardsFromIndexes(player, indexes);

		remaining.removeAll(selected);

		return remaining;
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
		boolean valid = false;
		String input;
		String[] parts;

		menu.showHand(player);
		input = menu.listCards();
		parts = input.trim().split("\\s+");
		valid = true;

		for (String p : parts) {

			try {

				int value = Integer.parseInt(p);

				if (value < 1 || value > 8) {
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
		boolean closed, decisionMade;

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
					closed = closeTurn(player);

					if (closed) {
						return true;
					} else {
						decisionMade = true;
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
