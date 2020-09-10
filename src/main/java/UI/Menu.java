package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Menu extends Application {

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        window = primaryStage;

        BackgroundImage myBI= new BackgroundImage(new Image("file:player.jpg",32,32,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);


        Button play = new Button("Play");
        Button instructions = new Button("Instructions");
        Button quit = new Button("Quit");

        PlayerSelection selection = new PlayerSelection();
        selection.buildPlayerSelect(this);
        play.setOnAction(e -> window.setScene(scene2));

        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(play, instructions, quit);
        layout1.setBackground(new Background(myBI));

        scene1 = new Scene(layout1, 1200, 800);


        window.setTitle("Risk");
        window.setScene(scene1);
        window.show();
    }
}
