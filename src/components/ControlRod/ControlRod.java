package components.ControlRod;

import components.*;
import failures.*;

public class ControlRod extends Component {

	static ControlRodState defaultState = new ControlRodState(ControlRodStates.NOMINAL);

	public ControlRod(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.CONTROL_ROD, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public ControlRod(String name, int inputPortsSize, int outputPortsSize, ControlRodStates state) {
		super(name, ComponentType.CONTROL_ROD, inputPortsSize, outputPortsSize);
		this.state = new ControlRodState(state);
	}

	@Override
	public void processFailure(Failure failure, int direction) {
		switch (failure.currentComponent.type) {
		case PIPE:
			processPipeFailure(failure, direction);
			break;
		case VALVE:
			processValveFailure(failure, direction);
			break;
		case PUMP:
			processPumpFailure(failure, direction);
			break;
		case BSCU:
			processBSCUFailure(failure, direction);
			break;	
		case ACTUATOR_CONTROL_ELECTRONICS:
			processActuatorControlElectronicsFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: // To be defined;
			
		}

	}
	
	private void processActuatorControlElectronicsFailure(Failure failure, int direction) {
	}

	private void processBSCUFailure(Failure failure, int direction) {
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub

	}

	private void processValveFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
	}

	// If a valve connected to an already-failed pipe, it process the failure here
	private void processPipeFailure(Failure failure, int direction) {
		
	}

	@Override
	public FailureType specifyFailureType() {
		ControlRodStates currentState = ((ControlRodState) state).state;
		switch(currentState) {
		case DISCONNECTED:
			return FailureType.DISCONNECTED;
		case JAMMING:
			return FailureType.JAMMING;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		ControlRodStates currentState = ((ControlRodState) this.state).state;		
		return !currentState.equals(ControlRodStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new ControlRodState(ControlRodStates.NOMINAL));
		
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case DISCONNECTED:
			this.changeStateTo(new ControlRodState(ControlRodStates.DISCONNECTED));
			this.sendFailure();
			break;
		case JAMMING:
			this.changeStateTo(new ControlRodState(ControlRodStates.JAMMING));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {ControlRodStates.NOMINAL, ControlRodStates.DISCONNECTED, ControlRodStates.JAMMING};
	}
}
