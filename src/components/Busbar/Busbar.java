package components.Busbar;

import java.util.ArrayList;

import components.*;
import components.ComponentType;
import components.Contactor.*;
import components.Cylinder.CylinderState;
import components.Cylinder.CylinderStates;
import components.DCConsumer.DCConsumerState;
import components.DCConsumer.DCConsumerStates;
import components.Pipe.PipeState;
import components.Pipe.PipeStates;
import failures.Failure;
import failures.FailureType;

public class Busbar extends Component {
	
	static BusbarState defaultState = new BusbarState(BusbarStates.NOMINAL);
	int inputBranches;
	ArrayList<Component> failedInputBranches;

	public Busbar(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.BUSBAR, inputPortsSize, outputPortsSize);
		this.state = defaultState;
		inputBranches = inputPortsSize;
		failedInputBranches = new ArrayList<Component>();
	}
	
	public Busbar(String name, int inputPortsSize, int outputPortsSize, BusbarStates state) {
		super(name, ComponentType.BUSBAR, inputPortsSize, outputPortsSize);
		this.state = new BusbarState(state);
		inputBranches = inputPortsSize;
		failedInputBranches = new ArrayList<Component>();
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
		case BATTERY:
			processBatteryFailure(failure, direction); 
			break;
		case DCCONSUMER:
			processDCConsumerFailure(failure, direction);
		case BUSBAR:
			processBusbarFailure(failure, direction);
			break;
		case DIODE:
			processDiodeFailure(failure, direction);
			break;
		case GENERATOR:
			processGeneratorFailure(failure, direction);
			break;	
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}
	
	private void processGeneratorFailure(Failure failure, int direction) {
		if (direction == 1) {
			boolean newFailedBranch;
			switch(failure.type) {
			case LOSS_OF_POWER:
				newFailedBranch = failComponentBranch(failure.currentComponent);
				if (netUnfailedInputBranches() > 0 || !newFailedBranch) {
					this.absorbFailure(failure);
					
				}
				else {
					this.state = new BusbarState(BusbarStates.FAILED_TO_CONDUCT);
				}
					
				break;
//			case UNDER_VOLTAGE:
//				newFailedBranch = failComponentBranch(failure.currentComponent);
//				// We can check here if the busbar is connected to a contactor which is stuck_closed
//				BusBarStates newState;
//				for (Component port: this.inputPorts) {
//					if (port.type == ComponentType.CONTACTOR) {
//						if (  ((ContactorState) port.state).state == ContactorStates.STUCK_CLOSED ) {
//							this.state = new BusbarState(BusbarStates.SHORT_CIRCUIT);
//							break;
//						} else {
//							
//						}
//					}
//						
//				}
//				else {
//					this.state = new BusbarState(BusbarStates.CONDUCTING_UNDER_VOLTAGE);
//				}
//					
//				break;
//			case OVER_VOLTAGE:
//				newFailedBranch = failComponentBranch(failure.currentComponent);
//				if (netUnfailedInputBranches() > 0 || !newFailedBranch) {
//					this.absorbFailure(failure);
//					
//				}
//				else {
//					this.state = new BusbarState(BusbarStates.CONDUCTING_OVER_VOLTAGE);
//				}
//					
//				break;
			default: // other cases to be defined;	
			}
		}
		else {
			switch(failure.type) {
			// not available now
			default: // other cases to be defined;	
			}
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
				this.absorbFailure(failure);
				this.setPropagationParemeters(failure.type, failure.currentComponent);
			default: // other cases to be defined;	
			}
		}
		
	}
	
	private void processBusbarFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case FAILED_TO_CONDUCT:
				boolean newFailedBranch = failComponentBranch(failure.currentComponent);
				if (netUnfailedInputBranches() > 0 || !newFailedBranch) {
					this.absorbFailure(failure);
					
				}
				else {
					this.state = new BusbarState(BusbarStates.FAILED_TO_CONDUCT);
				}
					
				break;
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			
			case FAILED_TO_CONDUCT:
				// contactor absorbs and doesn't propagate to this busbar so empty
				break;
			case SHORT_CIRCUIT:
				// We can check here if the busbar is connected to a contactor which is stuck_closed
				for (Component outport: this.outputPorts) {
					if (outport.type == ComponentType.CONTACTOR && ((ContactorState) outport.state).state == ContactorStates.STUCK_CLOSED) {
						this.state = new BusbarState(BusbarStates.SHORT_CIRCUIT);
						break;
					}	
				}
				// otherwise if contactor hasn't failed => absorb
				this.absorbFailure(failure);
				break;
			default:;	
			}
		}
		
	}

	private void processDCConsumerFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			// empty for now, impossible in this case I guess
			default:;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			case SHORT_CIRCUIT:
				// We can check here if the busbar is connected to a contactor which is stuck_closed
				for (Component outport: this.outputPorts) {
					if (outport.type == ComponentType.CONTACTOR && ((ContactorState) outport.state).state == ContactorStates.STUCK_CLOSED) {
						this.state = new BusbarState(BusbarStates.FAILED_TO_CONDUCT);
						break;
					}	
				}
				// otherwise if contactor hasn't failed => absorb
				this.absorbFailure(failure);
				break;
			case REVERSE_CURRENT: 
				this.absorbFailure(failure);
				this.setPropagationParemeters(failure.type, failure.currentComponent);				
				break;		
			default:;	
			}
		}
		
	}

	private void processBatteryFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case UNDER_CHARGED:
				boolean newFailedBranch = failComponentBranch(failure.currentComponent);
				if (netUnfailedInputBranches() > 0 || !newFailedBranch) {
					this.absorbFailure(failure);
					
				}
				else {
					this.state = new BusbarState(BusbarStates.FAILED_TO_CONDUCT);
				}				
				break;
			case SHORT_CIRCUIT:
				boolean newFailedBranchsc = failComponentBranch(failure.currentComponent);
				if (netUnfailedInputBranches() > 0 || !newFailedBranchsc) {
					this.absorbFailure(failure);
					
				}
				else {
					this.state = new BusbarState(BusbarStates.SHORT_CIRCUIT);
				}	
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

	private void processContactorFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case STUCK_CLOSED:
				this.absorbFailure(failure);
				break;
			case STUCK_OPEN:
				boolean newFailedBranch = failComponentBranch(failure.currentComponent);
				if (netUnfailedInputBranches() > 0 || !newFailedBranch) {
					this.absorbFailure(failure);
					
				}
				else {
					System.out.println("failed input branches:" + failedInputBranches);
//					this.absorbFailure(failure);
//					System.out.println("ABSORBING AND PROPAGATING");
//					this.setPropagationParemeters(failure.type, failure.currentComponent);
					this.state  = new BusbarState(BusbarStates.FAILED_TO_CONDUCT);
				}
				
				break;	
			default: // other cases to be defined;	
			}
		}
		else { // dir = -1
			switch(failure.type) {
			case STUCK_CLOSED:
				// We can check here if the contactor is connected to a DC consumer that is short_circuit
//				for (Component outport: this.outputPorts) {
//					if (outport.type == ComponentType.DCCONSUMER && ((DCConsumerState) outport.state).state == DCConsumerStates.SHORT_CIRCUIT) {
//						this.state = new BusbarState(BusbarStates.SHORT_CIRCUIT);
//						break;
//					}
//					if (outport.type == ComponentType.BUSBAR && ((BusbarState) outport.state).state == BusbarStates.FAILED_TO_CONDUCT) {
//						this.state = new BusbarState(BusbarStates.FAILED_TO_CONDUCT);
//						break;
//					}
//				}
				// otherwise if consumer hasn't failed => absorb
				this.absorbFailure(failure);
				break;
			case STUCK_OPEN:
				this.absorbFailure(failure);
				break;	
			default: this.absorbFailure(failure);// other cases to be defined;	
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
	
	public boolean failComponentBranch(Component failingComponent) {
		for (Component port: this.failedInputBranches) {
			if (failingComponent == port || failingComponent.isInputPortOf(port) || failingComponent.isOutputPortOf(port))
				return false;
		}
		failedInputBranches.add(failingComponent);	
		return true;
	}
	
	public int netUnfailedInputBranches() {
		return inputBranches - failedInputBranches.size();
	}
	
	@Override
	public FailureType specifyFailureType() {
		BusbarStates currentState = ((BusbarState) state).state;
		switch(currentState) {
		case FAILED_TO_CONDUCT:
			return FailureType.FAILED_TO_CONDUCT;
		case CONDUCTING_UNDER_VOLTAGE:
			return FailureType.CONDUCTING_UNDER_VOLTAGE;
		case CONDUCTING_OVER_VOLTAGE:
			return FailureType.CONDUCTING_OVER_VOLTAGE;	
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		BusbarStates currentState = ((BusbarState) this.state).state;		
		return !currentState.equals(BusbarStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.failedInputBranches = new ArrayList<Component>();
		this.changeStateTo(new BusbarState(BusbarStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case FAILED_TO_CONDUCT:
			this.changeStateTo(new BusbarState(BusbarStates.FAILED_TO_CONDUCT));
			this.sendFailure();
			break;
		case CONDUCTING_UNDER_VOLTAGE:
			this.changeStateTo(new BusbarState(BusbarStates.CONDUCTING_UNDER_VOLTAGE));
			this.sendFailure();
			break;
		case CONDUCTING_OVER_VOLTAGE:
			this.changeStateTo(new BusbarState(BusbarStates.CONDUCTING_OVER_VOLTAGE));
			this.sendFailure();
			break;	
		default: return;	
		}
		
	}
	
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {
				BusbarStates.NOMINAL, 
				BusbarStates.FAILED_TO_CONDUCT,
		};
	}

}
