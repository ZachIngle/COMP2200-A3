public class WorkerStage extends Stage {
   
    public WorkerStage(String name,int M, int N) {
        super(name, M, N);
    }

    @Override
    public void execute(ProductionLineSimulator sim) {    
        if (starved) return;
        if (blocked) return;
        
        if (currentItem == null && !blocked) {
            time = sim.currentTime();
            starvedTimes.add(time);
            blocked = true;
            System.out.println("Consumer starved " + time);
        } else {
            System.out.println("Consumer finished working on item at " + time);
            currentItem = null;
            blocked = false;
            System.out.println("Consumer consumed item!");
        }

        sim.insert(this);
    }
}
