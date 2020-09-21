package player;

import settings.Settings;

public class RiskUser extends Player{

    public String name;

    /**
     * actions and cards management for a human player
     */
    public RiskUser(int id) {
        super(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return "RiskUser{" +
                "id=" + id +
                '}';
    }
}
