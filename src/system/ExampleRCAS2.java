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
import components.Sensor.Sensor;
import components.Valve.Valve;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExampleRCAS2 extends ExampleSystem2 {
	
	// defining the Components
	
	static Component mainPump  = new Pump("Main Pump 1", 0, 1);
	static Component mainValve = new Valve("Main Valve 1", 2, 1);
	static Component pipeA1     = new Pipe("Pipe A1", 1, 1);
	static Component pipeA2     = new Pipe("Pipe A2", 1, 1);
	static Component cylinder  = new Cylinder("Cylinder 1", 1, 1);
	
	static Component ACE  = new ActuatorControlElectronics("ACE 1", 4, 1);
	static Component ACEpower  = new PowerSupply("ACEPower 1", 0, 1);	
	
	static Component PFC  = new PrimaryFlightControl("PFC 1", 3, 2);
	static Component PFCpower  = new PowerSupply("PFCPower 1", 0, 1);	
	
	static Component ADIRU  = new AirDataAndInertialReferenceUnit("ADIRU 1", 1, 2);
	static Component ADIRUpower  = new PowerSupply("ADIRUPower 1", 0, 1);	
	
	static Component DEL = new DirectElectricalLink("DEL1", 0, 1);
	
	//////////////////////////////////////
	
	static Component mainPump2  = new Pump("Main Pump 2", 0, 1);
	static Component mainValve2 = new Valve("Main Valve 2", 2, 1);
	static Component pipeB1     = new Pipe("Pipe B1", 1, 1);
	static Component pipeB2     = new Pipe("Pipe B2", 1, 1);
	static Component cylinder2  = new Cylinder("Cylinder 2", 1, 1);
	
	static Component ACE2  = new ActuatorControlElectronics("ACE 2", 4, 1);
	static Component ACEpower2  = new PowerSupply("ACEPower 2", 0, 1);	
	
	static Component PFC2  = new PrimaryFlightControl("PFC 2", 3, 2);
	static Component PFCpower2  = new PowerSupply("PFCPower 2", 0, 1);	
	
	static Component ADIRU2  = new AirDataAndInertialReferenceUnit("ADIRU 2", 1, 2);
	static Component ADIRUpower2  = new PowerSupply("ADIRUPower 2", 0, 1);	
	
	static Component DEL2 = new DirectElectricalLink("DEL2", 0, 1);
	
	static Component sensor = new Sensor("Position sensor 1", 1, 1);
	
	static Component sensor2 = new Sensor("Position sensor 2", 1, 1);
	
	
	
	static Component [] systemComponents = {
			mainPump, mainValve, pipeA1, pipeA2, cylinder,
			ACE, ACEpower, PFC, PFCpower, ADIRU, ADIRUpower, DEL,
			mainPump2, mainValve2, pipeB1, pipeB2, cylinder2,
			ACE2, ACEpower2, PFC2, PFCpower2, ADIRU2, ADIRUpower2, DEL2, 
			sensor,
	};
	
	
	
	
	@Override
	public void start(Stage stage) throws IOException {

		// Defining a model
		
		mainPump.outputPorts[0] = pipeA1;
		pipeA1.inputPorts[0]  = mainPump;
		
		pipeA1.outputPorts[0] = mainValve;
		mainValve.inputPorts[0]  = pipeA1;
		
		mainValve.outputPorts[0] = pipeA2;
		pipeA2.inputPorts[0] = mainValve;
		
		pipeA2.outputPorts[0] = cylinder;
		cylinder.inputPorts[0] = pipeA2;
		
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
		
		
		/////////////////////////////////
		
		
		mainPump2.outputPorts[0] = pipeB1;
		pipeB1.inputPorts[0]  = mainPump2;
		
		pipeB1.outputPorts[0] = mainValve2;
		mainValve2.inputPorts[0]  = pipeB1;
		
		mainValve2.outputPorts[0] = pipeB2;
		pipeB2.inputPorts[0] = mainValve2;
		
		pipeB2.outputPorts[0] = cylinder2;
		cylinder2.inputPorts[0] = pipeB2;
		
		mainValve2.inputPorts[1]  = ACE2;
		ACE2.outputPorts[0] = mainValve2;
		
		ACE2.inputPorts[0]  = ACEpower2;
		ACE2.inputPorts[1]  = DEL2;
		ACE2.inputPorts[2]  = PFC2;
		
		ACEpower2.outputPorts[0] = ACE2;
		DEL2.outputPorts[0] = ACE2;
		PFC2.outputPorts[0] = ACE2;
		
		PFC2.inputPorts[0]  = PFCpower2;
		PFC2.inputPorts[1]  = ADIRU2;
		
		PFCpower2.outputPorts[0] = PFC2;
		ADIRU2.outputPorts[0] = PFC2;
		
		ADIRU2.inputPorts[0]  = ADIRUpower2;
		ADIRUpower2.outputPorts[0] = ADIRU2;
		
		/////////////////////////////////////
		ACE.inputPorts[3] = PFC2;
		PFC2.outputPorts[1] = ACE;
		
		ACE2.inputPorts[3] = PFC;
		PFC.outputPorts[1] = ACE2;
		
		PFC.inputPorts[2] = ADIRU2;
		ADIRU2.outputPorts[1] = PFC;
		
		PFC2.inputPorts[2] = ADIRU;
		ADIRU.outputPorts[1] = PFC2;
		
		sensor.inputPorts[0] = cylinder;
		cylinder.outputPorts[0] = sensor;
		
		sensor2.inputPorts[0] = cylinder2;
		cylinder2.outputPorts[0] = sensor2;
		
		/////////////////////////////////////
				
		cylinder.isTopLevelComponent = true;
		cylinder2.isTopLevelComponent = true;

//
//		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
//		 
//		String topEventText = "";
//			
//		for (Component component: systemComponents) {
//			if (component.isTopLevelComponent) {
//				if (topEventText.length() > 0)
//					topEventText += " and " + component.name;
//				else 
//					topEventText += "Top Event: " + component.name;
//			}
//		}
//		
//		int i = 1;
//
//		for (TreeEvent event : eventList) {
//			System.out.println(i++ + ". " + event);
//			System.out.println("");
//		}
//
//		FXMLLoader fxmlLoader = new FXMLLoader();
//
//		GUIControllerExampleFiles controller = new GUIControllerExampleFiles(eventList, topEventText);
//		
//		fxmlLoader.setController(controller);
//		fxmlLoader.setLocation(getClass().getResource("Views/SimpleSystemIndex.fxml"));
//		Parent root = fxmlLoader.load();
//
//		// Creating a scene object
//		Scene scene = new Scene(root, 1200, 1000);
//
//		// Setting title to the Stage
//		stage.setTitle("Safety Analysis");
//
//		// Adding scene to the stage
//		stage.setScene(scene);
//
//		// Displaying the contents of the stage
//		stage.show();

	}
	
	public static void main(String [] args) {
//		launch(args);
		
		// Defining a model
		
		mainPump.outputPorts[0] = pipeA1;
		pipeA1.inputPorts[0]  = mainPump;
		
		pipeA1.outputPorts[0] = mainValve;
		mainValve.inputPorts[0]  = pipeA1;
		
		mainValve.outputPorts[0] = pipeA2;
		pipeA2.inputPorts[0] = mainValve;
		
		pipeA2.outputPorts[0] = cylinder;
		cylinder.inputPorts[0] = pipeA2;
		
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
		
		
		/////////////////////////////////
		
		
		mainPump2.outputPorts[0] = pipeB1;
		pipeB1.inputPorts[0]  = mainPump2;
		
		pipeB1.outputPorts[0] = mainValve2;
		mainValve2.inputPorts[0]  = pipeB1;
		
		mainValve2.outputPorts[0] = pipeB2;
		pipeB2.inputPorts[0] = mainValve2;
		
		pipeB2.outputPorts[0] = cylinder2;
		cylinder2.inputPorts[0] = pipeB2;
		
		mainValve2.inputPorts[1]  = ACE2;
		ACE2.outputPorts[0] = mainValve2;
		
		ACE2.inputPorts[0]  = ACEpower2;
		ACE2.inputPorts[1]  = DEL2;
		ACE2.inputPorts[2]  = PFC2;
		
		ACEpower2.outputPorts[0] = ACE2;
		DEL2.outputPorts[0] = ACE2;
		PFC2.outputPorts[0] = ACE2;
		
		PFC2.inputPorts[0]  = PFCpower2;
		PFC2.inputPorts[1]  = ADIRU2;
		
		PFCpower2.outputPorts[0] = PFC2;
		ADIRU2.outputPorts[0] = PFC2;
		
		ADIRU2.inputPorts[0]  = ADIRUpower2;
		ADIRUpower2.outputPorts[0] = ADIRU2;
		
		/////////////////////////////////////
		ACE.inputPorts[3] = PFC2;
		PFC2.outputPorts[1] = ACE;
		
		ACE2.inputPorts[3] = PFC;
		PFC.outputPorts[1] = ACE2;
		
		PFC.inputPorts[2] = ADIRU2;
		ADIRU2.outputPorts[1] = PFC;
		
		PFC2.inputPorts[2] = ADIRU;
		ADIRU.outputPorts[1] = PFC2;
		
		sensor.inputPorts[0] = cylinder;
		cylinder.outputPorts[0] = sensor;
		
		sensor2.inputPorts[0] = cylinder2;
		cylinder2.outputPorts[0] = sensor2;
		
		/////////////////////////////////////
				
		cylinder.isTopLevelComponent = true;
		cylinder2.isTopLevelComponent = true;

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
	}

}
