import cz.common.model.Produkt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

@WebServlet(name = "OrderPanelServlet", urlPatterns = "/pepa")
public class OrderPanelServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                int bbb = 5555;

                request.setAttribute("aaa", bbb);

        ArrayList<Produkt> produkty = orderService.ziskatProdukty();
        request.setAttribute("produkty", produkty);

        getServletContext().getRequestDispatcher("/main.jsp").forward(request, response);
    }

}
