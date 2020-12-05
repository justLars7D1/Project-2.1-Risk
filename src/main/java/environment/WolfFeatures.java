package environment;

import gameelements.board.Country;
import gameelements.player.Player;

import java.util.*;

import bot.Algorithms.MarkovChain.BattlePhaseEstimator;

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
                if (neighbours.get(j).getOwner() != player) {
                    conflict = true;
                    break;
                }
            }
        }
        if (!conflict) {
            hinterland++;
        } else {
            conflict = false;
        }
        return (((double) numcountries) / hinterland);
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
        return ((double) counter)/numcountries;
    }

    public static double averageThreatOn(Country country){
        int n=0;
        double threat=0.0;
        for(Country neighbor: country.getNeighboringCountries()){
            if(neighbor.getOwner().getId()!=country.getOwner().getId()){
                threat += BattlePhaseEstimator.winChance(country.getNumSoldiers(), neighbor.getNumSoldiers()-1); n++;
            }
        }
        return n==0 ? 0.0 : threat/n;
    }
}
