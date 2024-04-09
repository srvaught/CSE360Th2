package phase3pkg;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
}