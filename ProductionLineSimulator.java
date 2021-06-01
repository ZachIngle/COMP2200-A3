import java.util.ArrayList;
import java.util.PriorityQueue;

public class ProductionLineSimulator {
    private double time = 0;
    private final double timeLimit = 10000000; // 10000000

    private PriorityQueue<Event> events = new PriorityQueue<Event>();

    private int avgProcessingTime;
    private int rangeProcessingTime;
    private int storageCapacity;

    public ProductionLineSimulator(int M, int N, int QMax) {
        avgProcessingTime = M;
        rangeProcessingTime = N;
        storageCapacity = QMax;
    }
    
    public double currentTime() {
        return time;
    }

    public void insert(Event e) {
        if (e.getTime() < time) {
            e.setTime(time);
        }
        assert e.getTime() >= time;
        events.add(e);
    }

    public void start() {
        ProducerStage S0 = new ProducerStage("S0", avgProcessingTime, rangeProcessingTime);
        Storage Q01 = new Storage(storageCapacity);
        ConsumerStage S5 = new ConsumerStage("S5", avgProcessingTime, rangeProcessingTime);

        S0.setDestination(Q01);

        ArrayList<Stage> Q01Sources = new ArrayList<>();
        Q01Sources.add(S0);
        Q01.setSources(Q01Sources);

        ArrayList<Stage> Q01Destinations = new ArrayList<>();
        Q01Destinations.add(S5);
        Q01.setDestinations(Q01Destinations);

        S5.setSource(Q01);

        insert(S0);
        simulate();
        System.out.println("Total items created: " + S0.getTotalItemsCreated());
        System.out.println("Total items consumed: " + S5.getTotalItemsConsumed());
        System.out.println("S0 total time blocked = " + S0.getTotalTimeBlocked());
        System.out.println("S0 work " + S0.getWorkPercentage(timeLimit));
    }

    private void simulate() {
        Event e;
        while ((e = events.poll()) != null && time < timeLimit) {
            time = e.getTime();
            //System.out.format("[%8.0f] %3s: %s\n", time, e.name, e.message);
            e.execute(this);
        }
    }
}
