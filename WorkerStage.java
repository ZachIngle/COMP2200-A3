// ConsumerStage.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A stage which only works items

public class WorkerStage extends Stage {
    // Constructor
    public WorkerStage(String name, int M, int N) {
        super(name, M, N);
        starvedTimes.add(0.0);
    }

    // Event implementation
    // 1. Check if starved. Won't execute until unstarved.
    // 2. Check if blocked. Won't execute until unblocked.
    // 3. If not holding an item:
    //   a. If source is empty, starved and add starved time.
    //   b. If not, grab one from source and work item
    // 4. If holding item:
    //   a. If destination is full, block and add blocked time.
    //   b. If not, push to destination
    @Override
    public void execute(ProductionLineSimulator sim) {    
        if (starved) return;
        if (blocked) return;
        
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
            if (destination.isFull()) {
                blocked = true;
                blockedTimes.add(sim.currentTime());
            } else {
                currentItem.addMilestone(time, name, Item.Info.LEFT);
                destination.pushToQueue(sim, currentItem);
                currentItem = null;
            }
        }

        sim.insert(this);
    }
}
