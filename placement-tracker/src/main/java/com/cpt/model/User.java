package com.cpt.model;

public class User {
    private String usrId;
    private String usrPassword;
    private String usrRole;

    public User() {}

    public String getUsrId() { return usrId; }
    public void setUsrId(String usrId) { this.usrId = usrId; }
    public String getUsrPassword() { return usrPassword; }
    public void setUsrPassword(String usrPassword) { this.usrPassword = usrPassword; }
    public String getUsrRole() { return usrRole; }
    public void setUsrRole(String usrRole) { this.usrRole = usrRole; }
}
