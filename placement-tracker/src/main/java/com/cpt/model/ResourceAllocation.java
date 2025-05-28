package com.cpt.model;

import java.time.LocalDate;

public class ResourceAllocation {
    private Integer id;
    private Integer pldId;
    private String resourceType;
    private String resourceName;
    private LocalDate allocationDate;
    private String driveName; // For display purposes

    public ResourceAllocation() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPldId() { return pldId; }
    public void setPldId(Integer pldId) { this.pldId = pldId; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public LocalDate getAllocationDate() { return allocationDate; }
    public void setAllocationDate(LocalDate allocationDate) { this.allocationDate = allocationDate; }
    public String getDriveName() { return driveName; }
    public void setDriveName(String driveName) { this.driveName = driveName; }
}