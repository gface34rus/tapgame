package com.tapgame.tap_game.models;

/**
 * Класс, представляющий состояние игры "Гусь-Тапалка".
 *
 * <p>Игрок тапает по гусю, чтобы зарабатывать монеты и улучшать своего гуся.
 */
public class GooseGameState {
    // Базовые игровые параметры
    private long totalClicks;       // Общее количество кликов по гусю
    private long coins;             // Текущее количество монет
    private long coinsPerClick;     // Монет за клик
    private long coinsPerSecond;    // Пассивный доход в секунду

    // Уровни улучшений
    private int clickPowerLevel;    // Уровень силы клика
    private int autoClickerLevel;   // Уровень автокликера

    // Стоимость улучшений
    private static final int CLICK_POWER_BASE_COST = 10;
    private static final int AUTO_CLICKER_BASE_COST = 50;

    public GooseGameState() {
        this.totalClicks = 0;
        this.coins = 0;
        this.coinsPerClick = 1;
        this.coinsPerSecond = 0;
        this.clickPowerLevel = 1;
        this.autoClickerLevel = 0;
    }

    /**
     * Обработка клика по гусю
     *
     * @return количество заработанных монет за клик
     */
    public long tapGoose() {
        totalClicks++;
        coins += coinsPerClick;
        return coinsPerClick;
    }

    /**
     * Улучшение силы клика
     *
     * @return true, если улучшение куплено, false если недостаточно монет
     */
    public boolean upgradeClickPower() {
        long cost = getClickPowerUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            clickPowerLevel++;
            updateStats();
            return true;
        }
        return false;
    }

    /**
     * Улучшение автокликера
     *
     * @return true, если улучшение куплено, false если недостаточно монет
     */
    public boolean upgradeAutoClicker() {
        long cost = getAutoClickerUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            autoClickerLevel++;
            updateStats();
            return true;
        }
        return false;
    }

    /**
     * Обновление игровых параметров на основе уровней улучшений
     */
    private void updateStats() {
        coinsPerClick = clickPowerLevel;
        coinsPerSecond = autoClickerLevel;
    }

    // Геттеры для стоимости улучшений
    public long getClickPowerUpgradeCost() {
        return (long) (CLICK_POWER_BASE_COST * Math.pow(1.15, clickPowerLevel - 1));
    }

    public long getAutoClickerUpgradeCost() {
        return (long) (AUTO_CLICKER_BASE_COST * Math.pow(1.15, autoClickerLevel));
    }

    // Геттеры для UI
    public long getTotalClicks() {
        return totalClicks;
    }

    public long getCoins() {
        return coins;
    }

    public long getCoinsPerClick() {
        return coinsPerClick;
    }

    public long getCoinsPerSecond() {
        return coinsPerSecond;
    }

    public int getClickPowerLevel() {
        return clickPowerLevel;
    }

    public int getAutoClickerLevel() {
        return autoClickerLevel;
    }

    /**
     * Добавление пассивного дохода
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
