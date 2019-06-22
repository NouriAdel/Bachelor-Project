package components;

import java.util.ArrayList;

import failures.Failure;
import failures.FailureType;
import system.Controllers.CreateComponentController;

public class CustomizedComponent extends Component {
	
	public ArrayList<ComponentState> allStates ;
	public ArrayList<CustomizedState> allCustomizedStates ;
	public ArrayList<ComponentType> allTypes ;
	public ArrayList<Boolean> ifCustomizedInjected ;
	public ArrayList<Integer> allDirections ;
	
	public CustomizedComponent(String name, ComponentType type, int inputPortsSize, int outputPortsSize, String state) {
		super(name, type, inputPortsSize, outputPortsSize);
		this.state = new CustomizedState(state);
		allStates = new ArrayList<ComponentState>();
		allCustomizedStates = new ArrayList<CustomizedState>();
		allTypes = new ArrayList<ComponentType>();
		ifCustomizedInjected = new ArrayList<Boolean>();
		allDirections = new ArrayList<Integer>();
	}

	/*@Override
	public void processFailure(Failure failure) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void injectFailure(FailureType failureType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FailureType specifyFailureType() {
		// TODO Auto-generated method stub
		return null;
	}
}
