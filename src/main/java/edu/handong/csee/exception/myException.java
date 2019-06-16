package edu.handong.csee.exception;

public class myException extends Exception{
	public myException () {
		super("This string has to be revised. \n");
	}
	public myException(String message) {
		super(message);
	}
}
