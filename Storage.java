// Storage.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A class that can hold items in a queue. Notifies blocked or
// starved stages when the queue is updated.

import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class Storage {
    private String name; // Name of the storage

    private ArrayList<Stage> sources; // The source stages
    private ArrayList<Stage> destinations; // The destination stages

    private final int maxQueueSize; // The max queue size a storage can have
    private Queue<Item> queue = new LinkedList<>(); // The queue which stores items

    // Statistics 
    private HashMap<String, Double> ItemEntryTimes = new HashMap<>();
    private HashMap<String, Double> ItemRemovalTimes = new HashMap<>();
    private ArrayList<Integer> QueueSizes = new ArrayList<>();

    // Constructor
    public Storage(String name, int storageCapacity) {
        this.name = name;
        maxQueueSize = storageCapacity;
    }

    // Mutators
    public void setSources(ArrayList<Stage> sources) {
        this.sources = sources;
    }

    public void setDestinations(ArrayList<Stage> destinations) {
        this.destinations = destinations;
    }

    // Accessors
    public String getName() {
        return name;
    }

    // Queue queries
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean isFull() {
        return queue.size() == maxQueueSize;
    }

    // Adding to the queue
    public void pushToQueue(ProductionLineSimulator sim, Item i) {
        queue.add(i);
        i.addMilestone(sim.currentTime(), name, Item.Info.QUEUED);
        ItemEntryTimes.put(i.getID(), sim.currentTime());
        QueueSizes.add(queue.size());

        for (Stage destination : destinations) {
            if (destination.isReady()) {
                // Update destination time
                destination.setTime(sim.currentTime());
                sim.insert(destination);
                break;
            }
        }

        // Check destinations incase they are starved
        for (Stage destination : destinations) {
            if (destination.isStarved()) {
                destination.setStarved(false);
                destination.addUnstarvedTime(sim.currentTime());
                sim.insert(destination);
            }
        }
    }

    // Removing fromt the queue
    public Item pollFromQueue(ProductionLineSimulator sim) {
        QueueSizes.add(queue.size());
        // Check sources incase they are blocked
        for (Stage source : sources) {
            if (source.isBlocked()) {
                source.setBlocked(false);
                source.addUnblockedTime(sim.currentTime());
                source.setTime(sim.currentTime()); // Make sure source time is up to date
                sim.insert(source);
            }
        }

        Item removedItem = queue.poll();
        removedItem.addMilestone(sim.currentTime(), name, Item.Info.LEFT);
        ItemRemovalTimes.put(removedItem.getID(), sim.currentTime());
        return removedItem;
    }

    // Statistics
    // Calculate the average time an item stays in the queue
    public double getAverageTime() {
        double total = 0;
        for (HashMap.Entry<String, Double> entry : ItemEntryTimes.entrySet()) {
            if (!ItemRemovalTimes.containsKey(entry.getKey())) break;
            double removalTime = ItemRemovalTimes.get(entry.getKey());
            total += removalTime - entry.getValue();
        }

        return total / ItemEntryTimes.size();
    }

    // Calculate the average items at any time in the queue
    public double getAverageItems() {
        double total = 0;
        for (Integer size : QueueSizes) {
            total += size;
        }

        return total / QueueSizes.size();
    }
}
