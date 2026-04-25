package cz.okna.adminpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.*;

import cz.common.model.Kategorie;
import cz.common.model.Produkt;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminFrame extends JFrame {
    private JTextField nazevField;
    private JTextField cenaField;
    private JComboBox<String> stavComboBox, kategorieComboBox;

    private ArrayList<Produkt> produkty;
    private ArrayList<Kategorie> kategorie;

    int maxProduktuVKategorii = 0;
    boolean bezLabelu = false;

    JButton zpetButton, pridatProduktButton, pridatVylepseniButton, pridatKategoriiButton, produktButton,
            objednavkyButton;

    AdminService as;

    JButton yesButton, noButton, historieButton;

    public AdminFrame(AdminService as) {
        super("AdminFrame");

        this.as = as;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                zavrit();
            }
        });

        setSize(1000, 600);

        initComponents();
    }

    private void initComponents() {

        nacistData();

        JPanel hlavniPanel = new JPanel(new GridBagLayout());

        JPanel tlacitkaPanel = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel akcePanel = new JPanel(new GridLayout());

        zpetButton = new JButton("Zavřít");
        zpetButton.setBackground(Color.BLUE);
        zpetButton.addActionListener(e -> {
            zavrit();
        });

        pridatProduktButton = new JButton("Přidat produkt");
        pridatProduktButton.setBackground(Color.gray);
        pridatProduktButton.addActionListener(e -> {

            tlacitkaPanel.removeAll();

            tlacitkaPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.insets = new Insets(5, 5, 5, 5);
            gbc1.anchor = GridBagConstraints.WEST;

            gbc1.gridx = 0;
            gbc1.gridy = 0;
            tlacitkaPanel.add(new JLabel("Název:"), gbc1);

            gbc1.gridx = 1;
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            nazevField = new JTextField(10);
            nazevField.setPreferredSize(new Dimension(200, 30));
            tlacitkaPanel.add(nazevField, gbc1);

            gbc1.gridx = 0;
            gbc1.gridy = 1;
            tlacitkaPanel.add(new JLabel("Cena:"), gbc1);

            gbc1.gridx = 1;
            DecimalFormatSymbols symbol = new DecimalFormatSymbols();
            symbol.setDecimalSeparator('.');
            NumberFormat format = new DecimalFormat("#######0.00", symbol);
            cenaField = new JFormattedTextField(format);
            cenaField.setColumns(10);
            cenaField.setPreferredSize(new Dimension(200, 30));
            tlacitkaPanel.add(cenaField, gbc1);

            gbc1.gridx = 0;
            gbc1.gridy = 3;
            tlacitkaPanel.add(new JLabel("Kategorie:"), gbc1);


            gbc1.gridx = 1;
            kategorieComboBox = new JComboBox<>();
            for (Kategorie kat : kategorie) {
                kategorieComboBox.addItem(kat.getNazev());
            }
            kategorieComboBox.setPreferredSize(new Dimension(200, 30));
            tlacitkaPanel.add(kategorieComboBox, gbc1);

            gbc1.gridx = 0;
            gbc1.gridy = 5;
            gbc1.gridwidth = 3;
            gbc1.fill = GridBagConstraints.CENTER;
            JButton addProduktButton = new JButton("Přidat produkt");
            addProduktButton.addActionListener(e1 -> {
                String nazev = nazevField.getText();
                String cena = cenaField.getText();
                String kategorieBox = (String) kategorieComboBox.getSelectedItem();

                if (!nazev.isEmpty() && !cena.isEmpty() && !kategorieBox.isEmpty()) {

                    double cenaDoub = Double.parseDouble(cena);

                    int katId = -1;
                    for (Kategorie kat : kategorie) {
                        if (kat.getNazev() == kategorieBox) {
                            katId = kat.getId();
                            break;
                        }
                    }

                    Produkt produkt = new Produkt(0, nazev, cenaDoub, katId);
                    boolean vysledek = as.vytvoritProdukt(produkt);

                    if (vysledek) {
                        JOptionPane.showMessageDialog(addProduktButton, "Produkt byl přídán");
                        produktButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(addProduktButton, "Produkt nebyl přídán");
                    }

                } else {
                    JOptionPane.showMessageDialog(addProduktButton, "Jejda, vyskytla se nějaká chyba", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                }

            });
            tlacitkaPanel.add(addProduktButton, gbc1);

            hlavniPanel.revalidate();
            hlavniPanel.repaint();

        });

        pridatKategoriiButton = new JButton("Přidat kategorii");
        pridatKategoriiButton.setBackground(Color.gray);
        pridatKategoriiButton.addActionListener(e -> {

            tlacitkaPanel.removeAll();

            tlacitkaPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.insets = new Insets(5, 5, 5, 5);
            gbc1.anchor = GridBagConstraints.WEST;

            gbc1.gridx = 0;
            gbc1.gridy = 0;
            tlacitkaPanel.add(new JLabel("Název:"), gbc1);

            gbc1.gridx = 1;
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            nazevField = new JTextField(10);
            nazevField.setPreferredSize(new Dimension(200, 30));
            tlacitkaPanel.add(nazevField, gbc1);

            gbc1.gridx = 0;
            gbc1.gridy = 1;
            gbc1.gridwidth = 3;
            gbc1.fill = GridBagConstraints.CENTER;
            JButton addKategoriiButton = new JButton("Přidat kategorii");
            addKategoriiButton.addActionListener(e1 -> {
                String nazev = nazevField.getText();

                if (!nazev.isEmpty()) {

                    Kategorie kategorie = new Kategorie(0, nazev);

                    as.vytvoritKategorii(kategorie);

                    JOptionPane.showMessageDialog(addKategoriiButton, "Kategorie byla přídána");
                    produktButton.doClick();

                } else {
                    JOptionPane.showMessageDialog(addKategoriiButton, "Jejda, vyskytla se nějaká chyba", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                }

            });
            tlacitkaPanel.add(addKategoriiButton, gbc1);

            hlavniPanel.revalidate();
            hlavniPanel.repaint();
        });

        JPanel produktVylepseni = new JPanel(new GridLayout());

        produktButton = new JButton("Produkt");
        produktButton.setBackground(Color.CYAN);
        produktButton.addActionListener(e -> {

            nacistData();

            Map<String, ArrayList<Produkt>> produktyPodleKategorii = new LinkedHashMap<>();

            // Projít všechny produkty a přidat je do odpovídající kategorie
            for (Produkt produkt : produkty) {
                int idKategorie = produkt.getidKategorie();

                String nazevKategorie = "";
                for (Kategorie kategorie2 : kategorie) {
                    if (kategorie2.getId() == idKategorie) {
                        nazevKategorie = kategorie2.getNazev();
                        break;
                    }
                }

                // Pokud kategorie neexistuje v mapě, přidejte ji s prázdným seznamem
                produktyPodleKategorii.putIfAbsent(nazevKategorie, new ArrayList<>());

                // Přidejte produkt do seznamu odpovídající kategorie
                produktyPodleKategorii.get(nazevKategorie).add(produkt);
            }

            maxProduktuVKategorii = 0;
            var entrySet = produktyPodleKategorii.entrySet();// zjistí max počet produktů v kategorii
            for (var entry : entrySet) {
                ArrayList<Produkt> produktyVKategorii = entry.getValue();

                if (produktyVKategorii.size() > maxProduktuVKategorii) {
                    maxProduktuVKategorii = produktyVKategorii.size();
                }
            }

            int pocetKategoriiSProduktami = 0;
            ArrayList<String> kategorieBezProduktu = new ArrayList<>();

            for (ArrayList<Produkt> produktyVKategorii : produktyPodleKategorii.values()) {// zjistí počet kategorii s
                                                                                           // alespon jednim produktem
                boolean existujePlatnyProdukt = false;
                for (Produkt produkt : produktyVKategorii) {
                    if (produkt.getNazev() != null) {
                        existujePlatnyProdukt = true;
                        break;
                    }
                    Kategorie kateg = null;
                    for (Kategorie kat : kategorie) {
                        if (produkt.getidKategorie() == kat.getId()) {
                            kateg = kat;
                            break;
                        }
                    }
                    kategorieBezProduktu.add(kateg.getNazev());
                }
                if (existujePlatnyProdukt) {
                    pocetKategoriiSProduktami++;
                }
            }

            tlacitkaPanel.removeAll();
            tlacitkaPanel.setLayout(new GridLayout(pocetKategoriiSProduktami, maxProduktuVKategorii));
            for (var entry : entrySet) {
                ArrayList<Produkt> produktyVKategorii = entry.getValue();

                for (Produkt produkt : produktyVKategorii) {// vypíše produkty jako tlacitka
                    if (produkt.getNazev() != null) {

                        JButton button = new JButton(produkt.getNazev());
                        button.addActionListener(event -> {

                            tlacitkaPanel.removeAll();

                            tlacitkaPanel.setLayout(new GridBagLayout());

                            GridBagConstraints gbc1 = new GridBagConstraints();
                            gbc1.insets = new Insets(5, 5, 5, 5);
                            gbc1.anchor = GridBagConstraints.WEST;

                            gbc1.gridx = 0;
                            gbc1.gridy = 0;
                            tlacitkaPanel.add(new JLabel("Název:"), gbc1);

                            gbc1.gridx = 1;
                            gbc1.fill = GridBagConstraints.HORIZONTAL;
                            nazevField = new JTextField(10);
                            nazevField.setPreferredSize(new Dimension(200, 30));
                            nazevField.setText(produkt.getNazev());
                            tlacitkaPanel.add(nazevField, gbc1);

                            gbc1.gridx = 0;
                            gbc1.gridy = 1;
                            tlacitkaPanel.add(new JLabel("Cena:"), gbc1);

                            gbc1.gridx = 1;
                            DecimalFormatSymbols symbol = new DecimalFormatSymbols();
                            symbol.setDecimalSeparator('.');
                            NumberFormat format = new DecimalFormat("#######0.00", symbol);
                            cenaField = new JFormattedTextField(format);
                            cenaField.setColumns(10);
                            cenaField.setPreferredSize(new Dimension(200, 30));

                            cenaField.setText(Double.toString(produkt.getCena()));
                            tlacitkaPanel.add(cenaField, gbc1);

                            gbc1.gridx = 0;
                            gbc1.gridy = 3;
                            tlacitkaPanel.add(new JLabel("Kategorie:"), gbc1);

                            gbc1.gridx = 1;
                            kategorieComboBox = new JComboBox<>();
                            for (Kategorie kat : kategorie) {
                                kategorieComboBox.addItem(kat.getNazev());
                            }
                            String nazevKat = "";
                            for (Kategorie kat : kategorie) {
                                if (produkt.getidKategorie() == kat.getId()) {
                                    nazevKat = kat.getNazev();
                                    break;
                                }
                            }
                            kategorieComboBox.setPreferredSize(new Dimension(200, 30));
                            kategorieComboBox.setSelectedItem(nazevKat);
                            tlacitkaPanel.add(kategorieComboBox, gbc1);

                            gbc1.gridx = 0;
                            gbc1.gridy = 5;
                            gbc1.gridwidth = 3;
                            gbc1.fill = GridBagConstraints.CENTER;
                            JButton addProduktButton = new JButton("Upravit produkt");
                            addProduktButton.addActionListener(e1 -> {
                                String nazev = nazevField.getText();
                                String cena = cenaField.getText();
                                String kategorieBox = (String) kategorieComboBox.getSelectedItem();

                                if (!nazev.isEmpty() && !cena.isEmpty() && !kategorieBox.isEmpty()) {

                               
                                    double cenaDoub = Double.parseDouble(cena);

                                    int katId = -1;
                                    for (Kategorie kat : kategorie) {
                                        if (kat.getNazev() == kategorieBox) {
                                            katId = kat.getId();
                                            break;
                                        }
                                    }

                                    produkt.setNazev(nazev);
                                    produkt.setCena(cenaDoub);
                                    produkt.setidKategorie(katId);

                                    boolean vysledekP = as.updateProdukt(produkt);

                                    System.out.println(vysledekP);

                                    if (vysledekP) {
                                        JOptionPane.showMessageDialog(addProduktButton, "Produkt byl upraven");
                                        produktButton.doClick();
                                    } else {
                                        JOptionPane.showMessageDialog(addProduktButton, "Produkt nebyl upraven");
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(addProduktButton, "Jejda, vyskytla se nějaká chyba",
                                            "Chyba", JOptionPane.ERROR_MESSAGE);
                                }

                            });
                            tlacitkaPanel.add(addProduktButton, gbc1);

                            hlavniPanel.revalidate();
                            hlavniPanel.repaint();

                        });
                        tlacitkaPanel.add(button);
                    } else {
                        bezLabelu = true;
                    }
                }

                if (produktyVKategorii.size() < maxProduktuVKategorii && !bezLabelu) {// pokud je produktu v kategorii
                                                                                      // méně než v jiné
                    // kategorii vypíše prázdný label
                    for (int i = 0; i < maxProduktuVKategorii - produktyVKategorii.size(); i++) {
                        tlacitkaPanel.add(new JLabel());
                    }
                }
                bezLabelu = false;

            }

            hlavniPanel.revalidate();
            hlavniPanel.repaint();

        });

        objednavkyButton = new JButton("Objednavky");
        objednavkyButton.setBackground(Color.CYAN);
        objednavkyButton.addActionListener(e -> {
       
            hlavniPanel.revalidate();
            hlavniPanel.repaint();
        });

        produktButton.doClick();

        Font fontText = new Font("Arial", Font.BOLD, 30);
        Dimension aktualniVelikost = pridatProduktButton.getPreferredSize();
        Dimension velikost = new Dimension(aktualniVelikost.height, 20);
        zpetButton.setPreferredSize(velikost);
        zpetButton.setFont(fontText);
        pridatProduktButton.setPreferredSize(velikost);
        pridatProduktButton.setFont(fontText);
        pridatKategoriiButton.setPreferredSize(velikost);
        pridatKategoriiButton.setFont(fontText);

        produktButton.setPreferredSize(velikost);
        produktButton.setFont(fontText);
        objednavkyButton.setPreferredSize(velikost);
        objednavkyButton.setFont(fontText);

        akcePanel.add(zpetButton);
        akcePanel.add(pridatProduktButton);
        akcePanel.add(pridatKategoriiButton);

        produktVylepseni.add(produktButton);
        produktVylepseni.add(objednavkyButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        hlavniPanel.add(tlacitkaPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.05;
        hlavniPanel.add(akcePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        hlavniPanel.add(produktVylepseni, gbc);

        add(hlavniPanel);
    }

    private void nacistData() {
        this.produkty = as.ziskatProdukty();
        this.kategorie = as.ziskatKategorie();
    }

    private void zavrit() {
        as.odpojitKlienta();
        dispose();
    }
}