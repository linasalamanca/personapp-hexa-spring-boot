package co.edu.javeriana.as.personapp.model.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioRequest {
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate graduationDate;
    private String universityName;
    private String database;
}