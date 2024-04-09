package phase3pkg;
import java.util.*;
public class Patient extends User 
{
    private String patientID;
    private Date dateOfBirth;
    private MedicalHistory medicalHistory;
    private List<VisitSummary> visitSummaries;
    public Patient(String userID, String password, String name, String contactInformation, String patientID, Date dateOfBirth) 
    {
        super(userID, password, name, contactInformation); 
        this.patientID = patientID;
        this.dateOfBirth = dateOfBirth;
    }
    public List<VisitSummary> viewVisitSummaries() 
    {
        return visitSummaries;
    }
    public void updateProfile(ProfileUpdate profileUpdates) 
    {
        //TODO
    }
    public void sendMessage(Message message) 
    {
        //TODO
    }
}