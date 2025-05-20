import repository.ExaminationTypeRepository;
import repository.SpecialtyRepository;
import repository.StatusRepository;
import views.MainMenuView;


public class Main {
    public static void main(String[] args) {
        MainMenuView mainMenuView = new MainMenuView();

        ExaminationTypeRepository examinationTypeRepository= new ExaminationTypeRepository();
        StatusRepository statusRepository = new StatusRepository();
        SpecialtyRepository specialtyRepository = new SpecialtyRepository();


        examinationTypeRepository.initializeExaminationType();
        statusRepository.initializeStatuses();
        specialtyRepository.initializeSpecialties();

        System.out.println("Приложението стартира успешно!");

        mainMenuView.start();
    }
}