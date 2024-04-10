package phase3pkg;
import java.util.*;
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
    public List<String> getAllergiesAdded()
    {
    	return allergiesAdded;
    }
    public List<String> getConditionsAdded()
    {
    	return conditionsAdded;
    }
    public List<Immunization> getImmunizationsAdded()
    {
    	return immunizationsAdded;
    }
}