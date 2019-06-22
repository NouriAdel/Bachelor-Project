package system.Controllers;

import java.util.ArrayList;

import components.Component;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import system.ComponentLabel;

public class HLASystemController {
	
	@FXML
	Pane mainPane;
	
	@FXML
	Label greenPump;
	
	@FXML
	Label bluePump;
	
	@FXML
	Label greenValve;
	
	@FXML
	Label blueValve;
	
	@FXML
	Label pipeG1;
	
	@FXML
	Label pipeG2;
	
	@FXML
	Label pipeB1;
	
	@FXML
	Label pipeB2;
	
	@FXML
	Label cylinder;
	
	
	private CreateComponentController createComponentController;
	
	public ArrayList<ComponentLabel> componentLabels;
	public ArrayList<Component> topEventComponents;
	
	@FXML
	public Pane mainPain;

	public HLASystemController() {
		componentLabels = new ArrayList<ComponentLabel>();
		topEventComponents = new ArrayList<Component>();
	}
	
	public void viewNewComponent(ComponentLabel component) {
		componentLabels.add(component);
		customizeLabel(component);
		
		mainPain.getChildren().add(component);
	}
	
	public void customizeLabel(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; -fx-text-wrap: true;");
		label.autosize();
		label.setTextAlignment(TextAlignment.CENTER);
	}
	
	public void initialize() {
		
		Platform.runLater(new Runnable() {
			public void run() {
				drawLineBetween2Label(mainPane, greenPump, pipeG1);
				drawLineBetween2Label(mainPane, bluePump, pipeB1);
				drawLineBetween2Label(mainPane, pipeG1, greenValve);
				drawLineBetween2Label(mainPane, pipeB1, blueValve);
				drawLineBetween2Label(mainPane, greenValve, pipeG2);
				drawLineBetween2Label(mainPane, blueValve, pipeB2);
				drawLineBetween2Label(mainPane, pipeB2, cylinder);
				drawLineBetween2Label(mainPane, pipeG2, cylinder);
			}
		});
		
	}
	
public Line drawLineBetween2Label(Pane commonAncestor, Label upperLabel, Label lowerLabel) {
		
		Bounds bounds1 = getRelativeBounds(upperLabel, commonAncestor);
		Bounds bounds2 = getRelativeBounds(lowerLabel, commonAncestor);
		
		Bounds upperLabelToCommonAnscestor;
		Bounds lowerLabelToCommonAnscestor;
		
		if (bounds1.getMaxY() < bounds2.getMaxY()) {
			upperLabelToCommonAnscestor = bounds1;
			lowerLabelToCommonAnscestor = bounds2;
		}
		else {
			upperLabelToCommonAnscestor = bounds2;
			lowerLabelToCommonAnscestor = bounds1;
		}

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
