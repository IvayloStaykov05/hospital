package models;

import java.time.LocalDateTime;

public class Appointment extends BaseClass{
    private Patient patient;
    private Doctor doctor;
    private ExaminationType examinationType;
    private LocalDateTime dateTime;
    private Status status;

    public Appointment(int id, Patient patient, Doctor doctor, ExaminationType examinationType, LocalDateTime dateTime, Status status) {
        super(id);
        this.patient = patient;
        this.doctor = doctor;
        this.examinationType = examinationType;
        this.dateTime = dateTime;
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public ExaminationType getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(ExaminationType examinationType) {
        this.examinationType = examinationType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
