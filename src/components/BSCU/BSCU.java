package components.BSCU;

import components.*;
import components.Valve.ValveState;
import components.Valve.ValveStates;
import failures.*;

public class BSCU extends Component {
	
	static BSCUState defaultState = new BSCUState(BSCUStates.NOMINAL);

	public BSCU(String name,int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.BSCU, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public BSCU(String name, int inputPortsSize, int outputPortsSize, BSCUStates state) {
		super(name, ComponentType.BSCU, inputPortsSize, outputPortsSize);
		this.state = new BSCUState(state);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new BSCUState(BSCUStates.NOMINAL));
		
	}

	@Override
	public void processFailure(Failure failure, int direction) {
		switch (failure.currentComponent.type) {
//		case PIPE:
//			processPipeFailure(failure, direction);
//			break;
		case VALVE:
			processValveFailure(failure, direction);
			break;
		case POWER_SUPPLY:
			processPowerSupplyFailure(failure, direction);
//		case PUMP:
//			processPumpFailure(failure, direction);
//			break;
//		case CUSTOMIZED_COMPONENT: // To be defined;
		}

	}
	
	// We assume that the BSCU will absorb any failure, except power loss (because it can only cause failures, not receive them) 

	private void processPowerSupplyFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			case LOSS_OF_POWER_SUPPLY:
				this.state = new BSCUState(BSCUStates.LOSS_OF_CONTROL_SIGNAL);
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

	private void processValveFailure(Failure failure, int direction) {
		// EMPTY for now
	}

	@Override
	public boolean hasFailed() {
		BSCUStates currentState = ((BSCUState) this.state).state;		
		return !currentState.equals(BSCUStates.NOMINAL);
	}

	@Override
	public FailureType specifyFailureType() {
		BSCUStates currentState = ((BSCUState) state).state;
		switch(currentState) {
		case LOSS_OF_CONTROL_SIGNAL:
			return FailureType.LOSS_OF_CONTROL_SIGNAL;
		default:
			return null;
		}
	}

	@Override
	public void injectFailure(FailureType failureType) {
		switch(failureType) {
		case LOSS_OF_CONTROL_SIGNAL:
			this.changeStateTo(new BSCUState(BSCUStates.LOSS_OF_CONTROL_SIGNAL));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}

	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {BSCUStates.NOMINAL, BSCUStates.LOSS_OF_CONTROL_SIGNAL};
	}

}
