package phase3pkg;
import java.util.ArrayList;
import java.util.List;
public class MedicalHistory 
{
    private List<String> allergies;
    private List<String> pastIllnesses;
    private List<String> surgeries;
    private List<String> chronicConditions;
    private List<Immunization> immunizations;
    public MedicalHistory() 
    {
        this.allergies = new ArrayList<>();
        this.pastIllnesses = new ArrayList<>();
        this.surgeries = new ArrayList<>();
        this.chronicConditions = new ArrayList<>();
        this.immunizations = new ArrayList<>();
    }
    public void addAllergy(String allergy) 
    {
        allergies.add(allergy);
    }
    public void addPastIllness(String illness) 
    {
        pastIllnesses.add(illness);
    }
    public void addSurgery(String surgery) 
    {
        surgeries.add(surgery);
    }
    public void addChronicCondition(String condition)
    {
        chronicConditions.add(condition);
    }
    public void addImmunization(Immunization immunization) 
    {
        immunizations.add(immunization);
    }
}