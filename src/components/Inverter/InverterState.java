package components.Inverter;

import components.ComponentState;

public class InverterState extends ComponentState {
	public InverterStates state;
	
	public InverterState(InverterStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case REVERSE_CURRENT: 
			str = "Reverse current";
			break;
		case OVER_VOLTAGE: 
			str = "Over voltage";
			break;
		case UNDER_VOLTAGE: 
			str = "Under voltage";
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
