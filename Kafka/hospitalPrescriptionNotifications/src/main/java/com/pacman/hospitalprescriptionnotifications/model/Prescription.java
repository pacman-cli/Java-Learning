package com.pacman.hospitalprescriptionnotifications.model;


public class Prescription {
    private String patient;
    private String medicine;
    private String doctor;
    private String notes;

    // Constructors
    public Prescription() {}

    public Prescription(String patient, String medicine, String doctor, String notes) {
        this.patient = patient;
        this.medicine = medicine;
        this.doctor = doctor;
        this.notes = notes;
    }

    // Getters and Setters
    public String getPatient() { return patient; }
    public void setPatient(String patient) { this.patient = patient; }

    public String getMedicine() { return medicine; }
    public void setMedicine(String medicine) { this.medicine = medicine; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Prescription{" +
                "patient='" + patient + '\'' +
                ", medicine='" + medicine + '\'' +
                ", doctor='" + doctor + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}

