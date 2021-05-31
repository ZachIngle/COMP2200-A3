public class ProducerStage extends Stage {
    UIDGenerator gen = UIDGenerator.getInstance();

    public ProducerStage(int M, int N) {
        super(M, N);
    }

    @Override
    public void execute(ProductionLineSimulator sim) {
        if (!destination.isFull()) {
            blocked = false;
        } else {
            blocked = true;
        }

        if (blocked) {
            time = sim.currentTime();
            blockedTimes.add(time);
            blocked = true;
            System.out.println("Producer blocked at " + time);
        } else {
            time = time + productionTime();
            destination.pushToQueue(sim, new Item(gen.getID()));
            System.out.println("Producer pushed to destination at " + time);
            sim.insert(this);
        }
    }
}