<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@page import="java.util.Map" %>
        <%@page import="java.util.List" %>
            <%@page import="cz.common.model.Objednavka" %>
                <%@page import="cz.common.model.Produkt" %>
                    <!DOCTYPE html>
                    <html>

                    <head>
                        <meta charset="UTF-8">
                        <title>Objednávka</title>
                        <style>
                            table {
                                width: 100%;
                                border-collapse: collapse;
                            }

                            th,
                            td {
                                border: 1px solid #ccc;
                                padding: 10px;
                                text-align: left;
                            }

                            th {
                                background-color: #f4f4f4;
                            }

                            .objednavka-button {
                                display: inline-block;
                                padding: 10px 20px;
                                background-color: #4CAF50;
                                color: white;
                                text-decoration: none;
                                border-radius: 8px;
                                box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
                                transition: background-color 0.3s, transform 0.2s;
                                margin-bottom: 20px;
                            }

                            .objednavka-button:hover {
                                background-color: #45a049;
                                transform: translateY(-2px);
                            }

                            .objednavka-button:active {
                                background-color: #3e8e41;
                                transform: translateY(0);
                            }
                        </style>
                    </head>

                    <body>
                        <a class="objednavka-button" href="/orderpanel/main">Zpět</a>
                        <h1>Vaše objednávka</h1>

                        <% Objednavka objednavka=(Objednavka) request.getAttribute("objednavka"); List<Produkt> produkty
                            = (List<Produkt>) request.getAttribute("produkty");

                                if (objednavka != null && objednavka.getProdukty() != null &&
                                !objednavka.getProdukty().isEmpty() && produkty != null) {
                                %>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Produkt</th>
                                            <th>Počet</th>
                                            <th>Akce</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Map.Entry<Integer, Integer> entry : objednavka.getProdukty().entrySet())
                                            {
                                            int idProdukt = entry.getKey();
                                            int pocet = entry.getValue();
                                            Produkt prod = null;

                                            for (Produkt produkt : produkty) {
                                            if (produkt.getId() == idProdukt) {
                                            prod = produkt;
                                            break;
                                            }
                                            }

                                            if (prod != null) {
                                            %>
                                            <tr>
                                                <td>
                                                    <%= prod.getNazev() %>
                                                </td>
                                                <td>
                                                    <%= pocet %>
                                                </td>
                                                <td>
                                                    <form method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="removeProduct">
                                                        <input type="hidden" name="produktId" value="<%= idProdukt %>">
                                                        <button type="submit">Odebrat</button>
                                                    </form>
                                                </td>
                                            </tr>
                                            <% } } %>
                                    </tbody>
                                </table>

                                <% } else { %>
                                    <p>Vaše objednávka je prázdná.</p>
                                    <% } %>
                                        <form method="post">
                                            <button type="submit">Uložit objednávku</button>
                                        </form>
                    </body>

                    </html>