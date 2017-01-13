package com.uwschedulebuilder.uwapiclient;

public class UWAPIException extends Exception {

	private static final long serialVersionUID = 1L;

	public UWAPIException() {
		super();
	}
	
	public UWAPIException(String msg) {
		super(msg);
	}
	
	public String getMessage() {
		return String.format("UW API Error: %s", super.getMessage());
	}
}
