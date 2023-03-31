/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Lucky1234
 */
public class User {
    private int idMoniteur;
    private int idEleve;
    private String statutUser;
    private String mdp;
    private String mail;
    
    public User(){}
    public User(String unMail,String unMdp){
        mail=unMail;
        mdp=unMdp;
    }
    public User(int unIdMoniteur,int unIdEleve,String unStatut)
    {
        idMoniteur =unIdMoniteur;
        idEleve = unIdEleve;
        statutUser = unStatut;
    }

    public int getIdMoniteur() {
        return idMoniteur;
    }

    public void setIdMoniteur(int idMoniteur) {
        this.idMoniteur = idMoniteur;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public String getStatutUser() {
        return statutUser;
    }

    public void setStatutUser(String statutUser) {
        this.statutUser = statutUser;
    }

    /**
     * @return the mdp
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }
    
   
}
