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

    private final GridPane grid = new GridPane();
    private ArrayList<ComboBox<String>> playerList = new ArrayList<>();
    private ArrayList<ComboBox<String>> colorList = new ArrayList<>();
    private ArrayList<Color> colors;

    private static final String IDLE_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: black;-fx-background-color:transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-font-size: 1.8em; -fx-text-fill: black;-fx-background-color:#00000026;";

    public void buildScene(Menu menu) {

        fillColors();
        //Background image
        BackgroundImage myBI = new BackgroundImage(new Image("file:instr.jpg", 1200, 700, false, true),
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

        //Set style to buttons
        start.setStyle(IDLE_BUTTON_STYLE);
        back.setStyle(IDLE_BUTTON_STYLE);

        start.setOnMouseEntered(e -> start.setStyle(HOVERED_BUTTON_STYLE));
        back.setOnMouseEntered(e -> back.setStyle(HOVERED_BUTTON_STYLE));

        start.setOnMouseExited(e -> start.setStyle(IDLE_BUTTON_STYLE));
        back.setOnMouseExited(e -> back.setStyle(IDLE_BUTTON_STYLE));

        //Button actions
        start.setOnAction(e -> getPlayers());
        back.setOnAction(e -> menu.window.setScene(menu.scene1));

        grid.getChildren().addAll(player, colorLabel, start, back);
        grid.setBackground(new Background(myBI));

        menu.scene2 = new Scene(grid, 1200, 600);
    }

    private void fillColors() {
        colors = new ArrayList<>();
        colors.add(Color.TRANSPARENT);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.ORANGE);
        colors.add(Color.PURPLE);
    }

    private void createChoiceBoxes(int i) {

        ComboBox<String> playerBox = new ComboBox<>();
        playerBox.setStyle("-fx-pref-width: 200; -fx-pref-height: 35; -fx-font-size: 1.2em");
        playerList.add(playerBox);

        playerBox.getItems().addAll("EMPTY", "PLAYER", "EASY BOT", "HARD BOT");
        playerBox.setValue("EMPTY");
        grid.setConstraints(playerBox, 0, i + 1);

        ComboBox<String> colorBox = new ComboBox<>();
        colorBox.setStyle("-fx-pref-width: 200; -fx-pref-height: 35; -fx-font-size: 1.2em");

        colorList.add(colorBox);
        colorBox.setItems(FXCollections.observableArrayList("EMPTY", "RED", "BLUE", "GREEN", "YELLOW", "ORANGE", "PURPLE"));
        colorBox.setValue("EMPTY");

        colorBox.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {

            int indexOf = colorBox.getItems().indexOf(colorBox.getValue());

            Color finalColor = colors.get(indexOf);

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

    private Integer getColorID(String color) {
        if (color.equals("RED")) {
            return 1;
        } else if (color.equals("BLUE")) {
            return 2;
        } else if (color.equals("GREEN")) {
            return 3;
        } else if (color.equals("YELLOW")) {
            return 4;
        } else if (color.equals("ORANGE")) {
            return 5;
        } else if (color.equals("PURPLE")) {
            return 6;
        } else {
            return 0;
        }
    }

    public HashMap<Integer, Integer> getPlayers() {

        HashMap<Integer, Integer> players = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            if (!playerList.get(i).getValue().equals("Empty")) {
                players.put(i + 1, getColorID(colorList.get(i).getValue()));
            }
        }
        return players;
    }
}
