package UI.MainMenu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Instructions {

    private ArrayList<Text> rules;
    private int pageNumber = 0;
    private Label pageLabel;

    public void buildScene(Menu menu) {

        StackPane root = new StackPane();
        GridPane grid = new GridPane();

        grid.setPrefWidth(80);
        grid.setPrefHeight(30);

        addRulesPages();

        //Button initialization
        Button back = new Button("Back");
        Button prev = new Button("Previous page");
        Button next = new Button("Next page");

        //Button actions
        next.setOnAction(e -> nextPage());
        prev.setOnAction(e -> prevPage());
        back.setOnAction(e -> menu.window.setScene(menu.scene1));

        //Add Buttons
        HBox hBox = new HBox(back, prev, next);
        hBox.setSpacing(50);
        hBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < rules.size(); i++) {
            rules.get(i).setTextAlignment(TextAlignment.CENTER);
            rules.get(i).setStyle("-fx-font-size: 1.3em;");
            rules.get(i).setVisible(false);
            grid.add(rules.get(i), 0, 1);
        }

        //Page number label
        pageLabel = new Label(pageNumber+1 + " / " + rules.size());
        pageLabel.setAlignment(Pos.CENTER);

        //Add items to layout
        grid.add(pageLabel, 0, 2);
        grid.add(hBox, 0, 3);
        grid.setAlignment(Pos.CENTER);
        rules.get(0).setVisible(true);

        root.getChildren().addAll(grid);

        menu.scene3 = new Scene(root, 1200, 600);
        menu.scene3.getStylesheets().add("css/InstrStyle.css");
    }

    private void addRulesPages() {
         Text page1 = new Text("INTRODUCTION & STRATEGY HINTS\n" +
                "In the classic World Domination RISK game of military strategy, you are\n" +
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

        Text page2 = new Text("On your turn, try to capture territories by defeating your opponents’\n" +
                "armies. But be careful: Winning battles will depend on careful planning,\n" +
                "quick decisions and bold moves. You’ll have to place your forces wisely,\n" +
                "attack at just the right time and fortify your defenses against all enemies.\n" +
                "Note: At any time during the game, you may trade in Infantry pieces for the\n" +
                "equivalent (see page 4) in Cavalry or Artillery if you need to, or wish to.");

        Text page3 = new Text("On your turn, try to capture territories by defeating your opponents’\n" +
                "armies. But be careful: Winning battles will depend on careful planning,\n" +
                "quick decisions and bold moves. You’ll have to place your forces wisely,\n" +
                "attack at just the right time and fortify your defenses against all enemies.\n" +
                "Note: At any time during the game, you may trade in Infantry pieces for the\n" +
                "equivalent (see page 4) in Cavalry or Artillery if you need to, or wish to.\n" +
                "Note: At any time during the game, you may trade in Infantry pieces for the\n" +
                "Note: At any time during the game, you may trade in Infantry pieces for the\n" +
                "Note: At any time during the game, you may trade in Infantry pieces for the\n" +
                "Note: At any time during the game, you may trade in Infantry pieces for the");

        rules = new ArrayList<>();
        rules.add(page1);
        rules.add(page2);
        rules.add(page3);
    }

    private void nextPage() {
        if (pageNumber < rules.size() - 1) {
            rules.get(pageNumber).setVisible(false);
            pageNumber++;
            rules.get(pageNumber).setVisible(true);
            pageLabel.setText(pageNumber+1 + " / " + rules.size());
        }
    }

    private void prevPage() {
        if (pageNumber > 0) {
            rules.get(pageNumber).setVisible(false);
            pageNumber--;
            rules.get(pageNumber).setVisible(true);
            pageLabel.setText(pageNumber+1 + " / " + rules.size());
        }
    }
}
