package org.activiti.crystalball.simulator.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;

/**
 * This class changes behavior for Service tasks for simulation experiments
 */
public class SimulatorServiceTaskParseHandler extends ServiceTaskParseHandler {

  protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
    super.executeParse(bpmnParse, serviceTask);

    SimulatorParserUtils.setSimulationBehavior(bpmnParse.getCurrentScope(), serviceTask);
  }
}
