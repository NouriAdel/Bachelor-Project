package components.DCConsumer;

import components.*;
import failures.*;

public class DCConsumer extends Component {
	
	static DCConsumerState defaultState = new DCConsumerState(DCConsumerStates.NOMINAL);

	public DCConsumer(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.DCCONSUMER, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public DCConsumer(String name, int inputPortsSize, int outputPortsSize, DCConsumerStates state) {
		super(name, ComponentType.DCCONSUMER, inputPortsSize, outputPortsSize);
		this.state = new DCConsumerState(state);
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
			break;
		case CONTACTOR:
			processContactorFailure(failure, direction); 
			break;
		case BUSBAR:
			processBusbarFailure(failure, direction); 
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}

	private void processBusbarFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case FAILED_TO_CONDUCT:
				this.state = new DCConsumerState(DCConsumerStates.OPEN_CIRCUIT);
				break;
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			// empty for now
			default:;	
			}
		}
		
	}

	private void processContactorFailure(Failure failure, int direction) {
		// dir always 1 because it has no outputs
		switch(failure.type) {
		case STUCK_OPEN:
			this.state = new DCConsumerState(DCConsumerStates.OPEN_CIRCUIT);
			break;
		case STUCK_CLOSED:
			this.absorbFailure(failure);
			break;
		}
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		
	}

	// If a pipe connected to an already-failed valve, it process the failure here 
	private void processValveFailure(Failure failure, int direction) {

	}

	private void processPipeFailure(Failure failure, int direction) {

	}
	
	private void processCylinderFailure(Failure failure, int direction) {
		
	}
	
	@Override
	public FailureType specifyFailureType() {
		DCConsumerStates currentState = ((DCConsumerState) state).state;
		switch(currentState) {
		case REVERSE_CURRENT:
			return FailureType.REVERSE_CURRENT;
		case OVER_VOLTAGE:
			return FailureType.OVER_VOLTAGE;
		case UNDER_VOLTAGE:
			return FailureType.UNDER_VOLTAGE;
		case SHORT_CIRCUIT:
			return FailureType.SHORT_CIRCUIT;
		case OPEN_CIRCUIT:
			return FailureType.OPEN_CIRCUIT;	
		default: 
			return null;	
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		DCConsumerStates currentState = ((DCConsumerState) this.state).state;		
		return !currentState.equals(DCConsumerStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new DCConsumerState(DCConsumerStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case REVERSE_CURRENT:
			this.changeStateTo(new DCConsumerState(DCConsumerStates.REVERSE_CURRENT));
			this.sendFailure();
			break;
		case OVER_VOLTAGE:
			this.changeStateTo(new DCConsumerState(DCConsumerStates.OVER_VOLTAGE));
			this.sendFailure();
			break;
		case UNDER_VOLTAGE:
			this.changeStateTo(new DCConsumerState(DCConsumerStates.UNDER_VOLTAGE));
			this.sendFailure();
			break;
		case SHORT_CIRCUIT:
			this.changeStateTo(new DCConsumerState(DCConsumerStates.SHORT_CIRCUIT));
			this.sendFailure();
			break;	
		case OPEN_CIRCUIT:
			this.changeStateTo(new DCConsumerState(DCConsumerStates.OPEN_CIRCUIT));
			this.sendFailure();
			break;	
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {
				DCConsumerStates.NOMINAL, DCConsumerStates.REVERSE_CURRENT, 
				DCConsumerStates.OVER_VOLTAGE, DCConsumerStates.UNDER_VOLTAGE, 
				DCConsumerStates.SHORT_CIRCUIT, DCConsumerStates.OPEN_CIRCUIT
		};
	}

}
