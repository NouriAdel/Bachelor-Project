package system;

import java.io.IOException;
import java.util.ArrayList;

import components.Component;
import components.Valve.*;
import components.Pipe.*;
import components.BSCU.*;
import components.Pump.*;
import components.ControlRod.*;
import components.Engine.*;
import components.PowerSupply.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExampleECS extends ExampleSystem {
	// defining the Components

	static Component highPressurePump = new Pump("High Pressure Pump", 0, 1);
	static Component meterInlet = new Pipe("Meter Inlet Pipe", 1, 2);
	
	static Component differentialPressureValve = new Valve("Differential Pressure Valve", 1, 0);
	
	static Component ECU = new BSCU("ECU", 1, 2);
	static Component ECUPower = new PowerSupply("ECU Power", 0, 1);
	
	static Component controlRod = new ControlRod("Control Rod", 0, 2);
	
	static Component meterValve = new Valve("Meter Valve", 3, 1);
	static Component meterOutlet = new Pipe("Meter Outlet Pipe", 1, 1);
	
	
	static Component fuelShutoffValve = new Valve("Fuel Shutoff Valve", 3, 1);
	static Component engineInlet = new Pipe("Engine Inlet Pipe", 1, 1);
	
	static Component engine = new Engine("Engine", 1, 0);
	
	static Component[] systemComponents = { 
			highPressurePump,
			meterInlet,
			differentialPressureValve,
			ECU,
			ECUPower,
			controlRod,
			meterValve,
			meterOutlet,
			fuelShutoffValve,
			engineInlet,
			engine,
	};

	@Override
	public void start(Stage stage) throws IOException {

		// Defining a model

		highPressurePump.outputPorts[0] = meterInlet;
		meterInlet.inputPorts[0] = highPressurePump;

		meterInlet.outputPorts[0] = meterValve;
		meterValve.inputPorts[0] = meterInlet;
		
		meterInlet.outputPorts[1] = differentialPressureValve;
		differentialPressureValve.inputPorts[0] = meterInlet;
		
		meterValve.inputPorts[1] = ECU;
		ECU.outputPorts[0] = meterValve;
		
		ECU.inputPorts[0] = ECUPower;
		ECUPower.outputPorts[0] = ECU;
		
		meterValve.inputPorts[2] = controlRod;
		controlRod.outputPorts[0] = meterValve;
		
		meterValve.outputPorts[0] = meterOutlet;
		meterOutlet.inputPorts[0] = meterValve;
		
		meterOutlet.outputPorts[0] = fuelShutoffValve;
		fuelShutoffValve.inputPorts[0] = meterOutlet;
		
		fuelShutoffValve.inputPorts[1] = ECU;
		ECU.outputPorts[1] = fuelShutoffValve;
		
		fuelShutoffValve.inputPorts[2] = controlRod;
		controlRod.outputPorts[1] = fuelShutoffValve;
		
		fuelShutoffValve.outputPorts[0] = engineInlet;
		engineInlet.inputPorts[0] = fuelShutoffValve;
		
		engine.inputPorts[0] = engineInlet;
		engineInlet.outputPorts[0] = engine;

		
		engine.isTopLevelComponent = true;

		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);

		String topEventText = "";

		for (Component component : systemComponents) {
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

	public static void main(String[] args) {
		 launch(args);
	
		// Defining a model
	
//		highPressurePump.outputPorts[0] = meterInlet;
//		meterInlet.inputPorts[0] = highPressurePump;
//	
//		meterInlet.outputPorts[0] = meterValve;
//		meterValve.inputPorts[0] = meterInlet;
//		
//		meterInlet.outputPorts[1] = differentialPressureValve;
//		differentialPressureValve.inputPorts[0] = meterInlet;
//		
//		meterValve.inputPorts[1] = ECU;
//		ECU.outputPorts[0] = meterValve;
//		
//		ECU.inputPorts[0] = ECUPower;
//		ECUPower.outputPorts[0] = ECU;
//		
//		meterValve.inputPorts[2] = controlRod;
//		controlRod.outputPorts[0] = meterValve;
//		
//		meterValve.outputPorts[0] = meterOutlet;
//		meterOutlet.inputPorts[0] = meterValve;
//		
//		meterOutlet.outputPorts[0] = fuelShutoffValve;
//		fuelShutoffValve.inputPorts[0] = meterOutlet;
//		
//		fuelShutoffValve.inputPorts[1] = ECU;
//		ECU.outputPorts[1] = fuelShutoffValve;
//		
//		fuelShutoffValve.inputPorts[2] = controlRod;
//		controlRod.outputPorts[1] = fuelShutoffValve;
//		
//		fuelShutoffValve.outputPorts[0] = engineInlet;
//		engineInlet.inputPorts[0] = fuelShutoffValve;
//		
//		engine.inputPorts[0] = engineInlet;
//		engineInlet.outputPorts[0] = engine;
//	
//		
//		engine.isTopLevelComponent = true;
//	
//		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
//	
//		String topEventText = "";
//	
//		for (Component component : systemComponents) {
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
	}

}
