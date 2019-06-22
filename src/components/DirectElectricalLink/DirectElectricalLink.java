package components.DirectElectricalLink;

import components.Component;
import components.ComponentType;
import failures.Failure;
import failures.FailureType;

public class DirectElectricalLink extends Component {
	static DirectElectricalLinkState defaultState = new DirectElectricalLinkState(
			DirectElectricalLinkStates.NOMINAL);

	public DirectElectricalLink(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.DIRECT_ELECTRICAL_LINK, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public DirectElectricalLink(String name, int inputPortsSize, int outputPortsSize,
			DirectElectricalLinkStates state) {
		super(name, ComponentType.DIRECT_ELECTRICAL_LINK, inputPortsSize, outputPortsSize);
		this.state = new DirectElectricalLinkState(state);
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
		DirectElectricalLinkStates currentState = ((DirectElectricalLinkState) state).state;
		switch (currentState) {
		case LOSS_OF_ELECTRICAL_SIGNAL:
			return FailureType.LOSS_OF_ELECTRICAL_SIGNAL;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		DirectElectricalLinkStates currentState = ((DirectElectricalLinkState) this.state).state;
		return !currentState.equals(DirectElectricalLinkStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new DirectElectricalLinkState(DirectElectricalLinkStates.NOMINAL));

	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch (failureType) {
		case LOSS_OF_ELECTRICAL_SIGNAL:
			this.changeStateTo(
					new DirectElectricalLinkState(DirectElectricalLinkStates.LOSS_OF_ELECTRICAL_SIGNAL));
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
				DirectElectricalLinkStates.NOMINAL,
				DirectElectricalLinkStates.LOSS_OF_ELECTRICAL_SIGNAL,
		};
	}

}
