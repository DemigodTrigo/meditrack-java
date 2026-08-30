package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private final DataStore<Appointment> appointmentStore;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentService(DataStore<Appointment> appointmentStore,
                              DoctorService doctorService,
                              PatientService patientService) {
        this.appointmentStore = appointmentStore;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public void createAppointment(Appointment appointment) {
        Validator.requireNotNull(appointment, "appointment");
        Validator.requireNotNull(appointment.getDoctor(), "doctor");
        Validator.requireNotNull(appointment.getPatient(), "patient");
        Validator.requireNotNull(appointment.getAppointmentDateTime(), "appointmentDateTime");

        Doctor doctor = doctorService.getDoctorById(appointment.getDoctor().getId());
        Patient patient = patientService.getPatientById(appointment.getPatient().getId());

        if (!doctor.isAvailable()) {
            throw new InvalidDataException(
                    "Doctor " + doctor.getName() + " is not currently available");
        }

        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Appointment date/time must be in the future");
        }

        if (hasConflict(doctor.getId(), appointment.getAppointmentDateTime())) {
            throw new InvalidDataException(
                    "Doctor " + doctor.getName() + " already has an appointment at that time");
        }

        long appointmentId = appointment.getAppointmentId() > 0
                ? appointment.getAppointmentId()
                : IdGenerator.nextAppointmentId();

        Appointment toSave = new Appointment(
                appointmentId,
                doctor,
                patient,
                appointment.getAppointmentDateTime(),
                appointment.getStatus() != null ? appointment.getStatus() : AppointmentStatus.PENDING,
                appointment.getReason());

        appointmentStore.save(appointmentId, toSave);
    }

    /** Convenience overload: builds and saves an appointment by ID lookups only. */
    public Appointment createAppointment(long doctorId, long patientId,
                                         LocalDateTime appointmentDateTime, String reason) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        Patient patient = patientService.getPatientById(patientId);
        Appointment draft = new Appointment(0, doctor, patient, appointmentDateTime,
                AppointmentStatus.PENDING, reason);
        createAppointment(draft);
        // return the persisted copy (with the real generated ID)
        return getAppointmentsByDoctor(doctorId).stream()
                .filter(a -> a.getAppointmentDateTime().equals(appointmentDateTime))
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment was not persisted"));
    }

    public Appointment getAppointmentById(long appointmentId) {
        Appointment appointment = appointmentStore.findById(appointmentId);
        if (appointment == null) {
            throw new AppointmentNotFoundException(appointmentId);
        }
        return appointment;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.findAll();
    }

    /** Workflow step: PENDING -> CONFIRMED. */
    public void confirmAppointment(long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidDataException("Cannot confirm a cancelled appointment");
        }
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentStore.update(appointmentId, appointment);
    }

    /** Workflow step: PENDING/CONFIRMED -> CANCELLED. */
    public void cancelAppointment(long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentStore.update(appointmentId, appointment);
    }

    public List<Appointment> getAppointmentsByDoctor(long doctorId) {
        return appointmentStore.findAll().stream()
                .filter(a -> a.getDoctor().getId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByPatient(long patientId) {
        return appointmentStore.findAll().stream()
                .filter(a -> a.getPatient().getId() == patientId)
                .collect(Collectors.toList());
    }

    private boolean hasConflict(long doctorId, LocalDateTime dateTime) {
        return getAppointmentsByDoctor(doctorId).stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .anyMatch(a -> a.getAppointmentDateTime().equals(dateTime));
    }
}
