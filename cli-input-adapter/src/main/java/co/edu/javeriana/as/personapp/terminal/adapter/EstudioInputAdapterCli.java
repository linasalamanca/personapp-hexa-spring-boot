package co.edu.javeriana.as.personapp.terminal.adapter;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

    // MariaDB
    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    // MongoDB
    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private EstudioMapperCli estudioMapperCli;

    // Puertos de entrada a la aplicación
    private StudyInputPort studyInputPort;
    private ProfessionInputPort professionInputPort;
    private PersonInputPort personInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Fetching all studies in Input Adapter");
        studyInputPort.findAll().stream()
            .map(estudioMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }

    public void crearEstudio(EstudioModelCli estudio, String dbOption) {
        log.info("Creating study in Input Adapter");
        try {
            setStudyOutputPortInjection(dbOption);
            // Get person by id
            Person person = personInputPort.findOne(Integer.parseInt(estudio.getIdPerson()));
            // Get profession by id
            Profession profession = professionInputPort.findOne(Integer.parseInt(estudio.getIdProfession()));
            studyInputPort.create(estudioMapperCli.fromAdapterCliToDomain(estudio, profession, person));
            System.out.println("Estudio creado correctamente: " + estudio);
        } catch (Exception e) {
            log.warn("Error creating study: {}", e.getMessage());
            System.out.println("Error al crear el estudio");
        }
    }

    public void editarEstudio(Integer professionID, Integer personID, EstudioModelCli estudio, String dbOption) {
        log.info("Editing study with Profession ID: {} and Person ID: {}", professionID, personID);
        try {
            setStudyOutputPortInjection(dbOption);
            
            // Get existing study to verify it exists
            Study existingStudy = studyInputPort.findOne(professionID, personID); // Asegúrate que esto está bien en StudyInputPort
    
            // Get person by id
            Person person = personInputPort.findOne(Integer.parseInt(estudio.getIdPerson()));
            // Get profession by id
            Profession profession = professionInputPort.findOne(Integer.parseInt(estudio.getIdProfession()));
            
            // Update existing study fields
            existingStudy.setPerson(person);
            existingStudy.setProfession(profession);
            existingStudy.setGraduationDate(estudio.getFechaGraduacion()); // Asegúrate que este método existe
            existingStudy.setUniversityName(estudio.getNombreUniversidad()); // Asegúrate que este método existe
            
            // Save the updated study
            studyInputPort.edit(professionID, personID, existingStudy); 
            System.out.println("Estudio editado correctamente: " + estudio);
        } catch (NoExistException e) {
            log.warn("Error editing study: {}", e.getMessage());
            System.out.println("Estudio no encontrado para editar");
        } catch (Exception e) {
            log.warn("Error editing study: {}", e.getMessage());
            System.out.println("Error al editar el estudio");
        }
    }

    public void eliminarEstudio(Integer professionID, Integer personID, String dbOption) {
        log.info("Deleting study with professionID: {} and personID: {}", professionID, personID);
        try {
            setStudyOutputPortInjection(dbOption);
            studyInputPort.drop(professionID, personID); // Throws NoExistException if not found
            System.out.println("Estudio eliminado correctamente");
        } catch (NoExistException e) {
            log.warn("Error deleting study: {}", e.getMessage());
            System.out.println("Estudio no encontrado para eliminar");
        } catch (Exception e) {
            log.warn("Error deleting study: {}", e.getMessage());
            System.out.println("Error al eliminar el estudio");
        }
    }

    public void buscarEstudio(Integer professionID, Integer personID, String dbOption) {
        log.info("Searching for study with Profession ID: {} and Person ID: {}", professionID, personID);
        try {
            setStudyOutputPortInjection(dbOption);
            EstudioModelCli estudio = estudioMapperCli.fromDomainToAdapterCli(studyInputPort.findOne(professionID, personID));
            System.out.println("Estudio encontrado: " + estudio);
        } catch (NoExistException e) {
            log.warn("Error finding study: {}", e.getMessage());
            System.out.println("Estudio no encontrado");
        } catch (Exception e) {
            log.warn("Error finding study: {}", e.getMessage());
            System.out.println("Error al buscar el estudio");
        }
    }
}
