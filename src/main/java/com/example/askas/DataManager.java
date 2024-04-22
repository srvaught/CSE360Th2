package com.example.askas;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataManager {

    public static final Map<String, String> DELIMITERS = new HashMap<>();

    static {
        DELIMITERS.put("GEN_INFO_START", "<!GEN_INFO:BGN>");
        DELIMITERS.put("GEN_INFO_END", "<!GEN_INFO:END>");
        DELIMITERS.put("MED_HST_START", "<!MED_HST:BGN>");
        DELIMITERS.put("MED_HST_END", "<!MED_HST:END>");
        DELIMITERS.put("CRT_MED_START", "<!CRT_MED:BGN>");
        DELIMITERS.put("CRT_MED_END", "<!CRT_MED:END>");
        DELIMITERS.put("MSG_IBX_START", "<!MSG_IBX:BGN>");
        DELIMITERS.put("MSG_IBX_END", "<!MSG_IBX:END>");
        DELIMITERS.put("SMRY_VST_START", "<!SMRY_VST:BGN>");
        DELIMITERS.put("SMRY_VST_END", "<!SMRY_VST:END>");
        DELIMITERS.put("VTL_SMRY_START", "<!VTL_SMRY:BGN>");
        DELIMITERS.put("VTL_SMRY_END", "<!VTL_SMRY:END>");
        // ... Add other delimiters here
    }

    public static String readContentFromFile(Path filePath) throws IOException {
        return Files.readString(filePath);
    }

    public static boolean authenticateRecordExistence(String filePath) {
        File file = new File(filePath);
        return file.exists(); // Returns true if the file exists, false otherwise
    }

    public static void updateSection(Path filePath, String contentBetween, String startTag, String endTag) throws IOException {
        String content = Files.readString(filePath);
        int start = content.indexOf(startTag) + startTag.length();
        int end = content.indexOf(endTag);

        if (start == -1 || end == -1) {
            throw new IllegalArgumentException("Delimiter not found in the content.");
        }

        // Build the new content string
        String updatedContent = content.substring(0, start) + "\n" + contentBetween.trim() + "\n" + content.substring(end);

        // Write back to the file
        Files.writeString(filePath, updatedContent, StandardCharsets.UTF_8);
    }

    public static String getDataManagerDirPath() {
        String srcToJavaPath = "/src/main/java/";
        String packageName = DataManager.class.getPackage().getName().replace('.', '/');
        return System.getProperty("user.dir") + srcToJavaPath + packageName;
    }

    public static UserRecord authenticateUser(String filePath, String username, String password) {
        Map<String, String> userData = new HashMap<>();
        String key = null;
        StringBuilder valueBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for key-value pair
                if (line.contains(":")) {
                    // Commit the previous key-value pair if exists
                    if (key != null) {
                        userData.put(key, valueBuilder.toString().trim());
                        valueBuilder = new StringBuilder();
                    }

                    // Start a new key-value pair
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        key = parts[0].trim();
                        valueBuilder.append(parts[1].trim());
                    }
                } else {
                    // This line is a continuation of the previous value
                    valueBuilder.append(System.lineSeparator()).append(line.trim());
                }
            }
            // Commit the last key-value pair if exists
            if (key != null) {
                userData.put(key, valueBuilder.toString().trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        // Authentication check
        if (userData.containsKey("Username") && userData.get("Username").equals(username) &&
                userData.containsKey("Password") && userData.get("Password").equals(password)) {
            return new UserRecord(username, userData.get("Name"), userData.get("Role"), userData);
        }

        return null; // Return null if authentication fails
    }

    public static UserRecord updatedUserHandler(Path path) {
        String username = "";
        String name = "";
        String role = "";
        Map<String, String> additionalData = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username:")) {
                    username = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Name:")) {
                    name = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Role:")) {
                    role = line.substring(line.indexOf(":") + 1).trim();
                } else {
                    // Add other data to the map
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        additionalData.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or throw it to be handled by the caller
        }

        return new UserRecord(username, name, role, additionalData);
    }

    public static void createUserRecord(String username, String fullName, String address, String phoneNumber, String email, String password, String gender, LocalDate dob, String medicalHistory, String currentMedications, String role) throws IOException {
        // Format the date of birth to include in the file name
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dobFormatted = dob.format(formatter);

        // Normalize full name for filename usage
        String normalizedFullName = fullName.replaceAll("\\s+", "").toLowerCase();

        // Generate the filename using username and DOB for uniqueness
        String filename = username + "_" + normalizedFullName + "With" + dobFormatted + "_info.txt";

        // Create file path in the appropriate directory based on role
        Path filePath = Paths.get(DataManager.getDataManagerDirPath() + "/data/" + role.toLowerCase() + "/" + filename);

        // Create the directory if it doesn't exist
        File file = filePath.toFile();
        file.getParentFile().mkdirs(); // Ensure the directory exists

        // Write data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // General Information
            writer.write(DataManager.DELIMITERS.get("GEN_INFO_START"));
            writer.newLine();
            writer.write("Patient ID: GeneratedID"); // Consider implementing a method to generate unique IDs
            writer.newLine();
            writer.write("Username: " + username);
            writer.newLine();
            writer.write("Password: " + password); // Consider implementing secure password storage
            writer.newLine();
            writer.write("Name: " + fullName);
            writer.newLine();
            writer.write("Role: " + role);
            writer.newLine();
            writer.write("DOB: " + dobFormatted);
            writer.newLine();
            writer.write("Gender: " + gender);
            writer.newLine();
            writer.write("Address: " + address);
            writer.newLine();
            writer.write("Contact Number: " + phoneNumber);
            writer.newLine();
            writer.write("Email: " + email);
            writer.newLine();
            writer.write(DataManager.DELIMITERS.get("GEN_INFO_END"));
            writer.newLine();

            // Medical History
            writer.write(DataManager.DELIMITERS.get("MED_HST_START"));
            writer.newLine();
            writer.write("Medical History:");
            writer.newLine();
            writer.write(medicalHistory); // Assuming medical history is formatted correctly
            writer.newLine();
            writer.write(DataManager.DELIMITERS.get("MED_HST_END"));
            writer.newLine();

            // Current Medications
            writer.write(DataManager.DELIMITERS.get("CRT_MED_START"));
            writer.newLine();
            writer.write("Current Medications:");
            writer.newLine();
            writer.write(currentMedications); // Assuming medications are formatted correctly
            writer.newLine();
            writer.write(DataManager.DELIMITERS.get("CRT_MED_END"));
            writer.newLine();

            // Messages - Empty initially
            writer.write(DataManager.DELIMITERS.get("MSG_IBX_START"));
            writer.newLine();
            writer.write("Messages:");
            writer.newLine();
            writer.write(DataManager.DELIMITERS.get("MSG_IBX_END"));
            writer.newLine();

            // Vital Summaries - Empty initially
            writer.write(DataManager.DELIMITERS.get("VTL_SMRY_START"));
            writer.newLine();
            writer.write("Vitals:");
            writer.newLine();
            writer.write(DataManager.DELIMITERS.get("VTL_SMRY_END"));
            writer.newLine();

            // Visit Summaries - Empty initially
            writer.write(DataManager.DELIMITERS.get("SMRY_VST_START"));
            writer.newLine();
            writer.write("Summary Visits:");
            writer.newLine();
            writer.write(DataManager.DELIMITERS.get("SMRY_VST_END"));
            writer.newLine();
        }
    }

    public static boolean doesUsernameFileExist(String username, String role) {
        Path userFilesDirectoryPath = Paths.get(DataManager.getDataManagerDirPath() + "/data/" + role +"/");
        File userFilesDirectory = userFilesDirectoryPath.toFile();
        FilenameFilter filter = (dir, name) -> name.startsWith(username + "_") && name.endsWith("_info.txt");
        String[] matchingFiles = userFilesDirectory.list(filter);
        return matchingFiles != null && matchingFiles.length > 0;
    }

    public static Path findUserFilePath(String username, String role) {
        Path userFilesDirectoryPath = Paths.get(DataManager.getDataManagerDirPath() + "/data/" + role + "/");
        File userFilesDirectory = userFilesDirectoryPath.toFile();
        FilenameFilter filter = (dir, name) -> name.startsWith(username + "_") && name.endsWith("_info.txt");

        String[] matchingFiles = userFilesDirectory.list(filter);

        // Return the first match as a Path object
        if (matchingFiles != null && matchingFiles.length > 0) {
            // Assuming there is only one matching file for the username
            return Paths.get(userFilesDirectoryPath.toString(), matchingFiles[0]);
        }
        // Return null if no matching files are found
        return null;
    }

    public static Path findNameDOBFilePath(String fullname, LocalDate dob, String role) {
        // Formatting the date of birth in the expected part of the filename
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dobFormatted = dob.format(formatter);

        Path userFilesDirectoryPath = Paths.get(DataManager.getDataManagerDirPath() + "/data/" + role + "/");
        File userFilesDirectory = userFilesDirectoryPath.toFile();

        // Define the filename filter based on full name and date of birth
        FilenameFilter filter = (dir, name) -> name.contains("_" + fullname + "With" + dobFormatted + "_") && name.endsWith("_info.txt");

        String[] matchingFiles = userFilesDirectory.list(filter);

        // Return the first match as a Path object
        if (matchingFiles != null && matchingFiles.length > 0) {
            // Assuming there is only one matching file for the given criteria
            return Paths.get(userFilesDirectoryPath.toString(), matchingFiles[0]);
        }

        // Return null if no matching files are found
        return null;
    }
}

