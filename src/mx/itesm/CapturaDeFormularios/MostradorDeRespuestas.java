/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.itesm.CapturaDeFormularios;

import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Levi Carbellido
 */

// TODO

public class MostradorDeRespuestas {
    Component frame;
    
    DefaultListModel model1, model2;
    
    static MostradorDeRespuestas instance;
    
    int idFormulario;
    Queue<Integer> respuestasQ;
    
    boolean isColaIniciada;
    
    String query;
    Connection con;
    Statement st;
    ResultSet rs;
    
    public static MostradorDeRespuestas Instance() {
        if (instance == null) 
            instance = new MostradorDeRespuestas();
        
        return instance;
    }
    
    public MostradorDeRespuestas() {
        respuestasQ = new LinkedList<Integer>();
    }
    
    public void PonerRespuestasEnCola(int idFormulario) {
        try {
            query = "SELECT idRespuesta FROM Respuestas, Preguntas, Formularios WHERE fkFormulario = " + idFormulario + " AND fkPregunta = idPregunta;";
            con = Conexion.getconexion();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                respuestasQ.add(Integer.parseInt(rs.getString("idRespuesta")));
            }
            
            st.close();
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SeguirCola();
    }
    
    public void SeguirCola() {
        
        String tipoPregunta = "", pregunta = "";
        
        if (!respuestasQ.isEmpty()) {
            int idPregunta = respuestasQ.poll();
        
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
                // Consulta de respuestas abiertas
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
                
                try {
                    query = "SELECT pregunta FROM PreguntasAbiertas AS a, Respuestas AS b WHERE a.fkPregunta = b.fkPregunta AND idRespuesta = " + idPregunta + ";";
                    con = Conexion.getconexion();
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                } catch (SQLException ex) {
                    Logger.getLogger(AplicadorDeFormulario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                VerRespuestasAbiertas.Instance().getPregunta(pregunta);
                VerRespuestasAbiertas.Instance().getListaUsuarios(model1);
                VerRespuestasAbiertas.Instance().getListaRespuestas(model2);
                VerRespuestasAbiertas.Instance().setVisible(true);
                
                break;
                
            case "opcion multiple":
                // Consulta de respuestas opcion multiple
                
                break;
                
            case "seleccion multiple":
                // Consulta de respuestas seleccion multiple
                
                break;
                
            case "si no":
                // Consulta de respuestas si no
                
                break;
            }  
        } else {
            JOptionPane.showMessageDialog(frame,
                                          "Has visto todas las respuestas de este formulario.",
                                          "¡Éxito!",
                                          JOptionPane.PLAIN_MESSAGE);
            
            ResponderFormulario.Instance().setVisible(true);
        }
        
        
    }
}
