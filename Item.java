// Item.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A class that represents an item in the production line

import java.util.ArrayList;

public class Item {
    // Generator used to get the unique id of the item
    private static UIDGenerator gen = UIDGenerator.getInstance();

    private String ID = gen.getID(); // Unique id
    // List of milestones the item goes through
    private ArrayList<Milestone> milestones = new ArrayList<>(); 

    // Add a new milestone to the item
    public void addMilestone(double time, String name, Info info) {
        milestones.add(new Milestone(time, name, info));
    }

    // Accessors
    public String getID() {
        return ID;
    }

    // Get the path the item took
    public Path getPath() {
        ArrayList<String> stageNames = new ArrayList<>();

        for (Milestone m : milestones) {
            stageNames.add(m.getName());
        }

        if (stageNames.contains("S2a")) {
            if (stageNames.contains("S4a")) {
                return Path.S2AS4A;
            } else {
                return Path.S2AS4B;
            }
        } else {
            if (stageNames.contains("S4a")) {
                return Path.S2BS4A;
            } else {
                return Path.S2BS4B;
            }
        }
    }

    // Enum stating the different states the item goes through
    public enum Info {
        CREATED,
        ENTERED,
        WORKED,
        LEFT,
        QUEUED
    }

    // Enum with all paths
    public enum Path {
        S2AS4A,
        S2AS4B,
        S2BS4A,
        S2BS4B,
    }

    // A private class that holds information about a milestone
    private class Milestone implements Comparable<Milestone> {
        private double time; // The time the milestone was made
        private String name; // The name of the storage or stage the milestone was made
        private Info info; // The current state of the item

        // Constructor
        public Milestone(double time, String name, Info info) {
            this.time = time;
            this.name = name;
            this.info = info;
        }

        // Accessors
        public double getTime() {
            return time;
        }

        public String getName() {
            return name;
        }

        public Info getInfo() {
            return info;
        }

        // Comparable implementation so that the milestones can be sorted
        @Override
        public int compareTo(Milestone m) {
            if (time < m.getTime()) {
                return -1;
            } else if (time > m.getTime()) {
                return 1;
            }
    
            return 0;
        }
    }
}