package UI.MainMenu;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Instructions {

    private static final String IDLE_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: white;-fx-background-color:transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: white;-fx-background-color:#ffffff45;";

    public void buildScene(Menu menu) {

        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        GridPane.setValignment(grid, VPos.TOP);
        GridPane.setHalignment(grid, HPos.CENTER);
        grid.setPrefWidth(80);
        grid.setPrefHeight(30);
        //Background image
        BackgroundImage myBI = new BackgroundImage(new Image("file:instr.jpg", 1200, 700, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Text text = new Text("INTRODUCTION & STRATEGY HINTS\n" +
                "In the classic “World Domination RISK” game of military strategy, you are\n" +
                "battling to conquer the world. To win, you must launch daring attacks,\n" +
                "defend yourself on all fronts, and sweep across vast continents with\n" +
                "boldness and cunning. But remember, the dangers, as well as the rewards,\n" +
                "are high. Just when the world is within your grasp, your opponent might\n" +
                "strike and take it all away!\n" +
                "See pages 1l- 16 for gameplay variations and variations for RISK experts.\n" +
                "Strategy. In all the RISK games, keep these 3 strategy hints in mind as you\n" +
                "play, add armies, and fortify:\n" +
                "1.\n" +
                "2.\n" +
                "3.\n" +
                "Conquer whole continents: You will earn more armies that way.\n" +
                "(This doesnt apply in Secret Mission Risk.)\n" +
                "Watch your enemies: If they are building up forces on adjacent\n" +
                "territories or continents, they may be planning an attack. Beware!\n" +
                "Fortify borders adjacent to enemy territories for better defense if a\n" +
                "neighbor decides to attack you.");

        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-font-size: 1.3em;");

        //Text style
        grid.setStyle("-fx-background-color: #00000059");
        grid.add(text, 0, 0);

        //Button initialization
        Button back = new Button("BACK");
        Button prev = new Button("PREVIOUS");
        Button next = new Button("NEXT");

        //Button actions
        back.setOnAction(e -> menu.window.setScene(menu.scene1));

        //Add attributes to layout
        HBox hBox = new HBox(back, prev, next);
        hBox.setSpacing(50);

        grid.add(hBox, 0, 1);
        root.getChildren().addAll(grid, hBox);
        root.setBackground(new Background(myBI));

        menu.scene3 = new Scene(root, 1200, 600);
    }

}
