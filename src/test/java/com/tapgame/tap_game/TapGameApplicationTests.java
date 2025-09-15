package com.tapgame.tap_game;

import org.junit.jupiter.api.Test;
import com.tapgame.tap_game.models.GameState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор тестов для приложения "Тапалка Алабуга".
 * 
 * <p>Этот класс содержит unit-тесты для проверки корректности работы
 * игровой логики, реализованной в классе {@link GameState}.
 * 
 * <p><strong>Покрываемые сценарии:</strong>
 * <ul>
 *   <li>Инициализация игрового состояния</li>
 *   <li>Выполнение квестов и получение наград</li>
 *   <li>Покупка билетиков</li>
 *   <li>Улучшение характеристик персонажа</li>
 *   <li>Проверка граничных условий</li>
 * </ul>
 * 
 * <p><strong>Используемые технологии:</strong>
 * <ul>
 *   <li>JUnit 5 для фреймворка тестирования</li>
 *   <li>AssertJ для утверждений</li>
 * </ul>
 * 
 * @author Алабуга
 * @version 1.0
 * @since 1.0
 * @see GameState
 * @see Test
 * @see org.junit.jupiter.api.Assertions
 */
class TapGameApplicationTests {

	/**
	 * Тест инициализации игрового состояния.
	 * 
	 * <p>Проверяет, что при создании нового экземпляра {@link GameState}
	 * все значения инициализируются корректными начальными значениями:
	 * <ul>
	 *   <li>Монеты: 0</li>
	 *   <li>Уровень персонажа: 1</li>
	 *   <li>Уровень скорости: 1</li>
	 *   <li>Уровень награды: 1</li>
	 * </ul>
	 */
	@Test
	void gameStateInitialization() {
		GameState gameState = new GameState();
		assertEquals(0, gameState.getCoins());
		assertEquals(1, gameState.getCharacterLevel());
		assertEquals(1, gameState.getSpeedLevel());
		assertEquals(1, gameState.getRewardLevel());
	}

	/**
	 * Тест выполнения квестов.
	 * 
	 * <p>Проверяет следующую логику:
	 * <ul>
	 *   <li>Изначально квест не выполнен</li>
	 *   <li>После выполнения квеста он отмечается как выполненный</li>
	 *   <li>Игрок получает базовую награду (10 монет)</li>
	 *   <li>Повторное выполнение того же квеста невозможно</li>
	 * </ul>
	 */
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

	/**
	 * Тест покупки билетиков.
	 * 
	 * <p>Проверяет следующую логику:
	 * <ul>
	 *   <li>После выполнения всех квестов у игрока 30 монет</li>
	 *   <li>Билетик стоит 50 монет, поэтому покупка не удается</li>
	 *   <li>Повторные попытки выполнения квестов не дают монет</li>
	 *   <li>При достаточном количестве монет покупка успешна</li>
	 * </ul>
	 */
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

	/**
	 * Тест улучшения характеристик персонажа.
	 * 
	 * <p>Проверяет следующую логику:
	 * <ul>
	 *   <li>После выполнения всех квестов у игрока достаточно монет для улучшения</li>
	 *   <li>Улучшение скорости увеличивает уровень скорости</li>
	 *   <li>Уровень персонажа пересчитывается автоматически</li>
	 * </ul>
	 */
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
