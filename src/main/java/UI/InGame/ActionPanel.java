package UI.InGame;

import UI.MainMenu.Menu;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ActionPanel {

    private BoardMap map;

    public HBox setPanel(BoardMap map, int stage) {
        this.map = map;
        HBox hBox;

        switch (stage) {
            case 1:
                hBox = addTroopsPanel();
                break;
            case 2:
                hBox = battlePanel();
                break;
            case 3:
                hBox = mooveTroopsPanel();
                break;
            default:
                hBox = battlePanel();
                break;
        }
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    public HBox addTroopsPanel() {
        System.out.println("add");
        Label addTroops = new Label("Select a Land to Add Your Troops! ");
        Button confirmB = new Button("Confirm");

        confirmB.setOnAction(e -> {
            map.changeStage();
        });

        HBox hBox = new HBox(addTroops, confirmB);
        return hBox;
    }

    public HBox battlePanel() {
        System.out.println("battle");
        Label attackL = new Label("Pick a Land to Attack! ");
        Button attackB = new Button("Attack");
        Button endTurnB = new Button("End Phase");

        //Button Actions
        attackB.setOnAction(e -> {
        });
        endTurnB.setOnAction(e -> {
            map.changeStage();
        });

        HBox hBox = new HBox(attackL, attackB, endTurnB);
        return hBox;
    }

    public HBox mooveTroopsPanel() {
        System.out.println("move");
        Label addTroops = new Label("Select Lands to exchange Troops! ");
        Button confirmB = new Button("Confirm");

        confirmB.setOnAction(e -> {
            map.changeStage();
        });

        HBox hBox = new HBox(addTroops, confirmB);
        return hBox;
    }
}
