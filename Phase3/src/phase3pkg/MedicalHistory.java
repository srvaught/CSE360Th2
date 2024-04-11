package phase3pkg;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.io.*;
import java.text.*;
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
    public static MedicalHistory loadFromFile(String patientID) 
    {
        MedicalHistory medicalHistory = new MedicalHistory();
        String filename = "Data/MedicalHistories/" + patientID + "_MedicalHistory.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                if (line.startsWith("Allergy: ")) 
                {
                    medicalHistory.addAllergy(line.substring("Allergy: ".length()));
                }
                else if (line.startsWith("Illness: ")) 
                {
                    medicalHistory.addPastIllness(line.substring("Illness: ".length()));
                } 
                else if (line.startsWith("Surgery: ")) 
                {
                    medicalHistory.addSurgery(line.substring("Surgery: ".length()));
                } 
                else if (line.startsWith("Condition: ")) 
                {
                    medicalHistory.addChronicCondition(line.substring("Condition: ".length()));
                } 
                else if (line.startsWith("Immunization: ")) 
                {
                    String[] parts = line.substring("Immunization: ".length()).split(", ");
                    if (parts.length >= 3) 
                    {
                        try 
                        {
                            Date dateAdministered = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
                            Immunization immunization = new Immunization(parts[0], dateAdministered, parts[2]);
                            medicalHistory.addImmunization(immunization);
                        } 
                        catch (ParseException e) 
                        {
                            System.err.println("Error parsing immunization date: " + e.getMessage());
                        }
                    }
                }
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to load medical history: " + e.getMessage());
            return null;
        }
        return medicalHistory;
    }
    public List<Immunization> getImmunizations()
    {
    	return immunizations;
    }
    public List<String> getChronicConditions()
    {
    	return chronicConditions;
    }
    public List<String> getSurgeries()
    {
    	return surgeries;
    }
    public List<String> getPastIllnesses()
    {
    	return pastIllnesses;
    }
    public List<String> getAllergies()
    {
    	return allergies;
    }
    public void addEntry(String line) 
    {
        if (line.startsWith("Allergy: ")) 
        {
            this.addAllergy(line.substring("Allergy: ".length()));
        }
        else if (line.startsWith("Illness: ")) 
        {
            this.addPastIllness(line.substring("Illness: ".length()));
        } 
        else if (line.startsWith("Surgery: ")) 
        {
            this.addSurgery(line.substring("Surgery: ".length()));
        } 
        else if (line.startsWith("Condition: ")) 
        {
            this.addChronicCondition(line.substring("Condition: ".length()));
        }
        else if (line.startsWith("Immunization: "))
        {
            String[] parts = line.substring("Immunization: ".length()).split(", ");
            if (parts.length == 3) 
            {
                String vaccineName = parts[0];
                Date dateAdministered; 
                try 
                {
                    dateAdministered = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
                }
                catch (ParseException e) 
                {
                    System.err.println("Error parsing immunization date: " + e.getMessage());
                    return;
                }
                String reactions = parts[2];
                Immunization immunization = new Immunization(vaccineName, dateAdministered, reactions);
                this.addImmunization(immunization);
            }
        }
    }
}
