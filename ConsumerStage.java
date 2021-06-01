public class ConsumerStage extends Stage{
    private int totalItemsConsumed = 0;

    public ConsumerStage(String name, int M, int N) {
        super(name, M, N);
    }

    public int getTotalItemsConsumed() {
        return totalItemsConsumed;
    }

    @Override
    public void execute(ProductionLineSimulator sim) {
        if (starved) return;

        if (currentItem == null) {
            if (source.isEmpty()) {
                starved = true;
                starvedTimes.add(sim.currentTime());
                message = "Starved";
            } else {
                currentItem = source.pollFromQueue(sim);
                time = sim.currentTime() + productionTime();
                message = "Worked!";
                sim.insert(this);
            }
        } else {
            currentItem = null;
            totalItemsConsumed++;
            message = "Consumed!";
            sim.insert(this);
        }
    }
}
