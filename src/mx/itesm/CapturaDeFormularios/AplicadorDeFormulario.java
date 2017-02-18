package mx.itesm.CapturaDeFormularios;

import java.awt.Component;
import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AplicadorDeFormulario {
    
    Component frame;
    
    static AplicadorDeFormulario instance;
    
    int idFormulario;
    static int lastAvailableId_Respuesta = 0;
    Queue<Integer> preguntasQ;
    
    boolean isColaIniciada;
    
    public static AplicadorDeFormulario Instance() {
        if (instance == null)
            instance = new AplicadorDeFormulario();
        
        return instance;
    }
    
    public static int LastAvailableId_Respuesta() {
        lastAvailableId_Respuesta++;
        return lastAvailableId_Respuesta;
    }
    
    public AplicadorDeFormulario() {
        preguntasQ = new LinkedList<Integer>();
    }
    
    public void AplicarFormulario(int idFormulario) {
        String nombre = "", descripcion = "";
        
        this.idFormulario = idFormulario;
        
        try {
            String query = "SELECT nombre, descripcion FROM Formularios WHERE idFormulario = " + this.idFormulario;
            Connection con = Conexion.getconexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                nombre = rs.getString("nombre");
                descripcion = rs.getString("descripcion");
            }
            
            st.close();
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ResponderFormulario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Formulario.Instance().GetComponents(nombre, descripcion);
        Formulario.Instance().setVisible(true);
    }
    
    public void AplicarReactivos() {
        try {
            String query = "SELECT idPregunta FROM Preguntas, Formularios WHERE fkFormulario = " + this.idFormulario + ";";
            Connection con = Conexion.getconexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                preguntasQ.add(Integer.parseInt(rs.getString("idPregunta")));
            }
            
            st.close();
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        SeguirCola();
    }
    
    public void SeguirCola() {
        String tipoPregunta = "", pregunta = "nombre_de_pregunta";
        String opcion1 = "opcion1", opcion2 = "opcion2", opcion3 = "opcion3", opcion4 = "opcion4";
        
        String query;
        Connection con;
        Statement st;
        ResultSet rs;

        if (!preguntasQ.isEmpty()) {
            int idPregunta = preguntasQ.poll();
        
            try {
                query = "SELECT tipoPregunta FROM Preguntas, Formularios WHERE idPregunta = " + idPregunta + " AND fkFormulario = " + this.idFormulario + ";";
            
                con = Conexion.getconexion();
                st = con.createStatement();
                rs = st.executeQuery(query);
            
                while (rs.next()) {
                   tipoPregunta = rs.getString("tipoPregunta");
                }
            
                st.close();
                con.close();
            
            } catch (SQLException ex) {
                Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
            switch (tipoPregunta) {
                case "abierta":
                    
                    try {
                        query = "SELECT pregunta FROM PreguntasAbiertas WHERE fkPregunta = " + idPregunta + ";";
                        con = Conexion.getconexion();
                        st = con.createStatement();
                        rs = st.executeQuery(query);
                    
                        while (rs.next()) {
                            pregunta = rs.getString("pregunta");
                        }   
                    
                       st.close();
                       con.close();
                    
                    } catch (SQLException ex) {
                        Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                    ResponderAbierta.Instance().GetComponents(idPregunta, pregunta);
                    ResponderAbierta.Instance().setVisible(true);
                
                    break;
                
                case "opcion multiple":
                
                    try {
                        query = "SELECT pregunta, opcion1, opcion2, opcion3, opcion4 FROM PreguntasOpcionMultiple WHERE fkPregunta = " + idPregunta + ";";
                        con = Conexion.getconexion();
                        st = con.createStatement();
                        rs = st.executeQuery(query);
                    
                        while (rs.next()) {
                            pregunta = rs.getString("pregunta");
                            opcion1 = rs.getString("opcion1");
                            opcion2 = rs.getString("opcion2");
                            opcion3 = rs.getString("opcion3");
                            opcion4 = rs.getString("opcion4");
                        }   
                    
                        st.close();
                        con.close();
                    
                    } catch (SQLException ex) {
                        Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                   
                    ResponderOpcionMultiple.Instance().GetComponents(idPregunta, pregunta, opcion1, opcion2, opcion3, opcion4);
                    ResponderOpcionMultiple.Instance().setVisible(true);
                    
                    break;
                
                case "seleccion multiple":
            
                    try {
                        query = "SELECT pregunta, opcion1, opcion2, opcion3, opcion4 FROM PreguntasSeleccionMultiple WHERE fkPregunta = " + idPregunta + ";";
                        con = Conexion.getconexion();
                        st = con.createStatement();
                        rs = st.executeQuery(query);
                    
                        while (rs.next()) {
                            pregunta = rs.getString("pregunta");
                            opcion1 = rs.getString("opcion1");
                            opcion2 = rs.getString("opcion2");
                            opcion3 = rs.getString("opcion3");
                            opcion4 = rs.getString("opcion4");
                        }   
                    
                        st.close();
                        con.close();
                    
                    } catch (SQLException ex) {
                        Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                   
                    ResponderSeleccionMultiple.Instance().GetComponents(idPregunta, pregunta, opcion1, opcion2, opcion3, opcion4);
                    ResponderSeleccionMultiple.Instance().setVisible(true);
                    
                    break;
                
                case "si no":
            
                    try {
                        query = "SELECT pregunta FROM PreguntasSiNo WHERE fkPregunta = " + idPregunta + ";";
                        con = Conexion.getconexion();
                        st = con.createStatement();
                        rs = st.executeQuery(query);
                    
                        while (rs.next()) {
                            pregunta = rs.getString("pregunta");
                        }   
                    
                        st.close();
                        con.close();
                    
                    } catch (SQLException ex) {
                        Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                   
                    ResponderSiNo.Instance().GetComponents(idPregunta, pregunta);
                    ResponderSiNo.Instance().setVisible(true);
                    
                    break;
                
                default:
            
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                                          "Gracias por responder este formulario.",
                                          "¡Éxito!",
                                          JOptionPane.PLAIN_MESSAGE);
            
            ResponderFormulario.Instance().setVisible(true);
        }
    }
}