package components;

public class CustomizedState extends ComponentState {
	public String state;
	
	public CustomizedState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return null;
		return state;
	}
}
