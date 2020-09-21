package UI;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSelection {

    GridPane grid = new GridPane();
    ArrayList<ComboBox<String>> playerList = new ArrayList<>();
    ArrayList<ComboBox<String>> colorList = new ArrayList<>();


    public void buildPlayerSelect(Menu menu) {

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(20);

        Label player = new Label("Player");
        grid.setConstraints(player, 0, 0);

        Label colorLabel = new Label("Color");
        grid.setConstraints(colorLabel, 1, 0);

        //Player and color choice boxes

//        TODO remove color item if it is being used

        for (int i = 0; i < 6; i++) {
            createChoiceBoxes(i);
        }


        Button start = new Button("Start");
        grid.setConstraints(start, 1, 8);
        start.setOnAction(e -> getPlayers());

        Button back = new Button("Back");
        grid.setConstraints(back, 0, 8);
        back.setOnAction(e -> menu.window.setScene(menu.scene1));


        grid.getChildren().addAll(player, colorLabel, start, back);

        menu.scene2 = new Scene(grid, 1200, 800);
    }


    public void createChoiceBoxes(int i) {

        ComboBox<String> playerBox = new ComboBox<>();
        playerList.add(playerBox);

        int playerID = i + 1;
        playerBox.getItems().addAll("Empty", "Player " + playerID, "Easy Bot", "Hard Bot");
        playerBox.setValue("Empty");
        grid.setConstraints(playerBox, 0, i + 1);

        ComboBox<String> colorBox = new ComboBox<>();
        colorList.add(colorBox);
        colorBox.setItems(FXCollections.observableArrayList("Empty", "Red", "Blue", "Green", "Yellow", "Orange", "Purple"));
        colorBox.setValue("Empty");

        colorBox.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {

            int indexOf = colorBox.getItems().indexOf(colorBox.getValue());

            Color color = Color.TRANSPARENT;

            switch (indexOf) {
                case 1:
                    color = Color.RED;
                    break;
                case 2:
                    color = Color.BLUE;
                    break;
                case 3:
                    color = Color.GREEN;
                    break;
                case 4:
                    color = Color.YELLOW;
                    break;
                case 5:
                    color = Color.ORANGE;
                    break;
                case 6:
                    color = Color.PURPLE;
                    break;
                default:
                    break;
            }

            Color finalColor = color;

            // Get the arrow button of the combo-box
            StackPane arrowButton = (StackPane) colorBox.lookup(".arrow-button");

            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setBackground(Background.EMPTY);
                        setText("");
                    } else {
                        setBackground(new Background(new BackgroundFill(finalColor, CornerRadii.EMPTY, Insets.EMPTY)));
                        setText(item);
                    }

                    // Set the background of the arrow also
                    if (arrowButton != null)
                        arrowButton.setBackground(getBackground());
                }
            };
        }, colorBox.valueProperty()));

        grid.setConstraints(colorBox, 1, i + 1);

        grid.getChildren().addAll(playerBox, colorBox);
    }

    public HashMap<String, String> getPlayers() {

        // Maybe it's easier if we map from an int to a string and use a random color for every player
        // int would represent the player id
        // string would represent the type of player (e.g. user or bot)
        HashMap<String, String> players = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            if (!playerList.get(i).getValue().equals("Empty")) {
                players.put(playerList.get(i).getValue(), colorList.get(i).getValue());
            }
        }
        return players;
    }
}
