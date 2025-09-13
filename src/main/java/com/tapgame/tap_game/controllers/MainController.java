package com.tapgame.tap_game.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.tapgame.tap_game.models.GameState;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label coinsLabel;
    
    @FXML
    private Label seasonLabel;
    
    @FXML
    private Label totalCoinsLabel;
    
    @FXML
    private Label ticketPriceLabel;
    
    @FXML
    private Button buyTicketButton;
    
    @FXML
    private Label characterDisplay;
    
    @FXML
    private Label characterLevel;
    
    @FXML
    private Label speedLevel;
    
    @FXML
    private Label rewardLevel;
    
    @FXML
    private Label speedCost;
    
    @FXML
    private Label rewardCost;
    
    @FXML
    private Button upgradeSpeedButton;
    
    @FXML
    private Button upgradeRewardButton;
    
    @FXML
    private Button questTelegram;
    
    @FXML
    private Button questDzen;
    
    @FXML
    private Button questPortal;

    private GameState gameState;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameState = new GameState();
        updateUI();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Обработчики квестов
        questTelegram.setOnAction(e -> completeQuest("telegram"));
        questDzen.setOnAction(e -> completeQuest("dzen"));
        questPortal.setOnAction(e -> completeQuest("portal"));
        
        // Обработчик покупки билетика
        buyTicketButton.setOnAction(e -> buyTicket());
        
        // Обработчики улучшений
        upgradeSpeedButton.setOnAction(e -> upgradeSpeed());
        upgradeRewardButton.setOnAction(e -> upgradeReward());
    }

    private void completeQuest(String questType) {
        if (gameState.completeQuest(questType)) {
            updateUI();
            showMessage("Квест выполнен! Получено " + gameState.getQuestReward() + " монет.");
        } else {
            showMessage("Квест уже выполнен!");
        }
    }

    private void buyTicket() {
        if (gameState.buyTicket()) {
            updateUI();
            showMessage("Билетик куплен!");
        } else {
            showMessage("Недостаточно монет!");
        }
    }

    private void upgradeSpeed() {
        if (gameState.upgradeSpeed()) {
            updateUI();
            showMessage("Скорость увеличена!");
        } else {
            showMessage("Недостаточно монет!");
        }
    }

    private void upgradeReward() {
        if (gameState.upgradeReward()) {
            updateUI();
            showMessage("Награда увеличена!");
        } else {
            showMessage("Недостаточно монет!");
        }
    }

    private void updateUI() {
        // Обновляем монеты
        coinsLabel.setText(String.valueOf(gameState.getCoins()));
        
        // Обновляем информацию о персонаже
        characterLevel.setText("Уровень: " + gameState.getCharacterLevel());
        speedLevel.setText("Ур. " + gameState.getSpeedLevel());
        rewardLevel.setText("Ур. " + gameState.getRewardLevel());
        
        // Обновляем стоимость улучшений
        speedCost.setText("Стоимость: " + gameState.getSpeedUpgradeCost() + " монет");
        rewardCost.setText("Стоимость: " + gameState.getRewardUpgradeCost() + " монет");
        
        // Обновляем состояние кнопок
        updateButtonStates();
    }

    private void updateButtonStates() {
        // Обновляем состояние кнопок квестов
        questTelegram.setDisable(gameState.isQuestCompleted("telegram"));
        questDzen.setDisable(gameState.isQuestCompleted("dzen"));
        questPortal.setDisable(gameState.isQuestCompleted("portal"));
        
        // Обновляем состояние кнопки покупки билетика
        buyTicketButton.setDisable(gameState.getCoins() < gameState.getTicketPrice());
        
        // Обновляем состояние кнопок улучшений
        upgradeSpeedButton.setDisable(gameState.getCoins() < gameState.getSpeedUpgradeCost());
        upgradeRewardButton.setDisable(gameState.getCoins() < gameState.getRewardUpgradeCost());
    }

    private void showMessage(String message) {
        // Простое отображение сообщения в консоли
        // В будущем можно добавить popup или toast уведомления
        System.out.println(message);
    }
}
