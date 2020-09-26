package UI.MainMenu;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSelection {

    private final GridPane grid = new GridPane();
    private ArrayList<ComboBox<String>> playerList = new ArrayList<>();
    private ArrayList<ComboBox<String>> colorList = new ArrayList<>();
    private ArrayList<Color> colors;

    public void buildScene(Menu menu) {

        fillColors();

        grid.setPadding(new Insets(50, 0, 0, 120));
        grid.setVgap(10);
        grid.setPrefWidth(80);
        grid.setPrefHeight(30);

        //Column labels
        Label title = new Label("Player Selection");
        title.setId("title");
        Label player = new Label("Choose Player");
        Label colorLabel = new Label("Choose Color");
        HBox hBox = new HBox(title);
        hBox.setAlignment(Pos.CENTER);

        //Label style
        grid.setConstraints(hBox, 0, 0);
        grid.setConstraints(player, 0, 1);
        grid.setConstraints(colorLabel, 1, 1);

        //Player and color choice boxes

//        TODO remove color item if it is being used

        for (int i = 1; i < 7; i++) {
            createChoiceBoxes(i);
        }

        //Button initialization
        Button start = new Button("Start");
        Button back = new Button("Back");

        //Button style
        grid.setConstraints(start, 1, 9);
        grid.setConstraints(back, 0, 9);
        start.setMinWidth(grid.getPrefWidth());
        start.setMinHeight(grid.getPrefHeight());
        back.setMinWidth(grid.getPrefWidth());
        back.setMinHeight(grid.getPrefHeight());

        //Button actions
        start.setOnAction(e -> getPlayers());
        back.setOnAction(e -> menu.window.setScene(menu.scene1));

        grid.getChildren().addAll(title, player, colorLabel, start, back);

        menu.scene2 = new Scene(grid, 1200, 600);
        menu.scene2.getStylesheets().add("css/SelectionStyle.css");
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
        playerList.add(playerBox);

        playerBox.getItems().addAll("EMPTY", "PLAYER", "EASY BOT", "HARD BOT");
        playerBox.setValue("EMPTY");
        grid.setConstraints(playerBox, 0, i + 1);

        ComboBox<String> colorBox = new ComboBox<>();

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
            if (playerList.get(i).getValue().equals("PLAYER")) {
                players.put(getColorID(colorList.get(i).getValue()), 1);
            } else if(playerList.get(i).getValue().equals("EASY BOT")) {
                players.put(getColorID(colorList.get(i).getValue()), 2);
            } else if (playerList.get(i).getValue().equals("HARD BOT")) {
                players.put(getColorID(colorList.get(i).getValue()), 3);
            }
        }
        //System.out.println(Arrays.asList(players)); // method 1
        return players;

    }
}
