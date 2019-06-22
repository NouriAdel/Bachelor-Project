package components.Pump;

import components.*;
import failures.*;

public class Pump extends Component {
	
	static PumpState defaultState = new PumpState(PumpStates.NOMINAL); 
	
	public Pump(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.PUMP, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public Pump(String name, int inputPortsSize, int outputPortsSize, PumpStates state) {
		super(name, ComponentType.PUMP, inputPortsSize, outputPortsSize);
		this.state = new PumpState(state);
	}

	@Override
	public void processFailure(Failure failure, int direction) {
		switch(failure.currentComponent.type) {
		case PIPE: 
			processPipeFailure(failure, direction); 
			break;
		case VALVE:  
			processValveFailure(failure, direction); 
			break;
		case PUMP:  
			processPumpFailure(failure, direction); 
			break;
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
			processCustomizedFailure(failure, direction);
		}		
	}

	private void processCustomizedFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
		
	}

	private boolean processPumpFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean processValveFailure(Failure failure, int direction) {
		// TODO Auto-generated method stub
		return false;
	}

	// If a pump connected to an already-failed pipe, it process the failure here
	private void processPipeFailure(Failure failure, int direction) {
		
		if (direction == 1) {
			switch (failure.type) {
			case BLOCKED: 
				// if a Pump is connected to a blocked pipe
				this.state = new PumpState(PumpStates.NO_PRESSURE);
				break;	
			case LEAK:
				this.state = new PumpState(PumpStates.NO_PRESSURE);
				break;
			}
		} else {
			switch (failure.type) {
			case BLOCKED: 
				// if a Pump is connected to a blocked pipe
				this.state = new PumpState(PumpStates.OVER_PRESSURED);
				break;	
			case LEAK:
				this.state = new PumpState(PumpStates.NO_PRESSURE);
				break;
			}
		}

	}
	
	@Override
	public FailureType specifyFailureType() {
		PumpStates currentState = ((PumpState) state).state;
		switch(currentState) {
		case NO_PRESSURE:
			return FailureType.NO_PRESSURE;
		case OVER_PRESSURED:
			return FailureType.OVER_PRESSURED;
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		PumpStates currentState = ((PumpState) this.state).state;		
		return !currentState.equals(PumpStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new PumpState(PumpStates.NOMINAL));
		
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case NO_PRESSURE:
			this.changeStateTo(new PumpState(PumpStates.NO_PRESSURE));
			this.sendFailure();
			break;
		case OVER_PRESSURED:
			this.changeStateTo(new PumpState(PumpStates.OVER_PRESSURED));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {PumpStates.NOMINAL, PumpStates.OVER_PRESSURED, PumpStates.NO_PRESSURE};
	}

}
