package phase3pkg;
public class Prescription 
{
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String pharmacyID;
    private String conditionBeingTreated;
    public Prescription(String medicationName, String dosage, String frequency, String duration, String pharmacyID, String conditionBeingTreated) 
    {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.pharmacyID = pharmacyID;
        this.conditionBeingTreated = conditionBeingTreated;
    }
    public void updateDosage(String newDosage) 
    {
        this.dosage = newDosage;
    }
    public void updateFrequency(String newFrequency) 
    {
        this.frequency = newFrequency;
    }
}