package components.Pipe;

import components.ComponentState;

public class PipeState extends ComponentState {
	public PipeStates state;
	
	public PipeState(PipeStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case LEAK: 
			str = "Leak or burst => no or under pressure in pipe";
			break;
		case BLOCKED:
			str = "Blocked";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
