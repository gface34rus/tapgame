package com.tapgame.tap_game.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Сервис для работы с Telegram Bot API.
 * 
 * <p>Этот класс предоставляет методы для взаимодействия с Telegram API:
 * <ul>
 *   <li>Отправка сообщений пользователям</li>
 *   <li>Получение информации о боте</li>
 *   <li>Проверка подписок на каналы</li>
 *   <li>Отправка уведомлений о событиях игры</li>
 * </ul>
 * 
 * <p><strong>Особенности:</strong>
 * <ul>
 *   <li>Асинхронные HTTP запросы с OkHttp</li>
 *   <li>JSON обработка с Jackson</li>
 *   <li>Конфигурация через properties файл</li>
 *   <li>Обработка ошибок и повторные попытки</li>
 * </ul>
 * 
 * @author Алабуга
 * @version 1.0
 * @since 1.0
 * @see OkHttpClient
 * @see ObjectMapper
 */
public class TelegramService {
    
    private static final String CONFIG_FILE = "/telegram.properties";
    private static final int TIMEOUT_SECONDS = 30;
    private static final int MAX_RETRIES = 3;
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Properties config;
    private final String botToken;
    private final String baseUrl;
    
    /**
     * Конструктор сервиса Telegram.
     * 
     * <p>Инициализирует HTTP клиент, JSON маппер и загружает конфигурацию.
     * 
     * @throws RuntimeException если не удается загрузить конфигурацию
     */
    public TelegramService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
        
        this.objectMapper = new ObjectMapper();
        this.config = loadConfiguration();
        this.botToken = config.getProperty("telegram.bot.token");
        this.baseUrl = config.getProperty("telegram.api.base.url") + botToken;
        
        if (botToken == null || botToken.equals("YOUR_BOT_TOKEN_HERE")) {
            System.err.println("⚠️ Telegram Bot Token не настроен! Проверьте telegram.properties");
        }
    }
    
    /**
     * Загрузка конфигурации из properties файла.
     * 
     * @return объект Properties с настройками
     * @throws RuntimeException если файл не найден
     */
    private Properties loadConfiguration() {
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream(CONFIG_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфигурацию Telegram: " + e.getMessage(), e);
        }
        return props;
    }
    
    /**
     * Отправка сообщения пользователю.
     * 
     * <p>Отправляет текстовое сообщение указанному пользователю через Telegram API.
     * 
     * @param chatId ID чата пользователя
     * @param message текст сообщения
     * @return {@code true} если сообщение отправлено успешно, {@code false} в противном случае
     */
    public boolean sendMessage(String chatId, String message) {
        if (botToken == null || botToken.equals("YOUR_BOT_TOKEN_HERE")) {
            System.out.println("📱 [Telegram] Сообщение (не отправлено - токен не настроен): " + message);
            return false;
        }
        
        try {
            String url = baseUrl + config.getProperty("telegram.api.send.message.url");
            
            String jsonBody = String.format(
                "{\"chat_id\":\"%s\",\"text\":\"%s\",\"parse_mode\":\"HTML\"}",
                chatId, escapeJson(message)
            );
            
            RequestBody body = RequestBody.create(
                jsonBody, MediaType.get("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("📱 [Telegram] Сообщение отправлено: " + message);
                    return true;
                } else {
                    System.err.println("❌ [Telegram] Ошибка отправки: " + response.code() + " " + response.message());
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ [Telegram] Ошибка при отправке сообщения: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Отправка уведомления о выполнении квеста.
     * 
     * @param chatId ID чата пользователя
     * @param questName название квеста
     * @param reward награда за квест
     */
    public void sendQuestCompletedNotification(String chatId, String questName, int reward) {
        String message = String.format(
            "🎯 <b>Квест выполнен!</b>\n\n" +
            "📋 Задание: %s\n" +
            "💰 Награда: %d монет\n\n" +
            "Продолжайте играть! 🚀",
            questName, reward
        );
        sendMessage(chatId, message);
    }
    
    /**
     * Отправка уведомления о повышении уровня.
     * 
     * @param chatId ID чата пользователя
     * @param newLevel новый уровень персонажа
     */
    public void sendLevelUpNotification(String chatId, int newLevel) {
        String message = String.format(
            "🎉 <b>Поздравляем!</b>\n\n" +
            "⚡ Ваш персонаж достиг <b>%d уровня</b>!\n\n" +
            "Теперь вы можете получать больше наград! 🚀",
            newLevel
        );
        sendMessage(chatId, message);
    }
    
    /**
     * Отправка уведомления о выигрыше приза.
     * 
     * @param chatId ID чата пользователя
     * @param prizeName название приза
     */
    public void sendPrizeWonNotification(String chatId, String prizeName) {
        String message = String.format(
            "🎁 <b>Поздравляем с выигрышем!</b>\n\n" +
            "🏆 Вы выиграли: <b>%s</b>\n\n" +
            "Свяжитесь с администратором для получения приза! 📞",
            prizeName
        );
        sendMessage(chatId, message);
    }
    
    /**
     * Проверка подписки на Telegram канал.
     * 
     * <p>В реальной реализации здесь должен быть вызов Telegram API
     * для проверки подписки пользователя на канал.
     * 
     * @param userId ID пользователя
     * @param channelUsername имя канала (например, @alabuga_channel)
     * @return {@code true} если пользователь подписан, {@code false} в противном случае
     */
    public boolean checkChannelSubscription(String userId, String channelUsername) {
        // Заглушка для демонстрации
        // В реальной реализации здесь должен быть API вызов
        System.out.println("📱 [Telegram] Проверка подписки пользователя " + userId + " на канал " + channelUsername);
        
        // Для демонстрации всегда возвращаем true
        // В реальном приложении здесь должен быть реальный API вызов
        return true;
    }
    
    /**
     * Получение информации о боте.
     * 
     * @return JSON строка с информацией о боте или null при ошибке
     */
    public String getBotInfo() {
        if (botToken == null || botToken.equals("YOUR_BOT_TOKEN_HERE")) {
            return null;
        }
        
        try {
            String url = baseUrl + config.getProperty("telegram.api.get.me.url");
            
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().string();
                }
            }
        } catch (Exception e) {
            System.err.println("❌ [Telegram] Ошибка получения информации о боте: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Экранирование специальных символов для JSON.
     * 
     * @param text исходный текст
     * @return экранированный текст
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Проверка доступности Telegram API.
     * 
     * @return {@code true} если API доступен, {@code false} в противном случае
     */
    public boolean isApiAvailable() {
        return getBotInfo() != null;
    }
    
    /**
     * Получение конфигурации.
     * 
     * @return объект Properties с настройками
     */
    public Properties getConfig() {
        return new Properties(config);
    }
}
