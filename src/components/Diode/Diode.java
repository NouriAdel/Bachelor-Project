package components.Diode;

import components.*;
import failures.*;

public class Diode extends Component {
	
	static DiodeState defaultState = new DiodeState(DiodeStates.NOMINAL);

	public Diode(String name, int inputPortsSize, int outputPortsSize) {
		super(name, ComponentType.DIODE, inputPortsSize, outputPortsSize);
		this.state = defaultState;
	}
	
	public Diode(String name, int inputPortsSize, int outputPortsSize, DiodeStates state) {
		super(name, ComponentType.DIODE, inputPortsSize, outputPortsSize);
		this.state = new DiodeState(state);
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
			break;
		case CONTACTOR:
			processContactorFailure(failure, direction);
			break;
		case CUSTOMIZED_COMPONENT: 
			//To be defined;
		}	
	}
	
	private void processContactorFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case STUCK_CLOSED:
				this.absorbFailure(failure);
//				// Contactor absorbs failure but also propagates it!
//				this.absorbFailure(failure);
//				System.out.println("ABSORBING AND PROPAGATING");
//				this.setPropagationParemeters(failure.type, failure.currentComponent);
//				break;
//			case SHORT_CIRCUIT:
//				this.absorbFailure(failure);
//				break;
			default: // other cases to be defined;	
			}
		}
		else {
//			switch(failure.type) {
//			case FAILED_TO_CONDUCT:
//				this.absorbFailure(failure);
//				break;
//			case SHORT_CIRCUIT:
//				this.absorbFailure(failure);
//				break;	
//			default: // other cases to be defined;	
//			}
		}
		
	}

	private void processBusbarFailure(Failure failure, int direction) {
		if (direction == 1) {
			switch(failure.type) {
			case FAILED_TO_CONDUCT:
				// Diode absorbs failure but also propagates it!
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
//			switch(failure.type) {
//			case FAILED_TO_CONDUCT:
//				this.absorbFailure(failure);
//				break;
//			case SHORT_CIRCUIT:
//				this.absorbFailure(failure);
//				break;	
//			default: // other cases to be defined;	
//			}
		}
		
	}
		
	private void processDCConsumerFailure(Failure failure, int direction) {
		if (direction == 1) {
			// case not available right now
			// this dir is coming from a propagation that is not needed
			System.out.println("Here? ");
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
			case REVERSE_CURRENT:
				if (!this.hasFailed())
					this.absorbFailure(failure);
				else
					this.state = new DiodeState(DiodeStates.STUCK_CLOSED); // it is already the state but just to propagate again
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
		DiodeStates currentState = ((DiodeState) state).state;
		switch(currentState) {
		case STUCK_CLOSED:
			return FailureType.STUCK_CLOSED;
		default:
			return null;
		}
	}
	
	@Override
	public boolean hasFailed() {
		// if the state is anything other than nominal -> failed
		DiodeStates currentState = ((DiodeState) this.state).state;		
		return !currentState.equals(DiodeStates.NOMINAL);
	}

	@Override
	public void resetComponentState() {
		super.resetComponentState();
		this.changeStateTo(new DiodeState(DiodeStates.NOMINAL));
	}

	@Override
	public void injectFailure(FailureType failureType) {
		this.isAbsorber = false;
		switch(failureType) {
		case STUCK_CLOSED:
			this.changeStateTo(new DiodeState(DiodeStates.STUCK_CLOSED));
			this.sendFailure();
			break;
		default: return;	
		}
		
	}
	
	@Override
	public Object[] getPossibleStates() {
		// TODO Auto-generated method stub
		return new Object [] {DiodeStates.NOMINAL, DiodeStates.STUCK_CLOSED};
	}

}
