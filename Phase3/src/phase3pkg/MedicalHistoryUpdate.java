package phase3pkg;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class MedicalHistoryUpdate 
{
    private Date updateDate;
    private List<String> allergiesAdded;
    private List<String> conditionsAdded;
    private List<Immunization> immunizationsAdded;
    public MedicalHistoryUpdate() 
    {
        this.updateDate = new Date();
        this.allergiesAdded = new ArrayList<>();
        this.conditionsAdded = new ArrayList<>();
        this.immunizationsAdded = new ArrayList<>();
    }
    public void addAllergy(String allergy) 
    {
        allergiesAdded.add(allergy);
    }
    public void addCondition(String condition) 
    {
        conditionsAdded.add(condition);
    }
    public void addImmunization(Immunization immunization) 
    {
        immunizationsAdded.add(immunization);
    }
}