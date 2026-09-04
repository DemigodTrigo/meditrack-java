package com.airtribe.meditrack.entity;

public class Patient extends Person implements Cloneable {

    private String bloodGroup;
    private String medicalHistory;

    /**
     * Creates a Patient with the specified personal and medical details.
     *
     * @param id unique patient ID
     * @param name patient name
     * @param age patient age
     * @param phone phone number
     * @param email email address
     * @param bloodGroup blood group
     * @param medicalHistory medical history
     */
    public Patient(long id,
                   String name,
                   int age,
                   String phone,
                   String email,
                   String bloodGroup,
                   String medicalHistory) {

        super(id, name, age, phone, email);

        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    /**
     * Creates a copy of this Patient.
     *
     * <p>The Patient currently contains only primitive/String fields.
     * String is immutable, so sharing the String references does not
     * create mutable shared state. Therefore the clone is safe with
     * respect to the current object model.</p>
     *
     * @return cloned Patient
     */
    @Override
    public Patient clone() {
        try {
            return (Patient) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Patient cloning is not supported", e);
        }
    }

    @Override
    public String getEntityType() {
        return "Patient";
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", age=" + getAge() +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }
}