// Stage.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// An abstract class that specifies a stage. Holds shared methods and
// attributes between stages. Extends Event to it can be executed

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

abstract class Stage extends Event {
    protected String name; // Name of the stage

    protected int avgProcessingTime; // M
    protected int rangeProcessingTime; // N

    protected Item currentItem; // The current item the stage is holding

    protected Storage source; // Source storages
    protected Storage destination; // Destination storages

    protected boolean starved = true;
    protected boolean blocked = false;

    // Statistics
    protected SortedSet<Double> starvedTimes = new TreeSet<Double>();
    protected SortedSet<Double> unstarvedTimes = new TreeSet<Double>();
    protected SortedSet<Double> blockedTimes = new TreeSet<Double>();
    protected SortedSet<Double> unblockedTimes = new TreeSet<Double>();

    private static Random r = new Random(); // Used to calculate production time

    // Constructor
    public Stage(String name, int avgProcessingTime, int rangeProcessingTime) {
        this.name = name;
        this.avgProcessingTime = avgProcessingTime;
        this.rangeProcessingTime = rangeProcessingTime;
    }

    // Mutators
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

    // Accessors
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

    // Calculate the production time of an item
    protected double productionTime() {
        return avgProcessingTime + rangeProcessingTime * (r.nextDouble() - 0.5);
    }

    // Statistics
    // Calculate the total time starved
    public double getTotalTimeStarved() {
        if (starvedTimes.size() == 0) return 0;
        int smallerSize = starvedTimes.size() > unstarvedTimes.size() ? unstarvedTimes.size() : starvedTimes.size();

        Double[] starvedArray = starvedTimes.toArray(new Double[starvedTimes.size()]);
        Double[] unstarvedArray = unstarvedTimes.toArray(new Double[unstarvedTimes.size()]);

        double total = 0;
        for (int i = 0; i < smallerSize; i++) {
            total += unstarvedArray[i] - starvedArray[i];
        }

        return total;
    }

    // Calculate the total time blocked
    public double getTotalTimeBlocked() {
        if (blockedTimes.size() == 0) return 0;
        int smallerSize = blockedTimes.size() > unblockedTimes.size() ? unblockedTimes.size() : blockedTimes.size();

        Double[] blockedArray = blockedTimes.toArray(new Double[blockedTimes.size()]);
        Double[] unblockedArray = unblockedTimes.toArray(new Double[unblockedTimes.size()]);

        double total = 0;
        for (int i = 0; i < smallerSize; i++) {
            total += unblockedArray[i] - blockedArray[i];
        }

        return total;
    }

    // Get the percentage spent working
    public double getWorkPercentage(double timeLimit) {
        return 100 - ((getTotalTimeStarved() + getTotalTimeBlocked()) / timeLimit * 100);
    }
}
