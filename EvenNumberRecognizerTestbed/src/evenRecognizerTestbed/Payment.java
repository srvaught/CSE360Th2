package evenRecognizerTestbed;
import java.util.*;
import java.io.*;
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
        String directoryPath = "Data/Payments/";
        String filename = directoryPath + this.billingID + "_Payments.txt";
        new File(directoryPath).mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) 
        {
            writer.write("PaymentID: " + this.paymentID + "\n");
            writer.write("BillingID: " + this.billingID + "\n");
            writer.write("Amount: " + this.amount + "\n");
            writer.write("Date: " + this.date.toString() + "\n");
            writer.write("Method: " + this.method + "\n");
            writer.write("---------------------------------\n"); 
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to record payment: " + e.getMessage());
        }
    }
}