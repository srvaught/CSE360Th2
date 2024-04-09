package phase3pkg;
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
        //TODO
    }
}