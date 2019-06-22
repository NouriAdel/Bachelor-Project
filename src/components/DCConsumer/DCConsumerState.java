package components.DCConsumer;

import components.ComponentState;

public class DCConsumerState extends ComponentState {
	public DCConsumerStates state;
	
	public DCConsumerState(DCConsumerStates state) {
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
