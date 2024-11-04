package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersistence;

    public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public void setPersistence(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Into Phone create on Application Domain");
        return phonePersistence.save(phone);
    }

    @Override
    public Phone edit(Integer number, Phone phone) throws NoExistException {
        return Optional.ofNullable(phonePersistence.findById(number))
                .map(existingPhone -> phonePersistence.save(phone))
                .orElseThrow(() -> new NoExistException(
                        "The phone with number " + number + " does not exist into db, cannot be edited"));
    }

    @Override
    public Boolean drop(Integer number) throws NoExistException {
        return Optional.ofNullable(phonePersistence.findById(number))
                .map(existingPhone -> phonePersistence.delete(number))
                .orElseThrow(() -> new NoExistException(
                        "The phone with number " + number + " does not exist into db, cannot be dropped"));
    }

    @Override
    public List<Phone> findAll() {
        log.info("Output: " + phonePersistence.getClass());
        return phonePersistence.find();
    }

    @Override
    public Phone findOne(Integer number) throws NoExistException {
        return Optional.ofNullable(phonePersistence.findById(number))
                .orElseThrow(() -> new NoExistException(
                        "The phone with number " + number + " does not exist into db, cannot be found"));
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public Person getOwner(Integer number) throws NoExistException {
        return Optional.ofNullable(phonePersistence.findById(number))
                .map(Phone::getOwner)
                .orElseThrow(() -> new NoExistException(
                        "The phone with number " + number + " does not exist into db, cannot be getting its owner"));
    }
}
