package com.tapgame.tap_game.models;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private int coins;
    private int characterLevel;
    private int speedLevel;
    private int rewardLevel;
    private Map<String, Boolean> completedQuests;
    private int ticketsBought;
    
    // Константы
    private static final int QUEST_REWARD_BASE = 10;
    private static final int TICKET_PRICE = 50;
    private static final int SPEED_UPGRADE_BASE_COST = 25;
    private static final int REWARD_UPGRADE_BASE_COST = 30;
    private static final int CHARACTER_LEVEL_BASE = 1;

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

    public boolean completeQuest(String questType) {
        if (completedQuests.getOrDefault(questType, false)) {
            return false; // Квест уже выполнен
        }
        
        completedQuests.put(questType, true);
        int reward = getQuestReward();
        coins += reward;
        return true;
    }

    public boolean buyTicket() {
        if (coins >= TICKET_PRICE) {
            coins -= TICKET_PRICE;
            ticketsBought++;
            return true;
        }
        return false;
    }

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

    private void updateCharacterLevel() {
        // Уровень персонажа зависит от суммы уровней бустеров
        characterLevel = CHARACTER_LEVEL_BASE + (speedLevel - 1) + (rewardLevel - 1);
    }

    public int getQuestReward() {
        return QUEST_REWARD_BASE * rewardLevel;
    }

    public int getSpeedUpgradeCost() {
        return SPEED_UPGRADE_BASE_COST * speedLevel;
    }

    public int getRewardUpgradeCost() {
        return REWARD_UPGRADE_BASE_COST * rewardLevel;
    }

    // Геттеры
    public int getCoins() {
        return coins;
    }

    public int getCharacterLevel() {
        return characterLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public int getRewardLevel() {
        return rewardLevel;
    }

    public boolean isQuestCompleted(String questType) {
        return completedQuests.getOrDefault(questType, false);
    }

    public int getTicketPrice() {
        return TICKET_PRICE;
    }

    public int getTicketsBought() {
        return ticketsBought;
    }

    public int getTotalSeasonCoins() {
        return 1000; // Заглушка для общего количества монет в сезоне
    }

    // Метод для тестирования - добавляет монеты напрямую
    public void addCoins(int amount) {
        this.coins += amount;
    }
}
