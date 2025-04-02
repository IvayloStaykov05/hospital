import repository.ExaminationTypeRepository;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ExaminationTypeRepository examinationTypeRepository= new ExaminationTypeRepository();
        examinationTypeRepository.initializeExaminationType();

        System.out.println("🎯 Приложението стартира успешно!");
    }
}