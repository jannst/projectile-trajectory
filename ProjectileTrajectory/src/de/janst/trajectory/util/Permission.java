package de.janst.trajectory.util;

public enum Permission {

	USE("use");
	
	private String permission;
	private final String base = "trajectorysimulator.";

	Permission(String permission) {
		this.permission = base + permission;
	}
	
	public String getString() {
		return this.permission;
	}

}
