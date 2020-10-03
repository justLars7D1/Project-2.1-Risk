package UI.MainMenu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Menu extends Application {

    public Stage window;
    public Scene scene1, scene2, scene3, scene4;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        //Initialize layout
        GridPane grid = new GridPane();
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        grid.setPadding(new Insets(30, 0, 0, 50));

        //Initialize buttons
        Button play = new Button("Play");
        Button instructions = new Button("Rules");
        Button quit = new Button("Quit");

        //Initialize other screens
        PlayerSelection selection = new PlayerSelection();
        selection.buildScene(this);
        Instructions instr = new Instructions();
        instr.buildScene(this);

        //Button Actions
        play.setOnAction(e -> {selection.refreshScene();window.setScene(scene2);});
        instructions.setOnAction(e -> window.setScene(scene3));
        quit.setOnAction(e -> quitAction(quit));

        //Add attributes to layout
        vBox.getChildren().addAll(play, instructions, quit);
        grid.getChildren().addAll(vBox);

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        scene1 = new Scene(grid, screenSize.getWidth(), screenSize.getHeight());
        scene1.getStylesheets().add("css/MainStyle.css");

        window.setTitle("Risk");
        window.setScene(scene1);
        window.initStyle(StageStyle.UNDECORATED);
        window.setResizable(false);
        window.show();
    }

    private void quitAction(Button quit) {
        Stage stage = (Stage) quit.getScene().getWindow();
        stage.close();
    }
}
