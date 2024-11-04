package co.edu.javeriana.as.personapp.adapter;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

    private final ProfessionOutputPort professionOutputPortMaria;
    private final ProfessionOutputPort professionOutputPortMongo;
    private final ProfesionMapperRest profesionMapperRest;

    @Autowired
    public ProfesionInputAdapterRest(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionOutputPortMaria,
                                     @Qualifier("professionOutputAdapterMongo") ProfessionOutputPort professionOutputPortMongo,
                                     ProfesionMapperRest profesionMapperRest) {
        this.professionOutputPortMaria = professionOutputPortMaria;
        this.professionOutputPortMongo = professionOutputPortMongo;
        this.profesionMapperRest = profesionMapperRest;
    }

    public List<ProfesionResponse> getProfesiones(String database) {
        log.info("Into getProfesiones ProfesionEntity in Input Adapter");
        List<ProfesionResponse> responseList = new ArrayList<>();
        try {
            ProfessionInputPort professionInputPort = initializeProfessionInputPort(database);
            List<Profession> professionList = professionInputPort.findAll();
            if (DatabaseOption.MARIA.toString().equalsIgnoreCase(database)) {
                responseList = professionList.stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(database)) {
                responseList = professionList.stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
        }
        return responseList;
    }

    public ProfesionResponse createProfesion(ProfesionRequest request) {
        try {
            ProfessionInputPort professionInputPort = initializeProfessionInputPort(request.getDatabase());
            Profession profession = professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));
            return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProfesionResponse editProfesion(ProfesionRequest request) {
        try {
            ProfessionInputPort professionInputPort = initializeProfessionInputPort(request.getDatabase());
            Profession profession = professionInputPort.edit(Integer.parseInt(request.getIdentification()), profesionMapperRest.fromAdapterToDomain(request));
            return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProfesionResponse deleteProfesion(ProfesionRequest request) {
        try {
            ProfessionInputPort professionInputPort = initializeProfessionInputPort(request.getDatabase());
            boolean result = professionInputPort.drop(Integer.parseInt(request.getIdentification()));
            return new ProfesionResponse("DELETED", "DELETED", "DELETED", String.valueOf(result), "DELETED");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProfesionResponse getProfesionById(ProfesionRequest request) {
        try {
            ProfessionInputPort professionInputPort = initializeProfessionInputPort(request.getDatabase());
            Profession profession = professionInputPort.findOne(Integer.parseInt(request.getIdentification()));
            return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private ProfessionInputPort initializeProfessionInputPort(String database) throws InvalidOptionException {
        ProfessionOutputPort professionOutputPort = null;
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(database)) {
            professionOutputPort = professionOutputPortMaria;
        } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(database)) {
            professionOutputPort = professionOutputPortMongo;
        } else {
            throw new InvalidOptionException("Invalid database option: " + database);
        }
        return new ProfessionUseCase(professionOutputPort);
    }
}
