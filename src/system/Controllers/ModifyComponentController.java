package system.Controllers;

import components.*;
import components.BSCU.BSCU;
import components.BSCU.BSCUState;
import components.BSCU.BSCUStates;
import components.Pipe.*;
import components.Pump.*;
import components.Valve.*;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import system.ComponentLabel;

public class ModifyComponentController {

	@FXML
	public AnchorPane modifyCompPane;

	@FXML
	public Button modifyCompmodifyBtn;

	@FXML
	public Button modifyCompCancelBtn;

	@FXML
	public TextField modifyCompNameTextField;

	@FXML
	public ComboBox<Object> modifyCompStateCombBox;

	@FXML
	public Spinner<Integer> modifyCompInputPorts;
	int possibleInputs;
	int checkedInputs;

	@FXML
	public Spinner<Integer> modifyCompOutputPorts;
	int possibleOutputs;
	int checkedOutputs;

	@FXML
	public VBox portsVBox;
	
	@FXML
	public CheckBox modifyCompAddToTopEventCheckBox;

	@FXML
	public VBox inputPortsVBox;

	@FXML
	public VBox outputPortsVBox;

	public ComponentLabel toBeModifiedComponentLabel;
	public ArrayList<ComponentLabel> allComponents;

	public ArrayList<Component> modifiedInputPorts;
	public ArrayList<Component> modifiedOutputPorts;

	boolean firstModification;
	
	@FXML
	public ScrollPane scrollInputs;
	
	@FXML
	public ScrollPane scrollOutputs;

	public void setVisible(boolean b) {
		modifyCompPane.setVisible(b);
	}

	public void initializeVBox() {

		portsVBox.getChildren().clear();

		Label inputPortsLabel = new Label("Please specify input ports");
		portsVBox.getChildren().add(inputPortsLabel);

		for (ComponentLabel comp : allComponents) {

			if (comp != toBeModifiedComponentLabel) {
				CheckBox port = new CheckBox(comp.getText());
				portsVBox.getChildren().add(port);
			}

		}

		Label outPortsLabel = new Label("Please specify input ports");
		portsVBox.getChildren().add(outPortsLabel);

		for (ComponentLabel comp : allComponents) {

			if (comp != toBeModifiedComponentLabel) {
				CheckBox port = new CheckBox(comp.getText());
				portsVBox.getChildren().add(port);
			}

		}
	}

	public void initializePorts() {

		// resetting
		modifiedInputPorts = new ArrayList<Component>();
		modifiedOutputPorts = new ArrayList<Component>();

		int inputPortsNumber = toBeModifiedComponentLabel.component.inputPorts.length;
		int outputPortsNumber = toBeModifiedComponentLabel.component.outputPorts.length;

		possibleInputs = inputPortsNumber;
		possibleOutputs = outputPortsNumber;

		checkedInputs = 0;
		for (Component input : toBeModifiedComponentLabel.component.inputPorts) {
			if (input != null)
				checkedInputs++;
		}
		checkedOutputs = 0;
		for (Component output : toBeModifiedComponentLabel.component.outputPorts) {
			if (output != null)
				checkedOutputs++;
		}

		modifyCompInputPorts.getValueFactory().setValue(inputPortsNumber);
		modifyCompOutputPorts.getValueFactory().setValue(outputPortsNumber);

		portsVBox.getChildren().clear();
		portsVBox.getChildren().add(modifyCompAddToTopEventCheckBox);
		inputPortsVBox.getChildren().clear();
		outputPortsVBox.getChildren().clear();

		for (ComponentLabel comp : allComponents) {

			if (comp != toBeModifiedComponentLabel) {

				boolean isInput = false;
				Component[] inputs = toBeModifiedComponentLabel.component.inputPorts;

				for (Component inputPortComp : inputs) {
					if (comp.component == inputPortComp) {
						isInput = true;
						break;
					}
				}

				boolean isOutput = false;
				Component[] outputs = toBeModifiedComponentLabel.component.outputPorts;

				for (Component inputPortComp : outputs) {
					if (comp.component == inputPortComp) {
						isOutput = true;
						break;
					}
				}

				CheckBox port = new CheckBox(comp.getText());

				port.selectedProperty().addListener((obs, oldValue, newValue) -> {

					if (!firstModification) {
						if (newValue && !modifiedInputPorts.contains(comp.component)) {
							modifiedInputPorts.add(comp.component);
							checkedInputs++;
							modifyCompInputPorts.increment();
						}

						if (!newValue && modifiedInputPorts.contains(comp.component)) {
							modifiedInputPorts.remove(comp.component);
							checkedInputs--;
							modifyCompInputPorts.decrement();

//							if (checkedInputs < possibleInputs) {
//								
//							}
						}
					}

				});

				CheckBox port2 = new CheckBox(comp.getText());

				port2.selectedProperty().addListener((obs, oldValue, newValue) -> {
					
					if (!firstModification) {
						if (newValue && !modifiedOutputPorts.contains(comp.component)) {
							modifiedOutputPorts.add(comp.component);
							checkedOutputs++;
							modifyCompOutputPorts.increment();
						}

						if (!newValue && modifiedOutputPorts.contains(comp.component)) {
							modifiedOutputPorts.remove(comp.component);
							checkedOutputs--;
							modifyCompOutputPorts.decrement();
						}
					}

				});

				if (isInput) {
					inputPortsVBox.getChildren().add(port);
					port.setSelected(true);
					modifiedInputPorts.add(comp.component);

					outputPortsVBox.getChildren().add(port2);
					port2.setSelected(false);

					checkedInputs++;
				} else if (isOutput) {
					inputPortsVBox.getChildren().add(port);
					port.setSelected(false);

					outputPortsVBox.getChildren().add(port2);
					port2.setSelected(true);
					modifiedOutputPorts.add(comp.component);

					checkedOutputs++;
				} else {
					inputPortsVBox.getChildren().add(port);
					outputPortsVBox.getChildren().add(port2);
				}
			}
		}
		inputPortsVBox.autosize();
		inputPortsVBox.setStyle("-fx-spacing: 10;");
		inputPortsVBox.setAlignment(Pos.CENTER_LEFT);

		outputPortsVBox.autosize();
		outputPortsVBox.setStyle("-fx-spacing: 10;");
		outputPortsVBox.setAlignment(Pos.CENTER_LEFT);

		Label inputPortsLabel = new Label("Please specify input ports");
		portsVBox.getChildren().add(inputPortsLabel);
		portsVBox.getChildren().add(scrollInputs);
		scrollInputs.setContent(inputPortsVBox);

		Label outPortsLabel = new Label("Please specify output ports");
		portsVBox.getChildren().add(outPortsLabel);
		portsVBox.getChildren().add(scrollOutputs);
		scrollOutputs.setContent(outputPortsVBox);
		
		firstModification = false;

	}

