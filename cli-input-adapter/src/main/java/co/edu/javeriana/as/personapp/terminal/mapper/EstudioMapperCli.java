package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

    public EstudioModelCli fromDomainToAdapterCli(co.edu.javeriana.as.personapp.domain.Study study)
    {
        EstudioModelCli estudioModelCli = new EstudioModelCli();
        estudioModelCli.setFechaGraduacion(study.getGraduationDate());
        estudioModelCli.setNombreUniversidad(study.getUniversityName());
        estudioModelCli.setIdPerson(String.valueOf(study.getPerson().getIdentification()));
        estudioModelCli.setIdProfession(String.valueOf(study.getProfession().getIdentification()));
        return estudioModelCli;
    }

        public Study fromAdapterCliToDomain(EstudioModelCli estudioModelCli, Profession profession, Person person)
    {
        Study study = new Study();
        study.setGraduationDate(estudioModelCli.getFechaGraduacion());
        study.setUniversityName(estudioModelCli.getNombreUniversidad());
        study.setProfession(profession);
        study.setPerson(person);
        return study;
    }
}
