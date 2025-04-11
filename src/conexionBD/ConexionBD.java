package conexionBD;

import java.sql.*; //importar toda la libreria

public class ConexionBD {

    private Connection conexion; //objeto para la conexion a la BD
    private Statement stm; //para ejecutar consultas SQL
    private ResultSet rs; //para almacenar resultados de consultas

    public ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //carga el driver de MySQL
            String URL = "jdbc:mysql://localhost:3306/bd_topicos_2025"; //URL de la BD
            conexion = DriverManager.getConnection(URL, "root", "tommoway1991"); //conexion
            System.out.println("Yei, casi soy ingeniero inmortal");

        } catch (ClassNotFoundException e) {
            System.out.println("Error en el conector/Driver");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en la conexion a MySQL");
        }//try-catch

    }//public ConexionBD

    public boolean ejecutarInstruccionesLMD(String sql) {
        boolean resultado = false;
        try {
            stm = conexion.createStatement();
            if (stm.executeUpdate(sql) >= 1) { //ejecuta la instruccion
                resultado = true; //retorna true si afecta al menos 1 fila
            }//if
        } catch (SQLException e) {
            System.out.println("Error en la ejecucion de la instruccion SQL");
        }//try-catch
        return resultado;
    }//ejecutarInstruccionesLMD

    public ResultSet ejecutarInstruccionSQL(String sql) {
        rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql); //ejecuta la consulta
        } catch (SQLException e) {
            System.out.println("Error en la ejecucion de la instruccion SQL");
        }//try-catch
        return rs; //retorna los resultados
    }//ejecutarInstruccionesSQL

    public static void main(String[] args) {
        System.out.println("Magia magia con INTELLIJ");
        new ConexionBD(); //instancia para probar la conexion

    }//void main

}//public class