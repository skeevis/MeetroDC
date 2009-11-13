package com.skeevisarts.meetrodc;

public class Station {

	String name;
	int id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Station(String name, int id) {
		this.name = name;
		this.id = id;
	}

	@Override
	public String toString() {
		return name; 
	}
	
}
