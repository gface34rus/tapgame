module tap.game {
    requires javafx.controls;
    requires javafx.fxml;
    
    exports com.tapgame.tap_game;
    exports com.tapgame.tap_game.controllers;
    exports com.tapgame.tap_game.models;
    
    opens com.tapgame.tap_game.controllers to javafx.fxml;
}
