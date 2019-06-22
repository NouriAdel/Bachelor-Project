package system;

import java.io.IOException;
import java.util.ArrayList;

import components.Component;
import components.BSCU.BSCU;
import components.ControlRod.ControlRod;
import components.Engine.Engine;
import components.Pipe.Pipe;
import components.PowerSupply.PowerSupply;
import components.Pump.Pump;
import components.Valve.Valve;
import failures.FailureCombination;
import failures.FailureType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExampleFMS extends ExampleSystem {
	// defining the Components

	static Component boosterPumpForward = new Pump("Booster Pump Forward", 1, 1);
	static Component BPFPipe1 = new Pipe("Booster Forward Pipe 1", 1, 2);
	static Component BPFPipe2 = new Pipe("Booster Forward Pipe 2", 1, 1);
	static Component BPFPipe3 = new Pipe("Booster Forward Pipe 3", 2, 0);
	
	static Component rightTransferPump = new Pump("Right Transfer Pump", 0, 1);
	static Component FTPipe1 = new Pipe("Forward Transfer Pipe 1", 1, 1);
	
	static Component forwardTransferValve = new Valve("Forward Transfer Valve", 1, 1);
	static Component FTPipe2 = new Pipe("Forward Transfer Pipe 2", 1, 1);
	
	static Component forwardTransferPump = new Pump("Forward Transfer Pump", 1, 1);
	static Component FTPipe3 = new Pipe("Forward Transfer Pipe 3", 1, 1);
	
	
	static Component boosterPumpRear = new Pump("Booster Pump Rear", 1, 1);
	static Component BPRPipe1 = new Pipe("Booster Rear Pipe 1", 1, 2);
	static Component BPRPipe2 = new Pipe("Booster Rear Pipe 2", 1, 1);
	static Component BPRPipe3 = new Pipe("Booster Rear Pipe 3", 2, 0);
	
	static Component leftTransferPump = new Pump("Left Transfer Pump", 0, 1);
	static Component RTPipe1 = new Pipe("Rear Transfer Pipe 1", 1, 1);
	
	static Component rearTransferValve = new Valve("Rear Transfer Valve", 1, 1);
	static Component RTPipe2 = new Pipe("Rear Transfer Pipe 2", 1, 1);
	
	static Component rearTransferPump = new Pump("Rear Transfer Pump", 1, 1);
	static Component RTPipe3 = new Pipe("Rear Transfer Pipe 3", 1, 1);
	
	static Component crossFeedValve = new Valve("Cross Feed Valve", 2, 2);
	
	static Component[] systemComponents = { 
			boosterPumpForward,
			BPFPipe1, BPFPipe2, BPFPipe3,
			rightTransferPump, forwardTransferPump, forwardTransferValve,
			FTPipe1, FTPipe2, FTPipe3,
			
			boosterPumpRear,
			BPRPipe1, BPRPipe2, BPRPipe3,
			leftTransferPump, rearTransferValve, rearTransferPump,
			RTPipe1, RTPipe2, RTPipe3,
			
			crossFeedValve,
	};
	
	public static boolean topEventReached(Component [] systemComponents, String operator) {
		 
		 if (operator.equals("and")) {
			 int c = 0;
			 
			 for (Component comp : systemComponents) {
				 
				 if (comp.isTopLevelComponent) {
					 c++;
					 if (!comp.hasFailed())
						 return false;
				 }		 
			 }	 
			 return (c > 0)? true: false;
		 }
		 else {
			 for (Component comp : systemComponents) {
				 
				 if (comp.isTopLevelComponent && comp.hasFailed()) {
					 System.out.println("failed");
					 return true;
				 }		 
			 }
			 return false;
			 
		 }
		 
	 }
	
	public static ArrayList<TreeEvent> testCheckBeforeInjecting(Component[] systemComps) {
		systemComponents = systemComps;

		ArrayList<TreeEvent> eventAcc = new ArrayList();
		ArrayList<ArrayList<FailureCombination>> theList = new ArrayList();

		for (Component component : systemComps) {
			addComponentNewListsWithAllFCs(theList, new ArrayList<FailureCombination>(), component);
		}


		int k = 0;
		while (!theList.isEmpty()) {
			k++;
			System.out.println(k);
			System.out.println("Sorting lists to find the smallest list: ");
			ArrayList<FailureCombination> FCList = getSmallestList(theList);
			if (FCList.size() > 2)
				break;
			System.out.println("Current FC: " + FCList);

			int size = FCList.size();
			ArrayList<Component> list = new ArrayList<Component>();
			ArrayList<String> failures = new ArrayList();
			ArrayList<FailureType> failureModes = new ArrayList();
			FailureType failureMode;

			for (FailureCombination FC : FCList) {

				list.add(FC.component);
				failures.add(FC.failureType.toString());
				failureModes.add(FC.failureType); // ??? to know failure mode per component in componentList

			}

			System.out.println("Start checking if event is covered");

			// Test if we this combination has any subsets in the event list
			TreeEvent potentialEvent = createEvent(list, failures);
			boolean changeEvent = eventListCoversEvent(potentialEvent, eventAcc);
			boolean happenedBefore = eventHappened(potentialEvent, eventAcc);

			if (happenedBefore) {
				theList.remove(FCList);
				resetStatus(systemComponents); // no need to reset actually because no propagation yet
				System.out.println(theList);
				System.out.println(eventAcc);
				continue;
			}

			// if changeEvent is true => just remove from list
			// if false => Inject in all elements and add event to eventAcc
			if (changeEvent) {
				theList.remove(FCList);
				continue;
			} else {
				int i = 0;
				for (Component component : list) {
					component.isAbsorber = false;
					failureMode = failureModes.get(i);

					System.out.println("Injecting failure: " + failureMode + " in: " + component);
					component.injectFailure(failureMode);

					i++;
				}

				// Then check if the system failed
				// if failed => just skip to next iteration
				// if not => follow the absorbers
				if (topEventReached(systemComponents, "or")) {
					eventAcc.add(potentialEvent);
					System.out.println("Add event: " + potentialEvent);
///////////////////////////////////////////////////////////////////////////////////////////////////////////
					// We also want to consider the input ports of components
					for (Component component : list) {
						// we need FCList without this component FC
						ArrayList<FailureCombination> toBeReplacedList = removeComponentFCFromFCList(FCList, component);
						Component[] inputPorts = component.inputPorts;
						for (Component port : inputPorts) {
							
							addComponentNewListsWithAllFCs(theList, toBeReplacedList, port);
						}
					}

					// We also want to consider the output ports of components
					for (Component component : list) {
						// we need FCList without this component FC
						ArrayList<FailureCombination> toBeReplacedList = removeComponentFCFromFCList(FCList, component);
						Component[] outputPorts = component.outputPorts;

						for (Component port : outputPorts) {
							addComponentNewListsWithAllFCs(theList, toBeReplacedList, port);
						}
					}
///////////////////////////////////////////////////////////////////////////////////////////////////////////					
				} else {
					for (Component absorber : systemComponents) {
						if (absorber.isAbsorber && !absorber.hasFailed()) {
							System.out.println("One of the absorbers is: " + absorber);
							System.out.println(
									"And the source of failure is: " + absorber.previousComponentInPropagation);
//							ArrayList<Component> newList = createNewListWithComponent(componentsList, absorber);
//
//							theList.add(newList);
							addComponentNewListsWithAllFCs(theList, FCList, absorber);

							Component prevComp = absorber.previousComponentInPropagation;
							Component[] absorberPorts;
							if (prevComp.isOutputPortOf(absorber)) {
								absorberPorts = absorber.outputPorts;
							} else
								absorberPorts = absorber.inputPorts;

							for (Component absorberPort : absorberPorts) {
								if (!absorberPort.hasFailed()) {
									addComponentNewListsWithAllFCs(theList, FCList, absorberPort);
								}

							}

						}
					}
				}

			}

			// at the end of the iteration, reset and remove list
			theList.remove(FCList);
			resetStatus(systemComponents);
//			System.out.println(theList);
			System.out.println(eventAcc);

		}
		return eventAcc;

	}

	@Override
	public void start(Stage stage) throws IOException {

		// Defining a model

		boosterPumpForward.outputPorts[0] = BPFPipe1;
		BPFPipe1.inputPorts[0] = boosterPumpForward;
		
		///
		
		rightTransferPump.outputPorts[0] = FTPipe1;
		FTPipe1.inputPorts[0] = rightTransferPump;
		
		FTPipe1.outputPorts[0] = forwardTransferValve;
		forwardTransferValve.inputPorts[0] = FTPipe1;
		
		forwardTransferValve.outputPorts[0] = FTPipe2;
		FTPipe2.inputPorts[0] = forwardTransferValve;
		
		FTPipe2.outputPorts[0] = forwardTransferPump;
		forwardTransferPump.inputPorts[0] = FTPipe2;
		
		forwardTransferPump.outputPorts[0] = FTPipe3;
		FTPipe3.inputPorts[0] = forwardTransferPump;
		
		FTPipe3.outputPorts[0] = boosterPumpForward;
		boosterPumpForward.inputPorts[0] = FTPipe3;
		
		//

		BPFPipe1.outputPorts[0] = crossFeedValve;
		crossFeedValve.inputPorts[0] = BPFPipe1;
		
		BPFPipe1.outputPorts[1] = BPFPipe3;
		BPFPipe3.inputPorts[0] = BPFPipe1;
		
		BPFPipe2.inputPorts[0] = crossFeedValve;
		crossFeedValve.outputPorts[0] = BPFPipe2;
		
		BPFPipe2.outputPorts[0] = BPFPipe3;
		BPFPipe3.inputPorts[1] = BPFPipe2;
		
		// // // // // // // // // // // // // // // // // // // //
		
		boosterPumpRear.outputPorts[0] = BPRPipe1;
		BPRPipe1.inputPorts[0] = boosterPumpRear;
		
		///
		
		leftTransferPump.outputPorts[0] = RTPipe1;
		RTPipe1.inputPorts[0] = leftTransferPump;
		
		RTPipe1.outputPorts[0] = rearTransferValve;
		rearTransferValve.inputPorts[0] = RTPipe1;
		
		rearTransferValve.outputPorts[0] = RTPipe2;
		RTPipe2.inputPorts[0] = rearTransferValve;
		
		RTPipe2.outputPorts[0] = rearTransferPump;
		rearTransferPump.inputPorts[0] = RTPipe2;
		
		rearTransferPump.outputPorts[0] = RTPipe3;
		RTPipe3.inputPorts[0] = rearTransferPump;
		
		RTPipe3.outputPorts[0] = boosterPumpRear;
		boosterPumpRear.inputPorts[0] = RTPipe3;
		
		//

		BPRPipe1.outputPorts[0] = crossFeedValve;
		crossFeedValve.inputPorts[1] = BPRPipe1;
		
		BPRPipe1.outputPorts[1] = BPRPipe3;
		BPRPipe3.inputPorts[0] = BPRPipe1;
		
		BPRPipe2.inputPorts[0] = crossFeedValve;
		crossFeedValve.outputPorts[1] = BPRPipe2;
		
		BPRPipe2.outputPorts[0] = BPRPipe3;
		BPRPipe3.inputPorts[1] = BPRPipe2;
			

		BPRPipe3.isTopLevelComponent = true;
		BPFPipe3.isTopLevelComponent = true;

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
		// launch(args);
	
		// Defining a model
		

		boosterPumpForward.outputPorts[0] = BPFPipe1;
		BPFPipe1.inputPorts[0] = boosterPumpForward;
		
		///
		
		rightTransferPump.outputPorts[0] = FTPipe1;
		FTPipe1.inputPorts[0] = rightTransferPump;
		
		FTPipe1.outputPorts[0] = forwardTransferValve;
		forwardTransferValve.inputPorts[0] = FTPipe1;
		
		forwardTransferValve.outputPorts[0] = FTPipe2;
		FTPipe2.inputPorts[0] = forwardTransferValve;
		
		FTPipe2.outputPorts[0] = forwardTransferPump;
		forwardTransferPump.inputPorts[0] = FTPipe2;
		
		forwardTransferPump.outputPorts[0] = FTPipe3;
		FTPipe3.inputPorts[0] = forwardTransferPump;
		
		FTPipe3.outputPorts[0] = boosterPumpForward;
		boosterPumpForward.inputPorts[0] = FTPipe3;
		
		//

		BPFPipe1.outputPorts[0] = crossFeedValve;
		crossFeedValve.inputPorts[0] = BPFPipe1;
		
		BPFPipe1.outputPorts[1] = BPFPipe3;
		BPFPipe3.inputPorts[0] = BPFPipe1;
		
		BPFPipe2.inputPorts[0] = crossFeedValve;
		crossFeedValve.outputPorts[0] = BPFPipe2;
		
		BPFPipe2.outputPorts[0] = BPFPipe3;
		BPFPipe3.inputPorts[1] = BPFPipe2;
		
		// // // // // // // // // // // // // // // // // // // //
		
		boosterPumpRear.outputPorts[0] = BPRPipe1;
		BPRPipe1.inputPorts[0] = boosterPumpRear;
		
		///
		
		leftTransferPump.outputPorts[0] = RTPipe1;
		RTPipe1.inputPorts[0] = leftTransferPump;
		
		RTPipe1.outputPorts[0] = rearTransferValve;
		rearTransferValve.inputPorts[0] = RTPipe1;
		
		rearTransferValve.outputPorts[0] = RTPipe2;
		RTPipe2.inputPorts[0] = rearTransferValve;
		
		RTPipe2.outputPorts[0] = rearTransferPump;
		rearTransferPump.inputPorts[0] = RTPipe2;
		
		rearTransferPump.outputPorts[0] = RTPipe3;
		RTPipe3.inputPorts[0] = rearTransferPump;
		
		RTPipe3.outputPorts[0] = boosterPumpRear;
		boosterPumpRear.inputPorts[0] = RTPipe3;
		
		//

		BPRPipe1.outputPorts[0] = crossFeedValve;
		crossFeedValve.inputPorts[1] = BPRPipe1;
		
		BPRPipe1.outputPorts[1] = BPRPipe3;
		BPRPipe3.inputPorts[0] = BPRPipe1;
		
		BPRPipe2.inputPorts[0] = crossFeedValve;
		crossFeedValve.outputPorts[1] = BPRPipe2;
		
		BPRPipe2.outputPorts[0] = BPRPipe3;
		BPRPipe3.inputPorts[1] = BPRPipe2;
		

		BPRPipe3.isTopLevelComponent = true;
		BPFPipe3.isTopLevelComponent = true;
	
	
		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
		removePipesFromResults(eventList);
	
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
	}
	
	public static void removePipesFromResults(ArrayList<TreeEvent> eventAcc) {
		boolean[] remove = new boolean[eventAcc.size()];
		for (TreeEvent event : eventAcc) {
			for (String compName : event.componentNames) {
				if (compName.contains("Pipe")) {
					remove[eventAcc.indexOf(event)] = true;
					break;
				}
			}
		}
		for (int i = remove.length - 1; i >= 0; i--) {
			if (remove[i])
				eventAcc.remove(i);
		}
	}
}
