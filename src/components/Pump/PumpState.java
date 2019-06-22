package components.Pump;

import components.ComponentState;

public class PumpState extends ComponentState {
	public PumpStates state;
	
	public PumpState(PumpStates state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case NO_PRESSURE: 
			str = "No pressure";
			break;
		case OVER_PRESSURED:
			str = "Over pressured";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
