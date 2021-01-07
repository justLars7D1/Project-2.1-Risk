package environment;

import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FortifyHeuristic {
    /**
     * Returns a list of lists where each list contains the country ID's of countries that a grouped together i.e.
     * troops can move freely between them
     * Because of the way existspath works countries that have been added to a cluster do not need to be checked again (Skip)
     * Example: if I can get from 1 to 2 and to no over country then I can also not get to any other country from 2
     * @return list of lists (int)
     */
    public static List<List<Country>> getTerritoryClusters(Game game){
        Player CurrentPlayer = game.getCurrentPlayer();

        List<List<Country>> AllClusters = new ArrayList<>();

        // skip collects all countries that have already been assigned to a cluster
        List<Country> Skip = new ArrayList<>();

        for(Country ownedCountryFrom : CurrentPlayer.getCountriesOwned()){
            // clear the list for a new cluster
            List<Country> Cluster = new ArrayList<>();
            // If a country has already been assigned a cluster then it can be ignored
            if(Skip.contains(ownedCountryFrom)){
                continue;
            }
            for(Country ownedCountryTo : CurrentPlayer.getCountriesOwned()){
                if(CurrentPlayer.existsCountryPath(ownedCountryFrom, ownedCountryTo)){
                    Cluster.add(ownedCountryTo);
                    Skip.add(ownedCountryTo);
                }
            }
            // Append one cluster to the collection of clusters
            AllClusters.add(Cluster);
        }
        return AllClusters;
    }
    /**
     * Takes as input the output of the getTerritoryClusters method and reutrns a list of length 42 where the index corresponds
     * to the country ID and th value corresponds to the number of troops stationd in the country after fortifying
     * @return list (int)
     */
    public static List<Integer> ArmyDistributionFortify(List<List<Country>> AllClusters){

        int clusterArmyStrength;
        int clusterBST;
        int BST;
        double FortifiedArmyStrength;
        int distributedTroops;
        int ToBeDistributed;
        int maxTroopsInCountry = 10;

        List<Integer> output = new ArrayList<>(Collections.nCopies(42, 0));

        for(List<Country> cluster : AllClusters){
            List<Integer> BSTList = new ArrayList<>();
            List<Integer> CountryIDs = new ArrayList<>();
            clusterArmyStrength = 0;
            clusterBST = 0;
            distributedTroops = 0;
            ToBeDistributed = 0;
            for(Country ownedCountry : cluster){
                BST = 0;
                for(Country neighborCountry: ownedCountry.getNeighboringCountries()){
                    if(neighborCountry.getOwner() != ownedCountry.getOwner()){
                        BST += neighborCountry.getNumSoldiers();
                    }
                }
                BSTList.add(BST);
                CountryIDs.add(ownedCountry.getID());
                clusterBST += BST;
                clusterArmyStrength += ownedCountry.getNumSoldiers();
            }
            for(int i = 0; i < BSTList.size(); i++){

                // Substract number of countries in cluster from clusterArmyStrength to control for each contry having one troop by default
                FortifiedArmyStrength = Math.floor(((double)BSTList.get(i) / (double)clusterBST) * (clusterArmyStrength - cluster.size()));
                // Check for values aboth the maximum-1 (-1 because cluster.size is subtracted in the previous step)
                if(FortifiedArmyStrength > (maxTroopsInCountry-1)){FortifiedArmyStrength = (maxTroopsInCountry-1);}
                BSTList.set(i, (int)FortifiedArmyStrength+1);

                distributedTroops += (FortifiedArmyStrength +1);
                // Troops left to be distributed / fortified
                ToBeDistributed = clusterArmyStrength -distributedTroops;
            }
            // Add remaining troops to the countries with largest army stationed (below maxTroopsInCountry)
            int threshold = 0;
            int target_index =0;
            while(ToBeDistributed > 0){
                threshold = 0;
                target_index =0;
                // Find the index of the largest value below 10
                for(int i = 0; i < BSTList.size(); i++){

                    if(BSTList.get(i) < maxTroopsInCountry && BSTList.get(i) > threshold){
                        target_index = i;
                        threshold = BSTList.get(i);
                    }
                }
                // Check how much can be added to this territory
                // Add all the troops left to fortify if the country has enough room (below 10)
                if(maxTroopsInCountry -BSTList.get(target_index) >= ToBeDistributed){
                    BSTList.set(target_index, BSTList.get(target_index) + ToBeDistributed);
                    ToBeDistributed = 0;
                }
                // If the country has not enough room to accommodate all the troops left to fortify
                // then fill this country and continue with the next
                else{
                    ToBeDistributed -= (maxTroopsInCountry -BSTList.get(target_index));
                    BSTList.set(target_index, maxTroopsInCountry);
                }
            }
            // Add troop numbers to output array
            for(int i = 0; i < BSTList.size(); i++){
                output.set(CountryIDs.get(i), BSTList.get(i));
            }
        }
        return output;
    }
}
