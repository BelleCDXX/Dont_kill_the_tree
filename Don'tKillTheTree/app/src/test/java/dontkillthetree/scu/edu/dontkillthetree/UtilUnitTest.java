package dontkillthetree.scu.edu.dontkillthetree;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import dontkillthetree.scu.edu.Util.Util;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UtilUnitTest {

    @Test
    public void isOnTime() throws Exception {
        Calendar dueDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        dueDate.setTime(new Date(2016, 01, 01, 05, 00, 03));
        currentDate.setTime(new Date(2016, 01, 01, 05, 00, 02));

        assertEquals(true, Util.isOnTime(dueDate, currentDate));

        dueDate.setTime(new Date(2016, 01, 01, 05, 00, 03));
        currentDate.setTime(new Date(2016, 01, 02, 00, 00, 01));

        assertEquals(false, Util.isOnTime(dueDate, currentDate));
    }
}