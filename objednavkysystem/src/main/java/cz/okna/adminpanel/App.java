package cz.okna.adminpanel;

import java.rmi.RemoteException;

public class App {
    public static void main(String[] args) {
        try {
            AdminService as;
            as = new AdminService();
            AdminFrame af = new AdminFrame(as);
            af.setVisible(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
