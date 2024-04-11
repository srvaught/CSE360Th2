package evenRecognizerTestbed;
import java.util.*;
public class BillingInformation 
{
    private String billingID;
    private String patientID;
    private Map<String, Double> charges;
    private List<Payment> payments;
    private InsuranceClaim insuranceClaimDetails;
    public BillingInformation(String billingID, String patientID) 
    {
        this.billingID = billingID;
        this.patientID = patientID;
        this.charges = new HashMap<>();
        this.payments = new ArrayList<>();
    }
    public void addCharge(String service, double amount) 
    {
        charges.put(service, amount);
    }
    public void addPayment(Payment payment)
    {
        payments.add(payment);
    }
    public void fileInsuranceClaim(InsuranceClaim claim) 
    {
        this.insuranceClaimDetails = claim; 
    }
}