package components.AirDataAndInertialReferenceUnit;

import components.Component;
import components.ComponentType;
import components.ActuatorControlElectronics.ActuatorControlElectronicsState;
import components.ActuatorControlElectronics.ActuatorControlElectronicsStates;
import failures.Failure;
import failures.FailureType;

public class AirDataAndInertialReferenceUnit extends Component {
	static AirDataAndInertialReferenceUnitState defaultState = new AirDataAndInertialReferenceUnitState(
			AirDataAndInertialReferenceUnitStates.NOMINAL);

	public AirDataAndInertialReferenceUnit(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.AIR_DATA_AND_INERTIAL_REFERENCE_UNIT, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public AirDataAndInertialReferenceUnit(String name, int inputPortsSize, int outputPortsSize,
			AirDataAndInertialReferenceUnitStates state) {
		super(name, ComponentType.AIR_DATA_AND_INERTIAL_REFERENCE_UNIT, inputPortsSize, outputPortsSize);
		this.state = new AirDataAndInertialReferenceUnitState(state);
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
		case POWER_SUPPLY:
			processPowerSupplyFailure(failure, direction);
			break;
		case PRIMARY_FLIGHT_CONTROL:
			processPrimaryControlFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: // To be defined;

		}

	}
	
	private void processPrimaryControlFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			// empty for now
			default: this.absorbFailure(failure);	
			}
		}
		
	}

	private void processPowerSupplyFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			case LOSS_OF_POWER_SUPPLY:
				// check if no other source of power from other ports
				// if there is more than 1 unfailed port -> absorb
				if (netInputPortsNumber() - netFailedInputPorts() > 1)
					this.absorbFailure(failure);
				else
					this.state = new AirDataAndInertialReferenceUnitState(AirDataAndInertialReferenceUnitStates.LOSS_OR_INVALID_OUTPUT_SIGNAL);
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
		AirDataAndInertialReferenceUnitStates currentState = ((AirDataAndInertialReferenceUnitState) state).state;
		switch (currentState) {
		case LOSS_OF_POWER_SUPPLY:
			return FailureType.LOSS_OF_POWER_SUPPLY;
		case LOSS_OR_INVALID_OUTPUT_SIGNAL:
			return FailureType.LOSS_OR_INVALID_OUTPUT_SIGNAL;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		AirDataAndInertialReferenceUnitStates currentState = ((AirDataAndInertialReferenceUnitState) this.state).state;
		return !currentState.equals(AirDataAndInertialReferenceUnitStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new AirDataAndInertialReferenceUnitState(AirDataAndInertialReferenceUnitStates.NOMINAL));

	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch (failureType) {
		case LOSS_OF_POWER_SUPPLY:
			this.changeStateTo(
					new AirDataAndInertialReferenceUnitState(AirDataAndInertialReferenceUnitStates.LOSS_OF_POWER_SUPPLY));
			this.sendFailure();
			break;
		case LOSS_OR_INVALID_OUTPUT_SIGNAL:
			this.changeStateTo(
					new AirDataAndInertialReferenceUnitState(AirDataAndInertialReferenceUnitStates.LOSS_OR_INVALID_OUTPUT_SIGNAL));
			this.sendFailure();
			break;
		default:
			return;
		}

	}

	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object[] { 
				AirDataAndInertialReferenceUnitStates.NOMINAL,
				AirDataAndInertialReferenceUnitStates.LOSS_OF_POWER_SUPPLY,
				AirDataAndInertialReferenceUnitStates.LOSS_OR_INVALID_OUTPUT_SIGNAL,
		};
	}
}
