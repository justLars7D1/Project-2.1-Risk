package UI.MainMenu;

import UI.InGame.BoardMap;
import gameelements.player.PlayerType;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSelection {

    private GridPane grid = new GridPane();
    private ArrayList<ComboBox<String>> playerList = new ArrayList<>();
    private ArrayList<ComboBox<String>> colorList = new ArrayList<>();
    private ArrayList<Color> colors;
    public Scene scene4;
    private BoardMap board;
    private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    public void buildScene(Menu menu) {

        fillColors();

        grid.setPadding(new Insets(160, 0, 0, screenSize.getWidth() / 3.4));
        grid.setVgap(15);
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

        //Button actions
        start.setOnAction(e -> { //initialising the board map
            board = new BoardMap(getPlayers(), menu);
            menu.window.setScene(menu.scene4);});

        back.setOnAction(e -> menu.window.setScene(menu.scene1));

        grid.getChildren().addAll(title, player, colorLabel, start, back);


        menu.scene2 = new Scene(grid, screenSize.getWidth(), screenSize.getHeight());
        menu.scene2.getStylesheets().add("css/SelectionStyle.css");
    }

    public void refreshScene() {
        for (int i = 1; i < 7; i++) {
            createChoiceBoxes(i);
        }
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
        playerBox.setStyle("-fx-pref-width: " + screenSize.getWidth() / 6 + "; -fx-pref-height:" + 50);
        playerList.add(playerBox);

        playerBox.getItems().addAll("EMPTY", "USER", "EASY BOT", "HARD BOT");
        playerBox.setValue("EMPTY");
        grid.setConstraints(playerBox, 0, i + 1);

        ComboBox<String> colorBox = new ComboBox<>();
        colorBox.setStyle("-fx-pref-width: " + screenSize.getWidth()/6 + "; -fx-pref-height:" + 50);
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

    public HashMap<Integer, PlayerType> getPlayers() {

        HashMap<Integer, PlayerType> players = new HashMap<>();

        for (int i = 0; i < playerList.size(); i++) {
            //System.out.println(playerList.get(i).getValue());

            switch (playerList.get(i).getValue()) {
                case "USER":
                    players.put(getColorID(colorList.get(i).getValue()), PlayerType.USER);
                    break;
                case "EASY BOT":
                    players.put(getColorID(colorList.get(i).getValue()), PlayerType.TD);
                    break;
                case "HARD BOT":
                    players.put(getColorID(colorList.get(i).getValue()), PlayerType.DQN);
                    break;
            }
        }

        //System.out.println(Arrays.asList(players)); // method 1
        return players;

    }

}
