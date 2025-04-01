package models;

public class Doctor extends BasePerson {
    private Specialty specialty;

    public Doctor(int id, String firstName, String lastName, String email, String phoneNumber, Specialty specialty) {
        super(id, firstName, lastName, email, phoneNumber);
        this.specialty = specialty;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}
