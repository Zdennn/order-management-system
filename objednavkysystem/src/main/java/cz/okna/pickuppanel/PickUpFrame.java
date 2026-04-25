package cz.okna.pickuppanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import cz.common.model.ProduktObjednavka;

public class PickUpFrame extends JFrame {

    private PickUpService service;

    private Map<Integer, ArrayList<ProduktObjednavka>> objednavky1;// Připravuje se
    private Map<Integer, ArrayList<ProduktObjednavka>> objednavky2; // Čeká na vyzvednutí

    protected void nacti(Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> objednavky) {
        this.objednavky1 = objednavky.get(1);
        this.objednavky2 = objednavky.get(2);

        getContentPane().removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    public PickUpFrame(PickUpService service) {
        super("PickUpFrame");

        this.service = service;
        nacistData();

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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel panelPripravujeSe = new JPanel();
        panelPripravujeSe.setLayout(new BoxLayout(panelPripravujeSe, BoxLayout.X_AXIS));
        panelPripravujeSe.add(new JScrollPane(createObjednavkyPanel(objednavky1, false)));

        JLabel labelPripravujeSe = new JLabel("Objednávky připravuje se");
        panelPripravujeSe.add(labelPripravujeSe, BorderLayout.NORTH);

        JPanel panelCekaNaVyzvednuti = new JPanel();
        panelCekaNaVyzvednuti.setLayout(new BoxLayout(panelCekaNaVyzvednuti, BoxLayout.X_AXIS));
        panelCekaNaVyzvednuti.add(new JScrollPane(createObjednavkyPanel(objednavky2, true)));

        JLabel labelCekaNaVyzvednuti = new JLabel("Objednávky čeká na vyzvednutí");
        panelCekaNaVyzvednuti.add(labelCekaNaVyzvednuti, BorderLayout.NORTH);

        mainPanel.add(panelPripravujeSe, BorderLayout.NORTH);
        mainPanel.add(panelCekaNaVyzvednuti, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createObjednavkyPanel(Map<Integer, ArrayList<ProduktObjednavka>> objednavky, boolean sButtonem) {
        JPanel objednavkyPanel = new JPanel();
        objednavkyPanel.setLayout(new BoxLayout(objednavkyPanel, BoxLayout.Y_AXIS));

        objednavkyPanel.setBackground(new Color(240, 240, 240));
        objednavkyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (Map.Entry<Integer, ArrayList<ProduktObjednavka>> entry : objednavky.entrySet()) {
            int objednavkaId = entry.getKey();

            JPanel objednavkaPanelItem = new JPanel();
            objednavkaPanelItem.setLayout(new BoxLayout(objednavkaPanelItem, BoxLayout.Y_AXIS));

            objednavkaPanelItem.setBackground(new Color(255, 255, 255));

            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));

            cardPanel.setBackground(new Color(220, 220, 220));

            cardPanel.add(new JLabel("Objednávka ID: " + objednavkaId));

            if (sButtonem) {
                JButton button = new JButton("Vyzvednout");
                button.addActionListener(e -> zmenitStavObjednavky(objednavkaId));
                cardPanel.add(button);
            }

            objednavkaPanelItem.add(cardPanel);

            objednavkyPanel.add(objednavkaPanelItem);

            objednavkyPanel.add(Box.createVerticalStrut(10));
        }

        return objednavkyPanel;
    }

    private void zmenitStavObjednavky(int objednavkaId) {
        boolean vysledek = service.zmenitStavObjednavky(objednavkaId, 3);

        if (vysledek) {
            JOptionPane.showMessageDialog(this, "Stav objednávky byl změněn na 'vyzvednuto'.");

            nacistData();

            getContentPane().removeAll();
            initComponents();
            revalidate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Došlo k chybě při změně stavu objednávky.");
        }
    }

    private void nacistData() {
        this.objednavky1 = service.ziskatObjednavky(1);
        this.objednavky2 = service.ziskatObjednavky(2);
    }

    private void zavrit() {
        service.odpojitKlienta();
        dispose();
    }

}
