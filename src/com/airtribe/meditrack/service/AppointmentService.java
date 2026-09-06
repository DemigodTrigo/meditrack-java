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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {

    private final DataStore<Appointment> appointmentStore;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentService(
            DataStore<Appointment> appointmentStore,
            DoctorService doctorService,
            PatientService patientService) {

        Validator.requireNotNull(appointmentStore, "Appointment store");
        Validator.requireNotNull(doctorService, "Doctor service");
        Validator.requireNotNull(patientService, "Patient service");

        this.appointmentStore = appointmentStore;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // ---------------------------------------------------------
    // CREATE APPOINTMENT
    // ---------------------------------------------------------

    public Appointment createAppointment(Appointment appointment) {

        Validator.requireNotNull(appointment, "Appointment");

        Validator.requireNotNull(
                appointment.getDoctor(),
                "Doctor"
        );

        Validator.requireNotNull(
                appointment.getPatient(),
                "Patient"
        );

        Validator.requireNotNull(
                appointment.getAppointmentDateTime(),
                "Appointment date and time"
        );

        Doctor doctor = doctorService.getDoctorById(
                appointment.getDoctor().getId()
        );

        Patient patient = patientService.getPatientById(
                appointment.getPatient().getId()
        );

        LocalDateTime appointmentTime =
                appointment.getAppointmentDateTime();

        if (appointmentTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException(
                    "Appointment date and time cannot be in the past."
            );
        }

        if (!doctor.isAvailable()) {
            throw new InvalidDataException(
                    "Doctor " + doctor.getName() +
                            " is currently unavailable."
            );
        }

        // Doctor cannot have two appointments at the same time.
        if (hasDoctorConflict(
                doctor.getId(),
                appointmentTime,
                0)) {

            throw new InvalidDataException(
                    "Doctor already has an appointment at " +
                            appointmentTime
            );
        }

        // Patient cannot have two appointments at the same time.
        if (hasPatientConflict(
                patient.getId(),
                appointmentTime,
                0)) {

            throw new InvalidDataException(
                    "Patient already has an appointment at " +
                            appointmentTime
            );
        }

        long appointmentId = appointment.getAppointmentId();

        if (appointmentId <= 0) {
            appointmentId = IdGenerator.nextAppointmentId();
        }

        if (appointmentStore.exists(appointmentId)) {
            throw new InvalidDataException(
                    "Appointment with ID " +
                            appointmentId +
                            " already exists."
            );
        }

        Appointment savedAppointment = new Appointment(
                appointmentId,
                doctor,
                patient,
                appointmentTime,
                appointment.getStatus() == null
                        ? AppointmentStatus.PENDING
                        : appointment.getStatus(),
                appointment.getReason()
        );

        appointmentStore.save(
                appointmentId,
                savedAppointment
        );

        return savedAppointment;
    }

    // ---------------------------------------------------------
    // CREATE USING DOCTOR ID + PATIENT ID
    // ---------------------------------------------------------

    public Appointment createAppointment(
            long doctorId,
            long patientId,
            LocalDateTime appointmentDateTime,
            String reason) {

        Doctor doctor = doctorService.getDoctorById(doctorId);
        Patient patient = patientService.getPatientById(patientId);

        Appointment appointment = new Appointment(
                0,
                doctor,
                patient,
                appointmentDateTime,
                AppointmentStatus.PENDING,
                reason
        );

        return createAppointment(appointment);
    }

    // ---------------------------------------------------------
    // GET APPOINTMENT
    // ---------------------------------------------------------

    public Appointment getAppointmentById(long appointmentId) {

        Validator.requirePositive(
                appointmentId,
                "Appointment ID"
        );

        Appointment appointment =
                appointmentStore.findById(appointmentId);

        if (appointment == null) {
            throw new AppointmentNotFoundException(
                    "Appointment with ID " +
                            appointmentId +
                            " not found."
            );
        }

        return appointment;
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    public List<Appointment> getAllAppointments() {

        return appointmentStore.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Appointment::getAppointmentDateTime
                        )
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // CONFIRM
    // ---------------------------------------------------------

    public Appointment confirmAppointment(long appointmentId) {

        Appointment appointment =
                getAppointmentById(appointmentId);

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new InvalidDataException(
                    "Cancelled appointment cannot be confirmed."
            );
        }

        if (appointment.getAppointmentDateTime()
                .isBefore(LocalDateTime.now())) {

            throw new InvalidDataException(
                    "Past appointment cannot be confirmed."
            );
        }

        appointment.setStatus(
                AppointmentStatus.CONFIRMED
        );

        appointmentStore.update(
                appointmentId,
                appointment
        );

        return appointment;
    }

    // ---------------------------------------------------------
    // CANCEL
    // ---------------------------------------------------------

    public Appointment cancelAppointment(long appointmentId) {

        Appointment appointment =
                getAppointmentById(appointmentId);

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new InvalidDataException(
                    "Appointment is already cancelled."
            );
        }

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        appointmentStore.update(
                appointmentId,
                appointment
        );

        return appointment;
    }

    // ---------------------------------------------------------
    // UPDATE APPOINTMENT
    // ---------------------------------------------------------

    public Appointment updateAppointment(
            long appointmentId,
            LocalDateTime newDateTime,
            String newReason) {

        Appointment appointment =
                getAppointmentById(appointmentId);

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new InvalidDataException(
                    "Cancelled appointment cannot be updated."
            );
        }

        Validator.requireNotNull(
                newDateTime,
                "Appointment date and time"
        );

        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException(
                    "Appointment date and time cannot be in the past."
            );
        }

        long doctorId =
                appointment.getDoctor().getId();

        long patientId =
                appointment.getPatient().getId();

        if (hasDoctorConflict(
                doctorId,
                newDateTime,
                appointmentId)) {

            throw new InvalidDataException(
                    "Doctor already has another appointment at " +
                            newDateTime
            );
        }

        if (hasPatientConflict(
                patientId,
                newDateTime,
                appointmentId)) {

            throw new InvalidDataException(
                    "Patient already has another appointment at " +
                            newDateTime
            );
        }

        appointment.setAppointmentDateTime(
                newDateTime
        );

        appointment.setReason(
                newReason
        );

        appointmentStore.update(
                appointmentId,
                appointment
        );

        return appointment;
    }

    // ---------------------------------------------------------
    // SEARCH BY DOCTOR
    // ---------------------------------------------------------

    public List<Appointment> getAppointmentsByDoctor(
            long doctorId) {

        doctorService.getDoctorById(doctorId);

        return appointmentStore.findAll()
                .stream()
                .filter(appointment ->
                        appointment.getDoctor()
                                .getId() == doctorId)
                .sorted(
                        Comparator.comparing(
                                Appointment::getAppointmentDateTime
                        )
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // SEARCH BY PATIENT
    // ---------------------------------------------------------

    public List<Appointment> getAppointmentsByPatient(
            long patientId) {

        patientService.getPatientById(patientId);

        return appointmentStore.findAll()
                .stream()
                .filter(appointment ->
                        appointment.getPatient()
                                .getId() == patientId)
                .sorted(
                        Comparator.comparing(
                                Appointment::getAppointmentDateTime
                        )
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // SEARCH BY STATUS
    // ---------------------------------------------------------

    public List<Appointment> getAppointmentsByStatus(
            AppointmentStatus status) {

        Validator.requireNotNull(
                status,
                "Appointment status"
        );

        return appointmentStore.findAll()
                .stream()
                .filter(appointment ->
                        appointment.getStatus() == status)
                .sorted(
                        Comparator.comparing(
                                Appointment::getAppointmentDateTime
                        )
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // UPCOMING APPOINTMENTS
    // ---------------------------------------------------------

    public List<Appointment> getUpcomingAppointments() {

        LocalDateTime now = LocalDateTime.now();

        return appointmentStore.findAll()
                .stream()
                .filter(appointment ->
                        appointment.getAppointmentDateTime()
                                .isAfter(now))
                .filter(appointment ->
                        appointment.getStatus()
                                != AppointmentStatus.CANCELLED)
                .sorted(
                        Comparator.comparing(
                                Appointment::getAppointmentDateTime
                        )
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    public void deleteAppointment(long appointmentId) {

        getAppointmentById(appointmentId);

        appointmentStore.delete(appointmentId);
    }

    // ---------------------------------------------------------
    // COUNT
    // ---------------------------------------------------------

    public int getAppointmentCount() {

        return appointmentStore.size();
    }

    // ---------------------------------------------------------
    // DOCTOR CONFLICT
    // ---------------------------------------------------------

    private boolean hasDoctorConflict(
            long doctorId,
            LocalDateTime appointmentDateTime,
            long ignoredAppointmentId) {

        return appointmentStore.findAll()
                .stream()
                .anyMatch(appointment ->

                        appointment.getAppointmentId()
                                != ignoredAppointmentId

                                && appointment.getDoctor()
                                .getId() == doctorId

                                && appointment.getAppointmentDateTime()
                                .equals(appointmentDateTime)

                                && appointment.getStatus()
                                != AppointmentStatus.CANCELLED
                );
    }

    // ---------------------------------------------------------
    // PATIENT CONFLICT
    // ---------------------------------------------------------

    private boolean hasPatientConflict(
            long patientId,
            LocalDateTime appointmentDateTime,
            long ignoredAppointmentId) {

        return appointmentStore.findAll()
                .stream()
                .anyMatch(appointment ->

                        appointment.getAppointmentId()
                                != ignoredAppointmentId

                                && appointment.getPatient()
                                .getId() == patientId

                                && appointment.getAppointmentDateTime()
                                .equals(appointmentDateTime)

                                && appointment.getStatus()
                                != AppointmentStatus.CANCELLED
                );
    }
}