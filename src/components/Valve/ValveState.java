package components.Valve;

import components.ComponentState;

public class ValveState extends ComponentState {
	public ValveStates state;
	
	public ValveState(ValveStates state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case LEAK: 
			str = "leak or not allowing pressure";
			break;
		case STUCK:
			str = "Stuck";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
