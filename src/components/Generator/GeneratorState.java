package components.Generator;

import components.ComponentState;

public class GeneratorState extends ComponentState {
	public GeneratorStates state;
	
	public GeneratorState(GeneratorStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case OVER_VOLTAGE: 
			str = "Over voltage";
			break;
		case LOSS_OF_POWER: 
			str = "Loss of power";
			break;	
		case UNDER_VOLTAGE: 
			str = "Under voltage";
			break;	
		default:
			str = "Nominal";
		}
		return str;
	}
}
