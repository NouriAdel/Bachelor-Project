package system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import components.*;
import failures.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import system.Controllers.CreateComponentController;

public class NewFrontEndInput extends Application {

	static FailureType[] possibleValveFailures = { FailureType.STUCK, FailureType.LEAK };
	static FailureType[] possiblePumpFailures = { FailureType.OVER_PRESSURED, FailureType.NO_PRESSURE };
	static FailureType[] possiblePipeFailures = { FailureType.BLOCKED, FailureType.LEAK };
	static FailureType[] possibleBSCUFailures = { FailureType.LOSS_OF_CONTROL_SIGNAL };
	
	/*static ArrayList<ComponentState> allStates = CreateComponentController.getAllStates();
	static ArrayList<CustomizedState> allCustomizedStates = CreateComponentController.getAllCustomizedStates();
	static ArrayList<ComponentType> allTypes = CreateComponentController.getAllTypes();
	static ArrayList<Boolean> ifCustomizedInjected = CreateComponentController.getIfCustomizedInjected();
	static ArrayList<Integer> allDirections = CreateComponentController.getAllDirections();*/

	public static ArrayList<TreeEvent> prepareTreeFromFrontEndComponents(Component[] systemComponents) {

		/*ArrayList<ComponentState> allStates = CreateComponentController.getAllStates();
		ArrayList<CustomizedState> allCustomizedStates = CreateComponentController.getAllCustomizedStates();
		ArrayList<ComponentType> allTypes = CreateComponentController.getAllTypes();
		ArrayList<Boolean> ifCustomizedInjected = CreateComponentController.getIfCustomizedInjected();
		ArrayList<Integer> allDirections = CreateComponentController.getAllDirections();*/
		ArrayList<TreeEvent> eventAcc = new ArrayList();
		ArrayList<ArrayList<FailureCombination>> theList = new ArrayList();

		for (Component component : systemComponents) {
			addComponentNewListsWithAllFCs(theList, new ArrayList<FailureCombination>(), component);
		}

		int k = 0;
		while (!theList.isEmpty()) {
			k++;
			System.out.println(k);
			System.out.println("Sorting lists to find the smallest list: ");
			System.out.println(theList);
			ArrayList<FailureCombination> FCList = getSmallestList(theList);
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
					//component.injectFailure(failureMode , component , allStates ,allTypes , allCustomizedStates , allDirections , ifCustomizedInjected );
					component.injectFailure(failureMode);
					
					i++;
				}

				// Then check if the system failed
				// if failed => just skip to next iteration
				// if not => follow the absorbers
				if (topEventReached(systemComponents)) {
					eventAcc.add(potentialEvent);

					// We also want to consider the input ports of components
					System.out.println("component list To be changed for ports " + list);
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
			System.out.println(theList);
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
	//CHANGED-Nouran
	
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
		case BSCU: {
		//default: {
			for (FailureType failureType : possibleBSCUFailures) {
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}
		
		default: {
			CustomizedComponent custom = (CustomizedComponent)component;
			for (CustomizedState custom_state : custom.allCustomizedStates){
				FailureType failureType = FailureType.valueOf(custom_state.toString());
				FailureCombination failureCombination = new FailureCombination(component, failureType);
				componentFCs.add(failureCombination);
			}
		}
		
		/*default: { //customized component
			if (!allCustomizedStates.isEmpty()){
				for (int i=0;i<allCustomizedStates.size();i++){
					FailureType failureType = FailureType.valueOf(allCustomizedStates.get(i).toString());
					FailureCombination failureCombination = new FailureCombination(component , failureType);
					componentFCs.add(failureCombination);
				}
			}
			
		}*/
		}
		
		return componentFCs;

	}
	
	// This takes component, the big list (theList) with ALL FCS yet to be tried 
	
	public static void addComponentNewListsWithAllFCs(ArrayList<ArrayList<FailureCombination>> theList, 
			ArrayList<FailureCombination> toBeReplacedFCList, Component component) {

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
	public static void resetStatus(Component[] systemComponents) {
		// before injecting a failure in a new component, reset states to nominal
		for (Component comp : systemComponents) {
			comp.resetComponentState();
			comp.absorbingComponent = null;
		}
	}

	@Override
	public void start(Stage stage) throws IOException {


//		ArrayList<TreeEvent> eventList = testCheckBeforeInjecting(systemComponents);

		FXMLLoader fxmlLoader = new FXMLLoader();
//		GuiControllerTest controller = new GuiControllerTest(eventList, "Pipe G3 and Pipe B3 failed");
		
		GUIControllerComponentsFromFrontEnd controller = new GUIControllerComponentsFromFrontEnd();

		fxmlLoader.setController(controller);
		fxmlLoader.setLocation(getClass().getResource("Views/index.fxml"));
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

	public static void main(String[] args) {
		launch(args);
	}

}
