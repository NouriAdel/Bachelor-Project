package system.Controllers;

import java.util.ArrayList;

import components.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import system.ComponentLabel;

public class SystemController {
	
	private CreateComponentController createComponentController;
	
	public ArrayList<ComponentLabel> componentLabels;
	public ArrayList<Component> topEventComponents;
	
	@FXML
	public Pane mainPain;

	public SystemController() {
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
	}

}
