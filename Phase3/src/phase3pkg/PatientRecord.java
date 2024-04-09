package phase3pkg;
import java.util.ArrayList;
import java.util.List;
public class PatientRecord 
{
    private String patientID;
    private Vitals vitals;
    private MedicalHistory medicalHistory;
    private List<Prescription> prescriptions;
    private List<VisitSummary> visitSummaries;
    public PatientRecord(String patientID) 
    {
        this.patientID = patientID;
        this.prescriptions = new ArrayList<>();
        this.visitSummaries = new ArrayList<>();
        //TODO
    }
    public void addVisitSummary(VisitSummary visitSummary) 
    {
        visitSummaries.add(visitSummary);
    }
    public void updateMedicalHistory(MedicalHistoryUpdate medicalHistoryUpdate) 
    {
        //TODO
    }
    public void addPrescription(Prescription prescription) 
    {
        prescriptions.add(prescription);
    }
}