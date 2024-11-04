package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

    private final ProfesionInputAdapterRest profesionInputAdapterRest;

    @Autowired
    public ProfesionControllerV1(ProfesionInputAdapterRest profesionInputAdapterRest) {
        this.profesionInputAdapterRest = profesionInputAdapterRest;
    }

    @GetMapping(path = "/{database}", produces = "application/json")
    public List<ProfesionResponse> getProfesiones(@PathVariable String database) {
        log.info("Into getProfesiones REST API");
        return profesionInputAdapterRest.getProfesiones(database.toUpperCase());
    }

    @PostMapping(path = "", produces = "application/json", consumes = "application/json")
    public ProfesionResponse createProfesion(@RequestBody ProfesionResponse request) {
        log.info("Into createProfesion method in API controller");
        return profesionInputAdapterRest.createProfesion(request);
    }

    @PutMapping(path = "", produces = "application/json", consumes = "application/json")
    public ProfesionResponse updateProfesionById(@RequestBody ProfesionResponse request) {
        log.info("Into updateProfesionById REST API");
        return profesionInputAdapterRest.editProfesion(request);
    }

    @GetMapping(path = "", produces = "application/json")
    public ProfesionResponse getProfesionById(@RequestBody ProfesionResponse request) {
        log.info("Into getProfesionById REST API");
        return profesionInputAdapterRest.getProfesionById(request);
    }

    @DeleteMapping(path = "", produces = "application/json")
    public ProfesionResponse deleteProfesionById(@RequestBody ProfesionResponse request) {
        log.info("Into deleteProfesionById REST API");
        return profesionInputAdapterRest.deleteProfesion(request);
    }
}