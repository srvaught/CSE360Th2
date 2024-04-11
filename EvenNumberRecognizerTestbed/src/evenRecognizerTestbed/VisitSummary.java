package evenRecognizerTestbed;
import java.util.*;
import java.text.*;
public class VisitSummary
{
    private Date visitDate;
    private String reasonForVisit;
    private String diagnosis;
    private String treatment;
    private List<Prescription> prescriptionGiven;
    public VisitSummary(Date visitDate, String reasonForVisit, String diagnosis, String treatment) 
    {
        this.visitDate = visitDate;
        this.reasonForVisit = reasonForVisit;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescriptionGiven = new ArrayList<>();
    }
    public void addPrescription(Prescription prescription) 
    {
        prescriptionGiven.add(prescription);
    }
    public static VisitSummary fromString(String data) 
    {
        String[] parts = data.split(":");
        Date visitDate;
        try 
        {
            visitDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[0]);
        } 
        catch (ParseException e) 
        {
            throw new RuntimeException("Failed to parse visit date: " + e.getMessage());
        }
        String reasonForVisit = parts[1];
        String diagnosis = parts[2];
        String treatment = parts[3];
        VisitSummary summary = new VisitSummary(visitDate, reasonForVisit, diagnosis, treatment);
        if (parts.length > 4) 
        {
            String[] prescriptions = parts[4].split(",");
            for (String prescription : prescriptions) 
            {
                summary.addPrescription(Prescription.fromString(prescription));
            }
        }
        return summary;
    }
}