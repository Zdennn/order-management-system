package cz.okna.kitchenpanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cz.common.model.ProduktObjednavka;

public class KitchenFrame extends JFrame {

    KitchenService service;

    private Map<Integer, ArrayList<ProduktObjednavka>> objednavky1;// Připravuje se

    private JTable tablePripravujeSe;
    private JTable tableCekaNaVyzvednuti;

    protected void nacti(Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> objednavky){
        this.objednavky1 = objednavky.get(1);

        getContentPane().removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    public KitchenFrame(KitchenService service) {
        super("KitchenFrame");

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
        setLayout(new BorderLayout());

        String[] columnNames = { "ID Objednávky", "Produkt", "Cena", "Počet" };
        DefaultTableModel modelPripravujeSe = new DefaultTableModel(columnNames, 0);
        tablePripravujeSe = new JTable(modelPripravujeSe);

        naplnitTabulku(tablePripravujeSe, objednavky1);

        JPanel panelTabulky = new JPanel();
        panelTabulky.setLayout(new BoxLayout(panelTabulky, BoxLayout.Y_AXIS));
        panelTabulky.add(new JScrollPane(tablePripravujeSe));

        add(panelTabulky, BorderLayout.CENTER);

        JButton btnVydatObjednavku = new JButton("Dokončit objednávku");
        btnVydatObjednavku.addActionListener(this::odebratObjednavku);
        add(btnVydatObjednavku, BorderLayout.SOUTH);
    }

    private void naplnitTabulku(JTable table, Map<Integer, ArrayList<ProduktObjednavka>> objednavky) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 

        for (Map.Entry<Integer, ArrayList<ProduktObjednavka>> entry : objednavky.entrySet()) {
            Integer idObjednavky = entry.getKey();
            ArrayList<ProduktObjednavka> produktObjednavkaList = entry.getValue();

            for (ProduktObjednavka produktObjednavka : produktObjednavkaList) {
                Object[] row = {
                    idObjednavky,
                    produktObjednavka.getNazev(),
                    produktObjednavka.getCena(),
                    produktObjednavka.getPocet()
                };
                model.addRow(row);
            }
        }
    }

    private void odebratObjednavku(ActionEvent event) {
        int rowPripravujeSe = tablePripravujeSe.getSelectedRow();

        if (rowPripravujeSe != -1) {
            int idObjednavky = (int) tablePripravujeSe.getValueAt(rowPripravujeSe, 0);

            if(service.zmenitStavObjednavky(idObjednavky, 2)){
                JOptionPane.showMessageDialog(this, "Stav objednávky byl změněn na 'dokončeno'.");
    
                nacistData();
        
                getContentPane().removeAll();
                initComponents();
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Došlo k chybě při změně stavu objednávky.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vyberte objednávku k odebrání.");
        }
    }

    private void nacistData() {
        this.objednavky1 = service.ziskatObjednavky(1);
    }

    private void zavrit() {
        service.odpojitKlienta();
        dispose();
    }

}
