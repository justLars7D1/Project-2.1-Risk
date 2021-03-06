import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.player.Player;
import gameelements.player.PlayerFactory;
import gameelements.player.PlayerType;


import java.util.*;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.USER);
        players.put(1, PlayerType.USER);
        players.put(2, PlayerType.USER);
        Game game = new Game(players);

        // Initialize Test Case
        Player player3 = PlayerFactory.createPlayer(3,20,game,PlayerType.USER);
        Player player4 = PlayerFactory.createPlayer(4,20,game,PlayerType.USER);
        Player player5 = PlayerFactory.createPlayer(5,20,game,PlayerType.USER);

        Board game_board = game.getGameBoard();

        // Get all countries for testcases -> neighbors of east_africa
        Country east_africa = game_board.getCountryFromID(1);
        Country north_africa = game_board.getCountryFromName("North Africa");
        Country egypt = game_board.getCountryFromName("Egypt");
        Country central_africa = game_board.getCountryFromName("Congo (Central Africa)");
        Country madagascar = game_board.getCountryFromName("Madagascar");
        Country south_africa = game_board.getCountryFromName("South Africa");
        Country middle_east = game_board.getCountryFromName("Middle East");

        Country brazil = game_board.getCountryFromName("Brazil");
        Country peru = game_board.getCountryFromName("Peru");
        Country argentina = game_board.getCountryFromName("Argentina");
        Country venezuela = game_board.getCountryFromName("Venezuela");



        // Set set the current player as owner east africa
        Player current_player = game.getCurrentPlayer();
        current_player.onDistributionEvent(brazil);
        brazil.addNumSoldiers(9);
        current_player.onDistributionEvent(peru);
        peru.addNumSoldiers(8);
        current_player.onDistributionEvent(east_africa);
        east_africa.addNumSoldiers(7);
        current_player.onDistributionEvent(madagascar);
        madagascar.addNumSoldiers(5);
        current_player.onDistributionEvent(south_africa);
        south_africa.addNumSoldiers(6);



        // Set enemy players
        player3.onDistributionEvent(north_africa);
        north_africa.addNumSoldiers(3);
        player3.onDistributionEvent(egypt);
        egypt.addNumSoldiers(4);
        player3.onDistributionEvent(central_africa);
        central_africa.addNumSoldiers(9);
        player3.onDistributionEvent(venezuela);
        central_africa.addNumSoldiers(8);
        player3.onDistributionEvent(argentina);
        central_africa.addNumSoldiers(5);



        player5.onDistributionEvent(middle_east);
        middle_east.addNumSoldiers(2);

        // Run method
        //HashMap <Integer,List<Double>> feature_results =  get_state_features(game);
        System.out.println(getTerritoryClusters(game));

        System.out.println(ArmyDistributionFortify(getTerritoryClusters(game)));
        System.out.println("Brazil "+brazil.getID());
        System.out.println("Peru "+peru.getID());
        System.out.println("east_africa "+east_africa.getID());
        System.out.println("madagascar "+madagascar.getID());
        System.out.println("south_africa "+south_africa.getID());



    }
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


    public static HashMap get_state_features(Game game){
        Player current_player = game.getCurrentPlayer();
        HashSet<Country> owned_countries = current_player.getCountriesOwned();

        double bst;
        double bsr;
        int player_troops;
        List<Country> neighbor_countries;
        Player owner;
        HashMap<Integer, List<Double>> feature_results = new HashMap<Integer, List<Double>>();
        List<Double> feature_values = new ArrayList<>();

        for (Country owned_country : owned_countries) {
            bst = 0;
            bsr = 0;
            feature_values.clear();
            player_troops = owned_country.getNumSoldiers();

            neighbor_countries = owned_country.getNeighboringCountries();
            for (Country neighbor_country : neighbor_countries) {

                owner = neighbor_country.getOwner();
                if (owner != null && owner != current_player) {
                    bst += neighbor_country.getNumSoldiers();
                }
            }
            bsr = (double) bst/ (double) player_troops;
            System.out.println("BSR:" +bsr);
            System.out.println("BST:" +bst);

            // add to list
            feature_values.add(bst);
            feature_values.add(bsr);
            System.out.println("List containing BSR,BST:"+feature_values);

            // add list to hashmap
            feature_results.put(owned_country.getID(), feature_values);
            System.out.println("HashMap containing feature results: " +feature_results);

        }
        return feature_results;
    }
}
