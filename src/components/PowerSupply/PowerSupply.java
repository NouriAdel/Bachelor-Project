package components.PowerSupply;

import components.Component;
import components.ComponentType;
import failures.Failure;
import failures.FailureType;

public class PowerSupply extends Component {
	static PowerSupplyState defaultState = new PowerSupplyState(
			PowerSupplyStates.NOMINAL);

	public PowerSupply(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.POWER_SUPPLY, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public PowerSupply(String name, int inputPortsSize, int outputPortsSize,
			PowerSupplyStates state) {
		super(name, ComponentType.POWER_SUPPLY, inputPortsSize, outputPortsSize);
		this.state = new PowerSupplyState(state);
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
		case CUSTOMIZED_COMPONENT: // To be defined;

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
		PowerSupplyStates currentState = ((PowerSupplyState) state).state;
		switch (currentState) {
		case LOSS_OF_POWER_SUPPLY:
			return FailureType.LOSS_OF_POWER_SUPPLY;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		PowerSupplyStates currentState = ((PowerSupplyState) this.state).state;
		return !currentState.equals(PowerSupplyStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new PowerSupplyState(PowerSupplyStates.NOMINAL));

	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch (failureType) {
		case LOSS_OF_POWER_SUPPLY:
			this.changeStateTo(
					new PowerSupplyState(PowerSupplyStates.LOSS_OF_POWER_SUPPLY));
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
				PowerSupplyStates.NOMINAL,
				PowerSupplyStates.LOSS_OF_POWER_SUPPLY,
		};
	}
}
