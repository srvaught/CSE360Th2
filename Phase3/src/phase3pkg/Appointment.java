package phase3pkg;
import java.time.*;
import java.time.format.*;
import java.io.*;
public class Appointment 
{
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private LocalDateTime dateTime;
    private String purpose;
    public Appointment(String appointmentID, String patientID, String doctorID, LocalDateTime dateTime, String purpose) 
    {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.dateTime = dateTime;
        this.purpose = purpose;
    }
    public void schedule(String specialty)
    {
        DoctorManager manager = DoctorManager.getInstance();
        Doctor doctor = manager.findDoctor(doctorID, null);
        if (doctor == null || !doctor.isAvailable(dateTime)) 
        {
            doctor = manager.findDoctorBySpecialtyAndDate(specialty, dateTime);
            if (doctor != null) 
            {
                this.doctorID = doctor.getDoctorID();
                new Message("msg001", "system", patientID, "Appointment scheduled by specialty fallback for " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " with Dr. " + doctor.getName()).sendMessage();
                doctor.addAppointment(this);
            }
            else 
            {
                new Message("msg002", "system", patientID, "No available doctors found for this date and specialty.").sendMessage();
                return;
            }
        }
        else 
        {
            new Message("msg003", "system", patientID, "Appointment scheduled for " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +" with Dr. " + doctor.getName()).sendMessage();
            doctor.addAppointment(this);
        }
    }
    public void reschedule(LocalDateTime oldDateTime, LocalDateTime newDateTime) 
    {
        DoctorManager manager = DoctorManager.getInstance();
        Doctor doctor = manager.findDoctor(doctorID, null);
        if (doctor != null && doctor.isAvailable(newDateTime)) 
        {
            new Message("msg004", "system", patientID, "Rescheduling appointment from " + this.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +  " to " + newDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).sendMessage();
            this.dateTime = newDateTime;
        } 
        else 
        {
            new Message("msg005", "system", patientID, "Unable to reschedule, doctor not available at the new time.").sendMessage();
        }
    }
    public void cancel() 
    {
        new Message("msg006", "system", patientID,  "Cancelling appointment scheduled for " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).sendMessage();
    }
    public LocalDateTime getDateTime() 
    {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) 
    {
        this.dateTime = dateTime;
    }
    public String getDoctorID() 
    {
        return doctorID;
    }
    public String getPatientID() 
    {
        return patientID;
    }
    public String getPurpose() 
    {
        return purpose;
    }
    public String getAppointmentID() 
    {
        return appointmentID;
    }
    public void saveToFile() 
    {
        String filename = "Data/Appointments/" + this.appointmentID + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            writer.write("AppointmentID: " + this.appointmentID + "\n");
            writer.write("PatientID: " + this.patientID + "\n");
            writer.write("DoctorID: " + this.doctorID + "\n");
            writer.write("DateTime: " + this.dateTime.toString() + "\n");
            writer.write("Purpose: " + this.purpose + "\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to save appointment to file: " + e.getMessage());
        }
    }
}