package UI.InGame;

import UI.InGame.BoardMap;
import UI.MainMenu.Menu;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PauseMenu {

    public VBox pauseMenu(Menu menu, BorderPane pane, BoardMap game) {
        Label pauseL = new Label("Pause Menu");
        Button continueB = new Button("Continue");
        Button newGameB = new Button("New Game");
        Button restartB = new Button("Restart");
        Button quitB = new Button("Quit");

        VBox vBox = new VBox(pauseL, continueB, newGameB, restartB, quitB);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("pauseMenu");

        //Button Actions
        continueB.setOnAction(e -> { pane.setVisible(false);});
        newGameB.setOnAction(e -> menu.window.setScene(menu.scene2));
        restartB.setOnAction(e -> game.restart());
        quitB.setOnAction(e -> quitAction(quitB));

        return vBox;
    }

    /*
     * Quit the whole game.
     * */
    private void quitAction(Button quit) {
        Stage stage = (Stage) quit.getScene().getWindow();
        stage.close();
    }
}
