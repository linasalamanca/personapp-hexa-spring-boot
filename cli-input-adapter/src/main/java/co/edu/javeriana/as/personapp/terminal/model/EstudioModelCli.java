package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioModelCli {
    private LocalDate fechaGraduacion; // Fecha de graduación
    private String nombreUniversidad; // Nombre de la universidad
    private String idPerson;
    private String idProfession;
}