package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Adapter("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMongo telefonoRepositoryMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    @Override
    public Phone save(Phone phone) {
        log.debug("Into save on Adapter MongoDB");
        try {
            TelefonoDocument persistedTelefono = telefonoRepositoryMongo.save(telefonoMapperMongo.fromDomainToAdapter(phone));
            return telefonoMapperMongo.fromAdapterToDomain(persistedTelefono);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return phone;
        }
    }

    @Override
    public Boolean delete(Integer number) {
        log.debug("Into delete on Adapter MongoDB");
        telefonoRepositoryMongo.deleteById(String.valueOf(number));
        return telefonoRepositoryMongo.findById(String.valueOf(number)).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.debug("Into find on Adapter MongoDB");
        List<TelefonoDocument> telefonoDocuments = telefonoRepositoryMongo.findAll();
        List<Phone> phones = new ArrayList<>();
        for (TelefonoDocument telefonoDocument : telefonoDocuments) {
            phones.add(telefonoMapperMongo.fromAdapterToDomain(telefonoDocument));
        }
        return phones;
    }

    @Override
    public Phone findById(Integer number) {
        log.debug("Into findById on Adapter MongoDB");
        TelefonoDocument telefonoDocument = telefonoRepositoryMongo.findById(String.valueOf(number)).orElse(null);
        if (telefonoDocument == null) {
            return null;
        }
        return telefonoMapperMongo.fromAdapterToDomain(telefonoDocument);
    }
}