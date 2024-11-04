package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("professionOutputAdapterMaria") // Asegúrate de que esta anotación está configurada correctamente para el escaneo de componentes
@Transactional // Esto indica que las operaciones en este adaptador deben manejarse dentro de una transacción
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMaria profesionRepositoryMaria; // Asegúrate de que este repositorio esté definido como un bean

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria; // Asegúrate de que este mapper también esté definido como un bean

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on Adapter MariaDB");
        ProfesionEntity persistedProfesion = profesionRepositoryMaria.save(profesionMapperMaria.fromDomainToAdapter(profession));
        return profesionMapperMaria.fromAdapterToDomain(persistedProfesion);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MariaDB");
        profesionRepositoryMaria.deleteById(identification);
        return profesionRepositoryMaria.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on Adapter MariaDB");
        return profesionRepositoryMaria.findAll().stream()
                .map(profesionMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        log.debug("Into findById on Adapter MariaDB");
        return profesionRepositoryMaria.findById(identification)
                .map(profesionMapperMaria::fromAdapterToDomain) // Utiliza map para evitar llamadas adicionales
                .orElse(null); // Retorna null si no se encuentra
    }
}
