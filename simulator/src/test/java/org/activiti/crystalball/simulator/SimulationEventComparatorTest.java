package org.activiti.crystalball.simulator;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class SimulationEventComparatorTest {

  Comparator<SimulationEvent> comparator = new SimulationEventComparator();

  @Test
  public void testCompare() throws Exception {
    SimulationEvent event1_0 = new SimulationEvent.Builder(0, "type").build();
    SimulationEvent eq1_0 = new SimulationEvent.Builder(0, "type").build();
    SimulationEvent event1_1 = new SimulationEvent.Builder(0, "type").priority(1).build();
    SimulationEvent event2 = new SimulationEvent.Builder(1, "type").build();
    assertEquals(0, comparator.compare(event1_0, eq1_0));
    assertEquals(-1, comparator.compare(event1_0, event2));
    assertEquals(1, comparator.compare(event2, event1_0));
    assertEquals(-1, comparator.compare(event1_0, event1_1));
    assertEquals(1, comparator.compare(event1_1, event1_0));
    assertEquals(-1, comparator.compare(event1_1, event2));
    assertEquals(1, comparator.compare(event2, event1_1));
  }
}
