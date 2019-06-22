package components.Valve;

import java.util.ArrayList;

import components.*;
import components.AirDataAndInertialReferenceUnit.AirDataAndInertialReferenceUnitState;
import components.AirDataAndInertialReferenceUnit.AirDataAndInertialReferenceUnitStates;
import failures.*;

public class Valve extends Component {

	static ValveState defaultState = new ValveState(ValveStates.NOMINAL);

	public Valve(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.VALVE, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public Valve(String name, int inputPortsSize, int outputPortsSize, ValveStates state) {
		super(name, ComponentType.VALVE, inputPortsSize, outputPortsSize);
		this.state = new ValveState(state);
	}

	@Override
	/*public void processFailure(Failure failure, int direction , Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {*/
	public void processFailure(Failure failure, int direction) {
		switch (failure.currentComponent.type) {
		case PIPE:
			processPipeFailure(failure, direction);
			break;
		case VALVE:
			processValveFailure(failure, direction);
			break;
		case PUMP:
			processPumpFailure(failure, direction);
			break;
		case BSCU:
			processBSCUFailure(failure, direction);
			break;	
		case CONTROL_ROD:
			processControlRodFailure(failure, direction);
			break;
		case ACTUATOR_CONTROL_ELECTRONICS:
			processActuatorControlElectronicsFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT:
			processCustomized(failure, direction);
			
		/*case CUSTOMIZED_COMPONENT: // To be defined;
			processCustomizedFailure(component , allStates , allTypes , allCustomizedStates , allDirections , ifCustomizedInjected);*/
		}

	}
	
	
	private void processCustomized(Failure failure , int direction){
		CustomizedComponent custom = (CustomizedComponent)failure.currentComponent;
		for (int i=0;i
	}
	
	private void processCustomizedFailure(Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {
		// TODO Auto-generated method stub
		
		/*ArrayList<Integer> index = new ArrayList<>();
		for (int i=0; i<allTypes.size();i++){
			if (allTypes.get(i)==ComponentType.VALVE && ifCustomizedInjected.get(i))
				index.add(i);
		}
		
		//2olna 1 y3ny before w -1 y3ny after
		if (allDirections.get(i1)==1){ 
			//5alas da m3nah en i1 = 1 w i2 = -1 
			this.state = new ValveState(valveStates.)
			
		}
		else {
			//da m3nah en i1 = -1 w i2 = 1
			
		}*/
		
		
		
		for (int i=0 ; i<allTypes.size() ; i++){
			if (allTypes.get(i)==ComponentType.VALVE && ifCustomizedInjected.get(i)){
				String fail = allCustomizedStates.get(i).toString(); 
				Failure failure = new Failure(FailureType.valueOf(fail), component);
				if (allStates.get(i).toString().equals("Stuck")){
					this.state = new ValveState(ValveStates.STUCK);
				}
				else if (allStates.get(i).toString().equals("leak or not allowing pressure")){
					this.state = new ValveState(ValveStates.LEAK);
				}
				else {
					this.absorbFailure(failure);
				}
				
					
			}
		}
		
	}

	private void processControlRodFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case DISCONNECTED:
			case JAMMING:
				// check if no other control from other ports
				// if there is more than 0 unfailed control -> absorb
				if (netUnfailedInputControls() > 0)
					this.absorbFailure(failure);
				else
					this.state = new ValveState(ValveStates.STUCK);
				break;
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			// empty for now
			default:;	
			}
		}
		
	}

	private void processActuatorControlElectronicsFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			case LOSS_OR_INVALID_CONTROL_SIGNAL:
			case LOSS_OR_INVALID_OUTPUT_SIGNAL:
				// check if no other source of power from other ports
				// if there is more than 1 unfailed port -> absorb
				if (netUnfailedInputSignals() > 0)
					this.absorbFailure(failure);
				else
					this.state = new ValveState(ValveStates.STUCK);
				break;
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			// empty for now
			default:;	
			}
		}
		
	}

	// If BSCU is propagating the failure to a valve

	private void processBSCUFailure(Failure failure, int direction) {
		// if valve is an input port (dir = -1) => impossible case
		
		if (direction == -1) { 
			return;
		}
		
		switch(failure.type) {
		case LOSS_OF_CONTROL_SIGNAL:
			// LEAK?????? I changed it to stuck
			// this.state = new ValveState(ValveStates.LEAK);
			if (netUnfailedInputControls() > 0) {
				this.absorbFailure(failure);
			}
			else {
				this.state = new ValveState(ValveStates.STUCK);
			}		
			break;
		}
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub

	}

	private void processValveFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
	}

	// If a valve connected to an already-failed pipe, it process the failure here
	private void processPipeFailure(Failure failure, int direction) {
		
		switch (failure.type) {
		case BLOCKED:
			if (direction == 1) {
				
				// if a valve is connected to only 1 blocked pipe, the valve fails, allowing no
				// pressure
				if (netUnfailedInputPorts() <= 0) {
					this.state = new ValveState(ValveStates.LEAK);
				}
				else {
					// WBS case of selector valve and any of the above pipes
					//absorbs, no change in state
					this.absorbFailure(failure);
				}
				
			}
			// if dir is -1 
			else {
				// e.g pipe A blocked and sending to valve A
				if (this.outputPorts.length - netFailedOutPutPorts() <= 0) {
					this.state = new ValveState(ValveStates.STUCK);
				}
				else {
					// WBS case of selector valve and any (ONE) of the below pipes
					//absorbs, no change in state
					this.absorbFailure(failure);
				}
			}
	
		break;	
		case LEAK: 
			
			if (direction == 1) {
				
				// if a valve is connected to a leaking pipe, the valve fails, allowing no
				// pressure
				System.out.println(netUnfailedInputPorts() );
				if (netUnfailedInputPorts() <= 0) {
					this.state = new ValveState(ValveStates.LEAK);
					break;
				}
				else {
					// WBS case of selector valve and any of the above pipes
					//absorbs, no change in state
					this.absorbFailure(failure);
				}
				
			}
			// if dir is -1 
			else {
				// upward leak is absorbed by valve
				this.absorbFailure(failure);
			}		
		}
	}

	@Override
	public FailureType specifyFailureType() {
		ValveStates currentState = ((ValveState) state).state;
		switch(currentState) {
		case STUCK:
			return FailureType.STUCK;
		case LEAK:
			return FailureType.LEAK;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		ValveStates currentState = ((ValveState) this.state).state;		
		return !currentState.equals(ValveStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new ValveState(ValveStates.NOMINAL));
		
	}

	@Override
	/*public void injectFailure(FailureType failureType, Component component, ArrayList<ComponentState> allStates,
			ArrayList<ComponentType> allTypes, ArrayList<CustomizedState> allCustomizedStates,
			ArrayList<Integer> allDirections, ArrayList<Boolean> ifCustomizedInjected) {*/
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case STUCK:
			this.changeStateTo(new ValveState(ValveStates.STUCK));
			//this.sendFailure(component , allStates , allTypes , allCustomizedStates , allDirections , ifCustomizedInjected);
			this.sendFailure();
			break;
		case LEAK:
			this.changeStateTo(new ValveState(ValveStates.LEAK));
			//this.sendFailure(component , allStates , allTypes , allCustomizedStates , allDirections , ifCustomizedInjected);
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	

	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {ValveStates.NOMINAL, ValveStates.STUCK, ValveStates.LEAK};
	}

	/*@Override
	public void injectFailure(FailureType failureType) {
		// TODO Auto-generated method stub
		
	}*/

	/*@Override
	public void processFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
		
	}*/
}
