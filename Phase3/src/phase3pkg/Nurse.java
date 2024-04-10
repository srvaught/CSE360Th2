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
        return PatientRecord.loadFromFile(patientID);
    }
    public MedicalHistory accessPatientHistory(String patientID) 
    {
        return MedicalHistory.loadFromFile(patientID);
    }
    public void updateVitals(String patientID, Vitals vitals) 
    {
        PatientRecord patientRecord = searchPatientRecord(patientID);
        if (patientRecord != null) 
        {
            patientRecord.updateVitals(vitals);
            patientRecord.saveToFile();
        } 
        else 
        {
            System.err.println("Patient record not found for ID: " + patientID);
        }
    }
}