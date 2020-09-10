package UI.MainMenu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Menu extends Application {

    public Stage window;
    public Scene scene1, scene2, scene3;
    private static final String IDLE_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: white;-fx-background-color:transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: white;-fx-background-color:#ffffff45;";

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

        //Background of menu
        BackgroundImage myBI = new BackgroundImage(new Image("file:risk.jpg", 1200, 700, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        //Initialize buttons
        Button play = new Button("PLAY");
        Button instructions = new Button("RULES");
        Button quit = new Button("QUIT");

        //Set style to buttons
        play.setStyle(IDLE_BUTTON_STYLE);
        instructions.setStyle(IDLE_BUTTON_STYLE);
        quit.setStyle(IDLE_BUTTON_STYLE);

        play.setOnMouseEntered(e -> play.setStyle(HOVERED_BUTTON_STYLE));
        instructions.setOnMouseEntered(e -> instructions.setStyle(HOVERED_BUTTON_STYLE));
        quit.setOnMouseEntered(e -> quit.setStyle(HOVERED_BUTTON_STYLE));

        play.setOnMouseExited(e -> play.setStyle(IDLE_BUTTON_STYLE));
        instructions.setOnMouseExited(e -> instructions.setStyle(IDLE_BUTTON_STYLE));
        quit.setOnMouseExited(e -> quit.setStyle(IDLE_BUTTON_STYLE));

        play.setMinSize(100, 20);
        instructions.setMinSize(100, 20);
        quit.setMinSize(100,20);

        //Initialize other screens
        PlayerSelection selection = new PlayerSelection();
        selection.buildScene(this);
        Instructions instr = new Instructions();
        instr.buildScene(this);

        //Button Actions
        play.setOnAction(e -> window.setScene(scene2));
        instructions.setOnAction(e -> window.setScene(scene3));
        quit.setOnAction(e -> quitAction(quit));

        //Add attributes to layout
        vBox.getChildren().addAll(play, instructions, quit);
        grid.getChildren().addAll(vBox);
        grid.setBackground(new Background(myBI));

        scene1 = new Scene(grid, 1200, 600);
        window.setTitle("Risk");
        window.setScene(scene1);
        window.show();
    }

    private void quitAction(Button quit) {
        Stage stage = (Stage) quit.getScene().getWindow();
        stage.close();
    }
}
