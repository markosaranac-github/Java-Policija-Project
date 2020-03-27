package entities;
// Generated Jan 6, 2019 2:57:43 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Svedok generated by hbm2java
 */
public class Svedok  implements java.io.Serializable {


     private int sveId;
     private String sveIme;
     private String svePrezime;
     private Date sveDtrodj;
     private String sveAdresa;
     private String sveTelefon;

    public Svedok() {
    }

	
    public Svedok(int sveId, String sveIme, String svePrezime) {
        this.sveId = sveId;
        this.sveIme = sveIme;
        this.svePrezime = svePrezime;
    }
    public Svedok(int sveId, String sveIme, String svePrezime, Date sveDtrodj, String sveAdresa, String sveTelefon) {
       this.sveId = sveId;
       this.sveIme = sveIme;
       this.svePrezime = svePrezime;
       this.sveDtrodj = sveDtrodj;
       this.sveAdresa = sveAdresa;
       this.sveTelefon = sveTelefon;
    }
   
    public int getSveId() {
        return this.sveId;
    }
    
    public void setSveId(int sveId) {
        this.sveId = sveId;
    }
    public String getSveIme() {
        return this.sveIme;
    }
    
    public void setSveIme(String sveIme) {
        this.sveIme = sveIme;
    }
    public String getSvePrezime() {
        return this.svePrezime;
    }
    
    public void setSvePrezime(String svePrezime) {
        this.svePrezime = svePrezime;
    }
    public Date getSveDtrodj() {
        return this.sveDtrodj;
    }
    
    public void setSveDtrodj(Date sveDtrodj) {
        this.sveDtrodj = sveDtrodj;
    }
    public String getSveAdresa() {
        return this.sveAdresa;
    }
    
    public void setSveAdresa(String sveAdresa) {
        this.sveAdresa = sveAdresa;
    }
    public String getSveTelefon() {
        return this.sveTelefon;
    }
    
    public void setSveTelefon(String sveTelefon) {
        this.sveTelefon = sveTelefon;
    }




}


