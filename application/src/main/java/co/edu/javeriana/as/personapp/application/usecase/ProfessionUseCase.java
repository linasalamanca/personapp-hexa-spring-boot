package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public void setPersistence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Creating profession in Application Domain");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer identification, Profession updatedProfession) throws NoExistException {
        log.debug("Editing profession with identification: {}", identification);
        return Optional.ofNullable(professionPersistence.findById(identification))
                .map(existingProfession -> {
                    updatedProfession.setIdentification(identification); // Asegurarse de mantener el ID
                    return professionPersistence.save(updatedProfession);
                })
                .orElseThrow(() -> new NoExistException(
                        "Profession with identification " + identification + " does not exist."));
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        log.debug("Dropping profession with identification: {}", identification);
        return Optional.ofNullable(professionPersistence.findById(identification))
                .map(existingProfession -> {
                    professionPersistence.delete(identification);
                    return true;
                })
                .orElseThrow(() -> new NoExistException(
                        "Profession with identification " + identification + " does not exist."));
    }

    @Override
    public List<Profession> findAll() {
        log.info("Fetching all professions");
        return professionPersistence.find();
    }

    @Override
    public Profession findOne(Integer identification) throws NoExistException {
        log.debug("Finding profession with identification: {}", identification);
        return Optional.ofNullable(professionPersistence.findById(identification))
                .orElseThrow(() -> new NoExistException(
                        "Profession with identification " + identification + " does not exist."));
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public List<Study> getStudies(Integer identification) throws NoExistException {
        log.debug("Getting studies for profession with identification: {}", identification);
        return Optional.ofNullable(professionPersistence.findById(identification))
                .map(Profession::getStudies)
                .orElseThrow(() -> new NoExistException(
                        "Profession with identification " + identification + " does not exist."));
    }
}
