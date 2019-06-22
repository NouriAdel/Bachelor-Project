package components.Contactor;

import components.ComponentState;

public class ContactorState extends ComponentState {
	public ContactorStates state;
	
	public ContactorState(ContactorStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case STUCK_CLOSED: 
			str = "Stuck on closed";
			break;
		case STUCK_OPEN:
			str = "Stuck on opened";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
