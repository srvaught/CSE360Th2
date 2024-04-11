package evenRecognizerTestbed;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
public class PatientRecord 
{
    private String patientID;
    private Vitals vitals;
    private MedicalHistory medicalHistory;
    private List<Prescription> prescriptions;
    private List<VisitSummary> visitSummaries;
    public PatientRecord(String patientID) 
    {
        this.patientID = patientID;
        this.prescriptions = new ArrayList<>();
        this.visitSummaries = new ArrayList<>();
        //TODO
    }
    private void saveMedicalHistoryToFile() 
    {
        String filename = "Data/MedicalHistories/" + this.patientID + "_MedicalHistory.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            for (String allergy : this.medicalHistory.getAllergies()) 
            {
                writer.write("Allergy: " + allergy + "\n");
            }
            for (String illness : this.medicalHistory.getPastIllnesses()) 
            {
                writer.write("Illness: " + illness + "\n");
            }
            for (String surgery : this.medicalHistory.getSurgeries()) 
            {
                writer.write("Surgery: " + surgery + "\n");
            }
            for (String condition : this.medicalHistory.getChronicConditions()) 
            {
                writer.write("Condition: " + condition + "\n");
            }
            for (Immunization immunization : this.medicalHistory.getImmunizations()) 
            {
                writer.write("Immunization: " + immunization.toString() + "\n");
            }
        }
        catch (IOException e) 
        {
            System.err.println("Failed to save medical history: " + e.getMessage());
        }
    }
    public void updateVitals(Vitals newVitals) 
    {
        this.vitals = newVitals;
    }
    public void addVisitSummary(VisitSummary visitSummary) 
    {
        visitSummaries.add(visitSummary);
    }
    public void updateMedicalHistory(MedicalHistoryUpdate medicalHistoryUpdate) 
    {
        if (this.medicalHistory == null) 
        {
            this.medicalHistory = new MedicalHistory();
        }
        for (String allergy : medicalHistoryUpdate.getAllergiesAdded()) 
        {
            this.medicalHistory.addAllergy(allergy);
        }
        for (String illness : medicalHistoryUpdate.getConditionsAdded()) 
        {
            this.medicalHistory.addPastIllness(illness);
        }
        for (Immunization immunization : medicalHistoryUpdate.getImmunizationsAdded()) 
        {
            this.medicalHistory.addImmunization(immunization);
        }
        saveMedicalHistoryToFile();
    }

    public void addPrescription(Prescription prescription) 
    {
        prescriptions.add(prescription);
    }
    public static PatientRecord loadFromFile(String patientID) 
    {
        String filename = "Data/PatientRecords/" + patientID + "_Record.txt";
        File file = new File(filename);
        if (!file.exists()) 
        {
            System.err.println("File not found for Patient ID: " + patientID);
            return null;
        }
        PatientRecord patientRecord = new PatientRecord(patientID);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            String line;
            String currentSection = "";
            while ((line = reader.readLine()) != null) 
            {
                if (line.startsWith("[") && line.endsWith("]")) 
                {
                    currentSection = line.substring(1, line.length() - 1); 
                    continue;
                }
                switch (currentSection) 
                {
                    case "Vitals":
                        patientRecord.vitals = Vitals.fromString(line);
                        break;
                    case "MedicalHistory":
                        patientRecord.medicalHistory.addEntry(line);
                        break;
                    case "Prescriptions":
                        Prescription prescription = Prescription.fromString(line);
                        patientRecord.prescriptions.add(prescription);
                        break;
                    case "VisitSummaries":
                        VisitSummary visitSummary = VisitSummary.fromString(line);
                        patientRecord.visitSummaries.add(visitSummary);
                        break;
                }
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to load patient record from file: " + e.getMessage());
            return null;
        }
        return patientRecord;
    }
    public void saveToFile() 
    {
        String filename = "Data/PatientRecords/" + this.patientID + "_Record.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            writer.write("PatientID: " + this.patientID + "\n");
            if (this.vitals != null) 
            {
                writer.write("Vitals: " + this.vitals.toString() + "\n"); // Implement a suitable toString method in Vitals
            }
        } 
        catch (IOException e)
        {
            System.err.println("Failed to save patient record: " + e.getMessage());
        }
    }
    public void setPatientName(String patientName) {
        this.patientID = patientName;
    }
}