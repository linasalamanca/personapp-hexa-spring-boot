package co.edu.javeriana.as.personapp.adapter;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

    private final StudyOutputPort studyOutputPortMaria;
    private final StudyOutputPort studyOutputPortMongo;
    private final EstudioMapperRest estudioMapperRest;

    @Autowired
    public EstudioInputAdapterRest(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyOutputPortMaria,
                                   @Qualifier("studyOutputAdapterMongo") StudyOutputPort studyOutputPortMongo,
                                   EstudioMapperRest estudioMapperRest) {
        this.studyOutputPortMaria = studyOutputPortMaria;
        this.studyOutputPortMongo = studyOutputPortMongo;
        this.estudioMapperRest = estudioMapperRest;
    }

    public List<EstudioResponse> getEstudios(String database) {
        log.info("Fetching estudios from database: " + database);
        try {
            StudyInputPort studyInputPort = initializeStudyInputPort(database);
            List<Study> studyList = studyInputPort.findAll();
            if (DatabaseOption.MARIA.toString().equalsIgnoreCase(database)) {
                return studyList.stream()
                        .map(estudioMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(database)) {
                return studyList.stream()
                        .map(estudioMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + database + " " + e.getMessage());
        }
        return null;
    }

    public EstudioResponse createEstudio(EstudioRequest request) {
        try {
            StudyInputPort studyInputPort = initializeStudyInputPort(request.getDatabase());
            Study study = studyInputPort.create(estudioMapperRest.fromAdapterToDomain(request));
            return estudioMapperRest.fromDomainToAdapterRestMaria(study);
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + request.getDatabase() + " " + e.getMessage());
        }
        return null;
    }

    private StudyInputPort initializeStudyInputPort(String database) throws InvalidOptionException {
        StudyOutputPort studyOutputPort = null;
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(database)) {
            studyOutputPort = studyOutputPortMaria;
        } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(database)) {
            studyOutputPort = studyOutputPortMongo;
        } else {
            throw new InvalidOptionException("Invalid database option: " + database);
        }
        return new StudyUseCase(studyOutputPort);
    }

  
}
