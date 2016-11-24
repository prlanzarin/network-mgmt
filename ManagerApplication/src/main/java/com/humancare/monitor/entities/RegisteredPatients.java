/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amanda
 */
public class RegisteredPatients {
    
    private String name;
    private String ip;   
    private Integer age;
    private String gender;
    
    public RegisteredPatients() {
    }
    
    public RegisteredPatients(String name, String ip, Integer age, String gender){
        this.name = name;
        this.ip = ip;
        this.age = age;
        this.gender = gender;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    
    
}
