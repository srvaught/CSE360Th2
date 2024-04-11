package evenRecognizerTestbed;
import java.util.*;
import java.io.*;
public class Pharmacy 
{
    private String pharmacyID;
    private String name;
    private String address;
    private String phone;
    public Pharmacy(String pharmacyID, String name, String address, String phone) 
    {
        this.pharmacyID = pharmacyID;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
    public void updateInformation(Map<String, String> newInfo) 
    {
        if(newInfo.containsKey("name")) 
        {
            this.name = newInfo.get("name");
        }
        if(newInfo.containsKey("address")) 
        {
            this.address = newInfo.get("address");
        }
        if(newInfo.containsKey("phone")) 
        {
            this.phone = newInfo.get("phone");
        }
    } 
    public static void sendPrescription(Prescription prescription, String pharmacyID) 
    {
        String filename = "Data/Pharmacy/" + pharmacyID + "_Prescriptions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) 
        {
            writer.write("Prescription for " + prescription.getConditionBeingTreated() + ":\n");
            writer.write("Medication: " + prescription.getMedicationName() + "\n");
            writer.write("Dosage: " + prescription.getDosage() + "\n");
            writer.write("Frequency: " + prescription.getFrequency() + "\n");
            writer.newLine();
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to send prescription to pharmacy: " + e.getMessage());
        }
    }
}