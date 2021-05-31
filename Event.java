abstract class Event implements Comparable<Event> {
    protected double time = 0;

    abstract void execute(ProductionLineSimulator simulator);

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event e) {
        if (time < e.getTime()) {
            return -1;
        } else if (time > e.getTime()) {
            return 1;
        }

        return 0;
    }
}
