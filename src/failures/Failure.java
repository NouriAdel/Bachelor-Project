package failures;

import java.util.ArrayList;

import components.Component;

public class Failure {
	
	static int ids;
	
	public int id;
	public int sourceID;
	
	public Component sourceComponent;
	
	// to be changed as the failure propagates through components
	public FailureType type;	
	public Component currentComponent;
	
	// lists of all components that the failure propagated through them
	public ArrayList<Component> failedComponents;
	public ArrayList<Component> nonFailedComponents;
	
	// Constructor for an injected failure
	public Failure(FailureType type, Component currentComponent) {	
		this.id = id++;
		this.sourceID = currentComponent.id;
		this.type = type;
		this.sourceComponent = currentComponent;
		this.currentComponent = currentComponent;
		
		failedComponents = new ArrayList<Component>();
		failedComponents.add(currentComponent);
		
		nonFailedComponents = new ArrayList<Component>();
	}
	
	// Modifying failure for propagation
	public void modifyFailure(FailureType type, Component currentComponent) {	
		this.type = type;
		this.currentComponent = currentComponent;
	}
	
	public void addComponentToFailedList(Component component) {
		failedComponents.add(component);
	}
	
	public void addComponentToNonFailedList(Component component) {
		nonFailedComponents.add(component);
	}
}
