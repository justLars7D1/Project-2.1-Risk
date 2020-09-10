package UI.MainMenu;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSelection {

    private GridPane grid = new GridPane();
    private ArrayList<ComboBox<String>> playerList = new ArrayList<>();
    private ArrayList<ComboBox<String>> colorList = new ArrayList<>();

    private static final String IDLE_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: white;-fx-background-color:transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: white;-fx-background-color:#ffffff45;";

    public void buildScene(Menu menu) {

        //Background image
        BackgroundImage myBI = new BackgroundImage(new Image("file:player.jpg", 1200, 700, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        grid.setPadding(new Insets(100, 0, 0, 150));
        grid.setVgap(10);
        grid.setHgap(40);
        grid.setPrefWidth(80);
        grid.setPrefHeight(30);

        //Column labels
        Label player = new Label("PLAYER");
        Label colorLabel = new Label("COLOR");

        //Label style
        grid.setConstraints(player, 0, 0);
        grid.setConstraints(colorLabel, 1, 0);
        player.setStyle("-fx-font-size:1.6em");
        colorLabel.setStyle("-fx-font-size:1.6em");
        //Player and color choice boxes

//        TODO remove color item if it is being used

        for (int i = 0; i < 6; i++) {
            createChoiceBoxes(i);
        }

        //Button initialization
        Button start = new Button("Start");
        Button back = new Button("Back");

        //Button style
        grid.setConstraints(start, 1, 8);
        grid.setConstraints(back, 0, 8);
        start.setMinWidth(grid.getPrefWidth());
        start.setMinHeight(grid.getPrefHeight());
        back.setMinWidth(grid.getPrefWidth());
        back.setMinHeight(grid.getPrefHeight());

        //Button actions
        start.setOnAction(e -> getPlayers());
        back.setOnAction(e -> menu.window.setScene(menu.scene1));

        grid.getChildren().addAll(player, colorLabel, start, back);
        grid.setBackground(new Background(myBI));

        menu.scene2 = new Scene(grid, 1200, 600);
    }


    public void createChoiceBoxes(int i) {

        ComboBox<String> playerBox = new ComboBox<>();
        playerBox.setStyle("-fx-pref-width: 140; -fx-pref-height: 30; -fx-font-size: 1.2em");
        playerList.add(playerBox);

        int playerID = i + 1;
        playerBox.getItems().addAll("Empty", "Player " + playerID, "Easy Bot", "Hard Bot");
        playerBox.setValue("Empty");
        grid.setConstraints(playerBox, 0, i + 1);

        ComboBox<String> colorBox = new ComboBox<>();
        colorBox.setStyle("-fx-pref-width: 140; -fx-pref-height: 30; -fx-font-size: 1.2em");

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

        HashMap<String, String> players = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            if (!playerList.get(i).getValue().equals("Empty")) {
                players.put(playerList.get(i).getValue(), colorList.get(i).getValue());
            }
        }
        return players;
    }
}
