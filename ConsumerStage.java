public class ConsumerStage extends Stage{
    public ConsumerStage(int M, int N) {
        super(M, N);
    }

    @Override
    public void execute(ProductionLineSimulator sim) {
        if (currentItem == null && !starved) {
            if (source.isEmpty()) {
                time = sim.currentTime();
                starvedTimes.add(time);
                starved = true;
                System.out.println("Consumer starved " + time);
            } else {
                starved = false;
                insertItem(sim, source.pollFromQueue(sim));
            }
        } else {
            System.out.println("Consumer finished working on item at " + time);
            currentItem = null;
            starved = false;
            System.out.println("Consumer consumed item!");
            sim.insert(this);
        }
    }
}
