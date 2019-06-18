package edu.handong.csee.mygenerics;

public class checkingIndex<E> {
	//private S string;
	private E str;
	private String finalStr;
	private int i;
	
	public checkingIndex(E str) {
		this.str = str;
	}
	
	public String deleteEnter() {
		i = 0;
		
		finalStr = String.valueOf(str);
		for(String temp : finalStr.split("\n")) {
			if(i==0) { 
				finalStr = temp;
				i++;
			}
			else {
				finalStr = finalStr + temp;
				i++;
			}
		}
		
		return finalStr;
	}
	
	public String deleteComma() {
		i = 0;
		
		finalStr = String.valueOf(str);
		for(String temp : finalStr.split(",")) {
			if(i==0) { 
				finalStr = temp;
				i++;
			}
			else {
				finalStr = finalStr + temp;
				i++;
			}
		}
		
		return finalStr;
	}
}
