package com.denkarz.jcat.backend.model;

public class User {
  private String fname;
  private String lname;

  public User() {
    this.lname = "Snow";
    this.fname = "John";
  }

  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  public String getLname() {
    return lname;
  }

  public void setLname(String lname) {
    this.lname = lname;
  }

  public String getFullName() {
    return this.fname + " " + this.lname;
  }
}
