package components.PrimaryFlightControl;

import components.Component;
import components.ComponentType;
import failures.Failure;
import failures.FailureType;

public class PrimaryFlightControl extends Component {
	static PrimaryFlightControlState defaultState = new PrimaryFlightControlState(
			PrimaryFlightControlStates.NOMINAL);

	public PrimaryFlightControl(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.PRIMARY_FLIGHT_CONTROL, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public PrimaryFlightControl(String name, int inputPortsSize, int outputPortsSize,
			PrimaryFlightControlStates state) {
		super(name, ComponentType.PRIMARY_FLIGHT_CONTROL, inputPortsSize, outputPortsSize);
		this.state = new PrimaryFlightControlState(state);
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
		case AIR_DATA_AND_INERTIAL_REFERENCE_UNIT:
			processAirDataAndInertialReferenceUnitFailure(failure, direction);
			break;
		case ACTUATOR_CONTROL_ELECTRONICS:
			processActuatorControlElectronicFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: // To be defined;

		}

	}
	
	private void processActuatorControlElectronicFailure(Failure failure, int direction) {
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

	private void processAirDataAndInertialReferenceUnitFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			case LOSS_OR_INVALID_OUTPUT_SIGNAL:
				this.state = new PrimaryFlightControlState(PrimaryFlightControlStates.LOSS_OR_INVALID_CONTROL_SIGNAL);
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
					this.state = new PrimaryFlightControlState(PrimaryFlightControlStates.LOSS_OR_INVALID_CONTROL_SIGNAL);
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
		PrimaryFlightControlStates currentState = ((PrimaryFlightControlState) state).state;
		switch (currentState) {
		case LOSS_OF_POWER_SUPPLY:
			return FailureType.LOSS_OF_POWER_SUPPLY;
		case LOSS_OF_INPUT_SIGNAL:
			return FailureType.LOSS_OF_INPUT_SIGNAL;
		case LOSS_OR_INVALID_CONTROL_SIGNAL:
			return FailureType.LOSS_OR_INVALID_CONTROL_SIGNAL;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		PrimaryFlightControlStates currentState = ((PrimaryFlightControlState) this.state).state;
		return !currentState.equals(PrimaryFlightControlStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new PrimaryFlightControlState(PrimaryFlightControlStates.NOMINAL));

	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch (failureType) {
		case LOSS_OF_POWER_SUPPLY:
			this.changeStateTo(
					new PrimaryFlightControlState(PrimaryFlightControlStates.LOSS_OF_POWER_SUPPLY));
			this.sendFailure();
			break;
		case LOSS_OF_INPUT_SIGNAL:
			this.changeStateTo(
					new PrimaryFlightControlState(PrimaryFlightControlStates.LOSS_OF_INPUT_SIGNAL));
			this.sendFailure();
			break;
		case LOSS_OR_INVALID_CONTROL_SIGNAL:
			this.changeStateTo(new PrimaryFlightControlState(
					PrimaryFlightControlStates.LOSS_OR_INVALID_CONTROL_SIGNAL));
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
				PrimaryFlightControlStates.NOMINAL,
				PrimaryFlightControlStates.LOSS_OF_POWER_SUPPLY,
				PrimaryFlightControlStates.LOSS_OF_INPUT_SIGNAL,
				PrimaryFlightControlStates.LOSS_OR_INVALID_CONTROL_SIGNAL,
		};
	}

}
