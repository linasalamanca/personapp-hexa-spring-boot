package co.edu.javeriana.as.personapp.terminal.menu;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class EstudioMenu {

    private static String DATABASE = "MARIA";
    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR = 2;
    private static final int OPCION_ACTUALIZAR = 3;
    private static final int OPCION_BUSCAR = 4;
    private static final int OPCION_ELIMINAR = 5;

    public void iniciarMenu(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        EstudioMenu.DATABASE = "MARIA";
                        estudioInputAdapterCli.setStudyOutputPortInjection("MARIA");
                        menuOpciones(estudioInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        EstudioMenu.DATABASE = "MONGO";
                        estudioInputAdapterCli.setStudyOutputPortInjection("MONGO");
                        menuOpciones(estudioInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(EstudioInputAdapterCli estudioInputAdapterCli
            , Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        break;
                    case OPCION_VER_TODO:
                        estudioInputAdapterCli.historial();
                        break;
                    case OPCION_CREAR:
                        estudioInputAdapterCli.crearEstudio(leerEntidad(keyboard), EstudioMenu.DATABASE);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
        System.out.println(OPCION_CREAR + " para crear un estudio");
        System.out.println(OPCION_ACTUALIZAR + " para actualizar un estudio");
        System.out.println(OPCION_BUSCAR + " para buscar un estudio");
        System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return leerOpcion(keyboard);
        }
    }

    public EstudioModelCli leerEntidad(Scanner keyboard) {
        try {
            EstudioModelCli estudioModelCli = new EstudioModelCli();
            keyboard.nextLine();
            System.out.println("Ingrese la identificación de la persona:");
            estudioModelCli.setIdPerson(keyboard.nextLine());
            System.out.println("Ingrese la identificación de la profesión:");
            estudioModelCli.setIdProfession(keyboard.nextLine());
            System.out.println("Ingrese el nombre de la universidad:");
            estudioModelCli.setNombreUniversidad(keyboard.nextLine());
            estudioModelCli.setFechaGraduacion(leerFecha(keyboard));
            return estudioModelCli;
        } catch (Exception e) {
            System.out.println("Datos incorrectos, ingrese los datos nuevamente.");
            return leerEntidad(keyboard);
        }
    }

    public LocalDate leerFecha(Scanner keyboard) {
        try {
            System.out.println("Ingrese la fecha de graduación (dd/mm/yyyy):");
            String fecha = keyboard.nextLine();
            String[] fechaArray = fecha.split("/");
            return LocalDate.of(Integer.parseInt(fechaArray[2]), Integer.parseInt(fechaArray[1]), Integer.parseInt(fechaArray[0]));
        } catch (Exception e) {
            System.out.println("Fecha no válida, ingrese nuevamente");
            return leerFecha(keyboard);
        }
    }


}
