import java.util.ArrayList;

public class ConsumerStage extends Stage{
    private ArrayList<Item> finishedItems = new ArrayList<>();

    public ConsumerStage(String name, int M, int N) {
        super(name, M, N);
        starvedTimes.add(0.0);
    }

    public ArrayList<Item> getFinishedItems() {
        return finishedItems;
    }

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
