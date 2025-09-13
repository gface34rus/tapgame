package com.tapgame.tap_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Главный класс приложения "Тапалка Алабуга".
 * 
 * <p>Это JavaFX приложение представляет собой игру с тремя основными разделами:
 * <ul>
 *   <li><strong>Квесты</strong> - выполнение заданий для получения монет</li>
 *   <li><strong>Призы</strong> - покупка билетиков за монеты</li>
 *   <li><strong>Качать персонажа</strong> - улучшение характеристик персонажа</li>
 * </ul>
 * 
 * <p>Приложение использует FXML для создания пользовательского интерфейса
 * и следует паттерну MVC (Model-View-Controller).
 * 
 * <p><strong>Технические детали:</strong>
 * <ul>
 *   <li>Размер окна: 800x600 пикселей</li>
 *   <li>Минимальный размер: 800x600 пикселей</li>
 *   <li>Окно не изменяет размер</li>
 *   <li>Использует модульную систему Java 9+</li>
 * </ul>
 * 
 * @author Алабуга
 * @version 1.0
 * @since 1.0
 * @see Application
 * @see Stage
 * @see Scene
 */
public class TapGameApplication extends Application {

    /**
     * Точка входа в JavaFX приложение.
     * 
     * <p>Этот метод вызывается автоматически JavaFX при запуске приложения.
     * Он выполняет следующие действия:
     * <ol>
     *   <li>Загружает FXML файл с интерфейсом</li>
     *   <li>Создает сцену с заданными размерами</li>
     *   <li>Настраивает окно приложения</li>
     *   <li>Отображает окно пользователю</li>
     * </ol>
     * 
     * @param primaryStage главное окно приложения, предоставляемое JavaFX
     * @throws Exception если произошла ошибка при загрузке FXML или создании сцены
     * 
     * @see FXMLLoader#load(java.net.URL)
     * @see Stage#setTitle(String)
     * @see Stage#setScene(Scene)
     * @see Stage#setMinWidth(double)
     * @see Stage#setMinHeight(double)
     * @see Stage#setResizable(boolean)
     * @see Stage#show()
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("Тапалка Алабуга");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Главный метод приложения.
     * 
     * <p>Этот метод является точкой входа в приложение. Он запускает JavaFX приложение
     * с помощью метода {@link Application#launch(String...)}.
     * 
     * <p><strong>Пример использования:</strong>
     * <pre>{@code
     * // Запуск приложения
     * java com.tapgame.tap_game.TapGameApplication
     * 
     * // Или через Maven
     * mvn javafx:run
     * }</pre>
     * 
     * @param args аргументы командной строки (не используются)
     * 
     * @see Application#launch(String...)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
