package phase3pkg;
import java.util.Date;
public class Appointment 
{
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private Date date;
    private String time;
    private String purpose;
<<<<<<< Updated upstream
    public Appointment(String appointmentID, String patientID, String doctorID, Date date, String time, String purpose)
    {
=======

 

    public Appointment(String appointmentID, String patientID, String doctorID, LocalDateTime dateTime, String purpose) {
>>>>>>> Stashed changes
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.purpose = purpose;
    }
    public void schedule() 
    {
       //TODO
    }
    public void reschedule(Date newDate, String newTime) 
    {
        this.date = newDate;
        this.time = newTime;
    }
    public void cancel() 
    {
       //TODO
    }
}