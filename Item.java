public class Item {
    private String ID;
    private boolean workDone = false;

    private static UIDGenerator gen = UIDGenerator.getInstance();

    public Item() {
        ID = gen.getID();
    }

    public void setWorkDone(boolean workDone) {
        this.workDone = workDone;
    }

    public boolean getWorkDone() {
        return workDone;
    }
}