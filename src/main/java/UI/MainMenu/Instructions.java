package UI.MainMenu;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

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

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        menu.scene3 = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        menu.scene3.getStylesheets().add("css/InstrStyle.css");
    }

    private void addRulesPages() {
        Text page1 = new Text("GOAL OF THE GAME:\n\n" +
                "Conquer the world by occupying all territories on the board\n" +
                "  Setup:\n" +
                " 1. Every player starts with the same amount of infantry units \n" +
                " a. Canons count as 10 units, cavalry as 5 units, infantry equals 1 unit\n\n" +

                " Player	Infantry Units\n" +
                " 2	40*\n" +
                " 3	35\n" +
                " 4	30\n" +
                " 5	25\n" +
                " 6	20\n" +
                "       *special rules for two players\n\n" +

                "2. Player role a dice to determine who begins. The player who roles the highest number starts by placing one \n" +
                        "infantry unit onto any territory on the board,\n" +
                "thus claiming that territory.\n\n" +

                "3. Starting to the left of the first player, in turn, everyone places one army onto any\n" +
                        "unoccupied territory. Continue until all 42 territories have been claimed.\n\n" +

                "4. After all the 42 territories are claimed, each player in turn places one additional army onto\n" +
                        "any territory he or she already occupies.\n" +
                "Continue in this way until everyone has run out of armies. There is no limit to the number of\n" +
                        "armies you may place onto a single territory.\n\n" +

                "5. After all armies are set the player who placed the first army opens the game");

        Text page2 = new Text(
                "GAMEPLAY\n\n" +

                "Every player takes turns. A turn consists of the following actions:\n\n" +

                "1.Each player receives new infantry units which he can place on territories he occupies.The number of armies \n" +
                " received depends on the following conditions\n\n" +

                "1. TERRITORIES\n\n" +

                "Divide the number of currently occupied territories by three ignore the fraction. (The player\n" +
                "receives at least 3 infantry units)\n\n" +
                "2. CONTINENTS\n\n" +

                "A player receives bonus units if he controls every territory of a continent. \n" +
                "The number of additional units depends on the occupied continent\n" +
                "Continent	Infantry Units\n" +
                "Asia................7\n" +
                "North America.......5\n" +
                "Europe..............5\n" +
                "Africa..............3\n" +
                "South America.......2\n" +
                "Australia...........2\n\n"

        );

        Text page3 = new Text(
                "After placing infantry units, the player may attack.\n\n" +
                "-You may only attack a territory that is adjacent (touching) to one of your own, or \n" +
                "connected to it by a dashed line.\n" +

                "-You must always have at least two armies in the territory you’re attacking from.\n" +

                "-You may continue attacking one territory until you have eliminated all armies on it,\n" +
                "or you may shift your attack from on territory or another,\n" +
                "attacking each as often as you like and attacking as many territories as you like during one turn.\n\n" +

                "HOW TO ATTACK\n\n" +
                "-Before rolling, both you and your opponent must announce the number of dice you \n" +
                "intend to roll, and you both must roll at the same time.\n" +
                "-For simplicity, each player will always attack and defend with the maximum number of armies available.\n" +

                "-You, the attacker, will roll 1,2 or 3 red dice: You must have at least one more army in your\n" +
                " territory than the number of dice you roll. \n" +

                "-The defender will roll either 1 or 2 white dice: To roll 2 dice, he or she must have at least\n" +
                " 2 armies on the territory under attack. \n" +

                "-Compare the highest die each of you rolled. If yours (the attacker's) is higher, the defender\n" +
                "loses one army from the territory under attack.\n" +
                "But if the defender's die is higher than yours, you lose one army from the territory\n" +
                "you attacked from.\n" +
                "If each of you rolled more than one die, now compare the two next-highest dice and repeat the process.\n" +
                " (In case if a tie, the defender wins)");
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
