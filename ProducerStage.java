// ProducerStage.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A stage which creates infinite items

public class ProducerStage extends Stage {
    private int totalItemsCreated = 0; // The total items created in the stage

    // Constructor
    public ProducerStage(String name, int M, int N) {
        super(name, M, N);
        starved = false;
    }

    // Accessor
    public int getTotalItemsCreated() {
        return totalItemsCreated;
    }

    // Event implementation
    // 1. First check if blocked. Won't execute until unblocked.
    // 2. Next check if the destination storage is full. If so block and add a blocked time.
    // 3. If not holding an item, and we don't have an item created, make one.
    // 4. If holding an item, send it to destination.
    @Override
    public void execute(ProductionLineSimulator sim) {
        if (blocked) return;

        if (destination.isFull()) {
            blocked = true;
            blockedTimes.add(sim.currentTime());
        } else if (currentItem == null) {
            time = sim.currentTime() + productionTime();
            currentItem = new Item();
            currentItem.addMilestone(time, name, Item.Info.CREATED);
            totalItemsCreated++;
        } else {
            currentItem.addMilestone(time, name, Item.Info.LEFT);
            destination.pushToQueue(sim, currentItem);
            currentItem = null;
        }

        sim.insert(this);
    }
}