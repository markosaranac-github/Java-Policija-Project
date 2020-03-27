package servlets;

import db.HibernateUtil;
import entities.PolicijskaStanica;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.TransactionException;

public class StanicaServlet extends HttpServlet {

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
        
        if (action.equals("edit")){

            String[] params = URLDecoder.decode(request.getQueryString(), "UTF-8").split("&");
            
            String adresa = "";
            
            for(String par : params){
                if (par.split("=")[0].equals("adresa")){
                    adresa = par.split("=")[1];
                }
            }
            
            String telefon = request.getParameter("telefon");
            
            Session hibSession = null;
            try {
                hibSession = HibernateUtil.getSessionFactory().openSession();

                PolicijskaStanica stanica = (PolicijskaStanica) hibSession.get(PolicijskaStanica.class, 1);

                stanica.setPolAdresa(adresa);
                stanica.setPolTelefon(telefon);
                
                hibSession.beginTransaction();
                hibSession.update(stanica);
                hibSession.getTransaction().commit();

                hibSession.close();
                
                String msg = "Promene su uspesno sacuvane!";
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("stanica.jsp?message=" + msg);
                dispatcher.forward(request, response);
            } catch (Exception ex){
                String exMsg = ex.getMessage();
                
                try {
                    hibSession.getTransaction().rollback();
                } catch (TransactionException tex){ exMsg += " (" + tex.getMessage() + ")"; }
                
                hibSession.close();
                
                String msg = "Doslo je do greske pri cuvanju izmena. Prosledite administratoru: " + exMsg;
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("stanica.jsp?message=" + msg);
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
