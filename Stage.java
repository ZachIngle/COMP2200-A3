import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

abstract class Stage extends Event {
    protected int avgProcessingTime;
    protected int rangeProcessingTime;
    protected Item currentItem;

    protected Storage source;
    protected Storage destination;

    protected boolean starved = false;
    protected boolean blocked = false;
    protected SortedSet<Double> starvedTimes = new TreeSet<Double>();
    protected SortedSet<Double> unstarvedTimes = new TreeSet<Double>();
    protected SortedSet<Double> blockedTimes = new TreeSet<Double>();
    protected SortedSet<Double> unblockedTimes = new TreeSet<Double>();

    private static Random r = new Random(1);

    public Stage(String name, int avgProcessingTime, int rangeProcessingTime) {
        this.name = name;
        this.avgProcessingTime = avgProcessingTime;
        this.rangeProcessingTime = rangeProcessingTime;
    }

    public void setSource(Storage source) {
        this.source = source;
    }

    public void setDestination(Storage destination) {
        this.destination = destination;
    }

    public void setStarved(boolean starved) {
        this.starved = starved;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }


    public void addUnstarvedTime(double time) {
        unstarvedTimes.add(time);
    }

    public void addUnblockedTime(double time) {
        unblockedTimes.add(time);
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

    public boolean isStarved() {
        return starved;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public double getTotalTimeStarved() {
        return 0;
    }

    public double getTotalTimeBlocked() {
        int smallerSize = blockedTimes.size() < unblockedTimes.size() ? blockedTimes.size() : unblockedTimes.size();

        Double[] blockedArray = blockedTimes.toArray(new Double[blockedTimes.size()]);
        Double[] unblockedArray = unblockedTimes.toArray(new Double[unblockedTimes.size()]);

        double total = 0;
        for (int i = 0; i < smallerSize; i++) {
            //System.out.println(unblockedArray[i] - blockedArray[i]);
            total += unblockedArray[i] - blockedArray[i];
        }

        return total;
    }

    public void insertItem(ProductionLineSimulator sim, Item i) {
        currentItem = i;
        time = sim.currentTime() + productionTime();
        message = "Worked on currentItem";
        sim.insert(this);
    }

    protected double productionTime() {
        return avgProcessingTime + rangeProcessingTime * (r.nextDouble() - 0.5);
    }
}
