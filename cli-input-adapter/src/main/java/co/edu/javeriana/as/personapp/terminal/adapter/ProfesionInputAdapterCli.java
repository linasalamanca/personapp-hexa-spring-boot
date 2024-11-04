package co.edu.javeriana.as.personapp.terminal.adapter;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperCli profesionMapperCli;

    ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {

            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);

        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {

            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);

        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial(){
        log.info("Into historial ProfessionEntity in Input Adapter");
        professionInputPort.findAll().stream()
            .map(profesionMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }


    
public void crearProfesion(ProfesionModelCli profesionModelCli, String dbOption) {
    log.info("Creando profesión");
    try {
        setProfessionOutputPortInjection(dbOption);
        professionInputPort.create(profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
        System.out.println("Profesión creada exitosamente: " + profesionModelCli.toString());
    } catch (Exception e) {
        log.warn(e.getMessage());
        System.out.println("Error al crear profesión");
    }
}

public void editarProfesion(ProfesionModelCli profesionModelCli, String dbOption) {
    log.info("Editando profesión");
    try {
        setProfessionOutputPortInjection(dbOption);
        Profession profession = professionInputPort.edit(profesionModelCli.getProfessionId(), profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
        System.out.println("Profesión editada exitosamente: " + profesionModelCli.toString());
    } catch (Exception e) {
        log.warn(e.getMessage());
        System.out.println("Error al editar profesión: " + profesionModelCli.toString());
    }
}

public void eliminarProfesion(String dbOption, int cc) {
    log.info("Eliminando profesión");
    try {
        setProfessionOutputPortInjection(dbOption);
        boolean resultado = professionInputPort.drop(cc);
        if (resultado)
            System.out.println("Profesión eliminada exitosamente: " + cc);
    } catch (Exception e) {
        log.warn(e.getMessage());
        System.out.println("Error al eliminar profesión");
    }
}

public void buscarProfesion(String dbOption, int cc) {
    log.info("Buscando profesión");
    try {
        setProfessionOutputPortInjection(dbOption);
        Profession profession = professionInputPort.findOne(cc);
        ProfesionModelCli profesionModelCli = profesionMapperCli.fromDomainToAdapterCli(profession);
        System.out.println("Profesión encontrada: " + profesionModelCli.toString());
    } catch (Exception e) {
        log.warn(e.getMessage());
        System.out.println("Error al buscar profesión");
    }
}
}