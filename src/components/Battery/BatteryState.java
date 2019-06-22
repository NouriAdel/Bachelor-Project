package components.Battery;

import components.ComponentState;

public class BatteryState extends ComponentState {
	public BatteryStates state;
	
	public BatteryState(BatteryStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case UNDER_CHARGED: 
			str = "Under charging";
			break;	
		case SHORT_CIRCUIT:
			str = "Short circuit";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
