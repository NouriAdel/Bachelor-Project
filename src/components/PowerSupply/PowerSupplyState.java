package components.PowerSupply;

import components.ComponentState;

public class PowerSupplyState extends ComponentState {
	public PowerSupplyStates state;

	public PowerSupplyState(PowerSupplyStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case LOSS_OF_POWER_SUPPLY:
			str = "leak or not allowing pressure";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
