package components.ACConsumer;

import components.ComponentState;

public class ACConsumerState extends ComponentState {
	public ACConsumerStates state;
	
	public ACConsumerState(ACConsumerStates state) {
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
		case OPEN_CIRCUIT:
			str = "Open circuit";
			break;	
		default:
			str = "Nominal";
		}
		return str;
	}
}
