package components.BSCU;

import components.ComponentState;

public class BSCUState extends ComponentState {

	public BSCUStates state;
	
	public BSCUState(BSCUStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case LOSS_OF_CONTROL_SIGNAL: 
			str = "Loss of control signal";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}

}
