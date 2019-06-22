package components.DirectElectricalLink;

import components.ComponentState;

public class DirectElectricalLinkState extends ComponentState {
	public DirectElectricalLinkStates state;
		
		public DirectElectricalLinkState(DirectElectricalLinkStates state) {
			this.state = state;
		}
		
		@Override
		public String toString() {
			String str = null;
			switch (state) {
			case LOSS_OF_ELECTRICAL_SIGNAL: 
				str = "loss of electrical signak";
				break;
			default:
				str = "Nominal";
			}
			return str;
		}
}
