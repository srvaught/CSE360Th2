package phase3pkg;
public class Nurse extends User 
{
    private String nurseID;
    private String qualifications;
    public Nurse(String userID, String password, String name, String contactInformation, String nurseID, String qualifications)
    {
        super(userID, password, name, contactInformation);
        this.nurseID = nurseID;
        this.qualifications = qualifications;
    }
    public PatientRecord searchPatientRecord(String patientID)
    {
        //TODO
        return null;
    }
    public MedicalHistory accessPatientHistory(String patientID) 
    {
        //TODO
        return new MedicalHistory();
    }
    public void updateVitals(String patientID, Vitals vitals) 
    {
       //TODO
    }
}