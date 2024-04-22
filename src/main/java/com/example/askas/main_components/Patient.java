package com.example.askas.main_components;
import java.util.*;
import java.text.*;
import java.io.*;
public class Patient extends User 
{
    private String patientID;
    private Date dateOfBirth;
    private MedicalHistory medicalHistory;
    private List<VisitSummary> visitSummaries;
    public Patient(String userID, String password, String name, String contactInformation, String patientID, Date dateOfBirth) 
    {
        super(userID, password, name, contactInformation); 
        this.patientID = patientID;
        this.dateOfBirth = dateOfBirth;
    }
    public List<VisitSummary> viewVisitSummaries() 
    {
        return visitSummaries;
    }
    private void saveToFile() 
    {
        String filename = "Data/Patients/" + this.patientID + "_Profile.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) 
        { 
            writer.write("UserID: " + this.getUserID() + "\n");
            writer.write("Password: [Protected]\n");
            writer.write("Name: " + this.getName() + "\n");
            writer.write("ContactInformation: " + this.getContactInformation() + "\n");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            writer.write("DateOfBirth: " + (this.dateOfBirth != null ? sdf.format(this.dateOfBirth) : "Not provided") + "\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to save patient profile: " + e.getMessage());
        }
    }
    public void updateProfile(ProfileUpdate profileUpdates) 
    {
        for (Map.Entry<String, String> update : profileUpdates.getUpdatedFields().entrySet()) 
        {
            switch (update.getKey().toLowerCase()) 
            {
                case "name":
                    this.setName(update.getValue());
                    break;
                case "contactinformation":
                    this.setContactInformation(update.getValue());
                    break;
                case "dateofbirth":
                    try 
                    {
                        Date newDOB = new SimpleDateFormat("yyyy-MM-dd").parse(update.getValue());
                        this.dateOfBirth = newDOB;
                    } 
                    catch (ParseException e) 
                    {
                        System.err.println("Error parsing date of birth: " + e.getMessage());
                    }
                    break;
            }
        }
        saveToFile();
    }

    public void sendMessage(Message message) 
    {
        //TODO
    }
}