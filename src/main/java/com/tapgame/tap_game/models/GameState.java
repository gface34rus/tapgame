package com.tapgame.tap_game.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, представляющий состояние игры "Тапалка Алабуга".
 * 
 * <p>Этот класс содержит всю игровую логику и данные о текущем состоянии игры:
 * <ul>
 *   <li><strong>Монеты</strong> - валюта игры для покупок и улучшений</li>
 *   <li><strong>Квесты</strong> - система заданий с наградами</li>
 *   <li><strong>Персонаж</strong> - уровень и характеристики игрока</li>
 *   <li><strong>Бустеры</strong> - улучшения для персонажа</li>
 *   <li><strong>Призы</strong> - система покупки билетиков</li>
 * </ul>
 * 
 * <p><strong>Игровая механика:</strong>
 * <ul>
 *   <li>Квесты дают монеты и могут быть выполнены только один раз</li>
 *   <li>Бустеры увеличивают эффективность квестов</li>
 *   <li>Уровень персонажа зависит от суммы уровней бустеров</li>
 *   <li>Стоимость улучшений растет с каждым уровнем</li>
 * </ul>
 * 
 * <p><strong>Константы игры:</strong>
 * <ul>
 *   <li>Базовая награда за квест: 10 монет</li>
 *   <li>Стоимость билетика: 50 монет</li>
 *   <li>Базовая стоимость улучшения скорости: 25 монет</li>
 *   <li>Базовая стоимость улучшения награды: 30 монет</li>
 * </ul>
 * 
 * @author Алабуга
 * @version 1.0
 * @since 1.0
 * @see Map
 * @see HashMap
 */
public class GameState {
    // === Игровые данные ===
    
    /** Текущее количество монет у игрока */
    private int coins;
    
    /** Текущий уровень персонажа */
    private int characterLevel;
    
    /** Уровень бустера скорости квестов */
    private int speedLevel;
    
    /** Уровень бустера награды за квесты */
    private int rewardLevel;
    
    /** Карта выполненных квестов (ключ - тип квеста, значение - выполнено ли) */
    private Map<String, Boolean> completedQuests;
    
    /** Количество купленных билетиков */
    private int ticketsBought;
    
    // === Константы игры ===
    
    /** Базовая награда за выполнение квеста (в монетах) */
    private static final int QUEST_REWARD_BASE = 10;
    
    /** Стоимость одного билетика (в монетах) */
    private static final int TICKET_PRICE = 50;
    
    /** Базовая стоимость улучшения скорости (в монетах) */
    private static final int SPEED_UPGRADE_BASE_COST = 25;
    
    /** Базовая стоимость улучшения награды (в монетах) */
    private static final int REWARD_UPGRADE_BASE_COST = 30;
    
    /** Базовый уровень персонажа */
    private static final int CHARACTER_LEVEL_BASE = 1;

    /**
     * Конструктор по умолчанию.
     * 
     * <p>Создает новое игровое состояние с начальными значениями:
     * <ul>
     *   <li>Монеты: 0</li>
     *   <li>Уровень персонажа: 1</li>
     *   <li>Уровень скорости: 1</li>
     *   <li>Уровень награды: 1</li>
     *   <li>Все квесты: не выполнены</li>
     *   <li>Билетики: 0</li>
     * </ul>
     * 
     * <p>Инициализирует карту квестов со всеми доступными типами:
     * "telegram", "dzen", "portal"
     */
    public GameState() {
        this.coins = 0;
        this.characterLevel = CHARACTER_LEVEL_BASE;
        this.speedLevel = 1;
        this.rewardLevel = 1;
        this.completedQuests = new HashMap<>();
        this.ticketsBought = 0;
        
        // Инициализируем квесты как невыполненные
        completedQuests.put("telegram", false);
        completedQuests.put("dzen", false);
        completedQuests.put("portal", false);
    }

    /**
     * Выполнение квеста указанного типа.
     * 
     * <p>Пытается выполнить квест, если он еще не был выполнен.
     * При успешном выполнении:
     * <ul>
     *   <li>Отмечает квест как выполненный</li>
     *   <li>Добавляет награду к количеству монет</li>
     *   <li>Возвращает {@code true}</li>
     * </ul>
     * 
     * <p>Если квест уже выполнен, возвращает {@code false}.
     * 
     * @param questType тип квеста ("telegram", "dzen", "portal")
     * @return {@code true} если квест был выполнен, {@code false} если уже был выполнен
     * 
     * @see #getQuestReward()
     */
    public boolean completeQuest(String questType) {
        if (completedQuests.getOrDefault(questType, false)) {
            return false; // Квест уже выполнен
        }
        
        completedQuests.put(questType, true);
        int reward = getQuestReward();
        coins += reward;
        return true;
    }

    /**
     * Покупка билетика за монеты.
     * 
     * <p>Пытается купить билетик, если у игрока достаточно монет.
     * При успешной покупке:
     * <ul>
     *   <li>Списывает стоимость билетика с монет</li>
     *   <li>Увеличивает счетчик купленных билетиков</li>
     *   <li>Возвращает {@code true}</li>
     * </ul>
     * 
     * <p>Если недостаточно монет, возвращает {@code false}.
     * 
     * @return {@code true} если билетик был куплен, {@code false} если недостаточно монет
     * 
     * @see #TICKET_PRICE
     */
    public boolean buyTicket() {
        if (coins >= TICKET_PRICE) {
            coins -= TICKET_PRICE;
            ticketsBought++;
            return true;
        }
        return false;
    }

