package components.Busbar;

import components.ComponentState;

public class BusbarState extends ComponentState {
	public BusbarStates state;
	
	public BusbarState(BusbarStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case FAILED_TO_CONDUCT: 
			str = "Failed to conduct";
		case CONDUCTING_OVER_VOLTAGE: 
			str = "Conducting over voltage";
			break;
		case CONDUCTING_UNDER_VOLTAGE: 
			str = "Conducting under voltage";
			break;	
		default:
			str = "Nominal";
		}
		return str;
	}
}
