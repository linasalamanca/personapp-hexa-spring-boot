package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionModelCli {
    private Integer professionId; // Identificador de la profesión
    private String professionName; // Nombre de la profesión
    private String professionDescription; // Descripción de la profesión
}
