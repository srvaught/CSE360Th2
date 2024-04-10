package phase3pkg;
import java.util.*;
import java.io.*;
public class InsuranceClaim 
{
    private String claimID;
    private String patientID;
    private String insuranceInformationID;
    private Date dateFiled;
    private Map<String, Double> servicesClaimed = new HashMap<>();
    private String status;
    public InsuranceClaim(String claimID, String patientID, String insuranceInformationID, Date dateFiled, String status)
    {
        this.claimID = claimID;
        this.patientID = patientID;
        this.insuranceInformationID = insuranceInformationID;
        this.dateFiled = dateFiled;
        this.status = status;
    }
    private void saveToFile() 
    {
        String filename = "Data/InsuranceClaims/" + this.claimID + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename)))
        {
            writer.write("ClaimID: " + this.claimID + "\n");
            writer.write("PatientID: " + this.patientID + "\n");
            writer.write("InsuranceInformationID: " + this.insuranceInformationID + "\n");
            writer.write("DateFiled: " + this.dateFiled.toString() + "\n");
            writer.write("Status: " + this.status + "\n");
            writer.write("ServicesClaimed:\n");
            for (Map.Entry<String, Double> entry : servicesClaimed.entrySet()) 
            {
                writer.write("  " + entry.getKey() + ": " + entry.getValue() + "\n");
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to save insurance claim to file: " + e.getMessage());
        }
    }
    public void fileClaim(String serviceName, Double claimedAmount) 
    {
        servicesClaimed.put(serviceName, claimedAmount);
        saveToFile();
    }
    public void updateStatus(String newStatus) 
    {
        this.status = newStatus;
        saveToFile();
    }
}