package com.cpt.model;

import org.springframework.stereotype.Component;

@Component
public class ResourceDTO {

    private int ralId;
    private int ralPldId;
    private String ralPldName;
    private int ralResourceId;
    private int ralPhaseId;
    private int ralCapacity;
    private int ralOccupied;
    private int ralFacultyId;
    private String ralFacultyName;
   // private int ralQuantity;

    // Getters and Setters

    public int getRalId() {
        return ralId;
    }

    public void setRalId(int ralId) {
        this.ralId = ralId;
    }

    public int getRalPldId() {
        return ralPldId;
    }

    public void setRalPldId(int ralPldId) {
        this.ralPldId = ralPldId;
    }

    public String getRalPldName() {
        return ralPldName;
    }

    public void setRalPldName(String ralPldName) {
        this.ralPldName = ralPldName;
    }

    public int getRalResourceId() {
        return ralResourceId;
    }

    public void setRalResourceId(int ralResourceId) {
        this.ralResourceId = ralResourceId;
    }

    public int getRalPhaseId() {
        return ralPhaseId;
    }

    public void setRalPhaseId(int ralPhaseId) {
        this.ralPhaseId = ralPhaseId;
    }

    public int getRalCapacity() {
        return ralCapacity;
    }

    public void setRalCapacity(int ralCapacity) {
        this.ralCapacity = ralCapacity;
    }

    public int getRalOccupied() {
        return ralOccupied;
    }

    public void setRalOccupied(int ralOccupied) {
        this.ralOccupied = ralOccupied;
    }

    public int getRalFacultyId() {
        return ralFacultyId;
    }

    public void setRalFacultyId(int ralFacultyId) {
        this.ralFacultyId = ralFacultyId;
    }

    public String getRalFacultyName() {
        return ralFacultyName;
    }

    @Override
	public String toString() {
		return "ResourceDTO [ralId=" + ralId + ", ralPldId=" + ralPldId + ", ralPldName=" + ralPldName
				+ ", ralResourceId=" + ralResourceId + ", ralPhaseId=" + ralPhaseId + ", ralCapacity=" + ralCapacity
				+ ", ralOccupied=" + ralOccupied + ", ralFacultyId=" + ralFacultyId + ", ralFacultyName="
				+ ralFacultyName + ", getRalId()=" + getRalId() + ", getRalPldId()=" + getRalPldId()
				+ ", getRalPldName()=" + getRalPldName() + ", getRalResourceId()=" + getRalResourceId()
				+ ", getRalPhaseId()=" + getRalPhaseId() + ", getRalCapacity()=" + getRalCapacity()
				+ ", getRalOccupied()=" + getRalOccupied() + ", getRalFacultyId()=" + getRalFacultyId()
				+ ", getRalFacultyName()=" + getRalFacultyName() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	public void setRalFacultyName(String ralFacultyName) {
        this.ralFacultyName = ralFacultyName;
    }

//    public int getRalQuantity() {
//        return ralQuantity;
//    }
//
//    public void setRalQuantity(int ralQuantity) {
//        this.ralQuantity = ralQuantity;
//    }
}
