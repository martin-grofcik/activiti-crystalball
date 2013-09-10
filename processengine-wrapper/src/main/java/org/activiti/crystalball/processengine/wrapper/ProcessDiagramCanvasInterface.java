package org.activiti.crystalball.processengine.wrapper;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.InputStream;

/**
 *
 */
public interface ProcessDiagramCanvasInterface {
    InputStream generateImage(String imageType);

    void close();

    void drawNoneStartEvent(int x, int y, int width, int height);

    void drawTimerStartEvent(int x, int y, int width, int height);

    void drawStartEvent(int x, int y, int width, int height, Image image);

    void drawNoneEndEvent(int x, int y, int width, int height);

    void drawErrorEndEvent(String name, int x, int y, int width, int height);

    void drawErrorEndEvent(int x, int y, int width, int height);

    void drawErrorStartEvent(int x, int y, int width, int height);

    void drawCatchingEvent(int x, int y, int width, int height, boolean isInterrupting, Image image);

    void drawCatchingTimerEvent(String name, int x, int y, int width, int height, boolean isInterrupting);

    void drawCatchingTimerEvent(int x, int y, int width, int height, boolean isInterrupting);

    void drawCatchingErrorEvent(String name, int x, int y, int width, int height, boolean isInterrupting);

    void drawCatchingErrorEvent(int x, int y, int width, int height, boolean isInterrupting);

    void drawCatchingSignalEvent(String name, int x, int y, int width, int height, boolean isInterrupting);

    void drawCatchingSignalEvent(int x, int y, int width, int height, boolean isInterrupting);

    void drawThrowingSignalEvent(int x, int y, int width, int height);

    void drawThrowingNoneEvent(int x, int y, int width, int height);

    void drawSequenceflow(int srcX, int srcY, int targetX, int targetY, boolean conditional);

    void drawSequenceflow(int srcX, int srcY, int targetX, int targetY, boolean conditional, boolean highLighted);

    void drawSequenceflow(int[] xPoints, int[] yPoints, boolean conditional, boolean isDefault, boolean highLighted);

    void drawSequenceflowWithoutArrow(int srcX, int srcY, int targetX, int targetY, boolean conditional);

    void drawSequenceflowWithoutArrow(int srcX, int srcY, int targetX, int targetY, boolean conditional, boolean highLighted);

    void drawArrowHead(Line2D.Double line);

    void drawDefaultSequenceFlowIndicator(Line2D.Double line);

    void drawConditionalSequenceFlowIndicator(Line2D.Double line);

    void drawTask(String name, int x, int y, int width, int height);

    void drawPoolOrLane(String name, int x, int y, int width, int height);

    void drawUserTask(String name, int x, int y, int width, int height);

    void drawScriptTask(String name, int x, int y, int width, int height);

    void drawServiceTask(String name, int x, int y, int width, int height);

    void drawReceiveTask(String name, int x, int y, int width, int height);

    void drawSendTask(String name, int x, int y, int width, int height);

    void drawManualTask(String name, int x, int y, int width, int height);

    void drawBusinessRuleTask(String name, int x, int y, int width, int height);

    void drawExpandedSubProcess(String name, int x, int y, int width, int height, Boolean isTriggeredByEvent);

    void drawCollapsedSubProcess(String name, int x, int y, int width, int height, Boolean isTriggeredByEvent);

    void drawCollapsedCallActivity(String name, int x, int y, int width, int height);

    void drawCollapsedMarker(int x, int y, int width, int height);

    void drawActivityMarkers(int x, int y, int width, int height, boolean multiInstanceSequential, boolean multiInstanceParallel, boolean collapsed);

    void drawGateway(int x, int y, int width, int height);

    void drawParallelGateway(int x, int y, int width, int height);

    void drawExclusiveGateway(int x, int y, int width, int height);

    void drawInclusiveGateway(int x, int y, int width, int height);

    void drawEventBasedGateway(int x, int y, int width, int height);

    void drawMultiInstanceMarker(boolean sequential, int x, int y, int width, int height);

    void drawHighLight(int x, int y, int width, int height, Color color);

    void drawHighLight(int x, int y, int width, int height);

    public void drawStringToNode(String count, int x, int y, int width, int height);

    void drawLabel(String name, int x, int y, int width, int height);
}