    /**
     * Улучшение скорости выполнения квестов.
     * 
     * <p>Пытается улучшить бустер скорости, если у игрока достаточно монет.
     * При успешном улучшении:
     * <ul>
     *   <li>Списывает стоимость улучшения с монет</li>
     *   <li>Увеличивает уровень скорости</li>
     *   <li>Обновляет уровень персонажа</li>
     *   <li>Возвращает {@code true}</li>
     * </ul>
     * 
     * <p>Если недостаточно монет, возвращает {@code false}.
     * 
     * @return {@code true} если улучшение было выполнено, {@code false} если недостаточно монет
     * 
     * @see #getSpeedUpgradeCost()
     * @see #updateCharacterLevel()
     */
    public boolean upgradeSpeed() {
        int cost = getSpeedUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            speedLevel++;
            updateCharacterLevel();
            return true;
        }
        return false;
    }

    /**
     * Улучшение награды за выполнение квестов.
     * 
     * <p>Пытается улучшить бустер награды, если у игрока достаточно монет.
     * При успешном улучшении:
     * <ul>
     *   <li>Списывает стоимость улучшения с монет</li>
     *   <li>Увеличивает уровень награды</li>
     *   <li>Обновляет уровень персонажа</li>
     *   <li>Возвращает {@code true}</li>
     * </ul>
     * 
     * <p>Если недостаточно монет, возвращает {@code false}.
     * 
     * @return {@code true} если улучшение было выполнено, {@code false} если недостаточно монет
     * 
     * @see #getRewardUpgradeCost()
     * @see #updateCharacterLevel()
     */
    public boolean upgradeReward() {
        int cost = getRewardUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            rewardLevel++;
            updateCharacterLevel();
            return true;
        }
        return false;
    }

    /**
     * Обновление уровня персонажа.
     * 
     * <p>Уровень персонажа рассчитывается как сумма уровней бустеров
     * плюс базовый уровень. Формула:
     * <pre>
     * characterLevel = CHARACTER_LEVEL_BASE + (speedLevel - 1) + (rewardLevel - 1)
     * </pre>
     * 
     * <p>Этот метод вызывается автоматически при улучшении любого бустера.
     */
    private void updateCharacterLevel() {
        // Уровень персонажа зависит от суммы уровней бустеров
        characterLevel = CHARACTER_LEVEL_BASE + (speedLevel - 1) + (rewardLevel - 1);
    }

    /**
     * Получение текущей награды за выполнение квеста.
     * 
     * <p>Награда рассчитывается как базовая награда, умноженная на уровень бустера награды.
     * 
     * @return количество монет, получаемых за выполнение квеста
     * 
     * @see #QUEST_REWARD_BASE
     */
    public int getQuestReward() {
        return QUEST_REWARD_BASE * rewardLevel;
    }

    /**
     * Получение стоимости улучшения скорости.
     * 
     * <p>Стоимость рассчитывается как базовая стоимость, умноженная на текущий уровень скорости.
     * 
     * @return стоимость улучшения скорости в монетах
     * 
     * @see #SPEED_UPGRADE_BASE_COST
     */
    public int getSpeedUpgradeCost() {
        return SPEED_UPGRADE_BASE_COST * speedLevel;
    }

    /**
     * Получение стоимости улучшения награды.
     * 
     * <p>Стоимость рассчитывается как базовая стоимость, умноженная на текущий уровень награды.
     * 
     * @return стоимость улучшения награды в монетах
     * 
     * @see #REWARD_UPGRADE_BASE_COST
     */
    public int getRewardUpgradeCost() {
        return REWARD_UPGRADE_BASE_COST * rewardLevel;
    }

    // === Геттеры ===

    /**
     * Получение текущего количества монет.
     * 
     * @return количество монет у игрока
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Получение текущего уровня персонажа.
     * 
     * @return уровень персонажа
     */
    public int getCharacterLevel() {
        return characterLevel;
    }

    /**
     * Получение текущего уровня бустера скорости.
     * 
     * @return уровень бустера скорости
     */
    public int getSpeedLevel() {
        return speedLevel;
    }

    /**
     * Получение текущего уровня бустера награды.
     * 
     * @return уровень бустера награды
     */
    public int getRewardLevel() {
        return rewardLevel;
    }

    /**
     * Проверка выполнения квеста.
     * 
     * @param questType тип квеста для проверки
     * @return {@code true} если квест выполнен, {@code false} если нет
     */
    public boolean isQuestCompleted(String questType) {
        return completedQuests.getOrDefault(questType, false);
    }

    /**
     * Получение стоимости билетика.
     * 
     * @return стоимость одного билетика в монетах
     * 
     * @see #TICKET_PRICE
     */
    public int getTicketPrice() {
        return TICKET_PRICE;
    }

    /**
     * Получение количества купленных билетиков.
     * 
     * @return количество купленных билетиков
     */
    public int getTicketsBought() {
        return ticketsBought;
    }

    /**
     * Получение общего количества монет в сезоне.
     * 
     * <p>В текущей реализации возвращает фиксированное значение.
     * В будущих версиях может быть динамическим.
     * 
     * @return общее количество монет в сезоне (заглушка: 1000)
     */
    public int getTotalSeasonCoins() {
        return 1000; // Заглушка для общего количества монет в сезоне
    }

    /**
     * Добавление монет напрямую (для тестирования).
     * 
     * <p>Этот метод предназначен для тестирования и отладки.
     * В продакшене не должен использоваться.
     * 
     * @param amount количество монет для добавления
     */
    public void addCoins(int amount) {
        this.coins += amount;
    }
}
