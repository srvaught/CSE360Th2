package phase3pkg;
import java.util.Date;
public class HealthConcern
{
    private String concernID;
    private String patientID;
    private String description;
    private String notedBy;
    private Date dateNoted;
    public HealthConcern(String concernID, String patientID, String description, String notedBy, Date dateNoted)
    {
        this.concernID = concernID;
        this.patientID = patientID;
        this.description = description;
        this.notedBy = notedBy;
        this.dateNoted = dateNoted;
    }
    public void updateDescription(String newDescription) 
    {
        this.description = newDescription;
    }
}