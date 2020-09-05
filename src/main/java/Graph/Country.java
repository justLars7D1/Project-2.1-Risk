package Graph;

public class Country {
    private String name;
    private int iD;
    private int soldiers;

    public Country(String name){
        this.name = name;
        this.iD = name.hashCode();
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }

    public int getSoldiers() {
        return soldiers;
    }
}
