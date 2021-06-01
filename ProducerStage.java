public class ProducerStage extends Stage {
    public ProducerStage(String name, int M, int N) {
        super(name, M, N);
    }

    @Override
    public void execute(ProductionLineSimulator sim) {
        if (blocked) return;

        if (destination.isFull()) {
            blocked = true;
            blockedTimes.add(sim.currentTime());
            message = "Blocked";
        } else {
            time = sim.currentTime() + productionTime();
            currentItem = new Item();
            destination.pushToQueue(sim, currentItem);
            message = "Pushed";
            sim.insert(this);
        }
    }
}