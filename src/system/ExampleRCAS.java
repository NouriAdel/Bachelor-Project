package system;

import java.io.IOException;
import java.util.ArrayList;

import components.Component;
import components.ActuatorControlElectronics.ActuatorControlElectronics;
import components.AirDataAndInertialReferenceUnit.AirDataAndInertialReferenceUnit;
import components.Cylinder.Cylinder;
import components.DirectElectricalLink.DirectElectricalLink;
import components.Pipe.Pipe;
import components.PowerSupply.PowerSupply;
import components.PrimaryFlightControl.PrimaryFlightControl;
import components.Pump.Pump;
import components.Valve.Valve;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExampleRCAS extends ExampleSystem {
	
	// defining the Components
	
	static Component mainPump  = new Pump("Main Pump", 0, 1);
	static Component mainValve = new Valve("Main Valve", 2, 1);
	static Component pipe1     = new Pipe("Pipe 1", 1, 1);
	static Component pipe2     = new Pipe("Pipe 2", 1, 1);
	static Component cylinder  = new Cylinder("Cylinder", 1, 0);
	
	static Component ACE  = new ActuatorControlElectronics("ACE", 3, 1);
	static Component ACEpower  = new PowerSupply("ACEPower", 0, 1);	
	
	static Component PFC  = new PrimaryFlightControl("PFC", 2, 1);
	static Component PFCpower  = new PowerSupply("PFCPower", 0, 1);	
	
	static Component ADIRU  = new AirDataAndInertialReferenceUnit("ADIRU", 1, 1);
	static Component ADIRUpower  = new PowerSupply("ADIRUPower", 0, 1);	
	
	static Component DEL = new DirectElectricalLink("DEL", 0, 1);
	
	static Component [] systemComponents = {
			mainPump, mainValve, pipe1, pipe2, cylinder,
			ACE, ACEpower, PFC, PFCpower, ADIRU, ADIRUpower, DEL,
	};
	
	@Override
	public void start(Stage stage) throws IOException {

		// Defining a model
		
		mainPump.outputPorts[0] = pipe1;
		pipe1.inputPorts[0]  = mainPump;
		
		pipe1.outputPorts[0] = mainValve;
		mainValve.inputPorts[0]  = pipe1;
		
		mainValve.outputPorts[0] = pipe2;
		pipe2.inputPorts[0] = mainValve;
		
		pipe2.outputPorts[0] = cylinder;
		cylinder.inputPorts[0] = pipe2;
		
		mainValve.inputPorts[1]  = ACE;
		ACE.outputPorts[0] = mainValve;
		
		ACE.inputPorts[0]  = ACEpower;
		ACE.inputPorts[1]  = DEL;
		ACE.inputPorts[2]  = PFC;
		
		ACEpower.outputPorts[0] = ACE;
		DEL.outputPorts[0] = ACE;
		PFC.outputPorts[0] = ACE;
		
		PFC.inputPorts[0]  = PFCpower;
		PFC.inputPorts[1]  = ADIRU;
		
		PFCpower.outputPorts[0] = PFC;
		ADIRU.outputPorts[0] = PFC;
		
		ADIRU.inputPorts[0]  = ADIRUpower;
		ADIRUpower.outputPorts[0] = ADIRU;
				
		cylinder.isTopLevelComponent = true;

		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
		 
		String topEventText = "";
			
		for (Component component: systemComponents) {
			if (component.isTopLevelComponent) {
				if (topEventText.length() > 0)
					topEventText += " and " + component.name;
				else 
					topEventText += "Top Event: " + component.name;
			}
		}
		
		int i = 1;

		for (TreeEvent event : eventList) {
			System.out.println(i++ + ". " + event);
			System.out.println("");
		}

		FXMLLoader fxmlLoader = new FXMLLoader();

		GUIControllerExampleFiles controller = new GUIControllerExampleFiles(eventList, topEventText);
		
		fxmlLoader.setController(controller);
		fxmlLoader.setLocation(getClass().getResource("Views/SimpleSystemIndex.fxml"));
		Parent root = fxmlLoader.load();

		// Creating a scene object
		Scene scene = new Scene(root, 1200, 1000);

		// Setting title to the Stage
		stage.setTitle("Safety Analysis");

		// Adding scene to the stage
		stage.setScene(scene);

		// Displaying the contents of the stage
		stage.show();

	}
	
	public static void main(String [] args) {
		launch(args);
		
		// Defining a model
		
//				mainPump.outputPorts[0] = pipe1;
//				pipe1.inputPorts[0]  = mainPump;
//				
//				pipe1.outputPorts[0] = mainValve;
//				mainValve.inputPorts[0]  = pipe1;
//				
//				mainValve.outputPorts[0] = pipe2;
//				pipe2.inputPorts[0] = mainValve;
//				
//				pipe2.outputPorts[0] = cylinder;
//				cylinder.inputPorts[0] = pipe2;
//				
//				mainValve.inputPorts[1]  = ACE;
//				ACE.outputPorts[0] = mainValve;
//				
//				ACE.inputPorts[0]  = ACEpower;
//				ACE.inputPorts[1]  = DEL;
//				ACE.inputPorts[2]  = PFC;
//				
//				ACEpower.outputPorts[0] = ACE;
//				DEL.outputPorts[0] = ACE;
//				PFC.outputPorts[0] = ACE;
//				
//				PFC.inputPorts[0]  = PFCpower;
//				PFC.inputPorts[1]  = ADIRU;
//				
//				PFCpower.outputPorts[0] = PFC;
//				ADIRU.outputPorts[0] = PFC;
//				
//				ADIRU.inputPorts[0]  = ADIRUpower;
//				ADIRUpower.outputPorts[0] = ADIRU;
//						
//				cylinder.isTopLevelComponent = true;
//
//				ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
//				 
//				String topEventText = "";
//					
//				for (Component component: systemComponents) {
//					if (component.isTopLevelComponent) {
//						if (topEventText.length() > 0)
//							topEventText += " and " + component.name;
//						else 
//							topEventText += "Top Event: " + component.name;
//					}
//				}
//				
//				int i = 1;
//
//				for (TreeEvent event : eventList) {
//					System.out.println(i++ + ". " + event);
//					System.out.println("");
//				}
	}

}
