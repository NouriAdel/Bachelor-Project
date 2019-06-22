package components.PrimaryFlightControl;

import components.ComponentState;

public class PrimaryFlightControlState extends ComponentState {
	public PrimaryFlightControlStates state;

	public PrimaryFlightControlState(PrimaryFlightControlStates state) {
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
