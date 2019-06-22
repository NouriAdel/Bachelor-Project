package components.Contactor;

import components.*;
import failures.*;

public class Contactor extends Component {
	
	static ContactorState defaultState = new ContactorState(ContactorStates.NOMINAL);

	public Contactor(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.CONTACTOR, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public Contactor(String name, int inputPortsSize, int outputPortsSize, ContactorStates state) {
		super(name, ComponentType.CONTACTOR, inputPortsSize, outputPortsSize);
		this.state = new ContactorState(state);
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
		case DCCONSUMER:
			processDCConsumerFailure(failure, direction); 
			break;	
		case BUSBAR:
			processBusbarFailure(failure, direction);
		case DIODE:
			processDiodeFailure(failure, direction);
		case GENERATOR:
			processGeneratorFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}
	
	private void processGeneratorFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case LOSS_OF_POWER:
				this.absorbFailure(failure);
				this.setPropagationParemeters(failure.type, failure.currentComponent);			
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
				// Contactor absorbs failure but also propagates it!
				this.absorbFailure(failure);
				System.out.println("ABSORBING AND PROPAGATING");
				this.setPropagationParemeters(failure.type, failure.currentComponent);
				break;
			case SHORT_CIRCUIT:
				this.absorbFailure(failure);
				break;
			default: // other cases to be defined;	
			}
		}
		else {
			switch(failure.type) {
			case FAILED_TO_CONDUCT:
				this.absorbFailure(failure);
				break;
			case SHORT_CIRCUIT:
				this.absorbFailure(failure);
				break;	
			default: // other cases to be defined;	
			}
		}
		
	}
		
	private void processDCConsumerFailure(Failure failure, int direction) {
		if (direction == 1) {
			// case not available right now
		}
		else { // dir = -1
			switch(failure.type) {
			case OPEN_CIRCUIT:
				this.absorbFailure(failure);
				break;
			case SHORT_CIRCUIT: 
				this.absorbFailure(failure);
				this.setPropagationParemeters(failure.type, failure.currentComponent);				
				break;
			default: // other cases to be defined;	
			}
		}
		
	}

	private void processPumpFailure(Failure failure, int direction) {
		
	}

	
	private void processValveFailure(Failure failure, int direction) {
		
	}

	private void processPipeFailure(Failure failure, int direction) {

	}
	
	private void processCylinderFailure(Failure failure, int direction) {
		
	}
	
	@Override
	public FailureType specifyFailureType() {
		ContactorStates currentState = ((ContactorState) state).state;
		switch(currentState) {
		case STUCK_OPEN:
			return FailureType.STUCK_OPEN;
		case STUCK_CLOSED:
			return FailureType.STUCK_CLOSED;
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		ContactorStates currentState = ((ContactorState) this.state).state;		
		return !currentState.equals(ContactorStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new ContactorState(ContactorStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case STUCK_OPEN:
			this.changeStateTo(new ContactorState(ContactorStates.STUCK_OPEN));
			this.sendFailure();
			break;
		case STUCK_CLOSED:
			this.changeStateTo(new ContactorState(ContactorStates.STUCK_CLOSED));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {ContactorStates.NOMINAL, ContactorStates.STUCK_OPEN, ContactorStates.STUCK_CLOSED};
	}

}
