# Настройка IntelliJ IDEA для запуска JavaFX приложения

## Проблема
При запуске приложения через IntelliJ IDEA возникает ошибка:
```
Error: JavaFX runtime components are missing, and are required to run this application
```

## Решения

### Решение 1: Использование Maven (рекомендуется)

1. **Откройте Maven панель** в IntelliJ IDEA (обычно справа)
2. **Найдите проект** `tap-game`
3. **Разверните** `Plugins` → `javafx`
4. **Дважды кликните** на `javafx:run`

### Решение 2: Настройка конфигурации запуска

1. **Перейдите** в `Run` → `Edit Configurations...`
2. **Создайте новую конфигурацию** `Application`
3. **Установите следующие параметры:**
   - **Name:** `TapGame`
   - **Main class:** `com.tapgame.tap_game.TapGameApplication`
   - **Module:** `tap.game`
   - **VM options:** 
     ```
     --module-path "C:\Users\gface\.m2\repository\org\openjfx\javafx-controls\17.0.2\javafx-controls-17.0.2.jar;C:\Users\gface\.m2\repository\org\openjfx\javafx-fxml\17.0.2\javafx-fxml-17.0.2.jar;C:\Users\gface\.m2\repository\org\openjfx\javafx-graphics\17.0.2\javafx-graphics-17.0.2.jar;C:\Users\gface\.m2\repository\org\openjfx\javafx-base\17.0.2\javafx-base-17.0.2.jar" --add-modules javafx.controls,javafx.fxml
     ```

### Решение 3: Использование скриптов

Просто запустите `run.bat` (на Windows) из корневой папки проекта.

### Решение 4: Настройка проекта как модульного

1. **Откройте** `File` → `Project Structure`
2. **Перейдите** в `Modules`
3. **Убедитесь**, что модуль `tap.game` правильно настроен
4. **В разделе** `Dependencies` добавьте JavaFX модули:
   - `javafx.controls`
   - `javafx.fxml`
   - `javafx.graphics`
   - `javafx.base`

## Проверка

После настройки приложение должно запускаться без ошибок и показывать окно с тремя вкладками:
- Квесты
- Призы  
- Качать персонажа

## Альтернативный способ

Если ничего не помогает, можно запустить приложение через терминал:
```bash
mvn javafx:run
```
