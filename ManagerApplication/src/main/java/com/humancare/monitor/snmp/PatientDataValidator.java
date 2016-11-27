/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.snmp;

public class PatientDataValidator {
    
    
    public boolean tempAlert(double temp){
        if(temp > 38.0 || temp < 35.0){
            return true;
        }
        return false;    
    }
    
    public boolean glucoseAlert(int glucose){    
        if(glucose > 100 || glucose < 60){
            return true;
        }
        return false;
    }
    
    // TODO: levar em conta idade
    // valores medios, pois varia pela idade se tem diabetes
    public boolean pressureAlert(int sis, int dis){
        if(sis > 140 || dis> 90){
            return true;
        }
        return false;
        
    }
    
    // pode depender do genero e idade
    public boolean heartRateAlert(int value){
        return value > 120;
    }
    
    //spo2 de pulso
    public boolean spo2Alert(int value){
        return value < 85;
    }
}
