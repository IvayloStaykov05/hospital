package models;

public class Patient extends BasePerson {
    private int age;

    public Patient(int id, String firstName, String lastName, String email, String phoneNumber, String password, int age) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
