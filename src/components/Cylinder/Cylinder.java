package components.Cylinder;

import components.*;
import failures.*;

public class Cylinder extends Component {
	
	static CylinderState defaultState = new CylinderState(CylinderStates.NOMINAL);

	public Cylinder(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.CYLINDER, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public Cylinder(String name, int inputPortsSize, int outputPortsSize, CylinderStates state) {
		super(name, ComponentType.CYLINDER, inputPortsSize, outputPortsSize);
		this.state = new CylinderState(state);
	}

	@Override
	public void processFailure(Failure failure, int direction) {
		
		switch(failure.currentComponent.type) {
		case PIPE:
			processPipeFailure(failure, direction);
			break;
		case CYLINDER: 
			processCylinderFailure(failure, direction); 
			break;
		case	 VALVE:  
			processValveFailure(failure, direction); 
			break;
		case PUMP:  
			processPumpFailure(failure, direction); 
			break;
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	
	}

	private void processPipeFailure(Failure failure, int direction) {
		// no components can be connected to its output ports
		if (direction == -1) {
			this.absorbFailure(failure);
		}
		
		else {
			switch(failure.type) {
			case BLOCKED:
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
					this.state = new CylinderState(CylinderStates.STUCK); // no pressure
				}
				else {
					this.absorbFailure(failure);
				}
			case LEAK:
//				System.out.println(netInputPortsNumber());
//				System.out.println(netFailedInputPorts());
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
					this.state = new CylinderState(CylinderStates.LEAK); // no pressure
				}
				else {
					this.absorbFailure(failure);
				}			
			}
		}
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		
	}

	// If a Cylinder connected to an already-failed valve, it process the failure here 
	private void processValveFailure(Failure failure, int direction) {
		
	}

	private void processCylinderFailure(Failure failure, int direction) {

	}
	
	@Override
	public FailureType specifyFailureType() {
		CylinderStates currentState = ((CylinderState) state).state;
		switch(currentState) {
		case STUCK:
			return FailureType.STUCK;
		case LEAK:
			return FailureType.LEAK;
		case DISCONNECTED:
			return FailureType.DISCONNECTED;
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		CylinderStates currentState = ((CylinderState) this.state).state;		
		return !currentState.equals(CylinderStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new CylinderState(CylinderStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case STUCK:
			this.changeStateTo(new CylinderState(CylinderStates.STUCK));
			this.sendFailure();
			break;
		case LEAK:
			this.changeStateTo(new CylinderState(CylinderStates.LEAK));
			this.sendFailure();
			break;
		case DISCONNECTED:
			this.changeStateTo(new CylinderState(CylinderStates.DISCONNECTED));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {CylinderStates.NOMINAL, CylinderStates.STUCK, CylinderStates.LEAK, CylinderStates.DISCONNECTED};
	}

}

