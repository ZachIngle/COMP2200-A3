public class ProducerStage extends Stage {
    private int totalItemsCreated = 0;

    public ProducerStage(String name, int M, int N) {
        super(name, M, N);
    }

    public int getTotalItemsCreated() {
        return totalItemsCreated;
    }

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