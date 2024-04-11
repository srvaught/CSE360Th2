package evenRecognizerTestbed;
import java.io.*;
public class InsuranceInformation 
{
    private String patientID;
    private String insurer;
    private String policyNumber;
    private String coverageDetails;
    public InsuranceInformation(String patientID, String insurer, String policyNumber, String coverageDetails) 
    {
        this.patientID = patientID;
        this.insurer = insurer;
        this.policyNumber = policyNumber;
        this.coverageDetails = coverageDetails;
    }
    public void updateCoverageDetails(String details) 
    {
        this.coverageDetails = details;
        saveToFile();
    }
    private void saveToFile()
    {
        String filename = "Data/InsuranceInformation/" + this.patientID + "_" + this.policyNumber + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            writer.write("PatientID: " + this.patientID + "\n");
            writer.write("Insurer: " + this.insurer + "\n");
            writer.write("PolicyNumber: " + this.policyNumber + "\n");
            writer.write("CoverageDetails: " + this.coverageDetails + "\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to save insurance information to file: " + e.getMessage());
        }
    }
}