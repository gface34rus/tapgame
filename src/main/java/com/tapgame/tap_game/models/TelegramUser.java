package com.tapgame.tap_game.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Модель пользователя Telegram.
 * 
 * <p>Этот класс представляет данные пользователя Telegram, полученные
 * через Telegram Bot API. Содержит основную информацию о пользователе
 * для интеграции с игрой.
 * 
 * <p><strong>Поля:</strong>
 * <ul>
 *   <li>ID пользователя в Telegram</li>
 *   <li>Имя пользователя</li>
 *   <li>Имя и фамилия</li>
 *   <li>Язык интерфейса</li>
 *   <li>Статус в игре</li>
 * </ul>
 * 
 * @author Алабуга
 * @version 1.0
 * @since 1.0
 * @see JsonProperty
 */
public class TelegramUser {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    @JsonProperty("language_code")
    private String languageCode;
    
    private boolean isGameUser;
    private String gameStatus;
    
    /**
     * Конструктор по умолчанию.
     */
    public TelegramUser() {
        this.isGameUser = false;
        this.gameStatus = "guest";
    }
    
    /**
     * Конструктор с основными параметрами.
     * 
     * @param id ID пользователя в Telegram
     * @param username имя пользователя
     * @param firstName имя
     */
    public TelegramUser(Long id, String username, String firstName) {
        this();
        this.id = id;
        this.username = username;
        this.firstName = firstName;
    }
    
    /**
     * Получение полного имени пользователя.
     * 
     * @return полное имя (имя + фамилия) или имя пользователя
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (username != null) {
            return "@" + username;
        } else {
            return "Пользователь " + id;
        }
    }
    
    /**
     * Получение отображаемого имени.
     * 
     * @return имя для отображения в интерфейсе
     */
    public String getDisplayName() {
        if (firstName != null) {
            return firstName;
        } else if (username != null) {
            return "@" + username;
        } else {
            return "Пользователь";
        }
    }
    
    /**
     * Проверка, является ли пользователь игроком.
     * 
     * @return {@code true} если пользователь зарегистрирован в игре
     */
    public boolean isGameUser() {
        return isGameUser;
    }
    
    /**
     * Установка статуса игрока.
     * 
     * @param isGameUser является ли пользователь игроком
     */
    public void setGameUser(boolean isGameUser) {
        this.isGameUser = isGameUser;
        if (isGameUser) {
            this.gameStatus = "player";
        } else {
            this.gameStatus = "guest";
        }
    }
    
    // === Геттеры и сеттеры ===
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getLanguageCode() {
        return languageCode;
    }
    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    
    public String getGameStatus() {
        return gameStatus;
    }
    
    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
    
    @Override
    public String toString() {
        return "TelegramUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", isGameUser=" + isGameUser +
                ", gameStatus='" + gameStatus + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TelegramUser that = (TelegramUser) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
