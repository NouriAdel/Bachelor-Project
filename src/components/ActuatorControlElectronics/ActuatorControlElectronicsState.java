package components.ActuatorControlElectronics;

import components.ComponentState;

public class ActuatorControlElectronicsState extends ComponentState {
	public ActuatorControlElectronicsStates state;

	public ActuatorControlElectronicsState(ActuatorControlElectronicsStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case LOSS_OF_POWER_SUPPLY:
			str = "leak or not allowing pressure";
			break;
		case LOSS_OR_INVALID_CONTROL_SIGNAL:
			str = "No or invalid control signal";
			break;
		case LOSS_OF_INPUT_SIGNAL:
			str = "loss of input signal";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
