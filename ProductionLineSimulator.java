import java.util.ArrayList;
import java.util.PriorityQueue;

public class ProductionLineSimulator {
    private double time = 0;
    private final double timeLimit = 10000000; // 10000000

    private PriorityQueue<Event> events = new PriorityQueue<Event>();

    private int M; // Average processing time
    private int N; // Range processing time
    private int storageCapacity;

    public ProductionLineSimulator(int M, int N, int QMax) {
        this.M = M;
        this.N = N;
        storageCapacity = QMax;
    }
    
    public double currentTime() {
        return time;
    }

    public void insert(Event e) {
        if (!events.contains(e)) {
            if (e.getTime() < time) {
                e.setTime(time);
            }
            assert e.getTime() >= time;
                events.add(e);
        }
    }

    public void start() {
        // Setup stages and storages
        // TODO: Probably implement factory?
        ProducerStage S0 = new ProducerStage("S0", M, N);
        Storage Q01 = new Storage("Q01", storageCapacity);
        WorkerStage S1 = new WorkerStage("S1", M, N);
        Storage Q12 = new Storage("Q12", storageCapacity);
        WorkerStage S2a = new WorkerStage("S2a", 2 * M, 2 * N);
        WorkerStage S2b = new WorkerStage("S2b", 2 * M, 2 * N);
        Storage Q23 = new Storage("Q23", storageCapacity);
        WorkerStage S3 = new WorkerStage("S3", M, N);
        Storage Q34 = new Storage("Q34", storageCapacity);
        WorkerStage S4a = new WorkerStage("S4a", 2 * M, 2 * N);
        WorkerStage S4b = new WorkerStage("S4b", 2 * M, 2 * N);
        Storage Q45 = new Storage("Q45", storageCapacity);
        ConsumerStage S5 = new ConsumerStage("S5", M, N);

        S0.setDestination(Q01);

        ArrayList<Stage> Q01Sources = new ArrayList<>();
        Q01Sources.add(S0);
        Q01.setSources(Q01Sources);

        ArrayList<Stage> Q01Destinations = new ArrayList<>();
        Q01Destinations.add(S1);
        Q01.setDestinations(Q01Destinations);

        S1.setSource(Q01);
        S1.setDestination(Q12);

        ArrayList<Stage> Q12Sources = new ArrayList<>();
        Q12Sources.add(S1);
        Q12.setSources(Q12Sources);

        ArrayList<Stage> Q12Destinations = new ArrayList<>();
        Q12Destinations.add(S2a);
        Q12Destinations.add(S2b);
        Q12.setDestinations(Q12Destinations);

        S2a.setSource(Q12);
        S2a.setDestination(Q23);

        S2b.setSource(Q12);
        S2b.setDestination(Q23);

        ArrayList<Stage> Q23Sources = new ArrayList<>();
        Q23Sources.add(S2a);
        Q23Sources.add(S2b);
        Q23.setSources(Q23Sources);

        ArrayList<Stage> Q23Destinations = new ArrayList<>();
        Q23Destinations.add(S3);
        Q23.setDestinations(Q23Destinations);

        S3.setSource(Q23);
        S3.setDestination(Q34);

        ArrayList<Stage> Q34Sources = new ArrayList<>();
        Q34Sources.add(S3);
        Q34.setSources(Q34Sources);

        ArrayList<Stage> Q34Destinations = new ArrayList<>();
        Q34Destinations.add(S4a);
        Q34Destinations.add(S4b);
        Q34.setDestinations(Q34Destinations);

        S4a.setSource(Q34);
        S4a.setDestination(Q45);

        S4b.setSource(Q34);
        S4b.setDestination(Q45);

        ArrayList<Stage> Q45Sources = new ArrayList<>();
        Q45Sources.add(S4a);
        Q45Sources.add(S4b);
        Q45.setSources(Q45Sources);

        ArrayList<Stage> Q45Destinations = new ArrayList<>();
        Q45Destinations.add(S5);
        Q45.setDestinations(Q45Destinations);

        S5.setSource(Q45);

        insert(S0);
        simulate();
        System.out.println("Total items created: " + S0.getTotalItemsCreated());
        System.out.println("Total items consumed: " + S5.getFinishedItems().size());
        System.out.println();
        System.out.println("Production Stations");
        System.out.println("--------------------------------------------");
        System.out.format("%5s %5s %11s %11s%n", "Stage", "Work[%]", "Starve[t]", "Block[t]");
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S0", S0.getWorkPercentage(timeLimit), S0.getTotalTimeStarved(), S0.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S1", S1.getWorkPercentage(timeLimit), S1.getTotalTimeStarved(), S1.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S2a", S2a.getWorkPercentage(timeLimit), S2a.getTotalTimeStarved(), S2a.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S2b", S2b.getWorkPercentage(timeLimit), S2b.getTotalTimeStarved(), S2b.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S3", S3.getWorkPercentage(timeLimit), S3.getTotalTimeStarved(), S3.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S4a", S4a.getWorkPercentage(timeLimit), S4a.getTotalTimeStarved(), S4a.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S4b", S4b.getWorkPercentage(timeLimit), S4b.getTotalTimeStarved(), S4b.getTotalTimeBlocked());
        System.out.format("%-6s  %3.2f %11.2f %11.2f%n", "S5", S5.getWorkPercentage(timeLimit), S5.getTotalTimeStarved(), S5.getTotalTimeBlocked());

        System.out.println();
        System.out.println("Storage Queues");
        System.out.println("--------------------------------------------");
        System.out.format("%5s %11s %3s%n", "Store", "AvgTime[t]", "AvgItems");
        System.out.format("Q01   %11.2f %3.2f%n", Q01.getAverageTime(), Q01.getAverageItems());
        System.out.format("Q12   %11.2f %3.2f%n", Q12.getAverageTime(), Q12.getAverageItems());
        System.out.format("Q23   %11.2f %3.2f%n", Q23.getAverageTime(), Q23.getAverageItems());
        System.out.format("Q34   %11.2f %3.2f%n", Q34.getAverageTime(), Q34.getAverageItems());
        System.out.format("Q45   %11.2f %3.2f%n", Q45.getAverageTime(), Q45.getAverageItems());

        ArrayList<Item> finishedItems = S5.getFinishedItems();
        int S2AS4ACount = 0;
        int S2AS4BCount = 0;
        int S2BS4ACount = 0;
        int S2BS4BCount = 0;

        for (Item i : finishedItems) {
            switch (i.getPath()) {
                case S2AS4A:
                    S2AS4ACount++;
                    break;
                case S2AS4B:
                    S2AS4BCount++;
                    break;
                case S2BS4A:
                    S2BS4ACount++;
                    break;
                case S2BS4B:
                    S2BS4BCount++;
                    break;
            }
        }

        System.out.println();
        System.out.println("Production Paths");
        System.out.println("--------------------------------------------");
        System.out.println("S2a -> S4a: " + S2AS4ACount);
        System.out.println("S2a -> S4b: " + S2AS4BCount);
        System.out.println("S2b -> S4a: " + S2BS4ACount);
        System.out.println("S2b -> S4b: " + S2BS4BCount);
        
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
