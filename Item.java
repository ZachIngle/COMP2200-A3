public class Item {
    private String ID;
    private boolean workDone = false;

    public Item(String ID) {
        this.ID = ID;
    }

    public void setWorkDone(boolean workDone) {
        this.workDone = workDone;
    }

    public boolean getWorkDone() {
        return workDone;
    }
}