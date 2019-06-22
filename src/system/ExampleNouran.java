package system;

import java.io.IOException;
import java.util.ArrayList;

import components.Component;
import components.Pipe.Pipe;
import components.Pump.Pump;
import components.Valve.Valve;
import failures.FailureCombination;
import failures.FailureType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExampleNouran extends Application {
	
	static FailureType[] possibleValveFailures = { FailureType.STUCK, FailureType.LEAK };
	static FailureType[] possiblePumpFailures = { FailureType.OVER_PRESSURED, FailureType.NO_PRESSURE };
	static FailureType[] possiblePipeFailures = { FailureType.BLOCKED, FailureType.LEAK };
	
	static Component pump1 = new Pump ("Supply 1",0,1);
	static Component pump2 = new Pump ("Supply 2",0,1);
	static Component valve = new Valve ("Selector Valve",2,2);
	static Component valve1 = new Valve ("Valve 1",1,1);
	static Component valve2 = new Valve ("Valve 2",1,1);
	static Component pipe1 = new Pipe ("Pipe 1",1,0);
	static Component pipe2 = new Pipe ("Pipe 2",1,0);
	
	static Component [] systemComponents = {
			pump1, pump2, valve, valve1, valve2, pipe1, pipe2
	};
	
	public static ArrayList<TreeEvent> beginning(Component[] sysComp) {
		systemComponents = sysComp;

		ArrayList<TreeEvent> eventAcc = new ArrayList();
		ArrayList<ArrayList<FailureCombination>> theList = new ArrayList();

		for (Component component : sysComp) {
			addComponentNewListWithAllFCs(theList, new ArrayList<FailureCombination>(), component);
		}

		int i = 0;
		while (!theList.isEmpty()) {
			i++;
			System.out.println(i);
			ArrayList<FailureCombination> FClist = getSmallestFCList(theList);
			System.out.println("Current (smallest) FC " + FClist);

			int n = FClist.size();
			ArrayList<String> failure = new ArrayList();
			ArrayList<Component> list = new ArrayList<Component>();
			ArrayList<FailureType> failureModes = new ArrayList();
			FailureType failureMode;

			for (FailureCombination FC : FClist) {

				list.add(FC.component);
				failure.add(FC.failureType.toString());
				failureModes.add(FC.failureType); 
			}

			System.out.println("Let's check if a subset event or the whole event happened before");

			TreeEvent potentialEvent = createEvent(list, failure);
			boolean [] eventCheck = checkifCoveredOrHappened(potentialEvent , eventAcc);
			boolean EventCovered = eventCheck[0];
			boolean EventHappened = eventCheck[1];

			if (EventHappened) {
				theList.remove(FClist);
				resetsys(systemComponents); 
				System.out.println("Whole event happened before");
				System.out.println(theList);
				System.out.println(eventAcc);
			}
			else if (EventCovered){
				theList.remove(FClist);
			}
			else {
				int j = 0;
				for (Component comp : list) {
					comp.isAbsorber = false;
					failureMode = failureModes.get(j);
					System.out.println("Injecting failure " + failureMode + " in component " + comp);
					comp.injectFailure(failureMode);
					j++;
				}
				
				if (topEventReached(systemComponents)) {
					eventAcc.add(potentialEvent);
					System.out.println("Let's check the ports " + list);
					for (Component comp : list) {
						ArrayList<FailureCombination> replacingList = removeComponentFromFClist(FClist, comp);
						Component[] outputPorts = comp.outputPorts;
						Component[] inputPorts = comp.inputPorts;

						for (Component port : outputPorts) {
							addComponentNewListWithAllFCs(theList, replacingList, port);
						}
						
						for (Component port : inputPorts) {
							
							addComponentNewListWithAllFCs(theList, replacingList, port);
						}
					}
				} else {
					for (Component absorber : systemComponents) {
						if (absorber.isAbsorber && !absorber.hasFailed()) {
							System.out.println("One of the absorbers is " + absorber);
							System.out.println("the source of failure is " + absorber.previousComponentInPropagation);
							addComponentNewListWithAllFCs(theList, FClist, absorber);
							Component prevComp = absorber.previousComponentInPropagation;
							Component[] absorberPorts;
							if (prevComp.isOutputPortOf(absorber)) {
								absorberPorts = absorber.outputPorts;
							} else {
								absorberPorts = absorber.inputPorts;
							}
							for (Component absorberPort : absorberPorts) {
								if (!absorberPort.hasFailed()) {
									addComponentNewListWithAllFCs(theList, FClist, absorberPort);
								}

							}

						}
					}
				}

			}
			theList.remove(FClist);
			resetsys(systemComponents);
			System.out.println("iteration done");
			System.out.println(theList);
			System.out.println(eventAcc);
		}
		return eventAcc;
	}
	
	public static void addComponentNewListWithAllFCs (ArrayList<ArrayList<FailureCombination>> theList, 
			ArrayList<FailureCombination> replacingFCList, Component comp) {
		ArrayList<FailureCombination> newFCList = getAllFCs(comp);
		for (FailureCombination newFC : newFCList){
			ArrayList<FailureCombination> newReplacingFClist = createNewFClist(replacingFCList , newFC);
			theList.add(newReplacingFClist);
		}
		if (theList.contains(replacingFCList)){
			theList.remove(replacingFCList);
		}
	}
	
	public static ArrayList<FailureCombination> getAllFCs (Component comp){
		ArrayList<FailureCombination> componentFCs = new ArrayList<FailureCombination>();

		switch (comp.type) {
		case VALVE: {
			for (FailureType failureType : possibleValveFailures) {
				FailureCombination failureCombination = new FailureCombination(comp, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}		
		case PUMP: {
			for (FailureType failureType : possiblePumpFailures) {
				FailureCombination failureCombination = new FailureCombination(comp, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}
		default: {
			for (FailureType failureType : possiblePipeFailures) {
				FailureCombination failureCombination = new FailureCombination(comp, failureType);
				componentFCs.add(failureCombination);
			}
			break;
		}
		}
		return componentFCs;

	}
	
	public static ArrayList<FailureCombination> createNewFClist (ArrayList<FailureCombination> failureCombinationList, FailureCombination newToBeAddedfailureCombination){
		ArrayList<FailureCombination> newList = new ArrayList();
		for(FailureCombination newfc : failureCombinationList){
			newList.add(newfc);
		}
		newList.add(newToBeAddedfailureCombination);
		return newList;
	}
	
	public static void resetsys (Component [] syscomp){
		for (Component comp : syscomp) {
			comp.resetComponentState();
			comp.absorbingComponent = null;
		}
	}
	
	public static boolean topEventReached(Component [] syscomp){
		for(Component comp : syscomp){
			if (comp.isTopLevelComponent && comp.hasFailed())
				return true ;
		}
		return false;
	}
	
	public static ArrayList<FailureCombination> removeComponentFromFClist (ArrayList<FailureCombination> list , Component comp){
		ArrayList<FailureCombination> newList = new ArrayList<FailureCombination> ();
		for (FailureCombination FC : list ){
			if(FC.component!=comp){
				newList.add(FC);
			}
		}
		return newList;
	}
	
	public static TreeEvent createEvent (ArrayList<Component> list , ArrayList<String> failure){
		String[] eventComponents = new String[list.size()];
		String[] eventFailures = new String[list.size()];

		int i = 0;
		for (Component comp : list) {
			eventComponents[i++] = comp.name;
		}
		i = 0;
		for (String fail : failure) {
			eventFailures[i++] = fail;
		}
		return new TreeEvent(eventComponents, eventFailures, "and");
	}
	
	public static boolean [] checkifCoveredOrHappened (TreeEvent event, ArrayList<TreeEvent> eventList){
		boolean covered = false ;
		boolean happened = false ;
		for (TreeEvent previousEvent : eventList) {
			if (previousEvent.isSubEventOf(event)) {
				System.out.println("SUBEVENT");
				covered = true;
			}
			else if (previousEvent.equals(event)){
				System.out.println("HAPPENED BEFORE");
				happened = true;
			}
		}
		boolean [] res = new boolean [2];
		res[0] = covered ;
		res[1] = happened ;
		return res ;
	}
	
	public static ArrayList<FailureCombination> getSmallestFCList (ArrayList<ArrayList<FailureCombination>> theList){
		int n = theList.size();
		int min = theList.get(0).size();
		ArrayList<FailureCombination> minlist = theList.get(0);
		for(ArrayList<FailureCombination> fclist : theList){
			if (fclist.size()<min){
				min = fclist.size();
				minlist = fclist;
			}
		}
		return minlist ;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		pump1.outputPorts[0] = valve;
		pump2.outputPorts[0] = valve;
		valve.inputPorts[0]  = pump1;
		valve.inputPorts[1]  = pump2;
		valve.outputPorts[0]  = valve1;
		valve.outputPorts[1] = valve2;
		valve1.inputPorts[0] = valve;
		valve2.inputPorts[0]    = valve;
		valve1.outputPorts[0]   = pipe1;
		valve2.outputPorts[0]    = pipe2;
		pipe1.inputPorts[0]   = valve1;		
		pipe2.inputPorts[0] = valve2;
		
		pipe1.isTopLevelComponent  = true;
		pipe2.isTopLevelComponent  = true;
		
		ArrayList<TreeEvent> eventAcc = new ArrayList<TreeEvent>();
		FailureType[] failureTypes = new FailureType[4];

		ArrayList<TreeEvent> eventList = beginning(systemComponents);
		 
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
		fxmlLoader.setLocation(getClass().getResource("Views/NouranIndex.fxml"));
		Parent root = fxmlLoader.load();

		// Creating a scene object
		Scene scene = new Scene(root, 1200, 1000);

		// Setting title to the Stage
		primaryStage.setTitle("Safety Analysis");

		// Adding scene to the stage
		primaryStage.setScene(scene);

		// Displaying the contents of the stage
		primaryStage.show();

		int i = 1;

		for (TreeEvent event : eventList) {
			System.out.println(i++ + ". " + event);
			System.out.println("");
		}

		
	}
	
	public static void main(String[]args){
		launch(args);
	}

}
