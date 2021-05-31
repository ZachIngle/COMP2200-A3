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
        events.add(e);
    }

    public void start() {
        ProducerStage S0 = new ProducerStage(avgProcessingTime, rangeProcessingTime);
        Storage Q01 = new Storage(storageCapacity);
        ConsumerStage S5 = new ConsumerStage(avgProcessingTime, rangeProcessingTime);

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
    }

    private void simulate() {
        Event e;
        while ((e = events.poll()) != null && time < timeLimit) {
            time = e.getTime();
            e.execute(this);
        }
    }
}