package com.airtribe.meditrack.test;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.exception.DoctorNotFoundException;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.util.DataStore;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        DoctorTest.run();
        PatientTest.run();
    }
}