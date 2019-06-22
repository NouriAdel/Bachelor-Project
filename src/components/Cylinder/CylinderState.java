package components.Cylinder;

import components.ComponentState;

public class CylinderState extends ComponentState {
	CylinderStates state;
	
	public CylinderState(CylinderStates state) {
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
		case DISCONNECTED:
			str = "Disconnected";
		default:
			str = "Nominal";
		}
		return str;
	}
}
