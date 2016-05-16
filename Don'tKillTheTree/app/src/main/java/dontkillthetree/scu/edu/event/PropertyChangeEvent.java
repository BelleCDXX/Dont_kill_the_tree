package dontkillthetree.scu.edu.event;

/**
 * Created by Joey Zheng on 5/15/16.
 */
public class PropertyChangeEvent {
    private final long id;
    private final String propertyName;
    private final String value;

    public PropertyChangeEvent(long id, String propertyName, String value) {
        this.id = id;
        this.propertyName = propertyName;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValue() {
        return value;
    }
}
