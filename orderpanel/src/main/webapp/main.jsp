<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@page import="java.util.List" %>
        <%@page import="java.util.Map" %>
            <%@page import="java.util.HashMap" %>
                <%@page import="cz.common.model.Produkt" %>
                    <%@page import="cz.common.model.Kategorie" %>
                        <!DOCTYPE html>
                        <html>

                        <head>
                            <meta charset="UTF-8">
                            <title>Seznam produktů</title>
                            <style>
                                .kategorie {
                                    margin-bottom: 20px;
                                }

                                .produkty-container {
                                    display: flex;
                                    flex-wrap: wrap;
                                    gap: 10px;
                                }

                                .produkt-card {
                                    border: 1px solid #ccc;
                                    padding: 10px;
                                    width: 200px;
                                    border-radius: 8px;
                                    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
                                    text-align: center;
                                }

                                .produkt-card h3 {
                                    margin: 0 0 10px;
                                }

                                .objednavka-button {
                                    display: inline-block;
                                    padding: 10px 20px;
                                    background-color: #4CAF50;
                                    /* zelená barva, můžeš změnit */
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
                            <a class="objednavka-button" href="/orderpanel/objednavka">Objednávka</a>
                            <h1>Seznam produktů</h1>

                            <% List<Produkt> produkty = (List<Produkt>) request.getAttribute("produkty");
                                    List<Kategorie> kategorie = (List<Kategorie>) request.getAttribute("kategorie");

                                            Map<Integer, String> kategorieMapa = new HashMap<>();
                                                    if (kategorie != null) {
                                                    for (Kategorie kat : kategorie) {
                                                    kategorieMapa.put(kat.getId(), kat.getNazev());
                                                    }
                                                    }

                                                    int aktualniKategorie = -1;
                                                    if (produkty != null && !produkty.isEmpty()) {
                                                    for (Produkt produkt : produkty) {
                                                    if (produkt.getidKategorie() != aktualniKategorie) {
                                                    aktualniKategorie = produkt.getidKategorie();
                                                    %>
                                                    <div class="kategorie">
                                                        <br>
                                                        <div class="produkty-container">
                                                            <% } %>
                                                                <div class="produkt-card">
                                                                    <h3>
                                                                        <%= produkt.getNazev() %>
                                                                    </h3>
                                                                    <p>Cena: <strong>
                                                                            <%= produkt.getCena() %> Kč
                                                                        </strong></p>
                                                                    <p>ID produktu: <%= produkt.getId() %>
                                                                    </p>
                                                                    <form method="post">
                                                                        <input type="hidden"
                                                                            value="<%= produkt.getId() %>"
                                                                            name="idProdukt">
                                                                        <button type="submit">Přidat do košíku</button>
                                                                    </form>
                                                                </div>
                                                                <% if (produkty.indexOf(produkt)==produkty.size() - 1 ||
                                                                    produkt.getidKategorie()
                                                                    !=produkty.get(produkty.indexOf(produkt) +
                                                                    1).getidKategorie()) { %>
                                                        </div>
                                                    </div>
                                                    <% } } } else { %>
                                                        <p>Žádné produkty k zobrazení</p>
                                                        <% } %>
                        </body>

                        </html>