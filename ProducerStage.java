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
            message = "Blocked";
        } else if (currentItem == null) {
            time = sim.currentTime() + productionTime();
            currentItem = new Item();
            message = "Created new item";
            totalItemsCreated++;
            sim.insert(this);
        } else {
            destination.pushToQueue(sim, currentItem);
            message = "Pushed";
            currentItem = null;
            sim.insert(this);
        }
    }
}