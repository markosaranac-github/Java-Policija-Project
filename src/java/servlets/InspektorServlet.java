package servlets;

import db.HibernateUtil;
import entities.Inspektor;
import entities.PolicijskaStanica;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.TransactionException;

public class InspektorServlet extends HttpServlet {

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
            String query = URLDecoder.decode(request.getQueryString(), "UTF-8");
            String[] params = query.split("&");
            
            Session hibSession = null;
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                Inspektor inspektor = new Inspektor();
                
                for(String par : params){
                    switch (par.split("=")[0]) {
                        case "ime":
                            inspektor.setInsIme(par.split("=")[1]);
                            break;
                        case "prezime":
                            inspektor.setInsPrezime(par.split("=")[1]);
                            break;
                        case "datum":
                            inspektor.setInsDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(par.split("=")[1]));
                            break;
                        case "telefon":
                            inspektor.setInsTelefon(par.split("=")[1]);
                            break;
                        case "adresa":
                            inspektor.setInsAdresa(par.split("=")[1]);
                            break;
                        case "korisnicko_ime":
                            inspektor.setInsKorisnickoIme(par.split("=")[1]);
                            break;
                        case "sifra":
                            inspektor.setInsSifra(par.split("=")[1]);
                            break;
                    }
                }
                            
                inspektor.setPolicijskaStanica((PolicijskaStanica) hibSession.get(PolicijskaStanica.class, 1));
                
                hibSession.beginTransaction();
                hibSession.save(inspektor);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Uspesno je dodat novi inspektor!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("inspektori.jsp?message=" + msg);
                dispatcher.forward(request, response);
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                } catch (TransactionException tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                hibSession.close();
                
                String msg = "Doslo je do greske pri dodavanju inspektora. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("inspektori.jsp?message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("reset")){
            Session hibSession = null;
            
            int id = Integer.parseInt(request.getParameter("id"));
            
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();

                Inspektor inspektor = (Inspektor) hibSession.get(Inspektor.class, id);
                inspektor.setInsSifra("password");
                
                hibSession.beginTransaction();
                hibSession.update(inspektor);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Inspektoru je uspesno resetovana sifra!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("inspektor.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
                
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                } catch (TransactionException tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                hibSession.close();
                
                String msg = "Doslo je do greske pri resetovanju sifre. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("inspektor.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("edit")){
            String query = URLDecoder.decode(request.getQueryString(), "UTF-8");
            String[] params = query.split("&");
            
            Session hibSession = null;
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();
                
                String ime = "";
                String prezime = "";
                String datum = "";
                String telefon = "";
                String adresa = "";
                String korisnicko_ime = "";
                String id = "";
                
                for(String par : params){
                    switch (par.split("=")[0]) {
                        case "ime":
                            ime = par.split("=")[1];
                            break;
                        case "prezime":
                            prezime = par.split("=")[1];
                            break;
                        case "datum":
                            datum = "";
                            if (par.split("=").length > 1)
                            datum = par.split("=")[1];
                            break;
                        case "telefon":
                            telefon = "";
                            if (par.split("=").length > 1)
                            telefon = par.split("=")[1];
                            break;
                        case "adresa":
                            adresa = "";
                            if (par.split("=").length > 1)
                            adresa = par.split("=")[1];
                            break;
                        case "korisnicko_ime":
                            korisnicko_ime = par.split("=")[1];
                            break;
                        case "id":
                            id = par.split("=")[1];
                            break;
                    }
                }
                            
                Inspektor inspektor = (Inspektor) hibSession.get(Inspektor.class, Integer.parseInt(id));
                
                inspektor.setInsIme(ime);
                inspektor.setInsPrezime(prezime);
                if (!datum.equals("")){
                inspektor.setInsDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
                }
                inspektor.setInsTelefon(telefon);
                inspektor.setInsAdresa(adresa);
                inspektor.setInsKorisnickoIme(korisnicko_ime);
                
                hibSession.beginTransaction();
                hibSession.update(inspektor);
                hibSession.getTransaction().commit();
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("inspektor.jsp?id=" + id + "&message=" + msg);
                dispatcher.forward(request, response);
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                } catch (TransactionException tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                hibSession.close();
                
                String msg = "Doslo je do greske pri cuvanju izmena. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("inspektor.jsp?id=" + request.getParameter("id") + "&message=" + msg);
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
