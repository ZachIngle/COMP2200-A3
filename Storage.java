import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.HashMap;

public class Storage {
    private String name;

    private ArrayList<Stage> sources;
    private ArrayList<Stage> destinations;

    private final int maxQueueSize;
    private Queue<Item> queue = new LinkedList<>();

    private HashMap<String, Double> ItemEntryTimes = new HashMap<>();
    private HashMap<String, Double> ItemRemovalTimes = new HashMap<>();
    private ArrayList<Integer> QueueSizes = new ArrayList<>();

    public Storage(String name, int storageCapacity) {
        this.name = name;
        maxQueueSize = storageCapacity;
    }

    public Storage(ArrayList<Stage> sources, ArrayList<Stage> destinations, int storageCapacity) {
        this.sources = sources;
        this.destinations = destinations;
        maxQueueSize = storageCapacity;
    }

    public void setSources(ArrayList<Stage> sources) {
        this.sources = sources;
    }

    public void setDestinations(ArrayList<Stage> destinations) {
        this.destinations = destinations;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean isFull() {
        return queue.size() == maxQueueSize;
    }

    public double getAverageTime() {
        double total = 0;
        for (HashMap.Entry<String, Double> entry : ItemEntryTimes.entrySet()) {
            if (!ItemRemovalTimes.containsKey(entry.getKey())) break;
            double removalTime = ItemRemovalTimes.get(entry.getKey());
            total += removalTime - entry.getValue();
        }

        return total / ItemEntryTimes.size();
    }

    public double getAverageItems() {
        double total = 0;
        for (Integer size : QueueSizes) {
            total += size;
        }

        return total / QueueSizes.size();
    }

    public void pushToQueue(ProductionLineSimulator sim, Item i) {
        QueueSizes.add(queue.size());
        if (isFull()) {
            return;
        }

        queue.add(i);
        i.addMilestone(sim.currentTime(), name, Item.Info.QUEUED);
        ItemEntryTimes.put(i.getID(), sim.currentTime());

        for (Stage destination : destinations) {
            if (destination.isReady()) {
                sim.insert(destination);
                //destination.insertItem(sim, i);
                //System.out.println("Storage inserting into at " + sim.currentTime());
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

        //System.out.println("Item added to queue. Queue size " + queue.size());
    }

    public Item pollFromQueue(ProductionLineSimulator sim) {
        QueueSizes.add(queue.size());
        // Check sources incase they are blocked
        for (Stage source : sources) {
            if (source.isBlocked()) {
                source.setBlocked(false);
                source.addUnblockedTime(sim.currentTime());
                source.setTime(sim.currentTime());
                sim.insert(source);
            }
        }

        //System.out.println("Item removed from queue. Queue size " + (queue.size() - 1));
        Item removedItem = queue.poll();
        removedItem.addMilestone(sim.currentTime(), name, Item.Info.LEFT);
        ItemRemovalTimes.put(removedItem.getID(), sim.currentTime());
        return removedItem;
    }
}
