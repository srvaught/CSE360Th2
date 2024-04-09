package phase3pkg;
import java.util.Date;
public class Immunization 
{
    private String vaccineName;
    private Date dateAdministered;
    private String reactions;
    public Immunization(String vaccineName, Date dateAdministered, String reactions)
    {
        this.vaccineName = vaccineName;
        this.dateAdministered = dateAdministered;
        this.reactions = reactions;
    }
    public void updateReactions(String newReactions) 
    {
        this.reactions = newReactions;
    }
}