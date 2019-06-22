package components.Generator;

import components.*;
import components.Busbar.*;
import components.Contactor.*;
import components.DCConsumer.*;
import failures.*;

public class Generator extends Component {
	
	static GeneratorState defaultState = new GeneratorState(GeneratorStates.NOMINAL);

	public Generator(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.GENERATOR, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public Generator(String name, int inputPortsSize, int outputPortsSize, GeneratorStates state) {
		super(name, ComponentType.GENERATOR, inputPortsSize, outputPortsSize);
		this.state = new GeneratorState(state);
	}

	@Override
	public void processFailure(Failure failure, int direction) {

		switch(failure.currentComponent.type) {
		case PIPE: 
			processPipeFailure(failure, direction); 
			break;
		case	 VALVE:  
			processValveFailure(failure, direction); 
			break;
		case PUMP:  
			processPumpFailure(failure, direction); 
			break;
		case CYLINDER:
			processCylinderFailure(failure, direction); 
			break;
		case CONTACTOR:
			processContactorFailure(failure, direction); 
			break;
		case DIODE:
			processDiodeFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}

	private void processDiodeFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			
			default: // other cases to be defined;	
			}
		}
		else {
			switch(failure.type) {
			case STUCK_CLOSED:
				// We can check here if the diode is connected to a reverse current consumer
				for (Component outport: failure.currentComponent.outputPorts) {
					if (outport.type == ComponentType.DCCONSUMER && ((DCConsumerState) outport.state).state == DCConsumerStates.REVERSE_CURRENT) {
						this.state = new BusbarState(BusbarStates.SHORT_CIRCUIT);
						break;
					}	
				}
			default: // other cases to be defined;	
			}
		}
		
	}

	private void processContactorFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			case STUCK_OPEN:
				this.absorbFailure(failure);
				break;
			default:;	
			}
		}
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		
	}

	// If a pipe connected to an already-failed valve, it process the failure here 
	private void processValveFailure(Failure failure, int direction) {
		
	}

	private void processPipeFailure(Failure failure, int direction) {

	}
	
	private void processCylinderFailure(Failure failure, int direction) {
		
	}
	
	@Override
	public FailureType specifyFailureType() {
		GeneratorStates currentState = ((GeneratorState) state).state;
		switch(currentState) {
		case LOSS_OF_POWER:
			return FailureType.LOSS_OF_POWER;
		case OVER_VOLTAGE:
			return FailureType.OVER_VOLTAGE;
		case UNDER_VOLTAGE:
			return FailureType.UNDER_VOLTAGE;
		default: 
			return null;	
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		GeneratorStates currentState = ((GeneratorState) this.state).state;		
		return !currentState.equals(GeneratorStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new GeneratorState(GeneratorStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case LOSS_OF_POWER:
			this.changeStateTo(new GeneratorState(GeneratorStates.LOSS_OF_POWER));
			this.sendFailure();
			break;
//		case OVER_VOLTAGE:
//			this.changeStateTo(new GeneratorState(GeneratorStates.OVER_VOLTAGE));
//			this.sendFailure();
//			break;
//		case UNDER_VOLTAGE:
//			this.changeStateTo(new GeneratorState(GeneratorStates.UNDER_VOLTAGE));
//			this.sendFailure();
//			break;	
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {
				GeneratorStates.NOMINAL, GeneratorStates.LOSS_OF_POWER, GeneratorStates.OVER_VOLTAGE, GeneratorStates.UNDER_VOLTAGE
		};
	}

}
