package system;

import java.util.Arrays;

public class TreeEvent {
	
	public String description;
	public String [] componentNames;
	public String [] componentFailures;
	public String operator;
	public int size;
	
	public static int id;

	public TreeEvent(String [] names, String [] failures, String op) {
		componentNames = names;
		componentFailures = failures;
		operator = op;
		size = names.length;
		++id;
		
		description = "";
		for (int i = 0; i < names.length; i++) {
			description += "loss of: " + names[i] + " due to: " + failures[i];
			if (i < names.length - 1)
				description += " " + operator + " ";
		}
		
//		description = "";
//		for (int i = 0; i < names.length; i++) {
//			description += "loss of: " + names[i];
//			if (i < names.length - 1)
//				description += " " + operator + " ";
//		}
	}
	
	public String toString() {
		return description;
	}

	public boolean equals(TreeEvent otherEvent) {
		
		if (componentNames.length != otherEvent.componentNames.length || 
				componentFailures.length != otherEvent.componentFailures.length)
			return false;
		boolean found = false;
		
		for (int i = 0; i < componentNames.length; i++) {
			found = false;
			for (int j = 0; j < componentNames.length; j++) {
				if (otherEvent.componentNames[j].equals(componentNames[i])) {
					if(otherEvent.componentFailures[j].equals(componentFailures[i])) {
						found = true;
					}
				}
			}
			if (!found)
				return false;
		}
		
		return found; // or return true
	}
	
	public boolean isSubEventOf(TreeEvent otherEvent) {
		boolean found = false;
		
		
		for (int i = 0; i < componentNames.length; i++) {
			found = false;
			for (int j = 0; j < otherEvent.componentNames.length; j++) {
				if (otherEvent.componentNames[j].equals(componentNames[i])) {
					if(otherEvent.componentFailures[j].equals(componentFailures[i])) {
						found = true;
					}
				}
			}
			if (!found)
				return false;
		}
		
		return found;
	}
	
	
}
