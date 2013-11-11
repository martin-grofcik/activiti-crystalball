package org.activiti.crystalball.simulator.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.ScriptTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ScriptTaskParseHandler;

/**
 * This class changes behavior for Script tasks for simulation experiments
 */
public class SimulatorScriptTaskParseHandler extends ScriptTaskParseHandler {

  protected void executeParse(BpmnParse bpmnParse, ScriptTask scriptTask) {
    super.executeParse(bpmnParse, scriptTask);

    SimulatorParserUtils.setSimulationBehavior(bpmnParse.getCurrentScope(), scriptTask);
  }
}
