package com.airtribe.meditrack.interfaces;

import com.airtribe.meditrack.entity.Appointment;

public interface NotificationListener {

    void onAppointmentCreated(Appointment appointment);

    void onAppointmentConfirmed(Appointment appointment);

    void onAppointmentCancelled(Appointment appointment);
}