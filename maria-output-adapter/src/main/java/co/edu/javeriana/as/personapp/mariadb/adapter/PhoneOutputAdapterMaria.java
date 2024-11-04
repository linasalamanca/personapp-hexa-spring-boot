package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    private final TelefonoRepositoryMaria telefonoRepositoryMaria;
    private final TelefonoMapperMaria telefonoMapperMaria;

    @Autowired
    public PhoneOutputAdapterMaria(TelefonoRepositoryMaria telefonoRepositoryMaria, TelefonoMapperMaria telefonoMapperMaria) {
        this.telefonoRepositoryMaria = telefonoRepositoryMaria;
        this.telefonoMapperMaria = telefonoMapperMaria;
    }

    @Override
    public Phone save(Phone phone) {
        log.debug("Saving phone in MariaDB");
        TelefonoEntity persistedPhone = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(phone));
        return telefonoMapperMaria.fromAdapterToDomain(persistedPhone);
    }

    @Override
    public Boolean delete(Integer number) {
        log.debug("Deleting phone with number: {}", number);
        telefonoRepositoryMaria.deleteById(String.valueOf(number));
        return telefonoRepositoryMaria.findById(String.valueOf(number)).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.debug("Fetching all phones from MariaDB");
        return telefonoRepositoryMaria.findAll().stream()
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(Integer number) {
        log.debug("Finding phone by number: {}", number);
        return telefonoRepositoryMaria.findById(String.valueOf(number))
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .orElse(null); // o lanzar una excepción según sea necesario
    }
}
