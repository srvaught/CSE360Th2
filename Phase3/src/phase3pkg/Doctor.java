package phase3pkg;
package com.phase3.cse360th2t2;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {
    private String doctorID;
    private String specialty;
    private Map<LocalDateTime, Appointment> schedule;

    public Doctor(String userID, String password, String name, String contactInformation, String doctorID, String specialty) {
        super(userID, password, name, contactInformation);
        this.doctorID = doctorID;
        this.specialty = specialty;
        this.schedule = new HashMap<>();
    }

    public void initializeFile() {
        try {
            saveToFile();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            // Handle or log the error appropriately
        }
    }
    public PatientRecord viewPatientRecord(String patientID)
    {
        //TODO
        return null;
    }
    public void prescribeMedication(Prescription prescription)
    {
        //TODO
    }
    public void sendPrescriptionToPharmacy(Prescription prescription, String pharmacyID)
    {
        //TODO
    }
    public void updatePatientRecord(String patientID, RecordUpdate recordUpdates)
    {
        //TODO
    }
    // Method to check if the doctor is available at a given datetime
    public boolean isAvailable(LocalDateTime dateTime) {
        return !schedule.containsKey(dateTime);
    }

    private void saveToFile() throws IllegalStateException {
        String filename = "Data/Doctor/" + this.doctorID + "_DoctorInfo.txt";
        File file = new File(filename);

        // Check if the file already exists
        if (file.exists()) {
            throw new IllegalStateException("File already exists for Doctor ID: " + this.doctorID);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("UserID: " + this.getUserID() + "\n");
            writer.write("Password: " + this.getPassword() + "\n");
            writer.write("Name: " + this.getName() + "\n");
            writer.write("Contact Information: " + this.getContactInformation() + "\n");
            writer.write("Doctor ID: " + this.doctorID + "\n");
            writer.write("Specialty: " + this.specialty + "\n");
            writer.write("Appointments:\n");
            for (Appointment appointment : this.schedule.values()) {
                writer.write(String.format("%s | %s | %s | %s\n",
                        appointment.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        appointment.getAppointmentID(),
                        appointment.getPatientID(),
                        appointment.getPurpose()));
            }
        } catch (IOException e) {
            System.err.println("Failed to write doctor info to file: " + e.getMessage());
        }
    }

    public static Doctor loadFromFile(String doctorID) {
        String filename = "Data/Doctor/" + doctorID + "_DoctorInfo.txt";
        File file = new File(filename);

        if (!file.exists()) {
            throw new IllegalArgumentException("No file found for Doctor ID: " + doctorID + ". Please check the ID and try again.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<LocalDateTime, Appointment> schedule = new HashMap<>();
            String userID = "", password = "", name = "", contactInfo = "", specialty = "";
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("UserID: ")) {
                    userID = line.substring(8);
                } else if (line.startsWith("Password: ")) {
                    password = line.substring(10);
                } else if (line.startsWith("Name: ")) {
                    name = line.substring(6);
                } else if (line.startsWith("Contact Information: ")) {
                    contactInfo = line.substring(21);
                } else if (line.startsWith("Doctor ID: ")) {
                    // Normally doctorID is already known, ignore or validate
                } else if (line.startsWith("Specialty: ")) {
                    specialty = line.substring(10);
                } else if (line.contains("|")) { // Assumes appointment lines contain '|'
                    String[] parts = line.split(" \\| ");
                    LocalDateTime dateTime = LocalDateTime.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    String apptID = parts[1];
                    String patientID = parts[2];
                    String purpose = parts[3];
                    Appointment appointment = new Appointment(apptID, patientID, doctorID, dateTime, purpose);
                    schedule.put(dateTime, appointment);
                }
            }
            Doctor doctor = new Doctor(userID, password, name, contactInfo, doctorID, specialty);
            doctor.schedule = schedule;  // Assuming 'schedule' is accessible or use a setter
            return doctor;
        } catch (IOException e) {
            System.err.println("Error reading doctor info from file for Doctor ID: " + doctorID + "; Error: " + e.getMessage());
            throw new RuntimeException("Failed to load doctor information. Please contact support.");
        }
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Attempts to add an appointment to the doctor's schedule.
     * @param appointment The appointment to add.
     * @return true if the appointment was added successfully, false if there is a scheduling conflict.
     */
    public boolean addAppointment(Appointment appointment) {
        LocalDateTime dateTime = appointment.getDateTime();
        if (!schedule.containsKey(dateTime)) {
            schedule.put(dateTime, appointment);
            return true;
        }
        return false;  // There is already an appointment at this time
    }
}
