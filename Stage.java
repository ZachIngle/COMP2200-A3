import java.util.Random;
import java.util.ArrayList;

abstract class Stage extends Event {
    protected int avgProcessingTime;
    protected int rangeProcessingTime;
    protected Item currentItem;

    protected Storage source;
    protected Storage destination;

    protected ArrayList<Double> starvedTimes = new ArrayList<Double>();
    protected ArrayList<Double> blockedTimes = new ArrayList<Double>();

    protected boolean starved = false;
    protected boolean blocked = false;

    private static Random r = new Random();

    public Stage(int avgProcessingTime, int rangeProcessingTime) {
        this.avgProcessingTime = avgProcessingTime;
        this.rangeProcessingTime = rangeProcessingTime;
    }

    public void setSource(Storage source) {
        this.source = source;
    }

    public void setDestination(Storage destination) {
        this.destination = destination;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setStarved(boolean starved) {
        this.starved = starved;
    }

    public boolean isReady() {
        return currentItem == null;
    }

    public Storage getSource() {
        return source;
    }

    public Storage getDestination() {
        return destination;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public boolean getStarved() {
        return starved;
    }

    public void insertItem(ProductionLineSimulator sim, Item i) {
        currentItem = i;
        time = sim.currentTime() + productionTime();
        sim.insert(this);
    }

    protected double productionTime() {
        return avgProcessingTime + rangeProcessingTime * (r.nextDouble() - 0.5);
    }
}
