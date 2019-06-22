package components.ActuatorControlElectronics;

import components.Component;
import components.ComponentType;
import components.PrimaryFlightControl.PrimaryFlightControlState;
import components.PrimaryFlightControl.PrimaryFlightControlStates;
import failures.Failure;
import failures.FailureType;

public class ActuatorControlElectronics extends Component {
	static ActuatorControlElectronicsState defaultState = new ActuatorControlElectronicsState(
			ActuatorControlElectronicsStates.NOMINAL);

	public ActuatorControlElectronics(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.ACTUATOR_CONTROL_ELECTRONICS, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public ActuatorControlElectronics(String name, int inputPortsSize, int outputPortsSize,
			ActuatorControlElectronicsStates state) {
		super(name, ComponentType.ACTUATOR_CONTROL_ELECTRONICS, inputPortsSize, outputPortsSize);
		this.state = new ActuatorControlElectronicsState(state);
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
			processPrimaryFlightControlFailure(failure, direction);
			break;
		case DIRECT_ELECTRICAL_LINK:
			processDirectElectricLinkFailure(failure, direction);
			break;
		case SENSOR:
			processSensorFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: // To be defined;

		}

	}
	
	private void processSensorFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case LOSS_OF_OUTPUT_SIGNAL:
			case INVALID_OUTPUT_SIGNAL:	
				this.absorbFailure(failure);
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

	private void processDirectElectricLinkFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			case LOSS_OF_ELECTRICAL_SIGNAL:
				// check if no other source of signal from other ports
				// if there is more than 1 unfailed port -> absorb
				if (netUnfailedInputSignals() > 0)
					this.absorbFailure(failure);
				else
					this.state = new ActuatorControlElectronicsState(ActuatorControlElectronicsStates.LOSS_OR_INVALID_CONTROL_SIGNAL);
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

	private void processPrimaryFlightControlFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case LOSS_OR_INVALID_CONTROL_SIGNAL:
				// check if no other source of signal from other ports
				// if there is more than 1 unfailed port -> absorb
				System.out.println(netUnfailedInputSignals() );
				if (netUnfailedInputSignals() > 0)
					this.absorbFailure(failure);
				else
					this.state = new ActuatorControlElectronicsState(ActuatorControlElectronicsStates.LOSS_OR_INVALID_CONTROL_SIGNAL);
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
				// if there is more than >= 1 unfailed port -> absorb
				if (netUnfailedInputPorts() > 0)
					this.absorbFailure(failure);
				else
					this.state = new ActuatorControlElectronicsState(ActuatorControlElectronicsStates.LOSS_OR_INVALID_CONTROL_SIGNAL);
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
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			// empty for now
			case STUCK:
			case BLOCKED:
				this.absorbFailure(failure);
			default:;	
			}
		}
	}

	// If a valve connected to an already-failed pipe, it process the failure here
	private void processPipeFailure(Failure failure, int direction) {

	}

	@Override
	public FailureType specifyFailureType() {
		ActuatorControlElectronicsStates currentState = ((ActuatorControlElectronicsState) state).state;
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
		ActuatorControlElectronicsStates currentState = ((ActuatorControlElectronicsState) this.state).state;
		return !currentState.equals(ActuatorControlElectronicsStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new ActuatorControlElectronicsState(ActuatorControlElectronicsStates.NOMINAL));

	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch (failureType) {
		case LOSS_OF_POWER_SUPPLY:
			this.changeStateTo(
					new ActuatorControlElectronicsState(ActuatorControlElectronicsStates.LOSS_OF_POWER_SUPPLY));
			this.sendFailure();
			break;
		case LOSS_OF_INPUT_SIGNAL:
			this.changeStateTo(
					new ActuatorControlElectronicsState(ActuatorControlElectronicsStates.LOSS_OF_INPUT_SIGNAL));
			this.sendFailure();
			break;
		case LOSS_OR_INVALID_CONTROL_SIGNAL:
			this.changeStateTo(new ActuatorControlElectronicsState(
					ActuatorControlElectronicsStates.LOSS_OR_INVALID_CONTROL_SIGNAL));
			this.sendFailure();
			break;
		default:
			return;
		}

	}

	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object[] { ActuatorControlElectronicsStates.NOMINAL,
				ActuatorControlElectronicsStates.LOSS_OF_POWER_SUPPLY,
				ActuatorControlElectronicsStates.LOSS_OF_INPUT_SIGNAL,
				ActuatorControlElectronicsStates.LOSS_OR_INVALID_CONTROL_SIGNAL,
		};
	}

}
