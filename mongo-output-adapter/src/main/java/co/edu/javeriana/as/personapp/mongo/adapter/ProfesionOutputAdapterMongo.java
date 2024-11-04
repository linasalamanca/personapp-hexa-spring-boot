package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfesionOutputAdapterMongo implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMongo profesionRepositoryMongo;

    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;

    @Override
    public Profession save(Profession profession) {
        log.debug("In save on Adapter MongoDB");
        try {
            ProfesionDocument persistedProfesion = saveProfesion(profession);
            return mapProfesionDocumentToDomain(persistedProfesion);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return profession;
        }
    }

    private ProfesionDocument saveProfesion(Profession profession) {
        return profesionRepositoryMongo.save(profesionMapperMongo.fromDomainToAdapter(profession));
    }

    private Profession mapProfesionDocumentToDomain(ProfesionDocument profesionDocument) {
        return profesionMapperMongo.fromAdapterToDomain(profesionDocument);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("In delete on Adapter MongoDB");
        profesionRepositoryMongo.deleteById(identification);
        return isProfesionDeleted(identification);
    }

    private Boolean isProfesionDeleted(Integer identification) {
        return profesionRepositoryMongo.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("In find on Adapter MongoDB");
        List<Profession> professions = new ArrayList<>();
        List<ProfesionDocument> professionDocuments = findAllProfesions();
        for (ProfesionDocument profesionDocument : professionDocuments) {
            professions.add(mapProfesionDocumentToDomain(profesionDocument));
        }
        return professions;
    }

    private List<ProfesionDocument> findAllProfesions() {
        return profesionRepositoryMongo.findAll();
    }


    @Override
    public Profession findById(Integer identification) {
        log.debug("In findById on Adapter MongoDB");
        Optional<ProfesionDocument> profesionDocument = profesionRepositoryMongo.findById(identification);
        return profesionDocument.map(this::mapProfesionDocumentToDomain).orElse(null);
    }
}
