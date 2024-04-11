package phase3pkg;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
public class Doctor extends User 
{
    private String doctorID;
    private String specialty;
    private Map<LocalDateTime, Appointment> schedule;
    public Doctor(String userID, String password, String name, String contactInformation, String doctorID, String specialty)
    {
        super(userID, password, name, contactInformation);
        this.doctorID = doctorID;
        this.specialty = specialty;
        this.schedule = new HashMap<>();
    }
    public void initializeFile() 
    {
        try 
        {
            saveToFile();
        } 
        catch (IllegalStateException e) 
        {
            System.err.println(e.getMessage());
        }
    }
    public PatientRecord viewPatientRecord(String patientID)
    {
        return PatientRecord.loadFromFile(patientID);
    }
    public void prescribeMedication(Prescription prescription) 
    {
        prescription.saveToFile();
    }
    public void sendPrescriptionToPharmacy(Prescription prescription, String pharmacyID) 
    {
        String filename = "Data/Pharmacy/" + pharmacyID + "_Prescriptions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) 
        {
            writer.write("Prescription ID: " + System.currentTimeMillis());
            writer.newLine();
            writer.write("Medication Name: " + prescription.getMedicationName());
            writer.newLine();
            writer.write("Dosage: " + prescription.getDosage());
            writer.newLine();
            writer.write("Frequency: " + prescription.getFrequency());
            writer.newLine();
            writer.write("Duration: " + prescription.getDuration());
            writer.newLine();
            writer.write("Pharmacy ID: " + pharmacyID);
            writer.newLine();
            writer.write("Condition Being Treated: " + prescription.getConditionBeingTreated());
            writer.newLine();
            writer.write("----");
            writer.newLine();
        }
        catch (IOException e) 
        {
            System.err.println("Failed to send prescription to pharmacy: " + e.getMessage());
        }
    }
    public void updatePatientRecord(String patientID, RecordUpdate recordUpdates) 
    {
        String filename = "Data/PatientRecords/" + patientID + "_Updates.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) 
        { 
            writer.write("Update Date: " + recordUpdates.getUpdateDate().toString());
            writer.newLine();
            for (Map.Entry<String, String> entry : recordUpdates.getUpdateFields().entrySet()) 
            {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
            writer.write("Updated By: " + recordUpdates.getUpdaterID());
            writer.newLine();
            writer.write("----");
            writer.newLine();
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to update patient record: " + e.getMessage());
        }
    }
    public boolean isAvailable(LocalDateTime dateTime) 
    {
        return !schedule.containsKey(dateTime);
    }
    private void saveToFile() throws IllegalStateException 
    {
        String filename = "Data/Doctor/" + this.doctorID + "_DoctorInfo.txt";
        File file = new File(filename);
        if (file.exists()) 
        {
            throw new IllegalStateException("File already exists for Doctor ID: " + this.doctorID);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) 
        {
            writer.write("UserID: " + this.getUserID() + "\n");
            writer.write("Password: " + this.getPassword() + "\n");
            writer.write("Name: " + this.getName() + "\n");
            writer.write("Contact Information: " + this.getContactInformation() + "\n");
            writer.write("Doctor ID: " + this.doctorID + "\n");
            writer.write("Specialty: " + this.specialty + "\n");
            writer.write("Appointments:\n");
            for (Appointment appointment : this.schedule.values()) 
            {
                writer.write(String.format("%s | %s | %s | %s\n", appointment.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), appointment.getAppointmentID(), appointment.getPatientID(), appointment.getPurpose()));
            }
        } 
        catch (IOException e)
        {
            System.err.println("Failed to write doctor info to file: " + e.getMessage());
        }
    }
    public static Doctor loadFromFile(String doctorID) 
    {
        String filename = "Data/Doctor/" + doctorID + "_DoctorInfo.txt";
        File file = new File(filename);
        if (!file.exists()) 
        {
            throw new IllegalArgumentException("No file found for Doctor ID: " + doctorID + ". Please check the ID and try again.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            Map<LocalDateTime, Appointment> schedule = new HashMap<>();
            String userID = "", password = "", name = "", contactInfo = "", specialty = "";
            String line;
            while ((line = reader.readLine()) != null) 
            {
                if (line.startsWith("UserID: ")) 
                {
                    userID = line.substring(8);
                } 
                else if (line.startsWith("Password: ")) 
                {
                    password = line.substring(10);
                } 
                else if (line.startsWith("Name: ")) 
                {
                    name = line.substring(6);
                } 
                else if (line.startsWith("Contact Information: ")) 
                {
                    contactInfo = line.substring(21);
                } 
                else if (line.startsWith("Doctor ID: ")) 
                {
                    
                }
                else if (line.startsWith("Specialty: "))
                {
                    specialty = line.substring(10);
                } 
                else if (line.contains("|")) 
                {
                    String[] parts = line.split(" \\| ");
                    LocalDateTime dateTime = LocalDateTime.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    String apptID = parts[1];
                    String patientID = parts[2];
                    String purpose = parts[3];
                    Appointment appointment = new Appointment(apptID, patientID, doctorID, dateTime, purpose);
                    schedule.put(dateTime, appointment);
                }
            }
            Doctor doctor = new Doctor(userID, password, name, contactInfo, doctorID, specialty);
            doctor.schedule = schedule; 
            return doctor;
        }
        catch (IOException e) 
        {
            System.err.println("Error reading doctor info from file for Doctor ID: " + doctorID + "; Error: " + e.getMessage());
            throw new RuntimeException("Failed to load doctor information. Please contact support.");
        }
    }
    public String getSpecialty() 
    {
        return specialty;
    }
    public String getDoctorID() 
    {
        return doctorID;
    }
    public boolean addAppointment(Appointment appointment) 
    {
        LocalDateTime dateTime = appointment.getDateTime();
        if (!schedule.containsKey(dateTime)) 
        {
            schedule.put(dateTime, appointment);
            return true;
        }
        return false; 
    }
} 
