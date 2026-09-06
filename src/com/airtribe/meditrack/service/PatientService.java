package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.PatientNotFoundException;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class PatientService implements Searchable<Patient> {

    private final DataStore<Patient> patientStore;

    public PatientService(DataStore<Patient> patientStore) {
        Validator.requireNotNull(patientStore, "Patient store");
        this.patientStore = patientStore;
    }

    public void addPatient(Patient patient) {
        Validator.requireNotNull(patient, "Patient");

        Validator.requirePositive(patient.getId(), "Patient ID");
        Validator.requireNonBlank(patient.getName(), "Patient name");
        Validator.requireValidAge(patient.getAge());

        if (patientStore.exists(patient.getId())) {
            throw new IllegalArgumentException(
                    "Patient with ID " + patient.getId() + " already exists."
            );
        }

        patientStore.save(patient.getId(), patient);
    }

    public Patient getPatientById(long patientId) {
        Validator.requirePositive(patientId, "Patient ID");

        Patient patient = patientStore.findById(patientId);

        if (patient == null) {
            throw new PatientNotFoundException(
                    "Patient with ID " + patientId + " not found."
            );
        }

        return patient;
    }

    public List<Patient> getAllPatients() {
        return patientStore.findAll();
    }

    public void updatePatient(Patient patient) {
        Validator.requireNotNull(patient, "Patient");
        Validator.requirePositive(patient.getId(), "Patient ID");

        getPatientById(patient.getId());

        Validator.requireNonBlank(patient.getName(), "Patient name");
        Validator.requireValidAge(patient.getAge());

        patientStore.update(patient.getId(), patient);
    }

    public void deletePatient(long patientId) {
        getPatientById(patientId);
        patientStore.delete(patientId);
    }

    public List<Patient> searchPatients(String searchText) {

        Validator.requireNonBlank(searchText, "Search text");

        String search = searchText.trim();

        return patientStore.findAll()
                .stream()
                .filter(patient ->
                        matches(patient.getName(), search)
                                || String.valueOf(patient.getId()).contains(search)
                                || matches(patient.getPhone(), search)
                                || matches(patient.getEmail(), search)
                )
                .collect(Collectors.toList());
    }

    public int getPatientCount() {
        return patientStore.size();
    }
}