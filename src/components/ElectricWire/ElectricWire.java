package components.ElectricWire;

import components.*;
import failures.*;

public class ElectricWire extends Component {
	
	static ElectricWireState defaultState = new ElectricWireState(ElectricWireStates.NOMINAL);

	public ElectricWire(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.PIPE, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public ElectricWire(String name, int inputPortsSize, int outputPortsSize, ElectricWireStates state) {
		super(name, ComponentType.PIPE, inputPortsSize, outputPortsSize);
		this.state = new ElectricWireState(state);
	}

	@Override
	public void processFailure(Failure failure, int direction) {

		switch(failure.currentComponent.type) {
		case PIPE: 
			processPipeFailure(failure, direction); 
			break;
		case	 VALVE:  
			processValveFailure(failure, direction); 
			break;
		case PUMP:  
			processPumpFailure(failure, direction); 
			break;
		case CYLINDER:
			processCylinderFailure(failure, direction);
		case GENERATOR:
			processGeneratorFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}

	private void processGeneratorFailure(Failure failure, int direction) {
	 // in all cases dir = 1 bec generator is always on top
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		switch (failure.type) {
		case NO_PRESSURE: // scenario  of pump having no pressure
			if (direction == 1) {
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
				this.state = new PipeState(PipeStates.LEAK); // no pressure
				}
				else {
					System.out.println(this + " absorbs failure: " + failure.type);
					
					// if I absorb -> I may be used in the 2nd iteration
					// save this at the causing component
					this.isAbsorber = true;
					failure.sourceComponent.receiveAbsorption(failure, this);
					break;
				}
			}
			else { // dir = -1
				this.state = new PipeState(PipeStates.LEAK); // no pressure
			}
			
			break;
		case OVER_PRESSURED: //	 scenario  of pump being over pressurized
			this.state = new PipeState(PipeStates.LEAK); // pipe leaks or bursts
			break;

		}
	}

	// If a pipe connected to an already-failed valve, it process the failure here 
	private void processValveFailure(Failure failure, int direction) {
		
		switch (failure.type) {
		case STUCK: {
			// case 1: dir = -1. E.g: main pipe receiving STUCK from valve A
			if (direction == -1) {
				// if I have extra output ports, I will absorb the failure
				if (this.outputPorts.length - netFailedOutPutPorts() > 0) {
					System.out.println(this + " absorbs failure: " + failure.type);
					
					// if I absorb -> I may be used in the 2nd iteration
					// save this at the causing component
					failure.sourceComponent.receiveAbsorption(failure, this);
					this.isAbsorber = true;
					break;
				}
				
				else {
					// the pipe may become over pressured and burst => may leak
					this.state = new PipeState(PipeStates.LEAK); // no pressure
					break;
				}
			}
			else { // dir == 1
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
					// the pipe may not receive required pressure => may leak ???
					this.state = new PipeState(PipeStates.LEAK); // no pressure
					break;
				}
				else {
					// if I absorb -> I may be used in the 2nd iteration
					// save this at the causing component
					failure.sourceComponent.receiveAbsorption(failure, this);
					this.isAbsorber = true;
					break;
				}
				
			}
		}
			
		case LEAK:	
			if (direction == 1) {
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
					this.state = new PipeState(PipeStates.LEAK); // no pressure
					break;
				}
				else {
					//absorbs error
					System.out.println(this + " absorbs failure: " + failure.type);
					
					// if I absorb -> I may be used in the 2nd iteration
					// save this at the causing component
					failure.sourceComponent.receiveAbsorption(failure, this);
					this.isAbsorber = true;
					break;
				}
			}
			else { // dir = -1
				
				this.state = new PipeState(PipeStates.LEAK); // no pressure
				break;
			}
			
		}
	}

	private void processPipeFailure(Failure failure, int direction) {

	}
	
	private void processCylinderFailure(Failure failure, int direction) {
		if (direction == 1) {
			System.out.println(this + " absorbs failure: " + failure.type);
			
			// if I absorb -> I may be used in the 2nd iteration
			// save this at the causing component
			this.isAbsorber = true;
			failure.sourceComponent.receiveAbsorption(failure, this);
		}
		else {
			switch(failure.type) {
			case STUCK:
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
					this.state = new PipeState(PipeStates.BLOCKED); // no pressure
				}
				else {
					System.out.println(this + " absorbs failure: " + failure.type);
					
					// if I absorb -> I may be used in the 2nd iteration
					// save this at the causing component
					this.isAbsorber = true;
					failure.sourceComponent.receiveAbsorption(failure, this);
					break;
				}
			case LEAK:
				if (netInputPortsNumber() - netFailedInputPorts() <= 0) {
					this.state = new CylinderState(CylinderStates.LEAK); // no pressure
				}
				else {
					System.out.println(this + " absorbs failure: " + failure.type);
					
					// if I absorb -> I may be used in the 2nd iteration
					// save this at the causing component
					this.isAbsorber = true;
					failure.sourceComponent.receiveAbsorption(failure, this);
					break;
				}			
			}
		}
	}
	
	@Override
	public FailureType specifyFailureType() {
		PipeStates currentState = ((PipeState) state).state;
		switch(currentState) {
		case BLOCKED:
			return FailureType.BLOCKED;
		case LEAK:
			return FailureType.LEAK;
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		PipeStates currentState = ((PipeState) this.state).state;		
		return !currentState.equals(PipeStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new PipeState(PipeStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case BLOCKED:
			this.changeStateTo(new PipeState(PipeStates.BLOCKED));
			this.sendFailure();
			break;
		case LEAK:
			this.changeStateTo(new PipeState(PipeStates.LEAK));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {PipeStates.NOMINAL, PipeStates.BLOCKED, PipeStates.LEAK};
	}

}
