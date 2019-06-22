package system.Controllers;

import java.util.ArrayList;

import components.Component;
import components.ComponentState;
import components.ComponentType;
import components.CustomizedComponent;
import components.CustomizedState;
import components.BSCU.BSCUState;
import components.BSCU.BSCUStates;
import components.Pipe.*;
import components.Pump.PumpState;
import components.Pump.PumpStates;
import components.Valve.ValveState;
import components.Valve.ValveStates;
import failures.Failure;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import system.ComponentLabel;

public class CreateComponentController implements EventHandler<ActionEvent> {
	
	@FXML
	public Pane createCompPane;
	
	@FXML
	public ComboBox<ComponentType> createCompCombBox;
	
	@FXML
	public Button createCompCancelBtn;
	
	@FXML
	public Button createCompCreateBtn;
	
	@FXML
	public TextField createCompNameTextField;
	
	@FXML
	public Spinner<Integer> createCompInputPorts;
	
	@FXML
	public Spinner<Integer> createCompOutputPorts;
	
	@FXML
	public ComboBox<Object> createCompStateCombBox;
	
	@FXML
	public TextField createStateNameTextField;
	
	@FXML
	public Button createStateCreateBtn;
	
	@FXML
	public ComboBox<ComponentType> failedComp;
	
	@FXML
	public ComboBox<Object> CustomizedBehavior;
	
	@FXML
	public Button failureState;
	
	@FXML
	public ComboBox<String> direction;
	
	@FXML
	public ComboBox<Object> CompBehavior;
	
	@FXML
	public CheckBox WhereInjected;
	
	@FXML 
	public CheckBox IsControl;
	
	@FXML
	public Button removeStateBtn;
	
	@FXML
	public CheckBox ChangeExisting;
	
	public boolean added = false;
	
	public ArrayList<ComponentState> allStates ;
	public ArrayList<CustomizedState> allCustomizedStates ;
	public ArrayList<ComponentType> allTypes ;
	public ArrayList<Boolean> ifCustomizedInjected ;
	public ArrayList<Integer> allDirections;
	
	
	public void initializeTypesInComboBox() {
		createCompCombBox.getItems().addAll(ComponentType.PIPE, ComponentType.VALVE, ComponentType.PUMP, ComponentType.BSCU , 	ComponentType.CUSTOMIZED_COMPONENT );
	}

	public void setVisible(boolean b) {
		createCompPane.setVisible(b);	
	}

//	public void setButtonsActions(ActionEvent e) {
//		// TODO Auto-generated method stub
//		if (createCompCombBox.getSelectionModel().getSelectedItem()==ComponentType.CUSTOMIZED_COMPONENT){
//			String newState = createStateNameTextField.getText();
//			createCompStateCombBox.getItems().add(newState);
//		}
//	}
	
	public ComponentLabel onCreateComponent() {
		// TODO Auto-generated method stub
		String name = createCompNameTextField.getText();
		ComponentType type = (ComponentType) createCompCombBox.getValue();
		Object state = createCompStateCombBox.getSelectionModel().getSelectedItem();
		int inputPorts = (int) createCompInputPorts.getValue();
		int outputPorts = (int) createCompOutputPorts.getValue();
		
		ComponentLabel compLabel = new ComponentLabel(name, type, inputPorts, outputPorts, state);
		compLabel.component.label = compLabel; // lol
		
		System.out.println(compLabel.component.state);
		
		if(type == ComponentType.CUSTOMIZED_COMPONENT){
			CustomizedComponent custom = (CustomizedComponent)compLabel.component;
			custom.allStates = allStates;
			custom.allCustomizedStates = allCustomizedStates;
			custom.allDirections = allDirections;
			custom.allTypes = allTypes;
			custom.ifCustomizedInjected = ifCustomizedInjected;
			
			if (IsControl.isSelected()){
				compLabel.component.isControl = true;
			}
		}

		return compLabel;
	}
	
