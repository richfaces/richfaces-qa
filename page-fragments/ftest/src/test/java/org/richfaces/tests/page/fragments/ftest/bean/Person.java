package org.richfaces.tests.page.fragments.ftest.bean;

public class Person {

    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
    private String phone;
    private String job;

    public Person(String firstName, String surnameName, Integer age, String address, String phone) {
        super();
        this.firstName = firstName;
        this.lastName = surnameName;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public Person() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}