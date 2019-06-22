package components.Engine;

import components.ComponentState;

public class EngineState extends ComponentState {
	public EngineStates state;
		
	public EngineState(EngineStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String str = null;
		switch (state) {
		case SURGE: 
			str = "Surge";
			break;
		case STALL:
			str = "Stall";
			break;
		case FLAME_OUT: 
			str = "Flame out";
			break;
		case OVER_SPEED:
			str = "Over speed";
			break;
		case OVER_TEMPERATURE:
			str = "Over Temperature";
			break;
		case FUEL_SUPPLY_FAILURE:
			str = "Fuel Supply faiure";
			break;
		default:
			str = "Nominal";
		}
		return str;
	}
}