	public void initialize() {
		initializeTypesInComboBox();
		createStateCreateBtn.setOnAction(this);
		//if (createCompCombBox.getSelectionModel().getSelectedItem()==ComponentType.CUSTOMIZED_COMPONENT){
		//	failedComp.getItems().addAll(ComponentType.PIPE, ComponentType.VALVE, ComponentType.PUMP, ComponentType.BSCU , 	ComponentType.CUSTOMIZED_COMPONENT );
		//	direction.getItems().addAll("Before","After");

		//}
		
		removeStateBtn.setOnAction(this);
		
		/*removeStateBtn.setOnAction((event) -> {
			ComponentType type = (ComponentType) createCompCombBox.getSelectionModel().getSelectedItem();
			String newState = createStateNameTextField.getText();
			
			ArrayList<Object>states = (ArrayList<Object>) createCompStateCombBox.getItems();
			for(Object s : states){
				if ((Object)newState==s){
					states.remove(s);
					break;
				}
			}
			//createCompStateCombBox.getItems().remove((Object)newState);
			Object [] states=null;
			Object tobeDisplayed=null;
			
			switch(type) {
			case VALVE:
				states = new Object [] {ValveStates.NOMINAL, ValveStates.STUCK, ValveStates.LEAK};
				tobeDisplayed = ValveStates.NOMINAL;
				break;
			case PIPE:
				states = new Object [] {PipeStates.NOMINAL, PipeStates.BLOCKED, PipeStates.LEAK};
				tobeDisplayed = PipeStates.NOMINAL;
				break;
			case BSCU:
				states = new Object [] {BSCUStates.NOMINAL, BSCUStates.LOSS_OF_CONTROL_SIGNAL};
				tobeDisplayed = BSCUStates.NOMINAL;
				break;
			case PUMP: 
				states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
				tobeDisplayed = PumpStates.NOMINAL;
				break;
//			default: // cutomized
//				states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
//				tobeDisplayed = PumpStates.NOMINAL;
//				break;
				
			}
			
			createCompStateCombBox.getItems().clear();
			
			for (Object state: states) {
				createCompStateCombBox.getItems().add(state);
			}
			createCompStateCombBox.setValue(tobeDisplayed);
		});*/

		
		
		createCompCombBox.setOnAction((event) -> {
			ComponentType type = (ComponentType) createCompCombBox.getSelectionModel().getSelectedItem();
			Object [] states;
			Object tobeDisplayed;
			
			switch(type) {
			case VALVE:
				states = new Object [] {ValveStates.NOMINAL, ValveStates.STUCK, ValveStates.LEAK};
				tobeDisplayed = ValveStates.NOMINAL;
				break;
			case PIPE:
				states = new Object [] {PipeStates.NOMINAL, PipeStates.BLOCKED, PipeStates.LEAK};
				tobeDisplayed = PipeStates.NOMINAL;
				break;
			case BSCU:
				states = new Object [] {BSCUStates.NOMINAL, BSCUStates.LOSS_OF_CONTROL_SIGNAL};
				tobeDisplayed = BSCUStates.NOMINAL;
				break;
			//case PUMP: 
			default:
				states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
				tobeDisplayed = PumpStates.NOMINAL;
				break;
//			default: // cutomized
//				states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
//				tobeDisplayed = PumpStates.NOMINAL;
//				break;
				
			}
			
			createCompStateCombBox.getItems().clear();
			
			//if (states!=null){
				for (Object state: states) {
					createCompStateCombBox.getItems().add(state);
				}
			//}//HHHHHH
			
			createCompStateCombBox.setValue(tobeDisplayed);
		});
		
		failureState.setOnAction(event -> writeProcessFailure());
		
		failedComp.setOnAction((event) -> {
			ComponentType type = (ComponentType) failedComp.getSelectionModel().getSelectedItem();
			Object [] states=null;
//			ArrayList<Object> states=null;
			
			switch(type) {
			case VALVE:
				states = new Object [] { ValveStates.STUCK, ValveStates.LEAK};
				break;
			case PIPE:
				states = new Object [] { PipeStates.BLOCKED, PipeStates.LEAK};
				break;
			case BSCU:
				states = new Object [] { BSCUStates.LOSS_OF_CONTROL_SIGNAL};
				break;
			//case PUMP: 
			default:
				states = new Object [] {PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
				break;
			//default: // cutomized
				//states = (ArrayList<Object>) createCompStateCombBox.getItems();
				//break;
				
			}
			
			CompBehavior.getItems().clear();
			
			for (Object state: states) {
				CompBehavior.getItems().add(state);
			}
		});
		
	}
	
	public void writeProcessFailure(){
		//System.out.println("hello");
		
		int dir;
		Component comp;
		ComponentState compState;
		String custState;
		boolean custInjected;
		Failure failure;
		String directionOfCustomized;
		//CustomizedComponent custComp = 
		
		/*if (direction.getSelectionModel().getSelectedItem().equals("Before")){ 
			//y3ny el customized fo2 w t7tha el component el tnya
			if (WhereInjected.isSelected()){
				//y3ny b-inject el failure fl customized component 
				dir = 1;
			}
			else {
				dir = -1;
			}
		}
		else {
			if (WhereInjected.isSelected()){
				//y3ny b-inject el failure fl customized component 
				dir = -1;
			}
			else {
				dir = 1;
			}
		}*/
		//comp = failedComp.getSelectionModel().getSelectedItem();
		/*compState = (ComponentState)CompBehavior.getSelectionModel().getSelectedItem();
		custState = (String)CustomizedBehavior.getSelectionModel().getSelectedItem();*/
		custInjected = WhereInjected.isSelected();
		
		//ComponentLabel complabel = onCreateComponent();
		//complabel.component.processFailure(failure, dir);
		
		ComponentType type = failedComp.getSelectionModel().getSelectedItem();
		//ComponentState state = (ComponentState) CompBehavior.getSelectionModel().getSelectedItem();
		ComponentState state = null;
		if(type==ComponentType.PUMP){
			state = (PumpState)CompBehavior.getSelectionModel().getSelectedItem(); 
		}
		else if (type==ComponentType.PIPE){
			state = (PipeState)CompBehavior.getSelectionModel().getSelectedItem(); 
		}
		else if(type==ComponentType.VALVE){
			state = (ValveState)CompBehavior.getSelectionModel().getSelectedItem(); 
		}
		else { //BSCU
			state = (BSCUState)CompBehavior.getSelectionModel().getSelectedItem(); 
		}
		CustomizedState customizedState = (CustomizedState) CustomizedBehavior.getSelectionModel().getSelectedItem();
		directionOfCustomized = direction.getSelectionModel().getSelectedItem();
		if (directionOfCustomized.equals("Before"))
			allDirections.add(1);
		else
			allDirections.add(-1);
		allTypes.add(type);
		allStates.add(state);
		allCustomizedStates.add(customizedState);
		if (WhereInjected.isSelected())
			ifCustomizedInjected.add(true);
		else
			ifCustomizedInjected.add(false);
		
		System.out.println(allTypes+" "+allStates);
		
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if ( arg0.getSource()==createStateCreateBtn){
			
			if (createCompCombBox.getSelectionModel().getSelectedItem()==ComponentType.CUSTOMIZED_COMPONENT){
				String newState = createStateNameTextField.getText();
				createCompStateCombBox.getItems().add(newState);
				
				if (added==false){
					failedComp.getItems().addAll(ComponentType.PIPE, ComponentType.VALVE, ComponentType.PUMP, ComponentType.BSCU  );
					direction.getItems().addAll("Before","After");
					added = true;
					
				}
				/*failedComp.setOnAction((event) -> {
					ComponentType type = (ComponentType) failedComp.getSelectionModel().getSelectedItem();
					Object [] states=null;
//					ArrayList<Object> states=null;
					
					switch(type) {
					case VALVE:
						states = new Object [] { ValveStates.STUCK, ValveStates.LEAK};
						break;
					case PIPE:
						states = new Object [] { PipeStates.BLOCKED, PipeStates.LEAK};
						break;
					case BSCU:
						states = new Object [] { BSCUStates.LOSS_OF_CONTROL_SIGNAL};
						break;
					//case PUMP: 
					default:
						states = new Object [] {PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
						break;
					//default: // cutomized
						//states = (ArrayList<Object>) createCompStateCombBox.getItems();
						//break;
						
					}
					
					CompBehavior.getItems().clear();
					
					for (Object state: states) {
						CompBehavior.getItems().add(state);
					}
				});*/
				
				CustomizedBehavior.getItems().add(newState);
				
				/*ComponentType type = (ComponentType) failedComp.getSelectionModel().getSelectedItem();
				Object [] states=null;
				
				switch(type) {
				case VALVE:
					states = new Object [] { ValveStates.STUCK, ValveStates.LEAK};
					break;
				case PIPE:
					states = new Object [] { PipeStates.BLOCKED, PipeStates.LEAK};
					break;
				case BSCU:
					states = new Object [] { BSCUStates.LOSS_OF_CONTROL_SIGNAL};
					break;
				case PUMP: 
					states = new Object [] {PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
					break;
//				default: // cutomized
//					states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
//					tobeDisplayed = PumpStates.NOMINAL;
//					break;
					
				}
				
				CompBehavior.getItems().clear(); 
				
				for (Object state: states) {
					CompBehavior.getItems().add(state);
				}*/
			}
			
			if (createCompCombBox.getSelectionModel().getSelectedItem()!=ComponentType.CUSTOMIZED_COMPONENT && ChangeExisting.isSelected()){
				String newState = createStateNameTextField.getText();
				createCompStateCombBox.getItems().add((Object)newState);
			}
			
			
		}
		else if (arg0.getSource()==removeStateBtn){
			String newState = createStateNameTextField.getText();
			createCompStateCombBox.getItems().remove((Object)newState);
			ComponentType type = (ComponentType) createCompCombBox.getSelectionModel().getSelectedItem();
			Object [] states=null;
			if (ChangeExisting.isSelected()){
				
				switch(type) {
				case VALVE:
					if (newState.equals("STUCK")){
						states = new Object [] {ValveStates.NOMINAL, ValveStates.LEAK};
					}
					else if (newState.equals("LEAK")){
						states = new Object [] {ValveStates.NOMINAL, ValveStates.STUCK};
					}
					else
					states = new Object [] { ValveStates.STUCK, ValveStates.LEAK};
					
					break;
				case PIPE:
					if (newState.equals("BLOCKED")){
						states = new Object [] {PipeStates.NOMINAL, PipeStates.LEAK};
					}
					else if (newState.equals("LEAK")){
						states = new Object [] {PipeStates.NOMINAL, PipeStates.BLOCKED};
					}
					else
					states = new Object [] { PipeStates.BLOCKED, PipeStates.LEAK};
					
					break;
				case BSCU:
					if (newState.equals("LOSS_OF_CONTROL_SIGNAL")){
						states = new Object [] {BSCUStates.NOMINAL};
					}
					else
					states = new Object [] {BSCUStates.LOSS_OF_CONTROL_SIGNAL};
					
					break;
				//case PUMP: 
				default : //pump
					if (newState.equals("OVER_PRESSURED")){
						states = new Object [] {PumpStates.NOMINAL, PumpStates.NO_PRESSURE};
					}
					else if (newState.equals("NO_PRESSURE")){
						states = new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED};
					}
					else
					states = new Object [] { PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
					
					break;
				
				}
				
				createCompStateCombBox.getItems().clear();
				
				for (Object state: states) {
					createCompStateCombBox.getItems().add(state);
				}
				
			}
			else {
				createCompStateCombBox.getItems().remove(newState);
			}
			
		
			
		}
		

	}

	/*public void setAllStates(ArrayList<ComponentState> allStates) {
		this.allStates = allStates;
	}

	public void setAllCustomizedStates(ArrayList<CustomizedState> allCustomizedStates) {
		this.allCustomizedStates = allCustomizedStates;
	}

	public void setAllTypes(ArrayList<ComponentType> allTypes) {
		this.allTypes = allTypes;
	}

	public void setIfCustomizedInjected(ArrayList<Boolean> ifCustomizedInjected) {
		this.ifCustomizedInjected = ifCustomizedInjected;
	}

	public void setAllDirections(ArrayList<Integer> allDirections) {
		this.allDirections = allDirections;
	}*/

	/*public static ArrayList<ComponentState> getAllStates() {
		return allStates;
	}

	public static ArrayList<CustomizedState> getAllCustomizedStates() {
		return allCustomizedStates;
	}

	public static ArrayList<ComponentType> getAllTypes() {
		return allTypes;
	}

	public static ArrayList<Boolean> getIfCustomizedInjected() {
		return ifCustomizedInjected;
	}

	public static ArrayList<Integer> getAllDirections() {
		return allDirections;
	}*/
}
