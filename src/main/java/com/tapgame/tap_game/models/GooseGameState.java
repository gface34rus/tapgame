package com.tapgame.tap_game.models;

/**
 * Класс, представляющий состояние игры "Гусь-Тапалка".
 *
 * <p>Отвечает за хранение и обновление всех игровых параметров, включая:
 * <ul>
 *   <li>Количество монет у игрока</li>
 *   <li>Параметры улучшений</li>
 *   <li>Статистику игры</li>
 * </ul>
 *
 * <p>Игровой процесс построен на двух основных механиках:
 * <ol>
 *   <li>Активное взаимодействие - клики по гусю</li>
 *   <li>Пассивный доход - автоматическое получение монет</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 */
public class GooseGameState {
    // Базовые игровые параметры
    /** Общее количество кликов по гусю */
    private long totalClicks;
    
    /** Текущее количество монет у игрока */
    private long coins;
    
    /** Количество монет, получаемых за один клик */
    private long coinsPerClick;
    
    /** Количество монет, получаемых автоматически каждую секунду */
    private long coinsPerSecond;

    // Уровни улучшений
    /** Текущий уровень улучшения силы клика */
    private int clickPowerLevel;
    
    /** Текущий уровень улучшения автокликера */
    private int autoClickerLevel;

    // Стоимость улучшений
    /** Базовая стоимость улучшения силы клика */
    private static final int CLICK_POWER_BASE_COST = 10;
    
    /** Базовая стоимость улучшения автокликера */
    private static final int AUTO_CLICKER_BASE_COST = 50;

    /**
     * Создает новое состояние игры с начальными параметрами.
     * 
     * <p>Инициализирует игру со следующими значениями по умолчанию:
     * <ul>
     *   <li>Монеты: 0</li>
     *   <li>Монет за клик: 1</li>
     *   <li>Монет в секунду: 0</li>
     *   <li>Уровень улучшения клика: 1</li>
     *   <li>Уровень автокликера: 0</li>
     * </ul>
     */
    public GooseGameState() {
        this.totalClicks = 0;
        this.coins = 0;
        this.coinsPerClick = 1;
        this.coinsPerSecond = 0;
        this.clickPowerLevel = 1;
        this.autoClickerLevel = 0;
    }

    /**
     * Обработка клика по гусю.
     * 
     * <p>Увеличивает счетчик кликов и добавляет монеты в зависимости
     * от текущей силы клика.</p>
     *
     * @return количество заработанных монет за клик
     */
    public long tapGoose() {
        totalClicks++;
        coins += coinsPerClick;
        return coinsPerClick;
    }

    /**
     * Улучшает силу клика.
     * 
     * <p>Увеличивает количество монет, получаемых за клик.
     * Стоимость улучшения растет с каждым уровнем.</p>
     *
     * @return true, если улучшение куплено, false если недостаточно монет
     */
    public boolean upgradeClickPower() {
        long cost = getClickPowerUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            clickPowerLevel++;
            coinsPerClick = (long) Math.pow(2, clickPowerLevel - 1);
            return true;
        }
        return false;
    }

    /**
     * Улучшает автокликер.
     * 
     * <p>Увеличивает количество монет, получаемых автоматически каждую секунду.
     * Стоимость улучшения растет с каждым уровнем.</p>
     *
     * @return true, если улучшение куплено, false если недостаточно монет
     */
    public boolean upgradeAutoClicker() {
        long cost = getAutoClickerUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            autoClickerLevel++;
            coinsPerSecond = autoClickerLevel * 5L;
            return true;
        }
        return false;
    }

    /**
     * Рассчитывает стоимость следующего уровня улучшения силы клика.
     * 
     * @return стоимость улучшения в монетах
     */
    public long getClickPowerUpgradeCost() {
        return (long) (CLICK_POWER_BASE_COST * Math.pow(1.5, clickPowerLevel - 1));
    }

    /**
     * Рассчитывает стоимость следующего уровня улучшения автокликера.
     * 
     * @return стоимость улучшения в монетах
     */
    public long getAutoClickerUpgradeCost() {
        return (long) (AUTO_CLICKER_BASE_COST * Math.pow(1.5, autoClickerLevel));
    }

    // Геттеры для доступа к состоянию игры
    
    /**
     * Возвращает текущее количество монет.
     * 
     * @return количество монет
     */
    public long getCoins() {
        return coins;
    }

    /**
     * Возвращает текущее количество монет за клик.
     * 
     * @return монет за клик
     */
    public long getCoinsPerClick() {
        return coinsPerClick;
    }

    /**
     * Возвращает текущее количество монет в секунду.
     * 
     * @return монет в секунду
     */
    public long getCoinsPerSecond() {
        return coinsPerSecond;
    }

    /**
     * Возвращает текущий уровень улучшения клика.
     * 
     * @return уровень улучшения
     */
    public int getClickPowerLevel() {
        return clickPowerLevel;
    }

    /**
     * Возвращает текущий уровень автокликера.
     * 
     * @return уровень автокликера
     */
    public int getAutoClickerLevel() {
        return autoClickerLevel;
    }

    /**
     * Возвращает общее количество кликов.
     * 
     * @return количество кликов
     */
    public long getTotalClicks() {
        return totalClicks;
    }

    /**
     * Добавление пассивного дохода.
     * 
     * @param deltaTime время в миллисекундах с прошлого обновления
     */
    public void addPassiveIncome(long deltaTime) {
        if (coinsPerSecond > 0) {
            double secondsPassed = deltaTime / 1000.0;
            coins += (long) (coinsPerSecond * secondsPassed);
        }
    }
}
