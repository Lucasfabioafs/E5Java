/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlers;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Tools.ConnexionBDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import Entities.Eleve;
import Entities.User;
import Entities.Lecon;
import com.mysql.jdbc.Statement;
import java.awt.List;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.text.Element;
import org.jfree.data.time.Hour;
/**
 *
 * @author Lucky1234
 */
public class CtrlEleve {
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlEleve() {
        cnx = ConnexionBDD.getCnx();
    }
    
    public float totalPayer(int idEleve) throws SQLException{
        ps=cnx.prepareStatement("SELECT COUNT(lecon.codeLecon)*categorie.prix FROM lecon JOIN Vehicule ON lecon.immatriculation = vehicule.immatriculation JOIN Categorie ON vehicule.codeCategorie = categorie.codeCategorie WHERE lecon.CodeEleve = ? AND lecon.reglee = 0;");
        ps.setInt(1, idEleve);
        rs = ps.executeQuery();
        rs.next();
        return rs.getFloat(1);
    }
    public void ModifInformationEleve(String nom, String prenom, String sexe, String adresse, String codePostal, String ville, String tel, int codeEleve, String naissance)
    {
        try {
            ps = cnx.prepareStatement("UPDATE eleve " +
                "set Nom = ?, Prenom = ?, Sexe =?, Adresse1= ?," +
                "CodePostal = ?, ville = ?, Telephone = ?,  DateDeNaissance = ? where eleve.codeEleve = ? ");
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, sexe);
            ps.setString(4, adresse);
            ps.setString(5, codePostal);
            ps.setString(6, ville);
            ps.setString(7, tel);

            ps.setString(8, naissance);
            ps.setInt(9, codeEleve);
            
            
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ModifInformationEleve2(String mail, String mdp, int codeEleve)
    {
        try {
            ps = cnx.prepareStatement("UPDATE identifiant " +
                "set mail = ?, mdp = ? where identifiant.codeEleve = ? ");
            ps.setString(1, mail);
            ps.setString(2, mdp);
            ps.setInt(3, codeEleve);
      
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
   public Eleve getInfo(int idEleve) throws SQLException {
       Eleve eleve = new Eleve();
       try {
//        ps = cnx.prepareStatement("Select Nom,Prenom,Adresse1,CodePostal,Ville,sexe,DateDeNaissance,Telephone" +
//                                        "from Eleve" +
//                                        "Join identifiant On Eleve.CodeEleve= identifiant.codeEleve where Identifiant.codeEleve = ?;");
        ps = cnx.prepareStatement("Select * from eleve where codeEleve = ?");
        ps.setInt(1, idEleve);
        rs = ps.executeQuery();
        rs.next();
        
        eleve=new Eleve(idEleve, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
          
        ps.close();
       }catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("AGADOU");
        }
        return eleve;
   }
   
   public User getInfo2(int idEleve) throws SQLException {
       User user = new User();
       try {
//        ps = cnx.prepareStatement("Select Nom,Prenom,Adresse1,CodePostal,Ville,sexe,DateDeNaissance,Telephone" +
//                                        "from Eleve" +
//                                        "Join identifiant On Eleve.CodeEleve= identifiant.codeEleve where Identifiant.codeEleve = ?;");
        ps = cnx.prepareStatement("Select mail,mdp from identifiant where codeEleve = ?");
        ps.setInt(1, idEleve);
        rs = ps.executeQuery();
        rs.next();
        
        user=new User(rs.getString(1), rs.getString(2));
        
        ps.close();
       }catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("AGADOU");
        }
        return user;
   }
   
   
    public ArrayList<Lecon> GetRDV(int idEleve) throws SQLException
    {
        // Initialise le tableau
        ArrayList<Lecon> lesRDV = new ArrayList<>();
        
        try {
            ps = cnx.prepareStatement("SELECT lecon.Date,lecon.Heure,moniteur.Nom,categorie.Libelle, lecon.reglee from lecon "
                                    + "JOIN eleve on lecon.CodeEleve = eleve.CodeEleve "
                                    + "JOIN moniteur on lecon.CodeMoniteur = moniteur.CodeMoniteur "
                                    + "JOIN vehicule on lecon.Immatriculation = vehicule.Immatriculation "
                                    + "JOIN categorie on vehicule.CodeCategorie = categorie.CodeCategorie "
                                    + "where  eleve.CodeEleve = ? and lecon.Date >=CURRENT_DATE();");
            ps.setInt(1, idEleve);
            rs = ps.executeQuery();
            while(rs.next()) {
                String reglee= "oui";
                if(rs.getInt(5)== 0){
                    reglee="non";
                }
                Lecon lecon = new Lecon(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), reglee);
                lesRDV.add(lecon);
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
        return lesRDV;
    }
    
    public DefaultListModel getLicence() throws SQLException
    {
        // Initialise le tableau
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        try {
            ps = cnx.prepareStatement("SELECT categorie.Libelle "
                                    + "from categorie");
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            ArrayList<String> ar = new ArrayList<String>();
            for (int i = 0; i < rs.getRow(); i++) {
                String s1 =rs.getString("Libelle");
            rs.next();
            ar.add(s1);
            }
            for (int i = 0; i < ar.size(); i++) {
                model.add(i, ar.get(i));
            }
        }else {
            String[] items = {"Aucune Licence"};
            for (int i = 0; i < items.length; i++) {
                model.add(i, items[i]);
            }
        }
        ps.close();
        
        return model;
    }
    public DefaultListModel getMoniteur() throws SQLException
    {
        // Initialise le tableau
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        try {
            ps = cnx.prepareStatement("SELECT moniteur.Nom "
                                    + "from moniteur");
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            ArrayList<String> ar = new ArrayList<String>();
            for (int i = 0; i < rs.getRow(); i++) {
                String s1 =rs.getString("Nom");
            rs.next();
            ar.add(s1);
            }
            for (int i = 0; i < ar.size(); i++) {
                model.add(i, ar.get(i));
            }
        }else {
            String[] items = {"Aucune Licence"};
            for (int i = 0; i < items.length; i++) {
                model.add(i, items[i]);
            }
        }
        ps.close();
        
        return model;
    }
     public DefaultListModel getMontantPermis(int idEleve, String laLicence) throws SQLException
    {
        // Initialise le tableau
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        try {
            ps = cnx.prepareStatement("SELECT lecon.Date,categorie.Prix\n" +
"					FROM lecon\n" +
"					JOIN vehicule on lecon.Immatriculation = vehicule.Immatriculation\n" +
"					JOIN categorie on vehicule.CodeCategorie = categorie.CodeCategorie\n" +
"					where lecon.CodeEleve = ? and lecon.Reglee=1 and categorie.Libelle = ?;");
        ps.setInt(1, idEleve);    
        ps.setString(2, laLicence);    
	rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            ArrayList<String> ar = new ArrayList<String>();
            for (int i = 0; i < rs.getRow(); i++) {
                String s1 =rs.getString("Date");
            rs.next();
            ar.add(s1);
            }
            for (int i = 0; i < ar.size(); i++) {
                model.add(i, ar.get(i));
            }
        }else {
            String[] items = {"Aucune"};
            for (int i = 0; i < items.length; i++) {
                model.add(i, items[i]);
            }
        }
        ps.close();
        return model;
    }
     
public DefaultListModel getLecon(int idEleve, String laLicence) throws SQLException
    {
        // Initialise le tableau
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        try {
            ps = cnx.prepareStatement("SELECT lecon.Date,categorie.Prix\n" +
"					FROM lecon\n" +
"					JOIN vehicule on lecon.Immatriculation = vehicule.Immatriculation\n" +
"					JOIN categorie on vehicule.CodeCategorie = categorie.CodeCategorie\n" +
"					where lecon.CodeEleve = ? and lecon.Reglee=0 and categorie.Libelle = ?;");
        ps.setInt(1, idEleve);    
        ps.setString(2, laLicence);    
	rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            ArrayList<String> ar = new ArrayList<String>();
            for (int i = 0; i < rs.getRow(); i++) {
                String s1 =rs.getString("Date");
            rs.next();
            ar.add(s1);
            }
            for (int i = 0; i < ar.size(); i++) {
                model.add(i, ar.get(i));
            }
        }else {
            String[] items = {"Aucune"};
            for (int i = 0; i < items.length; i++) {
                model.add(i, items[i]);
            }
        }
        ps.close();
        return model;
    }

    public String getPrixLecon(int idEleve, String laLicence) throws SQLException
    {
        String resultat = "0";
        try {
            ps = cnx.prepareStatement("SELECT categorie.Prix\n" +
"					FROM lecon\n" +
"					JOIN vehicule on lecon.Immatriculation = vehicule.Immatriculation\n" +
"					JOIN categorie on vehicule.CodeCategorie = categorie.CodeCategorie\n" +
"					where lecon.CodeEleve = ? and lecon.Reglee=0 and categorie.Libelle = ?;");
        ps.setInt(1, idEleve);    
        ps.setString(2, laLicence);    
	rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            resultat = rs.getString("Prix");
        }
        ps.close();
        return resultat;
    }
    
    public String getPrixPermis(int idEleve, String laLicence) throws SQLException
    {
        String resultat = "0";
        try {
            ps = cnx.prepareStatement("SELECT categorie.Prix\n" +
"					FROM lecon\n" +
"					JOIN vehicule on lecon.Immatriculation = vehicule.Immatriculation\n" +
"					JOIN categorie on vehicule.CodeCategorie = categorie.CodeCategorie\n" +
"					where lecon.CodeEleve = ? and lecon.Reglee=1 and categorie.Libelle = ?;");
        ps.setInt(1, idEleve);    
        ps.setString(2, laLicence);    
	rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            resultat = rs.getString("Prix");
        }
        ps.close();
        return resultat;
    }
    
    public DefaultListModel getVoiture(String laCateg) throws SQLException
    {
        // Initialise le tableau
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        try {
            ps = cnx.prepareStatement("select DISTINCT marque\n" +
                                      "from vehicule\n" +
                                      "JOIN categorie on vehicule.CodeCategorie = categorie.CodeCategorie\n" +
                                      "WHERE categorie.Libelle = ?;");
            ps.setString(1, laCateg);   
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            ArrayList<String> ar = new ArrayList<String>();
            for (int i = 0; i < rs.getRow(); i++) {
                String s1 =rs.getString("marque");
            rs.next();
            ar.add(s1);
            }
            for (int i = 0; i < ar.size(); i++) {
                model.add(i, ar.get(i));
            }
        }else {
            String[] items = {"Aucune Voiture"};
            for (int i = 0; i < items.length; i++) {
                model.add(i, items[i]);
            }
        }
        ps.close();
        
        return model;
    }
    
    public DefaultListModel getLicenceMoniteur(String leNom) throws SQLException
    {
        // Initialise le tableau
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        try {
            ps = cnx.prepareStatement("select categorie.Libelle\n" +
                                      "from moniteur\n" +
                                      "JOIN licence on moniteur.CodeMoniteur = licence.CodeMoniteur\n" +
                                      "join categorie on licence.CodeCategorie = categorie.CodeCategorie\n" +
                                      "where moniteur.Nom =?;");
            ps.setString(1, leNom);   
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            ArrayList<String> ar = new ArrayList<String>();
            for (int i = 0; i < rs.getRow(); i++) {
                String s1 =rs.getString("Libelle");
            rs.next();
            ar.add(s1);
            }
            for (int i = 0; i < ar.size(); i++) {
                model.add(i, ar.get(i));
            }
        }else {
            String[] items = {"Aucune Licence"};
            for (int i = 0; i < items.length; i++) {
                model.add(i, items[i]);
            }
        }
        ps.close();
        
        return model;
    }
    public int InscriptionLecon(String laDate,String lheure,int leNomMoniteur,int leCodeEleve,String laVoiture) throws SQLException
    {
        int resultat = 0;
        try {
            ps = cnx.prepareStatement("INSERT INTO lecon (date,heure,CodeMoniteur,CodeEleve,Immatriculation)\n" +
                                      " VALUES (?,?,?,?,?);");
            ps.setString(1, laDate);
            ps.setString(2, lheure);
            ps.setInt(3, leNomMoniteur);
            ps.setInt(4, leCodeEleve);
            ps.setString(5, laVoiture);
            ps.executeUpdate();
            String query = "select date,heure,CodeMoniteur,CodeEleve,Immatriculation from lecon";
            ResultSet rs = ps.executeQuery(query);
            if (rs.next()) {
            resultat = 1;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultat;
    }
    
    public ArrayList getMoniteurVoitureId(String leNomMoniteur,String laVoiture) throws SQLException
    {
        ArrayList list = new ArrayList();
        int resultat1;
        String resultat2;
        try {
            ps = cnx.prepareStatement("SELECT DISTINCT moniteur.CodeMoniteur,vehicule.Immatriculation\n" +
                                        "FROM moniteur\n" +
                                        "JOIN lecon on  moniteur.CodeMoniteur = lecon.CodeMoniteur\n" +
                                        "JOIN vehicule on lecon.Immatriculation = vehicule.Immatriculation\n" +
                                        "WHERE moniteur.Nom = ? and vehicule.Marque = ?;");
            ps.setString(1, leNomMoniteur);
            ps.setString(2, laVoiture);
            rs = ps.executeQuery();
            /*
            ps.setString(1, laDate);
            ps.setInt(2, rs.getInt("CodeMoniteur"));
            ps.setInt(3, leCodeEleve);
            ps.setString(4, rs.getString("Immatriculation"));
            */
        } catch (SQLException ex) {
            Logger.getLogger(CtrlEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rs.next()) {
            resultat1 = rs.getInt("CodeMoniteur");
            resultat2 = rs.getString("Immatriculation");
            list.add(resultat1);
            list.add(resultat2);
        }else {
            resultat1 = 0; 
            resultat2= "erreur";
        }
        ps.close();
        return list;
    }

}
