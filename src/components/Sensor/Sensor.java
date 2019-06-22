package components.Sensor;

import components.Component;
import components.ComponentType;
import failures.Failure;
import failures.FailureType;

public class Sensor extends Component {
	static SensorState defaultState = new SensorState(
			SensorStates.NOMINAL);

	public Sensor(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.SENSOR, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public Sensor(String name, int inputPortsSize, int outputPortsSize,
			SensorStates state) {
		super(name, ComponentType.SENSOR, inputPortsSize, outputPortsSize);
		this.state = new SensorState(state);
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
		case CUSTOMIZED_COMPONENT: // To be defined;

		}

	}
	
	private void processPowerSupplyFailure(Failure failure, int direction) {
		
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
		SensorStates currentState = ((SensorState) state).state;
		switch (currentState) {
		case INVALID_OUTPUT_SIGNAL:
			return FailureType.INVALID_OUTPUT_SIGNAL;
		case LOSS_OF_OUTPUT_SIGNAL:
			return FailureType.LOSS_OF_OUTPUT_SIGNAL;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		SensorStates currentState = ((SensorState) this.state).state;
		return !currentState.equals(SensorStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new SensorState(SensorStates.NOMINAL));

	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch (failureType) {
		case INVALID_OUTPUT_SIGNAL:
			this.changeStateTo(
					new SensorState(SensorStates.INVALID_OUTPUT_SIGNAL));
			this.sendFailure();
			break;
		case LOSS_OF_OUTPUT_SIGNAL:
			this.changeStateTo(
					new SensorState(SensorStates.LOSS_OF_OUTPUT_SIGNAL));
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
				SensorStates.NOMINAL,
				SensorStates.INVALID_OUTPUT_SIGNAL,
				SensorStates.LOSS_OF_OUTPUT_SIGNAL,
		};
	}
}
