package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.exception.PatientNotFoundException;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.List;

public class PatientService implements Searchable<Patient> {

    private final DataStore<Patient> patientStore;

    public PatientService(DataStore<Patient> patientStore) {
        Validator.requireNotNull(patientStore, "patientStore");
        this.patientStore = patientStore;
    }

    public void addPatient(Patient patient) {
        validatePatient(patient);
        if (patientStore.exists(patient.getId())) {
            throw new InvalidDataException("Patient with ID " + patient.getId() + " already exists.");
        }
        patientStore.save(patient.getId(), patient);
    }

    public Patient getPatientById(long patientId) {
        Validator.requirePositive(patientId, "patientId");
        Patient patient = patientStore.findById(patientId);
        if (patient == null) {
            throw new PatientNotFoundException(patientId);
        }
        return patient;
    }

    public List<Patient> getAllPatients() {
        return patientStore.findAll();
    }


    public void updatePatient(Patient patient) {
        validatePatient(patient);
        getPatientById(patient.getId());
        patientStore.update(patient.getId(), patient);
    }

    public void deletePatient(long patientId) {
        Validator.requirePositive(patientId, "patientId");
        getPatientById(patientId);
        patientStore.delete(patientId);
    }

    public Patient searchPatient(long patientId) {
        return getPatientById(patientId);
    }

    public List<Patient> searchPatient(String name) {
        Validator.requireNonBlank(name, "name");
        return getAllPatients()
                .stream()
                .filter(patient -> matches(patient.getName(), name))
                .toList();
    }

    public List<Patient> searchPatient(int age) {
        Validator.requireValidAge(age);

        return getAllPatients()
                .stream()
                .filter(patient -> patient.getAge() == age)
                .toList();
    }

    @Override
    public Patient searchById(long id) {
        return getPatientById(id);
    }

    private void validatePatient(Patient patient) {
        Validator.requireNotNull(patient, "patient");
        Validator.requirePositive(patient.getId(), "patientId");
        Validator.requireNonBlank(patient.getName(), "name");
        Validator.requireValidAge(patient.getAge());
        Validator.requireNonBlank(patient.getPhone(), "phone");
        Validator.requireNonBlank(patient.getEmail(), "email");
        Validator.requireNonBlank(patient.getBloodGroup(), "bloodGroup");
        Validator.requireNonBlank(
                patient.getMedicalHistory(),
                "medicalHistory"
        );
    }
}