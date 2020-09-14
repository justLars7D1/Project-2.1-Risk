package player;

import settings.Settings;

public class RiskUser extends Player{
    public String name;
    public int id;

    /**
     * actions and cards management for a human player
     */
    public RiskUser(){
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

}
