package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;

@Mapper
public class ProfesionMapperCli {

    public ProfesionModelCli fromDomainToAdapterCli(Profession profession)
    {
        ProfesionModelCli profesionModelCli = new ProfesionModelCli();
        profesionModelCli.setProfessionId(profession.getIdentification());
        profesionModelCli.setProfessionName(profession.getName());
        profesionModelCli.setProfessionDescription(profession.getDescription());
        return profesionModelCli;
    }

    public Profession fromAdapterCliToDomain(ProfesionModelCli profesionModelCli)
    {
        Profession profession = new Profession();
        profession.setIdentification(profesionModelCli.getProfessionId());
        profession.setName(profesionModelCli.getProfessionName());
        profession.setDescription(profesionModelCli.getProfessionDescription());
        return profession;
    }
}