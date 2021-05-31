import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;

public class Storage {
    private ArrayList<Stage> sources;
    private ArrayList<Stage> destinations;

    private final int maxQueueSize;
    private Queue<Item> queue = new LinkedList<>();;

    public Storage(int storageCapacity) {
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

    public void pushToQueue(ProductionLineSimulator sim, Item i) {
        if (isFull()) {
            return;
        }

        for (Stage destination : destinations) {
            if (destination.isReady()) {
                System.out.println("Storage inserting into at " + sim.currentTime());
                destination.insertItem(sim, i);
                destination.setStarved(false);
                return;
            }
        }

        queue.add(i);
        System.out.println("Item added to queue. Queue size " + queue.size());
    }

    public Item pollFromQueue(ProductionLineSimulator sim) {
        for (Stage source : sources) {
            if (source.getBlocked()) {
                source.setBlocked(false);
                sim.insert(source);
            }
        }

        System.out.println("Item removed from queue. Queue size " + (queue.size() - 1));
        return queue.poll();
    }
}
