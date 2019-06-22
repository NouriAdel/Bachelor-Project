package components.AirDataAndInertialReferenceUnit;

import components.ComponentState;

public class AirDataAndInertialReferenceUnitState extends ComponentState {
	public AirDataAndInertialReferenceUnitStates state;

	public AirDataAndInertialReferenceUnitState(AirDataAndInertialReferenceUnitStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case LOSS_OF_POWER_SUPPLY:
			str = "loss of power supply";
			break;
		case LOSS_OR_INVALID_OUTPUT_SIGNAL:
			str = "loss/invalid of output signal";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
