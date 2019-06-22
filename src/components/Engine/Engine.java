package components.Engine;

import components.*;
import components.Valve.ValveState;
import components.Valve.ValveStates;
import failures.*;

public class Engine extends Component {

	static EngineState defaultState = new EngineState(EngineStates.NOMINAL);

	public Engine(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.ENGINE, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}

	public Engine(String name, int inputPortsSize, int outputPortsSize, EngineStates state) {
		super(name, ComponentType.ENGINE, inputPortsSize, outputPortsSize);
		this.state = new EngineState(state);
	}

	@Override
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
		case ACTUATOR_CONTROL_ELECTRONICS:
			processActuatorControlElectronicsFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: // To be defined;
			
		}

	}
	
	private void processActuatorControlElectronicsFailure(Failure failure, int direction) {
	}

	private void processBSCUFailure(Failure failure, int direction) {
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub

	}

	private void processValveFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
	}

	// If an engine connected to an already-failed pipe, it process the failure here
	private void processPipeFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case BLOCKED:
			case LEAK:
				if (netUnfailedInputPorts() > 0)
					this.absorbFailure(failure);
				else
					this.state = new EngineState(EngineStates.FUEL_SUPPLY_FAILURE);
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

	@Override
	public FailureType specifyFailureType() {
		EngineStates currentState = ((EngineState) state).state;
		switch(currentState) {
		case SURGE:
			return FailureType.SURGE;
		case STALL:
			return FailureType.STALL;
		case FLAME_OUT:
			return FailureType.FLAME_OUT;
		case OVER_SPEED:
			return FailureType.OVER_SPEED;
		case OVER_TEMPERATURE:
			return FailureType.OVER_TEMPERATURE;
		default:
			return null;
		}
	}

	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		EngineStates currentState = ((EngineState) this.state).state;		
		return !currentState.equals(EngineStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new EngineState(EngineStates.NOMINAL));
		
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case SURGE:
			this.changeStateTo(new EngineState(EngineStates.SURGE));
			this.sendFailure();
			break;
		case STALL:
			this.changeStateTo(new EngineState(EngineStates.STALL));
			this.sendFailure();
			break;
		case FLAME_OUT:
			this.changeStateTo(new EngineState(EngineStates.FLAME_OUT));
			this.sendFailure();
			break;
		case OVER_SPEED:
			this.changeStateTo(new EngineState(EngineStates.OVER_SPEED));
			this.sendFailure();
			break;
		case OVER_TEMPERATURE:
			this.changeStateTo(new EngineState(EngineStates.OVER_TEMPERATURE));
			this.sendFailure();
			break;
		case FUEL_SUPPLY_FAILURE:
			this.changeStateTo(new EngineState(EngineStates.FUEL_SUPPLY_FAILURE));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {
				EngineStates.NOMINAL, 
				EngineStates.SURGE,
				EngineStates.STALL,
				EngineStates.FLAME_OUT, 
				EngineStates.OVER_SPEED, 
				EngineStates.OVER_TEMPERATURE,
				EngineStates.FUEL_SUPPLY_FAILURE,
		};
	}
}
