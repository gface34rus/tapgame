package com.tapgame.tap_game.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.tapgame.tap_game.models.GameState;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер для главного окна приложения "Тапалка Алабуга".
 * 
 * <p>Этот класс реализует интерфейс {@link Initializable} и отвечает за:
 * <ul>
 *   <li>Обработку пользовательских действий (клики по кнопкам)</li>
 *   <li>Управление состоянием пользовательского интерфейса</li>
 *   <li>Взаимодействие с игровой логикой ({@link GameState})</li>
 *   <li>Обновление отображения данных в UI</li>
 * </ul>
 * 
 * <p><strong>Архитектура:</strong>
 * <p>Контроллер следует паттерну MVC и является посредником между
 * пользовательским интерфейсом (FXML) и бизнес-логикой (GameState).
 * 
 * <p><strong>Основные функции:</strong>
 * <ul>
 *   <li>Выполнение квестов и получение наград</li>
 *   <li>Покупка билетиков за монеты</li>
 *   <li>Улучшение характеристик персонажа</li>
 *   <li>Отображение текущего состояния игры</li>
 * </ul>
 * 
 * @author Алабуга
 * @version 1.0
 * @since 1.0
 * @see Initializable
 * @see GameState
 * @see FXML
 */
public class MainController implements Initializable {

    // === Элементы интерфейса для отображения информации ===
    
    /** Метка для отображения текущего количества монет игрока */
    @FXML
    private Label coinsLabel;
    
    /** Метка для отображения информации о текущем сезоне */
    @FXML
    private Label seasonLabel;
    
    /** Метка для отображения общего количества монет в сезоне */
    @FXML
    private Label totalCoinsLabel;
    
    /** Метка для отображения стоимости билетика */
    @FXML
    private Label ticketPriceLabel;
    
    /** Кнопка для покупки билетика */
    @FXML
    private Button buyTicketButton;
    
    /** Метка для отображения персонажа (эмодзи) */
    @FXML
    private Label characterDisplay;
    
    /** Метка для отображения уровня персонажа */
    @FXML
    private Label characterLevel;
    
    /** Метка для отображения уровня бустера скорости */
    @FXML
    private Label speedLevel;
    
    /** Метка для отображения уровня бустера награды */
    @FXML
    private Label rewardLevel;
    
    /** Метка для отображения стоимости улучшения скорости */
    @FXML
    private Label speedCost;
    
    /** Метка для отображения стоимости улучшения награды */
    @FXML
    private Label rewardCost;
    
    /** Кнопка для улучшения скорости квестов */
    @FXML
    private Button upgradeSpeedButton;
    
    /** Кнопка для улучшения награды за квесты */
    @FXML
    private Button upgradeRewardButton;
    
    // === Кнопки квестов ===
    
    /** Кнопка для выполнения квеста "Подписка на Telegram канал" */
    @FXML
    private Button questTelegram;
    
    /** Кнопка для выполнения квеста "Подписка на Яндекс.Дзен" */
    @FXML
    private Button questDzen;
    
    /** Кнопка для выполнения квеста "Участие в корпоративном портале" */
    @FXML
    private Button questPortal;

    /** Объект, содержащий игровую логику и состояние */
    private GameState gameState;

    /**
     * Инициализация контроллера.
     * 
     * <p>Этот метод вызывается автоматически JavaFX после загрузки FXML файла.
     * Он выполняет следующие действия:
     * <ol>
     *   <li>Создает новый экземпляр {@link GameState}</li>
     *   <li>Обновляет пользовательский интерфейс</li>
     *   <li>Настраивает обработчики событий для всех кнопок</li>
     * </ol>
     * 
     * @param location URL, используемый для разрешения относительных путей для корневого объекта
     * @param resources ресурсы, используемые для локализации корневого объекта
     * 
     * @see Initializable#initialize(URL, ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameState = new GameState();
        updateUI();
        setupEventHandlers();
    }

    /**
     * Настройка обработчиков событий для всех кнопок интерфейса.
     * 
     * <p>Этот метод связывает каждую кнопку с соответствующим обработчиком:
     * <ul>
     *   <li>Кнопки квестов → {@link #completeQuest(String)}</li>
     *   <li>Кнопка покупки билетика → {@link #buyTicket()}</li>
     *   <li>Кнопки улучшений → {@link #upgradeSpeed()} и {@link #upgradeReward()}</li>
     * </ul>
     */
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

