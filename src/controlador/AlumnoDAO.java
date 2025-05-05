package controlador;

import conexionBD.ConexionBD;
import modelo.Alumno; //clase que representa un alumno
import java.sql.ResultSet; //manejar resultados de consulta
import java.sql.SQLException; //excepciones SQL
import java.util.ArrayList;
import java.util.List; //Importar la List

//DAO - Data Access Object
public class AlumnoDAO {

    ConexionBD conexionBD = new ConexionBD();

    // Altas
    public boolean agregarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos (Num_Control, Nombre, Primer_Ap, Segundo_Ap, Edad, Semestre, Carrera) VALUES ('"
                + alumno.getNumControl() + "', '"
                + alumno.getNombre() + "', '"
                + alumno.getPrimerAp() + "', '"
                + alumno.getSegundoAp() + "', "
                + alumno.getEdad() + ", "
                + alumno.getSemestre() + ", '"
                + alumno.getCarrera() + "')";
        return conexionBD.ejecutarInstruccionesLMD(sql);
    }//agregarAlumno

    //===========================================================================================================================================================================================

    // Bajas
    public boolean eliminarAlumno(String numControl) { //recibe el numero de control como id
        String sql = "DELETE FROM alumnos WHERE Num_Control = '" + numControl + "'";
        try {
            return conexionBD.ejecutarInstruccionesLMD(sql); // Ya devuelve true/false, lo usamos directamente
        } catch (Exception e) {
            System.out.println("Error al eliminar alumno: " + e.getMessage());
            return false;
        }//try-catch
    }//eliminarAlumno

    //===========================================================================================================================================================================================

    // Cambios
    public boolean editarAlumno(Alumno alumno) {
        String sql = "UPDATE alumnos SET Nombre='" + alumno.getNombre() +"', Primer_Ap='" + alumno.getPrimerAp() +"', Segundo_Ap='" + alumno.getSegundoAp() +"', Edad=" + alumno.getEdad() + ", Semestre=" + alumno.getSemestre() + ", Carrera='" + alumno.getCarrera() + "' WHERE Num_Control='" + alumno.getNumControl() + "'";
        return conexionBD.ejecutarInstruccionesLMD(sql);
    }//editarAlumno

    //===========================================================================================================================================================================================

    // Consultas
    // Mostrar solo un alumno en específico
    public Alumno mostrarAlumno(String filtro) {
        String sql = "SELECT * FROM alumnos WHERE Num_Control ='" + filtro + "'";
        ResultSet rs = conexionBD.ejecutarInstruccionSQL(sql);
        try {
            if (rs != null && rs.next()) {
                String nc = rs.getString("Num_Control");
                String n = rs.getString("Nombre");
                String pa = rs.getString("Primer_Ap");
                String sa = rs.getString("Segundo_Ap");
                byte e = rs.getByte("Edad");
                byte s = rs.getByte("Semestre");
                String c = rs.getString("Carrera");
                return new Alumno(nc, n, pa, sa, e, s, c);
            }//if
        } catch (SQLException e) {
            e.printStackTrace();
        }//try-catch
        return null; // Retorna null si no encuentra el alumno

    }//mostrarAlumno

    //===========================================================================================================================================================================================

    // Mostrar todos los alumnos de la tabla
    public ArrayList<Alumno> mostrarAlumnos(String filtro) {
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";
        ResultSet rs = conexionBD.ejecutarInstruccionSQL(sql);

        //rs.next() es avanzar al siguiente registro

        try {
            if (rs != null) {
                while (rs.next()) {
                    String nc = rs.getString("Num_Control");
                    String n = rs.getString("Nombre");
                    String pa = rs.getString("Primer_Ap");
                    String sa = rs.getString("Segundo_Ap");
                    byte e = rs.getByte("Edad");
                    byte s = rs.getByte("Semestre");
                    String c = rs.getString("Carrera");
                    Alumno alumno = new Alumno(nc, n, pa, sa, e, s, c);
                    listaAlumnos.add(alumno);
                }//while
            }//if
        } catch (SQLException e) {
            e.printStackTrace();
        }//try catch
        return listaAlumnos;
    }//mostrarAlumno

//===========================================================================================================================================================================================

//Buscar Alumnos
public List<Alumno> buscarAlumnos(String columna, String valor) {
    List<Alumno> listaAlumnos = new ArrayList<>();
    String sql = "SELECT * FROM alumnos WHERE " + columna + " = '" + valor + "'";
    ResultSet rs = conexionBD.ejecutarInstruccionSQL(sql);

    try {
        if (rs != null) {
            while (rs.next()) {
                String nc = rs.getString("Num_Control");
                String n = rs.getString("Nombre");
                String pa = rs.getString("Primer_Ap");
                String sa = rs.getString("Segundo_Ap");
                byte e = rs.getByte("Edad");
                byte s = rs.getByte("Semestre");
                String c = rs.getString("Carrera");
                Alumno alumno = new Alumno(nc, n, pa, sa, e, s, c);
                listaAlumnos.add(alumno);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return listaAlumnos;
}
//===========================================================================================================================================================================================

}//public clas
