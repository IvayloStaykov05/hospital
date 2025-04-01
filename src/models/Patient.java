package models;

public class Patient extends BasePerson {
    private int age;

    public Patient(int id, String firstName, String lastName, String email, String phoneNumber, int age) {
        super(id, firstName, lastName, email, phoneNumber);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
