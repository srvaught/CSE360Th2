package phase3pkg;
public class Doctor extends User 
{
    private String doctorID;
    private String specialty;
    public Doctor(String userID, String password, String name, String contactInformation, String doctorID, String specialty) 
    {
        super(userID, password, name, contactInformation);
        this.doctorID = doctorID;
        this.specialty = specialty;
    }
    public PatientRecord viewPatientRecord(String patientID) 
    {
        //TODO
        return null;
    }
    public void prescribeMedication(Prescription prescription) 
    {
       //TODO
    }
    public void sendPrescriptionToPharmacy(Prescription prescription, String pharmacyID) 
    {
      //TODO
    }
    public void updatePatientRecord(String patientID, RecordUpdate recordUpdates) 
    {
       //TODO
    }
}