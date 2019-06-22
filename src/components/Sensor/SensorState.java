package components.Sensor;

import components.ComponentState;

public class SensorState extends ComponentState {

	public SensorStates state;

	public SensorState(SensorStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case INVALID_OUTPUT_SIGNAL:
			str = "Invalid of output signal";
			break;
		case LOSS_OF_OUTPUT_SIGNAL:
			str = "loss of output signal";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
