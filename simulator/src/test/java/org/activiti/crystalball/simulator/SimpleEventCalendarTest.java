package org.activiti.crystalball.simulator;

import org.activiti.engine.impl.util.ClockUtil;
import org.junit.Test;

import java.util.Comparator;
import java.util.Date;

import static org.junit.Assert.*;


public class SimpleEventCalendarTest {
    protected Comparator<SimulationEvent> comparator = new SimulationEventComparator();

    @Test
    public void testIsEmpty() throws Exception {
        EventCalendar calendar = new SimpleEventCalendar(comparator);
        assertTrue(calendar.isEmpty());
        SimulationEvent event = calendar.removeFirstEvent();
        assertNull(event);
    }

    @Test
    public void testAddEventsAndRemoveFirst() throws Exception {
        SimulationEvent event1 = new SimulationEvent.Builder(1, "any type").build();
        SimulationEvent event2 = new SimulationEvent.Builder(2, "any type").build();
        EventCalendar calendar = new SimpleEventCalendar(comparator);
        ClockUtil.setCurrentTime(new Date(0));

        calendar.addEvent(event1);
        calendar.addEvent( event2);
        calendar.addEvent( event1);

        SimulationEvent event = calendar.removeFirstEvent();
        assertEquals(event1, event);
        event = calendar.removeFirstEvent();
        assertEquals(event1, event);
        event = calendar.removeFirstEvent();
        assertEquals(event2, event);
    }

    @Test
    public void testClear() throws Exception {
      SimulationEvent event1 = new SimulationEvent.Builder(1, "any type").build();
      EventCalendar calendar = new SimpleEventCalendar(comparator);
      ClockUtil.setCurrentTime(new Date(0));

      calendar.addEvent(event1);

      calendar.clear();
      assertTrue( calendar.isEmpty());
      assertNull( calendar.removeFirstEvent());
    }

    @Test(expected = RuntimeException.class)
    public void testRunEventFromPast() throws Exception {
        SimulationEvent event1 = new SimulationEvent.Builder(1, "any type").build();
        EventCalendar calendar = new SimpleEventCalendar(comparator);

        calendar.addEvent(event1);
        ClockUtil.setCurrentTime(new Date(2));

        calendar.removeFirstEvent();
        fail("RuntimeException expected");
    }
}
