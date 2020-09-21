package player;

public class RiskBot extends Player {

    /**
     * algorithm and strategies for our risk bot
     */
    public RiskBot(int id) {
        super(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RiskBot{" +
                "id=" + id +
                '}';
    }

}
