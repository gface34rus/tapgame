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
 * Контроллер для игры "Гусь-Тапалка".
 * 
 * <p>Управляет логикой взаимодействия пользователя с игровым интерфейсом,
 * обновляет состояние игры и отображает актуальную информацию.</p>
 * 
 * <p>Основные функции:
 * <ul>
 *   <li>Обработка нажатий на гуся</li>
 *   <li>Управление улучшениями</li>
 *   <li>Обновление игрового состояния</li>
 *   <li>Отображение статистики</li>
 * </ul>
 * 
 * @see GooseGameState
 */
public class GooseGameController {
    /** Текущее состояние игры */
    private GooseGameState gameState;
    
    /** Время последнего обновления игры (в миллисекундах) */
    private long lastUpdateTime;
    
    /** Форматтер для отображения чисел */
    private final NumberFormat numberFormat = NumberFormat.getInstance();

    /** Метка для отображения количества монет */
    @FXML
    private Label coinsLabel;
    
    /** Метка для отображения количества монет в секунду */
    @FXML
    private Label cpsLabel;
    
    /** Метка для отображения количества монет за клик */
    @FXML
    private Label cpcLabel;
    
    /** Кнопка гуся, по которой нужно кликать */
    @FXML
    private Button gooseButton;
    
    /** Кнопка улучшения силы клика */
    @FXML
    private Button upgradeClickButton;
    
    /** Кнопка улучшения автокликера */
    @FXML
    private Button upgradeAutoClickerButton;
    
    /** Главная панель интерфейса */
    @FXML
    private AnchorPane mainPane;

    /**
     * Инициализация контроллера.
     * 
     * <p>Вызывается автоматически при загрузке FXML. Выполняет:
     * <ul>
     *   <li>Инициализацию состояния игры</li>
     *   <li>Настройку форматирования чисел</li>
     *   <li>Запуск игрового цикла</li>
     * </ul>
     */
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

    /**
     * Настройка кнопки гуся.
     * 
     * <p>Загружает изображение гуся и устанавливает обработчик клика.</p>
     */
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

    /**
     * Обновляет игровое состояние.
     * 
     * <p>Вызывается в игровом цикле. Вычисляет прошедшее время
     * и начисляет пассивный доход.</p>
     */
    private void updateGame() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;

        if (deltaTime >= 1000) { // Обновляем раз в секунду
            gameState.addPassiveIncome(deltaTime);
            updateUI();
            lastUpdateTime = currentTime;
        }
    }

    /**
     * Обновляет отображение игрового состояния.
     * 
     * <p>Обновляет все элементы интерфейса в соответствии
     * с текущим состоянием игры.</p>
     */
    private void updateUI() {
        coinsLabel.setText(numberFormat.format(gameState.getCoins()) + " монет");
        cpsLabel.setText(numberFormat.format(gameState.getCoinsPerSecond()) + " монет/сек");
        cpcLabel.setText(numberFormat.format(gameState.getCoinsPerClick()) + " монет/клик");

        // Обновление кнопок улучшений
        upgradeClickButton.setText("Улучшить клик (" + numberFormat.format(gameState.getClickPowerUpgradeCost()) + " монет)");
        upgradeAutoClickerButton.setText("Купить автокликер (" + numberFormat.format(gameState.getAutoClickerUpgradeCost()) + " монет)");
    }

    /**
     * Обработчик улучшения силы клика.
     * 
     * <p>Увеличивает количество монет, получаемых за клик.
     * Обновляет отображение стоимости улучшения.</p>
     */
    @FXML
    private void onUpgradeClick() {
        if (gameState.upgradeClickPower()) {
            updateUI();
            // Эффект при улучшении
            showUpgradeEffect(upgradeClickButton);
        }
    }

    /**
     * Обработчик улучшения автокликера.
     * 
     * <p>Увеличивает пассивный доход в секунду.
     * Обновляет отображение стоимости улучшения.</p>
     */
    @FXML
    private void onUpgradeAutoClicker() {
        if (gameState.upgradeAutoClicker()) {
            updateUI();
            // Эффект при улучшении
            showUpgradeEffect(upgradeAutoClickerButton);
        }
    }

    /**
     * Показывает анимацию получения монет.
     * 
     * <p>Создает всплывающую метку с количеством полученных монет.</p>
     * 
     * @param amount количество полученных монет
     */
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

    /**
     * Показывает эффект улучшения.
     * 
     * <p>Изменяет цвет кнопки на зеленый на 0.5 секунд.</p>
     * 
     * @param button кнопка, для которой показываем эффект
     */
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

    /**
     * Обработчик нажатия на гуся.
     * 
     * <p>Увеличивает количество монет в зависимости от силы клика.
     * Показывает анимацию получения монет.</p>
     */
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
