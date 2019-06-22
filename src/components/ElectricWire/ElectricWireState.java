package components.ElectricWire;

import components.ComponentState;

public class ElectricWireState extends ComponentState {
	public ElectricWireStates state;
	
	public ElectricWireState(ElectricWireStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case BROKEN: 
			str = "Broken wire";
			break;
		case CARRYING_REVERSE_CURRENT: 
			str = "Wire has reverse current or under voltaged";
			break;
		case CARRYING_HIGH_CURRENT: 
			str = "Wire has high current";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
