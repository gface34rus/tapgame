package com.tapgame.tap_game;

import org.junit.jupiter.api.Test;
import com.tapgame.tap_game.models.GameState;

import static org.junit.jupiter.api.Assertions.*;

class TapGameApplicationTests {

	@Test
	void gameStateInitialization() {
		GameState gameState = new GameState();
		assertEquals(0, gameState.getCoins());
		assertEquals(1, gameState.getCharacterLevel());
		assertEquals(1, gameState.getSpeedLevel());
		assertEquals(1, gameState.getRewardLevel());
	}

	@Test
	void questCompletion() {
		GameState gameState = new GameState();
		assertFalse(gameState.isQuestCompleted("telegram"));
		
		boolean result = gameState.completeQuest("telegram");
		assertTrue(result);
		assertTrue(gameState.isQuestCompleted("telegram"));
		assertEquals(10, gameState.getCoins()); // Базовая награда
		
		// Попытка выполнить уже выполненный квест
		result = gameState.completeQuest("telegram");
		assertFalse(result);
	}

	@Test
	void ticketPurchase() {
		GameState gameState = new GameState();
		gameState.completeQuest("telegram"); // Получаем монеты
		gameState.completeQuest("dzen");
		gameState.completeQuest("portal");
		
		// После выполнения всех квестов у нас должно быть 30 монет (3 квеста * 10 монет)
		assertEquals(30, gameState.getCoins());
		// Билетик стоит 50 монет, поэтому покупка должна не удаться
		boolean result = gameState.buyTicket();
		assertFalse(result);
		assertEquals(0, gameState.getTicketsBought());
		
		// Давайте добавим еще монет для успешной покупки
		gameState.completeQuest("telegram"); // Попытка выполнить уже выполненный квест
		gameState.completeQuest("dzen");     // Попытка выполнить уже выполненный квест
		gameState.completeQuest("portal");   // Попытка выполнить уже выполненный квест
		
		// Монеты не должны увеличиться
		assertEquals(30, gameState.getCoins());
		
		// Создадим новое состояние с достаточным количеством монет
		GameState gameState2 = new GameState();
		// Добавим монеты напрямую для тестирования
		gameState2.addCoins(60);
		
		assertTrue(gameState2.getCoins() >= 50);
		boolean result2 = gameState2.buyTicket();
		assertTrue(result2);
		assertEquals(1, gameState2.getTicketsBought());
	}

	@Test
	void characterUpgrades() {
		GameState gameState = new GameState();
		// Получаем достаточно монет
		gameState.completeQuest("telegram");
		gameState.completeQuest("dzen");
		gameState.completeQuest("portal");
		
		int initialSpeedLevel = gameState.getSpeedLevel();
		boolean result = gameState.upgradeSpeed();
		assertTrue(result);
		assertEquals(initialSpeedLevel + 1, gameState.getSpeedLevel());
		assertEquals(2, gameState.getCharacterLevel());
	}
}
