// ConsumerStage.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A stage which consumes infinite items

import java.util.ArrayList;

public class ConsumerStage extends Stage{
    private ArrayList<Item> finishedItems = new ArrayList<>(); // List of finished items

    // Constructor
    public ConsumerStage(String name, int M, int N) {
        super(name, M, N);
        starvedTimes.add(0.0);
    }

    // Accessor
    public ArrayList<Item> getFinishedItems() {
        return finishedItems;
    }

    // Event implementation
    // 1. First check if starved. Won't execute until unstarved.
    // 2. If not holding an item:
    //   a. If source is empty, starved and add starved time.
    //   b. If not, grab one from source and work item
    // 3. If holding item, add to finishedItems and set to no holding
    @Override
    public void execute(ProductionLineSimulator sim) {
        if (starved) return;

        if (currentItem == null) {
            if (source.isEmpty()) {
                starved = true;
                starvedTimes.add(sim.currentTime());
            } else {
                currentItem = source.pollFromQueue(sim);
                currentItem.addMilestone(time, name, Item.Info.ENTERED);
                time = sim.currentTime() + productionTime();
                currentItem.addMilestone(time, name, Item.Info.WORKED);
            }
        } else {
            currentItem.addMilestone(time, name, Item.Info.LEFT);
            finishedItems.add(currentItem);
            currentItem = null;
        }

        sim.insert(this);
    }
}
