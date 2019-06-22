//package failures;
//
//import components.Component;
//
//public class Failure {
//	
//	static int ids;
//	
//	public int id;
//	
//	// to be changed as the failure propagates through components
//	public FailureType type;
//	
//	// remain constant till the propagation terminates
//	public int sourceID;
//	public FailureType sourceFailureType;
//	
//	Component currentCompoenent;
//	
//	// To know the type of the component from which the failure is received
//	public Component previousComponent;
//	
//	public Failure(FailureType type, int sourceID, FailureType sourceFailureType, Component currentComponent,
//			Component previousComponent) {
//		
//		this.id = id++;
//		this.type = type;
//		this.sourceID = sourceID;
//		this.sourceFailureType = sourceFailureType;
//		this.currentCompoenent = currentComponent;
//		this.previousComponent = previousComponent;
//	}
//	
//	// A function that creates a new failure with the same sourceID and SourceFailureType
//	// This indicates a new failure in a component as it receives a failure form an adjacent component
//	// Note that the new previousComponent would be the current component
//	
//	public Failure newCreatedFailure(FailureType newFailureType, Component newComponent) {
//		Failure newFailure = new Failure(newFailureType, this.sourceID, this.sourceFailureType, newComponent, this.currentCompoenent);
//		return newFailure;
//	}	
//}
