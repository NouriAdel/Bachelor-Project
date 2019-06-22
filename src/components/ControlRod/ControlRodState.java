package components.ControlRod;

import components.ComponentState;

public class ControlRodState extends ComponentState {
	public ControlRodStates state;
		
	public ControlRodState(ControlRodStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case JAMMING: 
			str = "Jamming";
			break;
		case DISCONNECTED:
			str = "Disconnected";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
