package dontkillthetree.scu.edu.event;


public class DisposeEvent {
    private long id;

    public DisposeEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
