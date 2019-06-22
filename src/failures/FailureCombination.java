package failures;

import components.Component;

public class FailureCombination {
	public Component component;
	public FailureType failureType;

	public FailureCombination(Component component, FailureType failureType) {
		this.component = component;
		this.failureType = failureType;
	}
	
	public String toString() {
		return "(" + component.name + ", " + failureType + ")";
	}
	
	public boolean equals(FailureCombination otherCombination) {
		return this.component == otherCombination.component && this.failureType == otherCombination.failureType;
	}

}
