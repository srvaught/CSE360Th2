package phase3pkg;
import java.util.Date;
public class Payment 
{
    private String paymentID;
    private String billingID;
    private double amount;
    private Date date;
    private String method;
    public Payment(String paymentID, String billingID, double amount, Date date, String method) 
    {
        this.paymentID = paymentID;
        this.billingID = billingID;
        this.amount = amount;
        this.date = date;
        this.method = method;
    }
    public void recordPayment() 
    {
    	//TODO
    }
}