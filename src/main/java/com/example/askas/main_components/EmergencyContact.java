package com.example.askas.main_components;
import java.util.*;
public class EmergencyContact 
{
    private String patientID;
    private String name;
    private String relationship;
    private String phone;
    private String email;
    public EmergencyContact(String patientID, String name, String relationship, String phone, String email) 
    {
        this.patientID = patientID;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
        this.email = email;
    }
    public void updateContactInfo(Map<String, String> newContactInfo) 
    {
        if(newContactInfo.containsKey("name")) 
        {
            this.name = newContactInfo.get("name");
        }
        if(newContactInfo.containsKey("relationship")) 
        {
            this.relationship = newContactInfo.get("relationship");
        }
        if(newContactInfo.containsKey("phone")) 
        {
            this.phone = newContactInfo.get("phone");
        }
        if(newContactInfo.containsKey("email")) 
        {
            this.email = newContactInfo.get("email");
        }
    }
}