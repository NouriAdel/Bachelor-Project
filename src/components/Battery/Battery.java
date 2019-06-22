package components.Battery;

import components.*;
import failures.*;

public class Battery extends Component {
	
	static BatteryState defaultState = new BatteryState(BatteryStates.NOMINAL);

	public Battery(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.BATTERY, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public Battery(String name, int inputPortsSize, int outputPortsSize, BatteryStates state) {
		super(name, ComponentType.BATTERY, inputPortsSize, outputPortsSize);
		this.state = new BatteryState(state);
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
		case BUSBAR:
			processBusbarFailure(failure, direction);
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}

	private void processBusbarFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			default:;
			}
		}
		else { // dir = -1
			switch(failure.type) {
			case FAILED_TO_CONDUCT:
//				this.state = new BatteryState(BatteryStates.UNDER_CHARGED);
				this.absorbFailure(failure);
				break;
			case SHORT_CIRCUIT:
				this.state = new BatteryState(BatteryStates.SHORT_CIRCUIT);
				break;
			default:;	
			}
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
		BatteryStates currentState = ((BatteryState) state).state;
		switch(currentState) {
		case UNDER_CHARGED:
			return FailureType.UNDER_CHARGED;
		case SHORT_CIRCUIT:
			return FailureType.SHORT_CIRCUIT;
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		BatteryStates currentState = ((BatteryState) this.state).state;		
		return !currentState.equals(BatteryStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new BatteryState(BatteryStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case UNDER_CHARGED:
			this.changeStateTo(new BatteryState(BatteryStates.UNDER_CHARGED));
			this.sendFailure();
			break;
		case SHORT_CIRCUIT:
			this.changeStateTo(new BatteryState(BatteryStates.SHORT_CIRCUIT));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {
				BatteryStates.NOMINAL,
				BatteryStates.UNDER_CHARGED,
				BatteryStates.SHORT_CIRCUIT
		};
	}

}
