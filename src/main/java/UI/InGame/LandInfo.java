package UI.InGame;

import gameelements.board.Country;
import gameelements.game.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LandInfo {

    public VBox landInfo(Game game) {
        Label name = new Label("name");
        Label owner = new Label("Owner");
        Label troops = new Label("Troops");

        VBox vBox = new VBox(name, owner, troops);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("landInfo");

        return vBox;
    }

    public VBox landInfo(Game game, int countryid) {
        Country country = game.getGameBoard().getCountryFromID(countryid);

        Label name = new Label(country.getName());
        Label owner = new Label("Player: " + String.valueOf(country.getOwner().getId()));
        Label troops = new Label("Troops stationed: " + country.getNumSoldiers());

        VBox vBox = new VBox(name, owner, troops);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("landInfo");

        return vBox;
    }
}
