package phase3pkg;
<<<<<<< Updated upstream
public class Doctor extends User 
{
=======

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
 
public class Doctor extends User {
>>>>>>> Stashed changes
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