/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlers;

import Tools.ConnexionBDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jacqu
 */
public class CtrlGraphique 
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlGraphique() {
        cnx = ConnexionBDD.getCnx();
    }

  public HashMap<String,Integer> GetDatasGraphique3(int idMoniteur)
    {
        HashMap<String, Integer> datas = new HashMap();
        try {
            ps = cnx.prepareStatement("select modele,count(lecon.CodeLecon) as total \n" +
"from lecon\n" +
"join moniteur on lecon.CodeMoniteur = moniteur.CodeMoniteur \n" +
"join vehicule on lecon.Immatriculation=vehicule.Immatriculation\n" +
"WHERE moniteur.CodeMoniteur = ?\n" +
"group by modele;");
            ps.setInt(1, idMoniteur);
            rs = ps.executeQuery();
            while(rs.next())
            {
                datas.put(rs.getString(1), rs.getInt("total"));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlGraphique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datas;
    }

}
