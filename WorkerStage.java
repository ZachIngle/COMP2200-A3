public class WorkerStage extends Stage {
   
    public WorkerStage(String name,int M, int N) {
        super(name, M, N);
        starvedTimes.add(0.0);
    }

    @Override
    public void execute(ProductionLineSimulator sim) {    
        if (starved) return;
        if (blocked) return;
        
        if (currentItem == null) {
            if (source.isEmpty()) {
                starved = true;
                starvedTimes.add(sim.currentTime());
                message = "Starved";
            } else {
                currentItem = source.pollFromQueue(sim);
                currentItem.addMilestone(time, name, Item.Info.ENTERED);
                time = sim.currentTime() + productionTime();
                currentItem.addMilestone(time, name, Item.Info.WORKED);
                message = "Worked!";
            }
        } else {
            if (destination.isFull()) {
                blocked = true;
                blockedTimes.add(sim.currentTime());
                message = "Blocked";
            } else {
                currentItem.addMilestone(time, name, Item.Info.LEFT);
                destination.pushToQueue(sim, currentItem);
                message = "Pushed";
                currentItem = null;
            }
        }

        sim.insert(this);
    }
}
