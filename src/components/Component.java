package components;

import java.util.ArrayList;

import failures.*;
import system.ComponentLabel;

public abstract class Component {

	// to create an autoincrement id for each component
	static int ids;

	public int id;
	public String name;
	public int portsNumber; // to be used later to start iteration form the element with the most ports as
							// discussed
	
	// Could be combined in a single list easily -> minor architectural adjustment
	public Component[] inputPorts;
	public Component[] outputPorts;

	// Component Type could either be one of the already defined classes, or a new,
	// customized component
	public ComponentType type;

	// states indicate whether NOMINAL or failure type
	public ComponentState state;

	// a reference to the failure currently propagating through the component
	public Failure failure;
	
	// 2nd iteration, testing a new approach
	
	public boolean failedButAbsorbed;
	public Component absorbingComponent;
	public FailureType failureCausedAbsorption;
	public ArrayList<Component> absorbingComponents;
	
	// To know if it's an absorber
	public boolean isAbsorber;
	public Component previousComponentInPropagation;
	
	// For UI
	public ComponentLabel label;
	
	// 
	public boolean isTopLevelComponent;
	
	// To allow propagation even though the component isn't affected
	public boolean needsToPropagateAfterAbsorption;	
	public FailureType propagatingFailureType;
	public Component propagatingComponent;
	
	//NOURAN
	public boolean isControl;

	public Component(String name, ComponentType type, int inputPortsSize, int outputPortsSize) {
		this.name = name;
		this.portsNumber = inputPortsSize + outputPortsSize;
		this.inputPorts = new Component[inputPortsSize];
		this.outputPorts = new Component[outputPortsSize];
		this.type = type;
		this.id = ids++;
		this.absorbingComponents = new ArrayList<Component>();
		this.isTopLevelComponent = false;
	}

	public void changeStateTo(ComponentState newState) {
		this.state = newState;
	}
	
	// to inject a failure
	public void sendFailure() {
		FailureType failureType = specifyFailureType();		
		if (failureType != null) { // otherwise the component hasn't failed
			// the new to-be-injected failure
			Failure newFailure = new Failure(failureType, this);
			this.failure = newFailure;
			sendFailureOut(newFailure);
			
			
			if (this.type != ComponentType.BUSBAR) {
				failure.modifyFailure(this.specifyFailureType(), this);
				sendFailureIn(newFailure);
			}
			
		}
	}
	
