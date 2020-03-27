package servlets;

import db.HibernateUtil;
import entities.Inspektor;
import entities.KrivicnoDelo;
import entities.Osteceni;
import entities.Osumnjiceni;
import entities.Svedok;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Order;

public class DeloServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action.equals("add")){
            String clan = request.getParameter("clan");
            String pocetak = request.getParameter("pocetak");
            String kraj = request.getParameter("kraj");
            String opis = "";
            
            String[] params = URLDecoder.decode(request.getQueryString(), "UTF-8").split("&");
            
            for(String par : params){
                if (par.split("=")[0].equals("opis")){
                    opis = par.split("=")[1];
                    break;
                }
            }
            
            Session hibSession = null;
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                KrivicnoDelo kd = new KrivicnoDelo();
                
                kd.setKriClan(Integer.parseInt(clan));
                if (!kraj.equals("")){
                kd.setKriKraj(new SimpleDateFormat("yyyy-MM-dd").parse(kraj));
                }
                if (!pocetak.equals("")){
                kd.setKriPocetak(new SimpleDateFormat("yyyy-MM-dd").parse(pocetak));
                }
                kd.setKriOpis(opis);
                
                hibSession.beginTransaction();
                hibSession.save(kd);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Uspesno je dodato novo krivicno delo!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnadela.jsp?message=" + msg);
                dispatcher.forward(request, response);
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                     hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri dodavanju krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnadela.jsp?message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("edit")){
            String id = request.getParameter("id");
            String clan = request.getParameter("clan");
            String pocetak = request.getParameter("pocetak");
            String kraj = request.getParameter("kraj");
            String opis = "";
            
            String[] params = URLDecoder.decode(request.getQueryString(), "UTF-8").split("&");
            
            for(String par : params){
                if (par.split("=")[0].equals("opis")){
                    opis = par.split("=")[1];
                    break;
                }
            }
            
            Session hibSession = null;
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                KrivicnoDelo kd = (KrivicnoDelo) hibSession.get(KrivicnoDelo.class, Integer.parseInt(id));
                
                kd.setKriClan(Integer.parseInt(clan));
                kd.setKriOpis(opis);
                if(!pocetak.equals("")){
                    kd.setKriPocetak(new SimpleDateFormat("yyyy-MM-dd").parse(pocetak));
                }
                if(!kraj.equals("")){
                    kd.setKriKraj(new SimpleDateFormat("yyyy-MM-dd").parse(kraj));
                }
                
                hibSession.beginTransaction();
                hibSession.update(kd);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
                
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                     hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("addInspektor")){
            Session hibSession = null;

            String inspektor_id = request.getParameter("inspektorselect");
            int id = Integer.parseInt(request.getParameter("delo"));
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                int inspektor = Integer.parseInt(inspektor_id);
                
                KrivicnoDelo kd = (KrivicnoDelo)hibSession.get(KrivicnoDelo.class, id);
                
                Set inspektori = kd.getInspektori();
                inspektori.add((Inspektor)hibSession.get(Inspektor.class, inspektor));
                kd.setInspektori(inspektori);
                
                hibSession.beginTransaction();
                hibSession.update(kd);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
                
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                     hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("addOsteceni")){
            Session hibSession = null;

            String osteceniselect = request.getParameter("osteceniselect");
            String ostecenistring = "";
            int id = Integer.parseInt(request.getParameter("delo"));
            
            String[] params = URLDecoder.decode(request.getQueryString(), "UTF-8").split("&");
            
            for(String par : params){
                if (par.split("=").length == 2){
                    if (par.split("=")[0].equals("osteceni")){
                        ostecenistring = par.split("=")[1];
                        break;
                    }
                }
            }
            
            String[] osteceni_info = ostecenistring.split(",");
            
            int osteceni_id = -1; //za novog ostecenog
            
            String ime = "";
            String prezime = "";
            String datum = "";
            String adresa = "";
            String telefon = "";
            
            if (osteceni_info.length == 5){
                ime = osteceni_info[0].trim();
                prezime = osteceni_info[1].trim();
                datum = osteceni_info[2].trim();
                adresa = osteceni_info[3].trim();
                telefon = osteceni_info[4].trim();
            } else {
                osteceni_id = Integer.parseInt(osteceniselect);
            }
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                KrivicnoDelo kd = (KrivicnoDelo)hibSession.get(KrivicnoDelo.class, id);
                
                if (osteceni_id != -1){
                    Set osteceni = kd.getOsteceni();
                    osteceni.add((Osteceni)hibSession.get(Osteceni.class, osteceni_id));
                    kd.setOsteceni(osteceni);
                } else {
                    Osteceni novi_osteceni = new Osteceni();
                    
                    novi_osteceni.setOstAdresa(adresa);
                    novi_osteceni.setOstTelefon(telefon);
                    if (!datum.equals("")){
                        novi_osteceni.setOstDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
                    }
                    novi_osteceni.setOstIme(ime);
                    novi_osteceni.setOstPrezime(prezime);
                  
                    hibSession.beginTransaction();
                    hibSession.save(novi_osteceni);
                    hibSession.getTransaction().commit();

                    Set osteceni = kd.getOsteceni();
                    List temp = hibSession.createCriteria(Osteceni.class).addOrder(Order.asc("ostId")).list(); // da uzmemo sve ostecene kako bi dobili id ovog koji smo dodali
                    osteceni.add(temp.get(temp.size() - 1));
                    kd.setOsteceni(osteceni);
                }
                
                hibSession.beginTransaction();
                hibSession.update(kd);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
                
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                     hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("addOsumnjiceni")){
            Session hibSession = null;

            String osumnjiceniselect = request.getParameter("osumnjiceniselect");
            String osumnjicenistring = "";
            int id = Integer.parseInt(request.getParameter("delo"));
            
            String[] params = URLDecoder.decode(request.getQueryString(), "UTF-8").split("&");
            
            for(String par : params){
                if (par.split("=").length == 2){
                    if (par.split("=")[0].equals("osumnjiceni")){
                        osumnjicenistring = par.split("=")[1];
                        break;
                    }
                }
            }
            
            String[] osumnjiceni_info = osumnjicenistring.split(",");
            
            int osumnjiceni_id = -1; // za novog osumnjicenog
            
            String ime = "";
            String prezime = "";
            String datum = "";
            String adresa = "";
            String telefon = "";
            String tezina = "";
            String visina = "";
            String opis = "";
            
            if (osumnjiceni_info.length == 7){
                ime = osumnjiceni_info[0].trim();
                prezime = osumnjiceni_info[1].trim();
                datum = osumnjiceni_info[2].trim();
                adresa = osumnjiceni_info[3].trim();
                telefon = osumnjiceni_info[4].trim();
                visina = osumnjiceni_info[5].trim().split("/")[0].trim();
                tezina = osumnjiceni_info[5].trim().split("/")[1].trim();
                opis = osumnjiceni_info[6].trim();
            } else {
                osumnjiceni_id = Integer.parseInt(osumnjiceniselect);
            }
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                KrivicnoDelo kd = (KrivicnoDelo)hibSession.get(KrivicnoDelo.class, id);
                
                if (osumnjiceni_id != -1){
                    Set osumnjiceni = kd.getOsumnjiceni();
                    osumnjiceni.add((Osumnjiceni)hibSession.get(Osumnjiceni.class, osumnjiceni_id));
                    kd.setOsumnjiceni(osumnjiceni);
                } else {
                    Osumnjiceni novi_osumnjiceni = new Osumnjiceni();
                    
                    novi_osumnjiceni.setOsuAdresa(adresa);
                    novi_osumnjiceni.setOsuTelefon(telefon);
                    if (!datum.equals("")){
                        novi_osumnjiceni.setOsuDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
                    }
                    novi_osumnjiceni.setOsuIme(ime);
                    novi_osumnjiceni.setOsuPrezime(prezime);
                    novi_osumnjiceni.setOsuOpis(opis);
                    if (!tezina.equals("")){
                        novi_osumnjiceni.setOsuTezina(Integer.parseInt(tezina));
                    }
                    if (!visina.equals("")){
                        novi_osumnjiceni.setOsuVisina(Integer.parseInt(visina));
                    }
                    hibSession.beginTransaction();
                    hibSession.save(novi_osumnjiceni);
                    hibSession.getTransaction().commit();

                    Set osumnjiceni = kd.getOsumnjiceni();
                    List temp = hibSession.createCriteria(Osumnjiceni.class).addOrder(Order.asc("osuId")).list();
                    osumnjiceni.add(temp.get(temp.size() - 1));
                    kd.setOsumnjiceni(osumnjiceni);
                }
                
                hibSession.beginTransaction();
                hibSession.update(kd);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
                
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                     hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("addSvedok")){
            Session hibSession = null;

            String svedokselect = request.getParameter("svedokselect");
            String svedokstring = "";
            int id = Integer.parseInt(request.getParameter("delo"));
            
            String[] params = URLDecoder.decode(request.getQueryString(), "UTF-8").split("&");
            
            for(String par : params){
                if (par.split("=").length == 2){
                    if (par.split("=")[0].equals("svedok")){
                        svedokstring = par.split("=")[1];
                        break;
                    }
                }
            }
            
            String[] svedok_info = svedokstring.split(",");
            
            int svedok_id = -1;
            String svedok_svedocenje = "";
            
            int id_za_svedocenje = -1;
            
            String ime = "";
            String prezime = "";
            String datum = "";
            String adresa = "";
            String telefon = "";
            String svedocenje = "";
            
            if (svedok_info.length == 6){
                ime = svedok_info[0].trim();
                prezime = svedok_info[1].trim();
                datum = svedok_info[2].trim();
                adresa = svedok_info[3].trim();
                telefon = svedok_info[4].trim();
                svedocenje = svedok_info[5].trim();
            } else {
                svedok_id = Integer.parseInt(svedokselect);
                svedok_svedocenje = request.getParameter("svedocenje");
            }
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                KrivicnoDelo kd = (KrivicnoDelo)hibSession.get(KrivicnoDelo.class, id);
                
                if (svedok_id != -1){
                    Set svedoci = kd.getSvedoci();
                    svedoci.add((Svedok)hibSession.get(Svedok.class, svedok_id));
                    kd.setSvedoci(svedoci);
                } else {
                    Svedok novi_svedok = new Svedok();
                    
                    novi_svedok.setSveAdresa(adresa);
                    novi_svedok.setSveTelefon(telefon);
                    if (!datum.equals("")){
                        novi_svedok.setSveDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
                    }
                    novi_svedok.setSveIme(ime);
                    novi_svedok.setSvePrezime(prezime);
                    
                    hibSession.beginTransaction();
                    hibSession.save(novi_svedok);
                    hibSession.getTransaction().commit();

                    Set svedoci = kd.getSvedoci();
                    List temp = hibSession.createCriteria(Svedok.class).addOrder(Order.asc("sveId")).list();
                    svedoci.add(temp.get(temp.size() - 1));
                    
                    id_za_svedocenje = ((Svedok)temp.get(temp.size() - 1)).getSveId();
                    kd.setSvedoci(svedoci);
                }
                
                hibSession.beginTransaction();
                hibSession.update(kd);
                hibSession.getTransaction().commit();
                
                if (svedok_id != -1){ // kad jeste postojeci (imas id znaci postoji)
                    hibSession.beginTransaction();
                    hibSession.createSQLQuery("update kri_sve set kri_sve_opis = '" + svedok_svedocenje + "' where kri_id = " + kd.getKriId() + " and sve_id = " + svedok_id).executeUpdate();
                    hibSession.getTransaction().commit();
                } else {
                    // svedocenje novog svedoka je promenljiva svedocenje
                    hibSession.beginTransaction();
                    hibSession.createSQLQuery("update kri_sve set kri_sve_opis = '" + svedocenje + "' where kri_id = " + kd.getKriId() + " and sve_id = " + id_za_svedocenje).executeUpdate();
                    hibSession.getTransaction().commit();
                }
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
                
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                     hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("removeOsteceni")){
            Session hibSession = null;
            
            String osteceni_id = request.getParameter("id");
            String id = request.getParameter("delo");
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                hibSession.beginTransaction();
                hibSession.createSQLQuery("delete from kri_ost where kri_id = " + id + " and ost_id = " + osteceni_id).executeUpdate();
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                    hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("removeOsumnjiceni")){
            Session hibSession = null;
            
            String osumnjiceni_id = request.getParameter("id");
            String id = request.getParameter("delo");
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                hibSession.beginTransaction();
                hibSession.createSQLQuery("delete from kri_osu where kri_id = " + id + " and osu_id = " + osumnjiceni_id).executeUpdate();
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                    hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("removeSvedok")){
            Session hibSession = null;
            
            String svedok_id = request.getParameter("id");
            String id = request.getParameter("delo");
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                hibSession.beginTransaction();
                hibSession.createSQLQuery("delete from kri_sve where kri_id = " + id + " and sve_id = " + svedok_id).executeUpdate();
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                    
                    hibSession.close();
                } catch (Exception tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                String msg = "Doslo je do greske pri izmeni krivicnog dela. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("krivicnodelo.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
