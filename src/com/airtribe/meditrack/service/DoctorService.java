package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.exception.DoctorNotFoundException;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.Comparator;
import java.util.List;


public class DoctorService implements Searchable<Doctor> {
    private final DataStore<Doctor> doctorStore;

    public DoctorService(DataStore<Doctor> doctorStore) {
        this.doctorStore = doctorStore;
    }

    /**
     * Adds a doctor to the data store after validating the doctor and its ID.
     *
     * @param doctor doctor to add
     * @throws IllegalArgumentException if the doctor is null or its ID is not positive
     */
    public void addDoctor(Doctor doctor) {
        Validator.requireNotNull(doctor, "doctor");
        Validator.requirePositive(doctor.getId(), "doctorId");
        doctorStore.save(doctor.getId(), doctor);
    }
    /**
     * Retrieves a doctor by its unique ID.
     *
     * @param doctorId ID of the doctor to retrieve
     * @return the matching doctor
     * @throws IllegalArgumentException if the ID is not positive
     * @throws DoctorNotFoundException if no doctor exists with the given ID
     */
    public Doctor getDoctorById(long doctorId) {
        Validator.requirePositive(doctorId, "doctorId");
        Doctor doctor =  doctorStore.findById(doctorId);
        if(doctor == null) {
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found.");
        }
        return doctor;
    }
    /**
     * Retrieves all doctors from the data store.
     *
     * @return list of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return doctorStore.findAll();
    }
    /**
     * Updates an existing doctor in the data store.
     *
     * @param doctor doctor containing the updated information
     * @throws IllegalArgumentException if the doctor is null or its ID is not positive
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    public void updateDoctor(Doctor doctor) {
        Validator.requireNotNull(doctor, "doctor");
        getDoctorById(doctor.getId());
        doctorStore.update(doctor.getId(), doctor);
    }
    /**
     * Deletes an existing doctor using the doctor's unique ID.
     *
     * @param doctorId ID of the doctor to delete
     * @throws IllegalArgumentException if the ID is not positive
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    public void deleteDoctor(long doctorId){
        Validator.requirePositive(doctorId, "doctorId");
        if(!doctorStore.exists(doctorId)) {
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found.");
        }
        doctorStore.delete(doctorId);
    }
    /**
     * Searches for a doctor using the doctor's unique ID.
     *
     * @param doctorId ID of the doctor to search for
     * @return the matching doctor
     * @throws IllegalArgumentException if the ID is not positive
     * @throws DoctorNotFoundException if no doctor exists with the given ID
     */
    public Doctor searchDoctor(long doctorId){
        return getDoctorById(doctorId);
    }
    /**
     * Searches for doctors whose names contain the supplied search text.
     *
     * <p>Name matching is case-insensitive.</p>
     *
     * @param name text to search for in doctor names
     * @return list of doctors matching the search text
     * @throws IllegalArgumentException if the search text is blank
     */
    public List<Doctor> searchDoctor(String name){
        Validator.requireNonBlank(name, "name");
        return getAllDoctors().stream().filter(doctor -> matches(doctor.getName(), name)).toList();
    }
    /**
     * Finds doctors belonging to the specified medical specialization.
     *
     * @param specialization specialization to search for
     * @return list of doctors with the specified specialization
     * @throws IllegalArgumentException if the specialization is null
     */
    public List<Doctor> findBySpecialization(Specialization specialization){
        Validator.requireNotNull(specialization, "specialization");
        return getAllDoctors().stream().filter(doctor -> doctor.getSpecialization() == specialization).toList();
    }
    /**
     * Calculates the average consultation fee across all doctors.
     *
     * @return average consultation fee, or 0.0 if no doctors exist
     */
    public double calculateAverageConsultationFee(){
        return getAllDoctors().stream().mapToDouble(Doctor::getConsultationFee).average().orElse(0.0);
    }
    /**
     * Finds the doctor with the highest consultation fee.
     *
     * @return doctor with the highest consultation fee,
     *         or null if no doctors exist
     */
    public Doctor findHighestConsultationFeeDoctor(){
        return getAllDoctors().stream().max(Comparator.comparingDouble(Doctor::getConsultationFee)).orElse(null);
    }
}
