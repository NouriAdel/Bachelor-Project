package system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import components.*;
import components.Battery.Battery;
import components.Busbar.*;
import components.Contactor.*;
import components.DCConsumer.*;
import components.Diode.Diode;
import components.Generator.*;
import components.Pipe.*;
import components.Pump.*;
import components.Valve.*;
import failures.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExampleDCSystem extends Application {

	static FailureType[] possibleValveFailures = { FailureType.STUCK, FailureType.LEAK };
	static FailureType[] possiblePumpFailures = { FailureType.OVER_PRESSURED, FailureType.NO_PRESSURE };
	static FailureType[] possiblePipeFailures = { FailureType.BLOCKED, FailureType.LEAK };
	static FailureType[] possibleBSCUFailures = { FailureType.LOSS_OF_CONTROL_SIGNAL };
	
	static FailureType[] possibleGeneratorFailures = { 
			FailureType.LOSS_OF_POWER, 
//			FailureType.OVER_VOLTAGE, 
//			FailureType.UNDER_VOLTAGE
	};
	static FailureType[] possibleContactorFailures = { 
			FailureType.STUCK_OPEN, FailureType.STUCK_CLOSED		
	};
	static FailureType[] possibleBusbarFailures = { 
			FailureType.FAILED_TO_CONDUCT, 
//			FailureType.SHORT_CIRCUIT 
	};
	static FailureType[] possibleBatteryFailures = { 
			FailureType.UNDER_CHARGED, FailureType.SHORT_CIRCUIT
	};
	static FailureType[] possibleDCConsumerFailures = { 
//			FailureType.REVERSE_CURRENT, 
//			FailureType.OVER_VOLTAGE, FailureType.UNDER_VOLTAGE, 
			FailureType.SHORT_CIRCUIT, FailureType.OPEN_CIRCUIT 
	};
	static FailureType[] possibleDiodeFailures = { 
			FailureType.STUCK_CLOSED
	};

	// Simple System Components
	static Component mainPump = new Pump("Main Pump", 0, 1); // assuming pump is the first component (0 inputs)
	static Component mainPipe = new Pipe("Main pipe", 1, 2);
	static Component valveA   = new Valve("Valve A", 1, 1);
	static Component valveB   = new Valve("Valve B", 1, 1);
	static Component pipeA    = new Pipe("Pipe A", 1, 0);
	static Component pipeB    = new Pipe("Pipe B", 1, 0);
	
	static Component generatorA = new Generator("Generator A", 0, 1);
	static Component generatorB = new Generator("Generator B", 0, 1);
	static Component powerGenerationContactorA = new Contactor("Power Generation Contactor A", 1, 1);
	static Component powerGenerationContactorB = new Contactor("Power Generation Contactor B", 1, 1);
	static Component busbarA = new Busbar("Busbar A", 1, 1);
	static Component busbarB = new Busbar("Busbar B", 1, 1);
	static Component feedContactorA = new Contactor("Feed Contactor A", 1, 1);
	static Component feedContactorB = new Contactor("Feed Contactor B", 1, 1);
	static Component centreBusbar = new Busbar("Centre Busbar", 2, 1);
	static Component batteryContactor = new Contactor("Battery Bubar Contactor", 1, 1);
	static Component batteryBusbar = new Busbar("Battery Busbar", 2, 1);
	static Component battery = new Battery("Battery", 0, 1);
	static Component DCContactor = new Contactor("DC Contactor", 1, 1);
	static Component diode = new Diode("Diode", 1, 1);
	static Component DCConsumer = new DCConsumer("DC Consumer", 1, 0);
	
	static Component [] systemComponents = {
			generatorA, generatorB, powerGenerationContactorA, powerGenerationContactorB, 
			busbarA, busbarB, feedContactorA, feedContactorB, centreBusbar,
			batteryContactor, batteryBusbar, battery, DCContactor, DCConsumer, //diode
	};

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
			if (FCList.size() > 3)
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
				if (topEventReached(systemComponents)) {
					eventAcc.add(potentialEvent);
					System.out.println("Add event: " + potentialEvent);
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//					// We also want to consider the input ports of components
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

	// We change this to return MANY new lists with the component and all its
	// failure combinations
	// public static ArrayList<Component> createNewListWithComponent
	// (ArrayList<Component> componentsList, Component newToBeAddedComponent) {
	//
	// ArrayList<Component> newList = new ArrayList<Component>();
	// for (Component prevComponent : componentsList) {
	// newList.add(prevComponent);
	// }
	// newList.add(newToBeAddedComponent);
	//
	// return newList;
	// }
	
	public static ArrayList<FailureCombination> removeComponentFCFromFCList(ArrayList<FailureCombination> FCList, Component component) {
		ArrayList<FailureCombination> newList = new ArrayList<FailureCombination>();
		for (FailureCombination prevFC : FCList) {
			if (prevFC.component != component) {
				System.out.println("DON'T REMOVE FC");
				newList.add(prevFC);
			}
			else System.out.println("REMOVING THIS COMPONENT FC");
				
		}
		return newList;
		
	}

	// This takes a list of FCs (componentsList previously) and a new FC and 
	//returns a new list of of FCs + new FC

	public static ArrayList<FailureCombination> createNewListWithFC(
			ArrayList<FailureCombination> failureCombinationList, FailureCombination newToBeAddedfailureCombination) {

		ArrayList<FailureCombination> newList = new ArrayList<FailureCombination>();
		for (FailureCombination prevfailureCombination : failureCombinationList) {
			newList.add(prevfailureCombination);
		}
		newList.add(newToBeAddedfailureCombination);

		return newList;
	}
	
	// This takes a component and returns a list of all possible FCs for this component

	public static ArrayList<FailureCombination> getAllComponentFailureCombinations(Component component) {
		ArrayList<FailureCombination> componentFCs = new ArrayList<FailureCombination>();

		switch (component.type) {
		case VALVE: {
			for (FailureType failureType : possibleValveFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}		
		case PUMP: {
			for (FailureType failureType : possiblePumpFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}
		case PIPE: {
			for (FailureType failureType : possiblePipeFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}
		case GENERATOR:
			for (FailureType failureType : possibleGeneratorFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		case CONTACTOR:
			for (FailureType failureType : possibleContactorFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;	
		case BUSBAR:
			for (FailureType failureType : possibleBusbarFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		case BATTERY:
			for (FailureType failureType : possibleBatteryFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;	
		case DCCONSUMER:
			for (FailureType failureType : possibleDCConsumerFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		case DIODE:
			for (FailureType failureType : possibleDiodeFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;	
			
		default: {
			for (FailureType failureType : possibleBSCUFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
		
		}
		}
		
		return componentFCs;

	}
	
	// This takes component, the big list (theList) with ALL FCS yet to be tried 
	
	public static void addComponentNewListsWithAllFCs(ArrayList<ArrayList<FailureCombination>> theList, 
			ArrayList<FailureCombination> toBeReplacedFCList, Component component) {
		System.out.println("List to add to: " + toBeReplacedFCList);
		System.out.println("Component is: " + component);

		ArrayList<FailureCombination> newComponentFCsList = getAllComponentFailureCombinations(component);
		for (FailureCombination newFC : newComponentFCsList) {
			// createNewListWithFC just adds the FC to the list but it doesn't affect the list, it returns a new one
			ArrayList<FailureCombination> newReplacementFCList = createNewListWithFC(toBeReplacedFCList, newFC);
			theList.add(newReplacementFCList);	
		}	
		if (theList.contains(toBeReplacedFCList))
			theList.remove(toBeReplacedFCList);	
	}
	

	public static ArrayList<ArrayList<Component>> sortLists(ArrayList<ArrayList<Component>> theList) {
		ArrayList<ArrayList<Component>> resultList = new ArrayList<ArrayList<Component>>();

		while (!theList.isEmpty()) {
			int size = theList.size();
			int minSize = theList.get(0).size();
			ArrayList<Component> minList = theList.get(0);
			for (int i = 0; i < size - 1; i++) {
				ArrayList<Component> componentList = theList.get(i);
				if (componentList.size() < minSize) {
					minList = componentList;
					minSize = componentList.size();
				}
			}
			resultList.add(minList);
			theList.remove(minList);
		}
		return resultList;
	}

	public static ArrayList<FailureCombination> getSmallestList(ArrayList<ArrayList<FailureCombination>> theList) {

		int size = theList.size();
		int minSize = theList.get(0).size();
		ArrayList<FailureCombination> minList = theList.get(0);
		for (int i = 0; i < size - 1; i++) {
			ArrayList<FailureCombination> componentList = theList.get(i);
			if (componentList.size() < minSize) {
				minList = componentList;
				minSize = componentList.size();
			}
		}
		return minList;
	}

	public static boolean eventListCoversEvent(TreeEvent event, ArrayList<TreeEvent> eventList) {

		for (TreeEvent previousEvent : eventList) {
			if (previousEvent.isSubEventOf(event)) {
				System.out.println("SUBEVENT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return true;
			}
		}
		return false;
	}

	public static boolean eventHappened(TreeEvent event, ArrayList<TreeEvent> eventList) {

		for (TreeEvent previousEvent : eventList) {
			if (previousEvent.equals(event)) {
				System.out.println("WHOLE EVENT APPEARED BEFORE!");
				return true;
			}
		}
		return false;
	}

	public static TreeEvent createEvent(ArrayList<Component> list, ArrayList<String> failures) {

		String[] eventComponents = new String[list.size()];
		String[] eventFailures = new String[list.size()];

		int i = 0;
		for (Component comp : list) {
			eventComponents[i++] = comp.name;
		}
		i = 0;
		for (String failure : failures) {
			eventFailures[i++] = failure;
		}
		return new TreeEvent(eventComponents, eventFailures, "and");
	}


	 public static boolean topEventReached(Component [] systemComponents) {
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
//	 public static boolean topEventReached(Component [] systemComponents) {
//		 int c = 0;
//		 
//		 for (Component comp : systemComponents) {
//			 
//			 if (comp.isTopLevelComponent && ((DCConsumerState) comp.state).state == DCConsumerStates.NO_SUPPLY) {
//				 c++;
//				 if (!comp.hasFailed())
//					 return false;
//			 }		 
//		 }	 
//		 return false;
//	 }
	public static void resetStatus(Component[] systemComponents) {
		// before injecting a failure in a new component, reset states to nominal
		for (Component comp : systemComponents) {
			comp.resetComponentState();
			comp.absorbingComponent = null;
		}
	}

	@Override
	public void start(Stage stage) throws IOException {

		// Defining a model
		
		generatorA.outputPorts[0] = powerGenerationContactorA;
		powerGenerationContactorA.inputPorts[0]  = generatorA;
		powerGenerationContactorA.outputPorts[0] = busbarA;
		busbarA.inputPorts[0] = powerGenerationContactorA;
		busbarA.outputPorts[0]    = feedContactorA;
		feedContactorA.inputPorts[0]    = busbarA;
		feedContactorA.outputPorts[0]   = centreBusbar;		
		centreBusbar.inputPorts[0] = feedContactorA;
		
		generatorB.outputPorts[0] = powerGenerationContactorB;
		powerGenerationContactorB.inputPorts[0]  = generatorB;
		powerGenerationContactorB.outputPorts[0] = busbarB;
		busbarB.inputPorts[0] = powerGenerationContactorB;
		busbarB.outputPorts[0]    = feedContactorB;
		feedContactorB.inputPorts[0]    = busbarB;
		feedContactorB.outputPorts[0]   = centreBusbar;		
		centreBusbar.inputPorts[1] = feedContactorB;
		
		centreBusbar.outputPorts[0] = batteryContactor;
		batteryContactor.inputPorts[0] = centreBusbar;
		batteryContactor.outputPorts[0] = batteryBusbar;
		batteryBusbar.inputPorts[0] = batteryContactor;
		batteryBusbar.inputPorts[1] = battery;
		battery.outputPorts[0] = batteryBusbar;
		batteryBusbar.outputPorts[0] = DCContactor;
		DCContactor.inputPorts[0] = batteryBusbar;
		DCContactor.outputPorts[0] = DCConsumer;
		DCConsumer.inputPorts[0] = DCContactor;
		
		DCConsumer.isTopLevelComponent = true;

		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
		// removePipesFromResults(eventAcc);
		 
		String topEventText = "";
			
		for (Component component: systemComponents) {
			if (component.isTopLevelComponent) {
				if (topEventText.length() > 0)
					topEventText += " and " + component.name;
				else 
					topEventText += "Top Event: " + component.name;
			}
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
	
	public static void  pseudoMain() {
		
		// Defining a model
		
		generatorA.outputPorts[0] = powerGenerationContactorA;
		powerGenerationContactorA.inputPorts[0]  = generatorA;
		powerGenerationContactorA.outputPorts[0] = busbarA;
		busbarA.inputPorts[0] = powerGenerationContactorA;
		busbarA.outputPorts[0]    = feedContactorA;
		feedContactorA.inputPorts[0]    = busbarA;
		feedContactorA.outputPorts[0]   = centreBusbar;		
		centreBusbar.inputPorts[0] = feedContactorA;
		
		generatorB.outputPorts[0] = powerGenerationContactorB;
		powerGenerationContactorB.inputPorts[0]  = generatorB;
		powerGenerationContactorB.outputPorts[0] = busbarB;
		busbarB.inputPorts[0] = powerGenerationContactorB;
		busbarB.outputPorts[0]    = feedContactorB;
		feedContactorB.inputPorts[0]    = busbarB;
		feedContactorB.outputPorts[0]   = centreBusbar;		
		centreBusbar.inputPorts[1] = feedContactorB;
		
		centreBusbar.outputPorts[0] = batteryContactor;
		batteryContactor.inputPorts[0] = centreBusbar;
		batteryContactor.outputPorts[0] = batteryBusbar;
		batteryBusbar.inputPorts[0] = batteryContactor;
		batteryBusbar.inputPorts[1] = battery;
		battery.outputPorts[0] = batteryBusbar;
		batteryBusbar.outputPorts[0] = DCContactor;
		DCContactor.inputPorts[0] = batteryBusbar;
		
		DCContactor.outputPorts[0] = DCConsumer;
		DCConsumer.inputPorts[0] = DCContactor;
		
//		DCContactor.outputPorts[0] = diode;
//		DCConsumer.inputPorts[0] = diode;
//		diode.inputPorts[0] = DCContactor;
//		diode.outputPorts[0] = DCConsumer;
		
		DCConsumer.isTopLevelComponent = true;

		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);
		int i = 1;

		for (TreeEvent event : eventList) {
			System.out.println(i++ + ". " + event);
			System.out.println("");
		}
	}

	public static void main(String[] args) {
//		launch(args);
		pseudoMain();
	}

}
