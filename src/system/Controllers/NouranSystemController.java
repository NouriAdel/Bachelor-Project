package system.Controllers;

import java.util.ArrayList;

import components.Component;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import system.ComponentLabel;

public class NouranSystemController {
	
	@FXML
	Pane mainPane;
	
	@FXML
	Label pump1;
	
	@FXML
	Label pump2;
	
	@FXML
	Label valve;
	
	@FXML
	Label valve1;
	
	@FXML
	Label valve2;
	
	@FXML
	Label pipe1;
	
	@FXML
	Label pipe2;
	
	
	private CreateComponentController createComponentController;
	
	public ArrayList<ComponentLabel> componentLabels;
	public ArrayList<Component> topEventComponents;
	
	@FXML
	public Pane mainPain;

	public NouranSystemController() {
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
				drawLineBetween2Label(mainPane, pump1, valve);
				drawLineBetween2Label(mainPane, pump2, valve);
				drawLineBetween2Label(mainPane, valve, valve1);
				drawLineBetween2Label(mainPane, valve, valve2);
				drawLineBetween2Label(mainPane, valve1, pipe1);
				drawLineBetween2Label(mainPane, valve2, pipe2);
			}
		});
			
		
	}
	
public Line drawLineBetween2Label(Pane commonAncestor, Label upperLabel, Label lowerLabel) {
		
		Bounds bounds1 = getRelativeBounds(upperLabel, commonAncestor);
		Bounds bounds2 = getRelativeBounds(lowerLabel, commonAncestor);
		
		Bounds upperLabelToCommonAnscestor = bounds1;
		Bounds lowerLabelToCommonAnscestor = bounds2;
		
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
