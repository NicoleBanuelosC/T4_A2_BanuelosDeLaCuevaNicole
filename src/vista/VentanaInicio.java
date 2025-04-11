package vista;

import controlador.AlumnoDAO;
import modelo.Alumno;

import java.util.ArrayList;

public class VentanaInicio {

    public static void main (String [] args) {

        //=============================================================================================================================================================================================================

        //Prueba Proceso ALTAS
        Alumno a = new Alumno("3", "3", "3", "3", (byte) 3, (byte) 3, "3");

        AlumnoDAO alumnoDAO = new AlumnoDAO();

        if (alumnoDAO.agregarAlumno(a)) {
            System.out.println("Registro agregado CORRECTAMENTE");
        } else {
            System.out.println("Error en la insección");
        }//if-else

        //==============================================================================================================================================================================================================

        //Prueba procesos bajas

        if(alumnoDAO.eliminarAlumno("2"))
            System.out.println("Registro eliminado Correctamente");
        else
            System.out.println("Error en la eliminación");
        //=============================================================================================================================================================================================================

        //Prueba procesos cambios

        Alumno a1 = new Alumno ("2", "Luke", "Skywalker", "-", (byte) 100, (byte) 10, "ISC");

        if(alumnoDAO.editarAlumno(a1))
            System.out.println("Registro modificado correctamente" );
        else
            System.out.println("Error en la modificacion");

        //proceso Consultas
        ArrayList<Alumno> lista = alumnoDAO.mostrarAlumnos("");

        for (Alumno alumno : lista)
            System.out.println(alumno);

        //=============================================================================================================================================================================================================

    }//void main

}//public class
