package com.tapgame.tap_game.controllers;

import com.tapgame.tap_game.models.GooseGameState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.text.NumberFormat;

/**
 * Контроллер для игры "Гусь-Тапалка"
 */
public class GooseGameController {
    private GooseGameState gameState;
    private long lastUpdateTime;
    private final NumberFormat numberFormat = NumberFormat.getInstance();

    @FXML
    private Label coinsLabel;
    @FXML
    private Label cpsLabel; // Coins per second
    @FXML
    private Label cpcLabel; // Coins per click
    @FXML
    private Button gooseButton;
    @FXML
    private Button upgradeClickButton;
    @FXML
    private Button upgradeAutoClickerButton;
    @FXML
    private AnchorPane mainPane;

    @FXML
    public void initialize() {
        gameState = new GooseGameState();
        lastUpdateTime = System.currentTimeMillis();

        // Настройка форматирования чисел
        numberFormat.setGroupingUsed(true);

        // Запуск игрового цикла
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame();
            }
        }.start();

        // Настройка кнопки гуся
        setupGooseButton();
        updateUI();
    }

    private void setupGooseButton() {
        // Загрузка изображения гуся
        try {
            Image gooseImage = new Image(getClass().getResourceAsStream("/images/goose.png"));
            ImageView imageView = new ImageView(gooseImage);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            gooseButton.setGraphic(imageView);
        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение гуся: " + e.getMessage());
        }

        // Обработка клика по гусю
        gooseButton.setOnAction(event -> {
            long earned = gameState.tapGoose();
            // Анимация получения монет
            showEarnedCoins(earned);
            updateUI();
        });
    }

    private void updateGame() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;

        if (deltaTime >= 1000) { // Обновляем раз в секунду
            gameState.addPassiveIncome(deltaTime);
            updateUI();
            lastUpdateTime = currentTime;
        }
    }

    private void updateUI() {
        coinsLabel.setText(numberFormat.format(gameState.getCoins()) + " монет");
        cpsLabel.setText(numberFormat.format(gameState.getCoinsPerSecond()) + " монет/сек");
        cpcLabel.setText(numberFormat.format(gameState.getCoinsPerClick()) + " монет/клик");

        // Обновление кнопок улучшений
        upgradeClickButton.setText("Улучшить клик (" + numberFormat.format(gameState.getClickPowerUpgradeCost()) + " монет)");
        upgradeAutoClickerButton.setText("Купить автокликер (" + numberFormat.format(gameState.getAutoClickerUpgradeCost()) + " монет)");
    }

    @FXML
    private void onUpgradeClick() {
        if (gameState.upgradeClickPower()) {
            updateUI();
            // Эффект при улучшении
            showUpgradeEffect(upgradeClickButton);
        }
    }

    @FXML
    private void onUpgradeAutoClicker() {
        if (gameState.upgradeAutoClicker()) {
            updateUI();
            // Эффект при улучшении
            showUpgradeEffect(upgradeAutoClickerButton);
        }
    }

    private void showEarnedCoins(long amount) {
        Label popup = new Label("+" + amount);
        popup.setStyle("-fx-text-fill: gold; -fx-font-weight: bold; -fx-font-size: 16px;");

        // Позиционируем над гусем
        popup.setLayoutX(gooseButton.getLayoutX() + 50);
        popup.setLayoutY(gooseButton.getLayoutY() - 30);

        mainPane.getChildren().add(popup);

        // Анимация всплывающего текста
        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(1),
                        e -> mainPane.getChildren().remove(popup)
                )
        ).play();
    }

    private void showUpgradeEffect(Button button) {
        String originalText = button.getText();
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(0.5),
                        e -> button.setStyle("")
                )
        ).play();
    }

    @FXML
    private void onGooseClick(javafx.event.ActionEvent event) {
        // Получаем количество заработанных монет
        long earned = gameState.tapGoose();

        // Показываем анимацию получения монет
        showEarnedCoins(earned);

        // Обновляем интерфейс
        updateUI();
    }
}
