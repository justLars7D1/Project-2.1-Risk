package gameelements.player;

public class RiskUser extends Player {

    public String name;

    /**
     * actions and cards management for a human gameelements.player
     */
    public RiskUser(int id, int numTroopsInInventory) {
        super(id, numTroopsInInventory);
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
                ", numTroopsInInventory=" + numTroopsInInventory +
                '}';
    }
}
