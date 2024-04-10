package phase3pkg;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorManager {
    private static DoctorManager instance;  // The single instance of the manager
    private Map<String, Doctor> doctorsById;
    private Map<String, List<Doctor>> doctorsBySpecialty;

    // Private constructor to prevent instantiation outside of this class
    private DoctorManager() {
        this.doctorsById = new HashMap<>();
        this.doctorsBySpecialty = new HashMap<>();
    }

    // Public method to get the single instance of the class
    public static DoctorManager getInstance() {
        if (instance == null) {
            synchronized (DoctorManager.class) {
                if (instance == null) { // Double-checked locking
                    instance = new DoctorManager();
                }
            }
        }
        return instance;
    }

    // Methods to manage doctors (add, remove, find, etc.)
    public void loadDoctors(List<Doctor> existingDoctors) {
        for (Doctor doctor : existingDoctors) {
            doctorsById.put(doctor.getDoctorID(), doctor);
            doctorsBySpecialty.computeIfAbsent(doctor.getSpecialty(), k -> new ArrayList<>()).add(doctor);
        }
    }

    public Doctor findDoctor(String id, String specialty) {
        Doctor doctor = doctorsById.get(id);
        if (doctor == null) {
            List<Doctor> specialists = doctorsBySpecialty.get(specialty);
            if (specialists != null && !specialists.isEmpty()) {
                return specialists.get(0); // Return the first specialist
            }
        }
        return doctor;
    }

    public void addDoctor(Doctor doctor) {
        if (!doctorsById.containsKey(doctor.getDoctorID())) {
            doctorsById.put(doctor.getDoctorID(), doctor);
            doctorsBySpecialty.computeIfAbsent(doctor.getSpecialty(), k -> new ArrayList<>()).add(doctor);
        } else {
            throw new IllegalArgumentException("Doctor with ID " + doctor.getDoctorID() + " already exists.");
        }
    }

    public void removeDoctor(String doctorID) {
        Doctor doctor = doctorsById.remove(doctorID);
        if (doctor != null) {
            List<Doctor> specialists = doctorsBySpecialty.get(doctor.getSpecialty());
            if (specialists != null) {
                specialists.remove(doctor);
                if (specialists.isEmpty()) {
                    doctorsBySpecialty.remove(doctor.getSpecialty());
                }
            }
        }
    } 

    public Doctor findDoctorBySpecialtyAndDate(String specialty, LocalDateTime dateTime) {
        List<Doctor> specialists = doctorsBySpecialty.get(specialty);
        if (specialists != null && !specialists.isEmpty()) {
            for (Doctor doctor : specialists) {
                if (doctor.isAvailable(dateTime)) {
                    return doctor; // Return the first available doctor
                }
            }
        }
        return null; // Return null if no available doctor is found
    }
}
