package bot.Algorithms.MCTS.StoppingConditions;

public class TimeCondition implements StoppingCondition {

    private final double numMilis;

    private long startTime;

    public TimeCondition(double numMilis) {
        this.numMilis = numMilis;
    }


    @Override
    public void init() {
        startTime = System.nanoTime();
    }

    @Override
    public boolean isReached() {
        long curTime = System.nanoTime();
        double diffInMilis = (curTime - startTime) / 1000000.;
        return diffInMilis >= numMilis;
    }
}
