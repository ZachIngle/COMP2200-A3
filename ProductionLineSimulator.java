// ProductionLineSimulator.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A class that simulates a production with a production line according
// to A3 specifications.

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ProductionLineSimulator {
    private double time = 0;
    private final double timeLimit = 10000000; // 10,000,000
    private int M; // Average processing time
    private int N; // Range processing time
    private int QMax; // Capacity of storage queues

    private PriorityQueue<Event> events = new PriorityQueue<Event>();

    // Constructor
    public ProductionLineSimulator(int M, int N, int QMax) {
        this.M = M;
        this.N = N;
        this.QMax = QMax;
    }
    
    // Get the current time
    public double currentTime() {
        return time;
    }

    // Insert an event into the event queue
    public void insert(Event e) {
        // Check to make sure event doesn't exist in queue already
        if (!events.contains(e)) {
            events.add(e);
        }
    }

    // Main start method of the simulator
    public void start() {
        // Setup stages and storages
        StageFactory stageFactory = new StageFactory();
        ProducerStage S0 = (ProducerStage) stageFactory.createStage("Producer", "S0", M, N);
        Storage Q01 = new Storage("Q01", QMax);
        WorkerStage S1 = (WorkerStage) stageFactory.createStage("Worker", "S1", M, N);
        Storage Q12 = new Storage("Q12", QMax);
        WorkerStage S2a = (WorkerStage) stageFactory.createStage("Worker", "S2a", 2 * M, 2 * N);
        WorkerStage S2b = (WorkerStage) stageFactory.createStage("Worker", "S2b", 2 * M, 2 * N);
        Storage Q23 = new Storage("Q23", QMax);
        WorkerStage S3 = (WorkerStage) stageFactory.createStage("Worker", "S3", M, N);
        Storage Q34 = new Storage("Q34", QMax);
        WorkerStage S4a = (WorkerStage) stageFactory.createStage("Worker", "S4a", 2 * M, 2 * N);
        WorkerStage S4b = (WorkerStage) stageFactory.createStage("Worker", "S4b", 2 * M, 2 * N);
        Storage Q45 = new Storage("Q45", QMax);
        ConsumerStage S5 = (ConsumerStage) stageFactory.createStage("Consumer", "S5", M, N);

        // Set up relationships between stages and storages
        // S0
        S0.setDestination(Q01);

        // Q01
        ArrayList<Stage> Q01Sources = new ArrayList<>();
        Q01Sources.add(S0);
        Q01.setSources(Q01Sources);

        ArrayList<Stage> Q01Destinations = new ArrayList<>();
        Q01Destinations.add(S1);
        Q01.setDestinations(Q01Destinations);

        // S1
        S1.setSource(Q01);
        S1.setDestination(Q12);

        // Q12
        ArrayList<Stage> Q12Sources = new ArrayList<>();
        Q12Sources.add(S1);
        Q12.setSources(Q12Sources);

        ArrayList<Stage> Q12Destinations = new ArrayList<>();
        Q12Destinations.add(S2a);
        Q12Destinations.add(S2b);
        Q12.setDestinations(Q12Destinations);

        // S2a
        S2a.setSource(Q12);
        S2a.setDestination(Q23);

        // S2b
        S2b.setSource(Q12);
        S2b.setDestination(Q23);

        // Q23
        ArrayList<Stage> Q23Sources = new ArrayList<>();
        Q23Sources.add(S2a);
        Q23Sources.add(S2b);
        Q23.setSources(Q23Sources);

        ArrayList<Stage> Q23Destinations = new ArrayList<>();
        Q23Destinations.add(S3);
        Q23.setDestinations(Q23Destinations);

        // S3
        S3.setSource(Q23);
        S3.setDestination(Q34);

        // Q34
        ArrayList<Stage> Q34Sources = new ArrayList<>();
        Q34Sources.add(S3);
        Q34.setSources(Q34Sources);

        ArrayList<Stage> Q34Destinations = new ArrayList<>();
        Q34Destinations.add(S4a);
        Q34Destinations.add(S4b);
        Q34.setDestinations(Q34Destinations);

        // S4a
        S4a.setSource(Q34);
        S4a.setDestination(Q45);

        // S4b
        S4b.setSource(Q34);
        S4b.setDestination(Q45);

        // Q45
        ArrayList<Stage> Q45Sources = new ArrayList<>();
        Q45Sources.add(S4a);
        Q45Sources.add(S4b);
        Q45.setSources(Q45Sources);

        ArrayList<Stage> Q45Destinations = new ArrayList<>();
        Q45Destinations.add(S5);
        Q45.setDestinations(Q45Destinations);

        // S5
        S5.setSource(Q45);

        // Insert producer for first event
        insert(S0);
        // Start simulations loop
        simulate();

        // Print out statistics of run
        System.out.println("Total items created: " + S0.getTotalItemsCreated());
        System.out.println("Total items finished: " + S5.getFinishedItems().size());
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
        System.out.format("%5s %11s %s%n", "Store", "AvgTime[t]", "AvgItems");
        System.out.format("%-5s %11.2f %8.2f%n", Q01.getName(), Q01.getAverageTime(), Q01.getAverageItems());
        System.out.format("%-5s %11.2f %8.2f%n", Q12.getName(), Q12.getAverageTime(), Q12.getAverageItems());
        System.out.format("%-5s %11.2f %8.2f%n", Q23.getName(), Q23.getAverageTime(), Q23.getAverageItems());
        System.out.format("%-5s %11.2f %8.2f%n", Q34.getName(), Q34.getAverageTime(), Q34.getAverageItems());
        System.out.format("%-5s %11.2f %8.2f%n", Q45.getName(), Q45.getAverageTime(), Q45.getAverageItems());

        // Find paths of finished items
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

    // Simulation loop method
    // Loops until we have no events or time reaches the time limit
    private void simulate() {
        Event e;
        while ((e = events.poll()) != null && time < timeLimit) {
            time = e.getTime();
            e.execute(this);
        }
    }
}
