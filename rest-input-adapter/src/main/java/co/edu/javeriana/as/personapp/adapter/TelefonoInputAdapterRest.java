package co.edu.javeriana.as.personapp.adapter;


import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Adapter
public class TelefonoInputAdapterRest {
    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;
    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;
    @Autowired
    private TelefonoMapperRest telefonoMapperRest;
    PhoneInputPort phoneInputPort;
    private String setPhoneOutportInjection(String dbOption) throws InvalidOptionException{
        if(dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())){
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        }else if(dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())){
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        }else{
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }
    public List<TelefonoResponse> historial(String database)
    {
        log.info("Into historial TelefonoEntity in Input Adapter");
        try{
            String db = setPhoneOutportInjection(database);
            if(db.equalsIgnoreCase(DatabaseOption.MARIA.toString())){
                return phoneInputPort.findAll().stream().map(telefonoMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
        }else{
                return phoneInputPort.findAll().stream().map(telefonoMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        }catch(InvalidOptionException e){
            log.warn("Invalid database option: " + database+" "+e.getMessage());
            return new ArrayList<TelefonoResponse>();
        }
    }
    public TelefonoResponse crearTelefono(TelefonoRequest request)
    {
        try {
            setPhoneOutportInjection(request.getDatabase());
            Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request));
            return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + request.getDatabase()+" "+e.getMessage());
            //return new TelefonoResponse();
        }
        return null;
    }

    public TelefonoResponse editarTelefono(TelefonoRequest request)
    {
        try{
            setPhoneOutportInjection(request.getDatabase());
            Phone phone = phoneInputPort.edit(Integer.valueOf(request.getNumber()),telefonoMapperRest.fromAdapterToDomain(request));
            return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
        } catch (Exception e) {
            log.warn("Invalid database option: " + request.getDatabase()+" "+e.getMessage());
            //return new TelefonoResponse();
            return null;
        }
    }

    public TelefonoResponse eliminarTelefono(TelefonoRequest request)
    {
        try{
            setPhoneOutportInjection(request.getDatabase());
            Boolean resultado = phoneInputPort.drop(Integer.valueOf(request.getNumber()));
            String msg = "DELETED";
            return new TelefonoResponse(msg,msg,request.getDatabase(),msg);
        } catch (Exception e) {
            log.warn("Invalid database option: " + request.getDatabase()+" "+e.getMessage());
            //return new TelefonoResponse();
            return null;
        }
    }

    public TelefonoResponse buscarTelefono(TelefonoRequest request)
    {
        try{
            setPhoneOutportInjection(request.getDatabase());
            Phone phone = phoneInputPort.findOne(Integer.valueOf(request.getNumber()));
            return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
        } catch (Exception e) {
            log.warn("Invalid database option: " + request.getDatabase()+" "+e.getMessage());
            //return new TelefonoResponse();
            return null;
        }
    }

}