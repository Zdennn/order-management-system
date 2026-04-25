import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import cz.common.model.Objednavka;
import cz.common.model.Produkt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

@WebServlet(name = "OrderPanelServlet", urlPatterns = "/objednavka")
public class ObjednavkaServlet extends HttpServlet {

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
        HttpSession session = req.getSession();
        Objednavka objednavka = (Objednavka) session.getAttribute("objednavka");

        req.setAttribute("objednavka", objednavka);

        ArrayList<Produkt> produkty = orderService.ziskatProdukty();
        req.setAttribute("produkty", produkty);

        getServletContext().getRequestDispatcher("/objednavka.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Objednavka objednavka = (Objednavka) session.getAttribute("objednavka");

        String action = req.getParameter("action");

        if ("removeProduct".equals(action)) {
            int produktId = Integer.parseInt(req.getParameter("produktId"));
            if (objednavka != null && objednavka.getProdukty() != null) {
                objednavka.getProdukty().remove(produktId);
            }
            session.setAttribute("objednavka", objednavka);
            resp.sendRedirect(req.getContextPath() + "/objednavka");
            return;
        }

        orderService.vytvoritObjednavku(objednavka);
        tiskUctenky(objednavka);
        session.setAttribute("objednavka", null);
        resp.sendRedirect(req.getContextPath() + "/main");
    }

    private void tiskUctenky(Objednavka objednavka) {

        String cesta = "C:/Users/zdend/OneDrive/Dokumenty/skola4r/programovani4/sys2.0/orderpanel/src/main/uctenky/";
        String fileName;

        File dir = new File(cesta);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Adresář byl vytvořen.");
            } else {
                System.out.println("Nepodařilo se vytvořit adresář.");
            }
        } else {
            System.out.println("Adresář již existuje.");
        }

        int pocetUctenek = dir.list((d, name) -> name.startsWith("uctenka") && name.endsWith(".txt")).length + 1;

        if (pocetUctenek > 10) {
            pocetUctenek = 1;
        }

        fileName = cesta + "uctenka" + pocetUctenek + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName), Charset.forName("UTF-8")))) {
            writer.printf("%30s%n", "ÚČTENKA");
            writer.println("Kód účtenky: " + objednavka.getId());
            writer.println("=".repeat(50));
            writer.printf("%-30s %15s%n", "Název produktu", "Cena");
            writer.println("-".repeat(50));

            double celkovaCena = 0.0;
            ArrayList<Produkt> produkty = orderService.ziskatProdukty();

            for (Map.Entry<Integer, Integer> entry : objednavka.getProdukty().entrySet()) {
                int produktId = entry.getKey();
                int mnozstvi = entry.getValue();

                Produkt produkt = null;
                for (Produkt produktt : produkty) {
                    if (produktt.getId() == produktId) {
                        produkt = produktt;
                        break;
                    }
                }

                double cena = produkt.getCena() * mnozstvi;

                writer.printf("%-25s %8d %8.2f Kč%n", produkt.getNazev(), mnozstvi, cena);
                celkovaCena += cena;
            }

            writer.println("=".repeat(50));
            writer.printf("%-30s %15.2f Kč%n", "Celková cena:", celkovaCena);
            writer.println("Děkujeme za nákup!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        orderService.odpojitKlienta();
        super.destroy();
    }

}
