package models;

public class Doctor extends BasePerson {
    private Specialty specialty;
    private String password;

    public Doctor(int id, String firstName, String lastName, String email, String phoneNumber, Specialty specialty, String password) {
        super(id, firstName, lastName, email, phoneNumber);
        this.specialty = specialty;
        this.password = password;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
