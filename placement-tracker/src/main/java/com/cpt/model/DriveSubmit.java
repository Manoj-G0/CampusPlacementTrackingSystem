
package com.cpt.model;

import org.springframework.stereotype.Component;

@Component
public class DriveSubmit {

    private String driveName;
    private int numStudents;
    private int numOfFaculty;
    private int pld_id;
    private int clg_id; 
    
    private int rounds;


	public int getPld_id() {
		return pld_id;
	}

	public void setPld_id(int pld_id) {
		this.pld_id = pld_id;
	}

	public int getClg_id() {
		return clg_id;
	}

	public void setClg_id(int clg_id) {
		this.clg_id = clg_id;
	}

	// Constructor to initialize the fields (optional)
    public DriveSubmit() {}

    // Getters and Setters
    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

   




    public int getNumOfFaculty() {
        return numOfFaculty;
    }

    public void setNumOfFaculty(int numOfFaculty) {
        this.numOfFaculty = numOfFaculty;
    }

    // Optional: Override toString() to easily print the object's details
    

	public int getNumStudents() {
		return numStudents;
	}

	@Override
	public String toString() {
		return "DriveSubmit [driveName=" + driveName + ", numStudents=" + numStudents + ", numOfFaculty=" + numOfFaculty
				+ ", pld_id=" + pld_id + ", clg_id=" + clg_id + "]";
	}

	public void setNumStudents(int numStudents) {
		this.numStudents = numStudents;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
}
