import repository.ExaminationTypeRepository;
import repository.SpecialtyRepository;
import repository.StatusRepository;


public class Main {
    public static void main(String[] args) {
        ExaminationTypeRepository examinationTypeRepository= new ExaminationTypeRepository();
        StatusRepository statusRepository = new StatusRepository();
        SpecialtyRepository specialtyRepository = new SpecialtyRepository();


        examinationTypeRepository.initializeExaminationType();
        statusRepository.initializeStatuses();
        specialtyRepository.initializeSpecialties();

        System.out.println("Приложението стартира успешно!");


    }
}