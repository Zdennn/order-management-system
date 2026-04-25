import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.common.model.Kategorie;
import cz.common.model.Objednavka;
import cz.common.model.Produkt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "MainServlet", urlPatterns = "/main")
public class MainServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderService = new OrderService();
            orderService.novyKlient();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ArrayList<Produkt> produkty = orderService.ziskatProdukty();
        req.setAttribute("produkty", produkty);

        ArrayList<Kategorie> kategorie = orderService.ziskatKategorie();
        req.setAttribute("katogerie", kategorie);

        getServletContext().getRequestDispatcher("/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idProduktParam = req.getParameter("idProdukt");
        if (idProduktParam != null) {
            try {
                int idProdukt = Integer.parseInt(idProduktParam);

                HttpSession session = req.getSession();

                Objednavka objednavka = (Objednavka) session.getAttribute("objednavka");
                if (objednavka == null) {
                    objednavka = new Objednavka(0, 1, new HashMap<>());
                }

                Map<Integer, Integer> produkty = objednavka.getProdukty();
                produkty.put(idProdukt, produkty.getOrDefault(idProdukt, 0) + 1);

                session.setAttribute("objednavka", objednavka);

                resp.sendRedirect(req.getContextPath() + "/main");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Neplatné ID produktu");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID produktu nebylo poskytnuto");
        }
    }

}