    /**
     * Выполнение квеста указанного типа.
     * 
     * <p>Этот метод пытается выполнить квест через {@link GameState#completeQuest(String)}.
     * Если квест выполнен успешно, обновляется интерфейс и показывается сообщение о награде.
     * Если квест уже был выполнен, показывается соответствующее сообщение.
     * 
     * @param questType тип квеста ("telegram", "dzen", "portal")
     * 
     * @see GameState#completeQuest(String)
     * @see GameState#getQuestReward()
     */
    private void completeQuest(String questType) {
        if (gameState.completeQuest(questType)) {
            updateUI();
            showMessage("Квест выполнен! Получено " + gameState.getQuestReward() + " монет.");
        } else {
            showMessage("Квест уже выполнен!");
        }
    }

    /**
     * Покупка билетика за монеты.
     * 
     * <p>Этот метод пытается купить билетик через {@link GameState#buyTicket()}.
     * Если покупка успешна, обновляется интерфейс и показывается сообщение.
     * Если недостаточно монет, показывается соответствующее сообщение.
     * 
     * @see GameState#buyTicket()
     */
    private void buyTicket() {
        if (gameState.buyTicket()) {
            updateUI();
            showMessage("Билетик куплен!");
        } else {
            showMessage("Недостаточно монет!");
        }
    }

    /**
     * Улучшение скорости выполнения квестов.
     * 
     * <p>Этот метод пытается улучшить скорость через {@link GameState#upgradeSpeed()}.
     * Если улучшение успешно, обновляется интерфейс и показывается сообщение.
     * Если недостаточно монет, показывается соответствующее сообщение.
     * 
     * @see GameState#upgradeSpeed()
     */
    private void upgradeSpeed() {
        if (gameState.upgradeSpeed()) {
            updateUI();
            showMessage("Скорость увеличена!");
        } else {
            showMessage("Недостаточно монет!");
        }
    }

    /**
     * Улучшение награды за выполнение квестов.
     * 
     * <p>Этот метод пытается улучшить награду через {@link GameState#upgradeReward()}.
     * Если улучшение успешно, обновляется интерфейс и показывается сообщение.
     * Если недостаточно монет, показывается соответствующее сообщение.
     * 
     * @see GameState#upgradeReward()
     */
    private void upgradeReward() {
        if (gameState.upgradeReward()) {
            updateUI();
            showMessage("Награда увеличена!");
        } else {
            showMessage("Недостаточно монет!");
        }
    }

    /**
     * Обновление пользовательского интерфейса.
     * 
     * <p>Этот метод синхронизирует отображение данных в интерфейсе с текущим
     * состоянием игры. Обновляются:
     * <ul>
     *   <li>Количество монет</li>
     *   <li>Уровень персонажа</li>
     *   <li>Уровни бустеров</li>
     *   <li>Стоимость улучшений</li>
     *   <li>Состояние кнопок</li>
     * </ul>
     * 
     * @see #updateButtonStates()
     */
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

    /**
     * Обновление состояния кнопок интерфейса.
     * 
     * <p>Этот метод активирует/деактивирует кнопки в зависимости от текущего
     * состояния игры:
     * <ul>
     *   <li>Кнопки квестов деактивируются после выполнения</li>
     *   <li>Кнопка покупки билетика деактивируется при недостатке монет</li>
     *   <li>Кнопки улучшений деактивируются при недостатке монет</li>
     * </ul>
     */
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

    /**
     * Отображение сообщения пользователю.
     * 
     * <p>В текущей реализации сообщения выводятся в консоль.
     * В будущих версиях можно добавить popup окна или toast уведомления.
     * 
     * @param message текст сообщения для отображения
     */
    private void showMessage(String message) {
        // Простое отображение сообщения в консоли
        // В будущем можно добавить popup или toast уведомления
        System.out.println(message);
    }
}
