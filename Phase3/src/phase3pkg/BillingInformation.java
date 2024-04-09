package phase3pkg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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