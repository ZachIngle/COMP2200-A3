abstract class Event implements Comparable<Event> {
    protected double time = 0;
    protected String name;
    protected String message;

    abstract void execute(ProductionLineSimulator simulator);

    public void setTime(double time) {
        this.time = time;
    }

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
