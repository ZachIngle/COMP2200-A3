// Event.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// An abstract class that specifies an event that can be executed
// by the ProductionLineSimulator.

abstract class Event implements Comparable<Event> {
    protected double time = 0; // The time the event will run

    // Execute method that specifies what will happen in the event
    abstract void execute(ProductionLineSimulator simulator);

    // Setter/getter
    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    // Comparable implementation to order events in event queue
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
