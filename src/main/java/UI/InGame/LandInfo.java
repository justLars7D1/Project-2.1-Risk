package UI.InGame;

import gameelements.game.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LandInfo {

    public VBox landInfo(Game game) {
        Label name = new Label("name");
        Label owner = new Label("owner");
        Label troops = new Label("troops");

        VBox vBox = new VBox(name, owner, troops);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("landInfo");

        return vBox;
    }
}
