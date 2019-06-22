package system.Controllers;


import components.Component;
import components.ComponentType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class HomeController {
	
	@FXML
	public AnchorPane homePane;
	
	@FXML
	public Button createCompBtn;
	
	@FXML
	public Button modifyCompBtn;
	
	public Button generateTreeBtn;
	
	@FXML
	public ComboBox<Component> modifyCompCombobox;
	
//	@FXML
//	public VBox topEventVBox;
	
//	public ArrayList<Component> topEventComponentList;
	
	public void setVisible(boolean b) {
		homePane.setVisible(b);	
	}
	
//	public void initializeTypesInComboBox() {
//		modifyCompCombobox.getItems().addAll(ComponentType.PIPE, ComponentType.VALVE, ComponentType.PUMP, ComponentType.BSCU  );
//	}
	
//	public void addTopEventCheckBoxForComponent(Component component) {
//		CheckBox comp = new CheckBox(component.name);
//		topEventVBox.getChildren().remove(generateTreeBtn);
//
//		comp.selectedProperty().addListener((obs, oldValue, newValue) -> {
//			System.out.println(newValue);
//			if (newValue && !topEventComponentList.contains(component)) {
//				topEventComponentList.add(component);
//			}
//
//			if (!newValue && topEventComponentList.contains(component)) {
//				topEventComponentList.remove(component);
//			}
//			
//		});
//		
//		if (topEventComponentList.contains(component)) {
//			for (Node node: topEventVBox.getChildren()) {
//				CheckBox checkBox = (CheckBox) node;
//				if (checkBox.getText().equals(component.name))
//					topEventVBox.getChildren().remove(node);
//			}
//		}
//
//		topEventVBox.getChildren().add(comp);
//		topEventVBox.getChildren().add(generateTreeBtn);	
//	}
//	
//	public void initializeTopEventCheckBoxes(ArrayList<ComponentLabel> allComponents) {
//		topEventVBox.getChildren().clear();
//		
//		for (ComponentLabel compLabel : allComponents) {
//			Component component = compLabel.component;
//			System.out.println(component);
//			
//			CheckBox comp = new CheckBox(compLabel.getText());
//
//			comp.selectedProperty().addListener((obs, oldValue, newValue) -> {
//				System.out.println(newValue);
//				if (newValue && !topEventComponentList.contains(component)) {
//					topEventComponentList.add(component);
//				}
//
//				if (!newValue && topEventComponentList.contains(component)) {
//					topEventComponentList.remove(component);
//				}
//				
//			});
//			
//			topEventVBox.getChildren().add(comp);
//		}
//		topEventVBox.getChildren().add(generateTreeBtn);	
//	}
	
	public void initialize() {
		//initializeTypesInComboBox();
	}

}
