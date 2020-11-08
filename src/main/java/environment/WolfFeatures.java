package environment;

import gameelements.board.Country;
import gameelements.player.Player;

import java.util.*;

public class WolfFeatures {
    public static double hinterland(Player player){
        int hinterland = 0;
        HashSet<Country> countries = player.getCountriesOwned();
        boolean conflict = false;
        int numcountries = 0;

        for(Country c:countries){
            numcountries++;
            ArrayList<Country> neighbours = (ArrayList<Country>)c.getNeighboringCountries();
            for(int j = 0; j < neighbours.size(); j++){
                if(neighbours.get(j).getOwner() != player){
                    conflict = true;
                }
            }
        }
        if (!conflict) {
            hinterland++;
        } else {
            conflict = false;
        }
        double result = (numcountries / hinterland);
        return result;
    }
    public static double moreThanOneArmy(Player player){
        HashSet<Country> countries = player.getCountriesOwned();
        int counter = 0;
        int numcountries = 0;
        for(Country c:countries){
            numcountries++;
            if (c.getNumSoldiers() > 1){
                counter++;
            }
        }
        double result = counter/numcountries;
        return result;
    }
}
