import java.util.ArrayList;

public class Item {
    private String ID;
    private ArrayList<Milestone> milestones = new ArrayList<>();

    private static UIDGenerator gen = UIDGenerator.getInstance();

    public Item() {
        ID = gen.getID();
    }

    public String getID() {
        return ID;
    }

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

    public void addMilestone(double time, String name, Info info) {
        milestones.add(new Milestone(time, name, info));
    }

    public enum Info {
        CREATED,
        ENTERED,
        WORKED,
        LEFT,
        QUEUED
    }

    public enum Path {
        S2AS4A,
        S2AS4B,
        S2BS4A,
        S2BS4B,
    }

    private class Milestone implements Comparable<Milestone>{
        private double time;
        private String name;
        private Info info;

        public Milestone(double time, String name, Info info) {
            this.time = time;
            this.name = name;
            this.info = info;
        }

        public double getTime() {
            return time;
        }

        public String getName() {
            return name;
        }

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