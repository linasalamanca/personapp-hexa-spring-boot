package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public Study create(Study study) {
        log.debug("Creating study in Application Domain");
        return studyPersistence.save(study);
    }

    @Override
    public Study edit(Integer professionID, Integer personID, Study study) throws NoExistException {
        return Optional.ofNullable(studyPersistence.findById(professionID, personID))
                .map(existingStudy -> studyPersistence.save(study))
                .orElseThrow(() -> new NoExistException(
                        "The study with professionID " + professionID + " and personID " + personID + " does not exist into db, cannot be edited"));
    }

    @Override
    public Boolean drop(Integer professionID, Integer personID) throws NoExistException {
        log.debug("Dropping study for professionID: {} and personID: {}", professionID, personID);
        return Optional.ofNullable(studyPersistence.findById(professionID, personID))
                .map(existingStudy -> {
                    studyPersistence.delete(professionID, personID);
                    return true;
                })
                .orElseThrow(() -> new NoExistException(
                        "Study with professionID " + professionID + " and personID " + personID + " does not exist."));
    }

    @Override
    public List<Study> findAll() {
        log.info("Output: " + studyPersistence.getClass());
        return studyPersistence.find();
    }

    @Override
    public Study findOne(Integer professionID, Integer personID) throws NoExistException {
        log.debug("Finding study for professionID: {} and personID: {}", professionID, personID);
        return Optional.ofNullable(studyPersistence.findById(professionID, personID))
                .orElseThrow(() -> new NoExistException(
                        "Study with professionID " + professionID + " and personID " + personID + " does not exist."));
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public Person getPerson(Integer professionID, Integer personID) throws NoExistException {
        log.debug("Getting person from study for professionID: {} and personID: {}", professionID, personID);
        return Optional.ofNullable(studyPersistence.findById(professionID, personID))
                .map(Study::getPerson)
                .orElseThrow(() -> new NoExistException(
                        "Study with professionID " + professionID + " and personID " + personID + " does not exist."));
    }

    @Override
    public Profession getProfession(Integer professionID, Integer personID) throws NoExistException {
        log.debug("Getting profession from study for professionID: {} and personID: {}", professionID, personID);
        return Optional.ofNullable(studyPersistence.findById(professionID, personID))
                .map(Study::getProfession)
                .orElseThrow(() -> new NoExistException(
                        "Study with professionID " + professionID + " and personID " + personID + " does not exist."));
    }
}
