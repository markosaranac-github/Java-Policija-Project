package servlets;

import db.HibernateUtil;
import entities.Inspektor;
import entities.Nacelnik;
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

public class UserServlet extends HttpServlet {

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
        
        if (action.equals("login")){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            Session hibSession = HibernateUtil.getSessionFactory().openSession();
            
            boolean loggedIn = false;
            
            for (Object n : hibSession.createCriteria(Nacelnik.class).list()){
                Nacelnik nacelnik = (Nacelnik)n;
                
                if (nacelnik.getNacKorisnickoIme().equals(username) &&
                    nacelnik.getNacSifra().equals(password)){
                    HttpSession session = request.getSession(true);
                    session.setAttribute("user", nacelnik);
                    loggedIn = true;
                    break;
                }
            }
            
            if (loggedIn == false){
                for (Object i : hibSession.createCriteria(Inspektor.class).list()){
                    Inspektor inspektor = (Inspektor)i;

                    if (inspektor.getInsKorisnickoIme().equals(username) &&
                        inspektor.getInsSifra().equals(password)){
                        HttpSession session = request.getSession(true);
                        session.setAttribute("user", inspektor);
                        loggedIn = true;
                        break;
                    }
                }
            }
            
            hibSession.close();
            
            String msg = "Uspesno ste se ulogovali, dobrodosli na sistem!";
            if (loggedIn){
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp?message=" + msg);
                dispatcher.forward(request, response);
            } else {
                msg = "Greska pri logovanju, molimo proverite podatke i pokusajte ponovo!";
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp?message=" + msg);
                dispatcher.forward(request, response);
            }
           
        } else if (action.equals("logout")){
            HttpSession session = request.getSession(true);
            session.removeAttribute("user");
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        } else if (action.equals("edit")){
            HttpSession session = request.getSession();
            
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
                    }
                }

                if (session.getAttribute("user").getClass().equals(Nacelnik.class)){

                    Nacelnik user = (Nacelnik) hibSession.get(Nacelnik.class, ((Nacelnik)session.getAttribute("user")).getNacId());

                    user.setNacIme(ime);
                    user.setNacPrezime(prezime);
                    if (!datum.equals("")){
                    user.setNacDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
                    }
                    user.setNacTelefon(telefon);
                    user.setNacAdresa(adresa);
                    user.setNacKorisnickoIme(korisnicko_ime);

                    hibSession.beginTransaction();
                    hibSession.update(user);
                    hibSession.getTransaction().commit();
                    
                    session.setAttribute("user", user);

                } else {

                    Inspektor user = (Inspektor) hibSession.get(Inspektor.class, ((Inspektor)session.getAttribute("user")).getInsId());

                    user.setInsIme(ime);
                    user.setInsPrezime(prezime);
                    if (!datum.equals("")){
                    user.setInsDtrodj(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
                    }
                    user.setInsTelefon(telefon);
                    user.setInsAdresa(adresa);
                    user.setInsKorisnickoIme(korisnicko_ime);

                    hibSession.beginTransaction();
                    hibSession.update(user);
                    hibSession.getTransaction().commit();

                    session.setAttribute("user", user);
                    
                }
                
                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("profil.jsp?message=" + msg);
                dispatcher.forward(request, response);
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                } catch (TransactionException tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                hibSession.close();
                
                String msg = "Doslo je do greske pri cuvanju izmena. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("profil.jsp?message=" + msg);
                dispatcher.forward(request, response);
            }
        } else if (action.equals("changepassword")){
            String sifra = request.getParameter("sifra");
            
            if (sifra.length() > 3){
                HttpSession session = request.getSession();

                Session hibSession = null;

                try {
                    hibSession = HibernateUtil.getSessionFactory().openSession();
                    
                    if (session.getAttribute("user").getClass().equals(Nacelnik.class)){
                        Nacelnik user = (Nacelnik) hibSession.get(Nacelnik.class, ((Nacelnik)session.getAttribute("user")).getNacId());
                    
                        user.setNacSifra(sifra);
                        
                        hibSession.beginTransaction();
                        hibSession.update(user);
                        hibSession.getTransaction().commit();

                        session.setAttribute("user", user);
                    } else {
                        Inspektor user = (Inspektor) hibSession.get(Inspektor.class, ((Inspektor)session.getAttribute("user")).getInsId());
                        
                        user.setInsSifra(sifra);
                        
                        hibSession.beginTransaction();
                        hibSession.update(user);
                        hibSession.getTransaction().commit();

                        session.setAttribute("user", user);
                    }
                
                    hibSession.close();

                    String msg = "Promene su uspesno sacuvane!";

                    RequestDispatcher dispatcher = request.getRequestDispatcher("profil.jsp?message=" + msg);
                    dispatcher.forward(request, response);
                } catch (Exception ex){
                    String exMsg = ex.getMessage();

                    try {
                        hibSession.getTransaction().rollback();
                    } catch (TransactionException tex){ exMsg += " (" + tex.getMessage() + ")"; }

                    hibSession.close();

                    String msg = "Doslo je do greske pri cuvanju izmena. Prosledite administratoru: " + exMsg;

                    RequestDispatcher dispatcher = request.getRequestDispatcher("profil.jsp?message=" + msg);
                    dispatcher.forward(request, response);
                }  
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
