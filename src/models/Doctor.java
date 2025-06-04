package models;

public class Doctor extends BasePerson {
    private Specialty specialty;


    public Doctor(int id, String firstName, String lastName, String email, String phoneNumber, Specialty specialty, String password) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.specialty = specialty;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

}
