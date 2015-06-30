import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by aperte on 29.06.2015.
 * Class responsible for displaying a menu for the game.
 */
public class GameMenu  {
    Image background;
    Image button_normal_start;
    Image button_normal_maps;
    Image button_normal_exit;
    Image button_hover_start;
    Image button_hover_maps;
    Image button_hover_exit;

    GameWindow master;
    Scene scene;

    public GameMenu(GameWindow master, Stage stage) {
        this.master = master;
        Pane pane = new Pane();
        scene = new Scene(pane, 800, 600);

        Insets buttonInsets = new Insets(331, 160, 57, 400);
        // values only fit for this specific background image, used to align buttons
        // with the placeholders on the image

        // TODO: Read image data from config?
        // OR: Use CSS, if possible?
        try {
            background = new Image(new FileInputStream("gfx/menu_draft.bmp"));
            button_normal_start = new Image(new FileInputStream("gfx/button_normal_start.bmp"));
            button_normal_maps = new Image(new FileInputStream("gfx/button_normal_maps.bmp"));
            button_normal_exit = new Image(new FileInputStream("gfx/button_normal_exit.bmp"));

            button_hover_start = new Image(new FileInputStream("gfx/button_hover_start.bmp"));
            button_hover_maps = new Image(new FileInputStream("gfx/button_hover_maps.bmp"));
            button_hover_exit = new Image(new FileInputStream("gfx/button_hover_exit.bmp"));

        }
        catch (IOException ioe) {
            System.out.println("Failed to read menu files. Skipping menu.");
            master.startGame();
            return;
        }

        BackgroundImage backgroundImage = new BackgroundImage(
                background,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        pane.setBackground(new Background(backgroundImage));
        VBox buttonBox = new VBox(9);
        buttonBox.setPadding(buttonInsets);
        pane.getChildren().add(buttonBox);

        // TODO: Generalize button creation
        Button startButton = new Button();
        startButton.setGraphic(new ImageView(button_normal_start));
        startButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                event -> startButton.setGraphic(new ImageView(button_hover_start)));
        startButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                event -> startButton.setGraphic(new ImageView(button_normal_start)));
        startButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> master.startGame());

        Button mapsButton = new Button();
        mapsButton.setGraphic(new ImageView(button_normal_maps));
        mapsButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                event -> mapsButton.setGraphic(new ImageView(button_hover_maps)));
        mapsButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                event -> mapsButton.setGraphic(new ImageView(button_normal_maps)));

        Button exitButton = new Button();
        exitButton.setGraphic(new ImageView(button_normal_exit));
        exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                event -> exitButton.setGraphic(new ImageView(button_hover_exit)));
        exitButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                event -> exitButton.setGraphic(new ImageView(button_normal_exit)));
        exitButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> stage.close());

        Button[] buttons = {startButton, mapsButton, exitButton};
        for (Button b: buttons) {
            //b.setBorder(null);
            //b.setPrefSize(240, 64);
            //b.setMaxSize(240, 64);
            //b.setMinSize(240, 64);
            b.setPadding(new Insets(0, 0, -1, 0)); // remove weird white border at bottom
            // of all elements in vbox
            b.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        }
        buttonBox.setMargin(exitButton, new Insets(1, 0, 0, 0));
        // compensates for 9px separator between two last buttons

        buttonBox.getChildren().addAll(startButton, mapsButton, exitButton);
    }
}