	public void setPortsActions() {

		modifyCompInputPorts.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			if (!oldValue.equals(newValue) ) {
				modifyCompInputPorts.getEditor().textProperty().set("" + checkedInputs);
				possibleInputs = checkedInputs;
			}
		});

		modifyCompOutputPorts.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {
				modifyCompOutputPorts.getEditor().textProperty().set("" + checkedOutputs);
				possibleOutputs = checkedOutputs;
			}
		});

	}

	public void preModifyComponent(ComponentLabel componentLabel, ArrayList<ComponentLabel> allComponents) {

		this.toBeModifiedComponentLabel = componentLabel;
		this.allComponents = allComponents;
		firstModification = true;

		// setting ports
		initializePorts();
		
		// setting component name
		modifyCompNameTextField.setText(componentLabel.component.name);
		
		// initialize states depending on component
		Object [] states=null;
		Object toBeDisplayed;
		
		modifyCompStateCombBox.getItems().clear();
		
		switch (componentLabel.component.type) {
		
		case VALVE:
			states = new Object [] {ValveStates.NOMINAL, ValveStates.STUCK, ValveStates.LEAK};
			Valve valve = (Valve) toBeModifiedComponentLabel.component;
			toBeDisplayed = ((ValveState) valve.state).state;
			break;
		case PIPE:
			states = new Object [] {PipeStates.NOMINAL, PipeStates.BLOCKED, PipeStates.LEAK};
			Pipe pipe = (Pipe) toBeModifiedComponentLabel.component;
			toBeDisplayed = ((PipeState) pipe.state).state;
			break;
		case BSCU:
			states = new Object [] {BSCUStates.NOMINAL, BSCUStates.LOSS_OF_CONTROL_SIGNAL};
			BSCU bscu = (BSCU) toBeModifiedComponentLabel.component;
			toBeDisplayed =  ((BSCUState) bscu.state).state;
			break;
		case PUMP: // pump
			states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
			Pump pump = (Pump) toBeModifiedComponentLabel.component;
			toBeDisplayed = ((PumpState) pump.state).state;
			break;
		default: // customized
			//states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
			//states = CreateComponentController.getAllCustomizedStates();
			if (CreateComponentController.allCustomizedStates!=null){
				states = new Object[CreateComponentController.allCustomizedStates.size()];
				for (int i=0;i<CreateComponentController.allCustomizedStates.size();i++){
					states[i]=CreateComponentController.allCustomizedStates.get(i);
				}
			} //HHHH
			
			//Pump pump = (Pump) toBeModifiedComponentLabel.component;
			//toBeDisplayed = ((PumpState) pump.state).state;
			CustomizedComponent custom = (CustomizedComponent) toBeModifiedComponentLabel.component;
			toBeDisplayed = ((CustomizedState)custom.state).state;
			break;			
		}
		
		System.out.println("in pre modify");
		for (Object state: states) {
			modifyCompStateCombBox.getItems().add(state);
		}
		
		// setting component state
		modifyCompStateCombBox.setValue(toBeDisplayed);
		
		// setting whether the component is a top level one
		if (componentLabel.component.isTopLevelComponent)
			modifyCompAddToTopEventCheckBox.setSelected(true);
		else
			modifyCompAddToTopEventCheckBox.setSelected(false);
	}

	public Component onModifyComponent() {

		Component component = toBeModifiedComponentLabel.component;

		String name = modifyCompNameTextField.getText();

		if (!name.equals(toBeModifiedComponentLabel.getText())) {
			toBeModifiedComponentLabel.setText(name);
			component.name = name;
		}

		Object state = modifyCompStateCombBox.getValue();
		toBeModifiedComponentLabel.changeStateTo(state);

		// int inputPorts = (int) modifyCompInputPorts.getValue();
		component.inputPorts = new Component[possibleInputs];

		int i = 0;
		for (Component comp : modifiedInputPorts) {
			component.inputPorts[i++] = comp;
		}

		// int outputPorts = (int) modifyCompOutputPorts.getValue();
		component.outputPorts = new Component[possibleOutputs];

		i = 0;
		for (Component comp : modifiedOutputPorts) {
			component.outputPorts[i++] = comp;
		}
		
		component.isTopLevelComponent = modifyCompAddToTopEventCheckBox.isSelected();

		System.out.println(Arrays.toString(component.inputPorts));
		System.out.println(Arrays.toString(component.outputPorts));

		return component;
	}

	public void initialize() {
		setPortsActions();
		portsVBox.setStyle("-fx-spacing: 15;");
		portsVBox.setAlignment(Pos.CENTER_LEFT);
	}

}
