package dontkillthetree.scu.edu.event;

/**
 * Created by Joey Zheng on 5/15/16.
 */
public class DisposeEvent {
    private long id;

    public DisposeEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