	public void sendFailure(Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {
		// TODO Auto-generated method stub
		FailureType failureType = specifyFailureType();		
		if (failureType != null) { // otherwise the component hasn't failed
			// the new to-be-injected failure
			Failure newFailure = new Failure(failureType, this);
			this.failure = newFailure;
			sendFailureOut(newFailure , component , allStates , allTypes , allCustomizedStates , allDirections , ifCustomizedInjected);
			//sendFailureOut(newFailure);
			
			if (this.type != ComponentType.BUSBAR) {
				failure.modifyFailure(this.specifyFailureType(), this);
				sendFailureIn(newFailure);
			}
			
		}
	}
	
	private void sendFailureOut(Failure newFailure, Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {
		// TODO Auto-generated method stub
		boolean componentReached = false;
		System.out.println(this);
		
//		if (this.hasFailed())
			for (int i = 0; i < outputPorts.length; i++) {
				
				componentReached = failure.failedComponents.contains(outputPorts[i]);
				System.out.println("Component " + outputPorts[i] + " reached: " + componentReached);

				if (!componentReached) {
					
					if (this.specifyFailureType() != null) {
						failure.modifyFailure(this.specifyFailureType(), this);
						System.out.println("here");
					}
						
					else {
						failure.modifyFailure(this.propagatingFailureType, this.propagatingComponent);
						System.out.println(" or here");
					}
						
					outputPorts[i].receiveFailure(failure, 1 , component , allStates , allTypes , allCustomizedStates , allDirections , ifCustomizedInjected );
					outputPorts[i].previousComponentInPropagation = this;
				}
			}
	}

	private void receiveFailure(Failure failure, int direction, Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {
		// TODO Auto-generated method stub
		System.out.println(this.hasFailed());
		
		if (!this.hasFailed()) {
			
			System.out.println("..........................................");
			System.out.print("Component: "+ this.name + " is receiving " + failure.type + " from: ");
			System.out.println(failure.currentComponent.name);
			
			processFailure(failure, direction , component , allStates , allTypes , allCustomizedStates , allDirections , ifCustomizedInjected );
			
			System.out.println("Status after receiving failure: " + this.state);
			System.out.println("Component has failed: " + this.hasFailed());
			System.out.println("..........................................");
			
			// check after processing failure, if not defected -> add to list
			if (!this.hasFailed()) {
				failure.nonFailedComponents.add(this);
				if (needsToPropagateAfterAbsorption) {
					needsToPropagateAfterAbsorption = false;
					propagateFailureAfterAbsorption(failure);
				}
			}				
			else {
					propagateFailure(failure);
			}
		}	
	}

	public void processFailure(Failure failure2, int direction, Component component,
			ArrayList<ComponentState> allStates, ArrayList<ComponentType> allTypes,
			ArrayList<CustomizedState> allCustomizedStates, ArrayList<Integer> allDirections,
			ArrayList<Boolean> ifCustomizedInjected) {
		// TODO Auto-generated method stub
		
	}

	// to propagate a failure
	public void propagateFailure(Failure failure) {
		// now the component has failed, so we add it to the list of failed components
		failure.addComponentToFailedList(this);
		
		FailureType newFailureType = specifyFailureType();		
		if (newFailureType != null) { // otherwise the component hasn't failed
			// modifies the failure with the new failure type
			failure.modifyFailure(newFailureType, this);
			this.failure = failure;
			sendFailureOut(failure);
			
			if (this.type != ComponentType.BUSBAR) {
				failure.modifyFailure(this.specifyFailureType(), this);
				sendFailureIn(failure);
			}
		}
	}
	
	// to propagate a failure after absorption
	public void propagateFailureAfterAbsorption(Failure failure) {			
		// We need to make the current type and component of failure 
		// those of the component before the one that absorbed
		System.out.println("will try to propagate out");
		failure.modifyFailure(propagatingFailureType, propagatingComponent);
		sendFailureOut(failure);
		System.out.println("will try to propagate in");
		
		if (this.type != ComponentType.BUSBAR) {
			failure.modifyFailure(propagatingFailureType, propagatingComponent);
			sendFailureIn(failure);
		}
		
	}
	
	public void setPropagationParemeters(FailureType toBePropagatedType, Component previousComponent) {
		this.needsToPropagateAfterAbsorption = true;
		this.propagatingFailureType = toBePropagatedType;
		this.propagatingComponent = previousComponent;
	}

	// if the component doesn't appear failedComponentList -> propagate failure to it
	// otherwise, terminate propagation at this port
	public void sendFailureOut(Failure failure) {
		boolean componentReached = false;
		System.out.println(this);
		
//		if (this.hasFailed())
			for (int i = 0; i < outputPorts.length; i++) {
				
				componentReached = failure.failedComponents.contains(outputPorts[i]);
				System.out.println("Component " + outputPorts[i] + " reached: " + componentReached);

				if (!componentReached) {
					
					if (this.specifyFailureType() != null) {
						failure.modifyFailure(this.specifyFailureType(), this);
						System.out.println("here");
					}
						
					else {
						failure.modifyFailure(this.propagatingFailureType, this.propagatingComponent);
						System.out.println(" or here");
					}
						
					outputPorts[i].receiveFailure(failure, 1);
					outputPorts[i].previousComponentInPropagation = this;
				}
			}
	}

	public void sendFailureIn(Failure failure) { // upward direction of failure propagation
		boolean componentReached = false;
		System.out.println(this);
		
//		if (this.hasFailed())
			for (int i = 0; i < inputPorts.length; i++) {
				componentReached = failure.failedComponents.contains(inputPorts[i]);
				
				if (!componentReached) {					
					
					if (this.specifyFailureType() != null)
						failure.modifyFailure(this.specifyFailureType(), this);
					else
						failure.modifyFailure(this.propagatingFailureType, this.propagatingComponent);
					
					System.out.println(this);
					inputPorts[i].receiveFailure(failure, -1);
					inputPorts[i].previousComponentInPropagation = this;
				}
					
			}
	}
	
	// direction 1 ----> downward -> to an output port
	// direction -1 ----> upward -> to an input port

	public void receiveFailure(Failure failure, int direction) {
		System.out.println(this.hasFailed());
		
		if (!this.hasFailed()) {
			
			System.out.println("..........................................");
			System.out.print("Component: "+ this.name + " is receiving " + failure.type + " from: ");
			System.out.println(failure.currentComponent.name);
			
			processFailure(failure, direction);
			
			System.out.println("Status after receiving failure: " + this.state);
			System.out.println("Component has failed: " + this.hasFailed());
			System.out.println("..........................................");
			
			// check after processing failure, if not defected -> add to list
			if (!this.hasFailed()) {
				failure.nonFailedComponents.add(this);
				if (needsToPropagateAfterAbsorption) {
					needsToPropagateAfterAbsorption = false;
					propagateFailureAfterAbsorption(failure);
				}
			}				
			else {
					propagateFailure(failure);
			}
		}	
	}

	
	public int netInputPortsNumber() {
		int ports = this.inputPorts.length;
		
		for (Component comp: inputPorts) {
//			if (comp.type == ComponentType.BSCU)
//				ports--;
			if (comp.isControlComponent())
				ports--;
		}
		
		return ports;
	}
	
	
	public int netInputControlsNumber() {
		int ports = this.inputPorts.length;
		return ports - netInputPortsNumber();
	}
	
	public boolean isSignallingComponent() {
		if (this.isControl)
			return true;
		else {
		switch(this.type) {
		case BSCU: 
		case ACTUATOR_CONTROL_ELECTRONICS:
		case AIR_DATA_AND_INERTIAL_REFERENCE_UNIT:
		case DIRECT_ELECTRICAL_LINK:
		case PRIMARY_FLIGHT_CONTROL:
			return true;
		default: return false;	
		}
		}
	}
	
	public boolean isMechanicalControlComponent() {
		switch(this.type) {
		case CONTROL_ROD:
			return true;
		default: return false;	
		}
	}
	
	public boolean isControlComponent() {
		return isSignallingComponent() || isMechanicalControlComponent();
	}
	
	public int netUnfailedInputSignals() {
		int unfailed = 0;	
		for (Component comp: inputPorts) {
			if (comp.isSignallingComponent() && !comp.hasFailed())
				unfailed++;
		}	
		return unfailed;
	}
	
	public int netUnfailedInputMechnicalControl() {
		int unfailed = 0;	
		for (Component comp: inputPorts) {
			if (comp.isMechanicalControlComponent() && !comp.hasFailed())
				unfailed++;
		}	
		return unfailed;
	}
	
	public int netUnfailedInputControls() {
		return netUnfailedInputSignals() + netUnfailedInputMechnicalControl();
	}
	
	public int netUnfailedInputPorts() {
		int unfailed = 0;	
		for (Component comp: inputPorts) {
			if (!comp.isControlComponent() && !comp.hasFailed())
				unfailed++;
		}	
		return unfailed;
	}
	
	
	
	public void absorbFailure(Failure failure) {
		System.out.println(this + " absorbs failure: " + failure.type);
		this.isAbsorber = true;
		failure.sourceComponent.receiveAbsorption(failure, this);
	}
	
	public void receiveAbsorption(Failure failure, Component absorbingComponent) {
		this.failedButAbsorbed = true;
		this.absorbingComponent = absorbingComponent;
		if (!this.absorbingComponents.contains(absorbingComponent))
			this.absorbingComponents.add(absorbingComponent);
		this.failureCausedAbsorption = failure.type;
	}
	
	public abstract void injectFailure(FailureType failureType);

	// To be implemented per component
	
	// A method that resets component to nominal
	public void resetComponentState() {
		this.isAbsorber = false;
		previousComponentInPropagation = null;
		
		failedButAbsorbed = false;
		absorbingComponent = null;
		failureCausedAbsorption = null;
		absorbingComponents = new ArrayList<Component>() ;
	}
	
	public int netFailedOutPutPorts() {
		int failed = 0;
		for (Component comp: outputPorts) {
			if (comp.hasFailed())
				failed++;
		}
		return failed;
	}
	
	public int netFailedInputPorts() {
		int failed = 0;
		for (Component comp: inputPorts) {
			if (comp.hasFailed() && !comp.isSignallingComponent())
				failed++;
		}
		return failed;
	}
	
	public boolean isOutputPortOf(Component component) {
		for (Component port: component.outputPorts) {
			if (this.equals(port))
				return true;
		}
		return false;
	}
	
	public boolean isInputPortOf(Component component) {
		for (Component port: component.inputPorts) {
			if (this.equals(port))
				return true;
		}
		return false;
	}
	
	public  abstract Object[] getPossibleStates();
	
	public abstract void processFailure(Failure failure, int direction);
	
	// A method that returns true if the component has failed, false otherwise
	public abstract boolean hasFailed();
	
	// A method that specifies the failure according to the component state
	public abstract FailureType specifyFailureType();
	
	// For printing purposes, to print the name of component rather than its reference
	public String toString() {
		return this.name;
	}

	public void injectFailure(FailureType failureMode, Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {
		// TODO Auto-generated method stub
		
	}
}
