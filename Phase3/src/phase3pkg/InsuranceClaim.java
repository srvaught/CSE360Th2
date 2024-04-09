package phase3pkg;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    public void fileClaim(String serviceName, Double claimedAmount) 
    {
        servicesClaimed.put(serviceName, claimedAmount);
       //TODO
    }
    public void updateStatus(String newStatus) 
    {
        this.status = newStatus;
       //TODO
    }
}