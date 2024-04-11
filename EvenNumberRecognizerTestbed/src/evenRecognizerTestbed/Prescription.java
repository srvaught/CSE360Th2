package evenRecognizerTestbed;
import java.io.*;
public class Prescription 
{
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String pharmacyID;
    private String conditionBeingTreated;
    public Prescription(String medicationName, String dosage, String frequency, String duration, String pharmacyID, String conditionBeingTreated) 
    {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.pharmacyID = pharmacyID;
        this.conditionBeingTreated = conditionBeingTreated;
    }
    public void updateDosage(String newDosage) 
    {
        this.dosage = newDosage;
    }
    public void updateFrequency(String newFrequency) 
    {
        this.frequency = newFrequency;
    }
    public String getConditionBeingTreated()
    {
    	return conditionBeingTreated;
    }
    public String getMedicationName()
    {
    	return medicationName;
    }
    public String getDosage()
    {
    	return dosage;
    }
    public String getFrequency()
    {
    	return frequency;
    }
    public String getDuration()
    {
    	return duration;
    }
    public void saveToFile() 
    {
        String identifier = this.medicationName + "_" + System.currentTimeMillis();
        String filename = "Data/Prescriptions/" + identifier + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            writer.write("Medication Name: " + this.medicationName + "\n");
            writer.write("Dosage: " + this.dosage + "\n");
            writer.write("Frequency: " + this.frequency + "\n");
            writer.write("Duration: " + this.duration + "\n");
            writer.write("Pharmacy ID: " + this.pharmacyID + "\n");
            writer.write("Condition Being Treated: " + this.conditionBeingTreated + "\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to save prescription to file: " + e.getMessage());
        }
    }
    public static Prescription fromString(String data) 
    {
        String[] parts = data.split(":");
        return new Prescription(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}