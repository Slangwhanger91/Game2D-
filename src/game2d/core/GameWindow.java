package game2d.core;

import game2d.core.Game2d;
import game2d.menu.GameMenu;
import game2d.sound.SoundController;
import game2d.util.Settings;

import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class GameWindow extends Application {
    SoundController soundController;

    @Override
    public void start(Stage stage) {
        Settings config;
        soundController = new SoundController();

        config = new Settings("config.properties");
        int show_menu = Integer.parseInt(config.get("show_menu", "1"));

        if (show_menu > 0) {
            GameMenu menu = new GameMenu(this, stage, soundController);
            stage.setScene(menu.scene);
        } else {
            startGame(stage);
        }

        stage.setTitle(config.get("title", "The amazing adventures of BOX"));
        stage.show();
    }

    public void startGame(Stage stage) {
        Game2d game = new Game2d(soundController);
        stage.setScene(game.scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
