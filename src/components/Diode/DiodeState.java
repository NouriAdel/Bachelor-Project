package components.Diode;

import components.ComponentState;

public class DiodeState extends ComponentState {
	public DiodeStates state;
	
	public DiodeState(DiodeStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case STUCK_CLOSED: 
			str = "Stuck closed";
			break;	
		default:
			str = "Nominal";
		}
		return str;
	}
}
