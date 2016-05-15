package dontkillthetree.scu.edu.event;

/**
 * Created by Joey Zheng on 5/15/16.
 */
public class PropertyChangeEvent<T> {
    private long id;
    private String propertyName;
    private T value;

    public PropertyChangeEvent(long id, String propertyName, T value) {
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

    public T getValue() {
        return value;
    }
}
