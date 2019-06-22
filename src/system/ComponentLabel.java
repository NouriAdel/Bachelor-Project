package system;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import components.*;
import components.BSCU.BSCU;
import components.BSCU.BSCUState;
import components.BSCU.BSCUStates;
import components.Pipe.*;
import components.Pump.Pump;
import components.Pump.PumpState;
import components.Pump.PumpStates;
import components.Valve.Valve;
import components.Valve.ValveState;
import components.Valve.ValveStates;

public class ComponentLabel extends Label {
	
	public Component component;
	public double mouseX;
	public double mouseY;

	public ComponentLabel(String name, ComponentType type, int inputPortsSize, int outputPortsSize, Object state) {
		super(name);
		
		switch (type) {
		case VALVE:
			component = new Valve(name, inputPortsSize, outputPortsSize, (ValveStates) state);
			break;
		case BSCU:
			component = new BSCU(name, inputPortsSize, outputPortsSize, (BSCUStates) state);
			break;
		case PIPE:
			component = new Pipe(name, inputPortsSize, outputPortsSize, (PipeStates) state);
			break;
		case PUMP:
			component = new Pump(name, inputPortsSize, outputPortsSize, (PumpStates) state);
			break;	
		default: //customized
			component = new CustomizedComponent(name, ComponentType.CUSTOMIZED_COMPONENT , inputPortsSize, outputPortsSize, (String)state);
			break;
		}
		
		onComponentLabelDragged();

	}
	
	public void onComponentLabelDragged() {
		setOnMousePressed(event -> {
            mouseX = event.getSceneX() ;
            mouseY = event.getSceneY() ;
        });

        setOnMouseDragged(event -> {
           double deltaX = event.getSceneX() - mouseX ;
           double deltaY = event.getSceneY() - mouseY ;
           relocate(getLayoutX() + deltaX, getLayoutY() + deltaY);
           mouseX = event.getSceneX() ;
           mouseY = event.getSceneY() ;
        });
	}
	
	public void changeStateTo(Object state) {
		
		switch(component.type) {
		case VALVE:
			component.changeStateTo(new ValveState((ValveStates) state));
			break;
		case PIPE:
			component.changeStateTo(new PipeState((PipeStates) state));
			break;
		case BSCU:
			component.changeStateTo(new BSCUState((BSCUStates) state));
			break;
		case PUMP: 
			component.changeStateTo(new PumpState((PumpStates) state));
			break;
		default: // customized
			component.changeStateTo(new CustomizedState((String) state));
			break;
			
		}
	}
	
//	public void changeInputPorts(int number, )
	
	
	public Line drawLineBetween2Label(Pane commonAncestor, Label label2) {
		
		Bounds thisLabelBounds = getRelativeBounds(this, commonAncestor);
		Bounds otherLabelBounds = getRelativeBounds(label2, commonAncestor);
		
		Bounds upperLabelToCommonAnscestor = getRelativeBounds(this, commonAncestor);
		Bounds lowerLabelToCommonAnscestor = getRelativeBounds(label2, commonAncestor);
		
		if (thisLabelBounds.getMaxY() < otherLabelBounds.getMaxY()) {
			upperLabelToCommonAnscestor = thisLabelBounds;
			lowerLabelToCommonAnscestor = otherLabelBounds;
		}
		else {
			upperLabelToCommonAnscestor = otherLabelBounds;
			lowerLabelToCommonAnscestor = thisLabelBounds;
		}
			

		// System.out.println("upper bounds: " + upperLabelToCommonAnscestor);
		// System.out.println("lower bounds: " + lowerLabelToCommonAnscestor);

		Point2D operatorCenter = getCenter(upperLabelToCommonAnscestor);
		Point2D labelCenter = getCenter(lowerLabelToCommonAnscestor);

		double maxYL1 = upperLabelToCommonAnscestor.getMaxY();
		double minYl2 = lowerLabelToCommonAnscestor.getMinY();

		Line levelToOperator = new Line(operatorCenter.getX(), maxYL1, labelCenter.getX(), minYl2);
		commonAncestor.getChildren().add(levelToOperator);

		return levelToOperator;
	}
	// https://stackoverflow.com/questions/43115807/how-to-draw-line-between-two-nodes-placed-in-different-panes-regions/43119383

	private Bounds getRelativeBounds(Node node, Node relativeTo) {
		Bounds nodeBoundsInScene = node.localToScene(node.getBoundsInLocal());
		return relativeTo.sceneToLocal(nodeBoundsInScene);
	}

	private Point2D getCenter(Bounds b) {
		return new Point2D(b.getMinX() + b.getWidth() / 2, b.getMinY() + b.getHeight() / 2);
	}



}
