package com.example.askas;

import java.util.Map;
import java.util.*;
import java.text.*;
import java.io.*;

public class Patient extends UserRecord {
    private String patientID;
    private Date dateOfBirth;
    private MedicalHistory medicalHistory;
    private List<VisitSummary> visitSummaries;

    public Patient(String username, String name, String role, Map<String, String> additionalData) {
        super(username, name, role, additionalData);
    }

    public Patient(UserRecord userRecord) {
        super(userRecord.getUsername(), userRecord.getName(), userRecord.getRole(), userRecord.getAdditionalData());

        this.patientID = userRecord.getAdditionalData().get("Patient ID");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            this.dateOfBirth = sdf.parse(userRecord.getAdditionalData().get("DOB"));
        } catch (ParseException e) {
            System.err.println("Error parsing date of birth from UserRecord: " + e.getMessage());
            this.dateOfBirth = null; // Handle default or error case
        }
        // Initialize or handle other details like medical history and visit summaries if needed
    }
}