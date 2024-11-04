package co.edu.javeriana.as.personapp.controller;
import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

    private final EstudioInputAdapterRest estudioInputAdapterRest;

    @Autowired
    public EstudioControllerV1(EstudioInputAdapterRest estudioInputAdapterRest) {
        this.estudioInputAdapterRest = estudioInputAdapterRest;
    }

    // Get ALL ESTUDIOS
    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EstudioResponse> getEstudios(@PathVariable String database) {
        log.info("Fetching estudios from database: " + database);
        return estudioInputAdapterRest.getEstudios(database.toUpperCase());
    }

    // POST NEW ESTUDIO
    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse createEstudio(@RequestBody EstudioResponse request) {
        log.info("Creating new estudio: " + request);
        return estudioInputAdapterRest.createEstudio(request);
    }
}
