package cz.okna.pickuppanel;

import cz.okna.kitchenpanel.KitchenFrame;
import cz.okna.kitchenpanel.KitchenService;

public class App 
{
    public static void main( String[] args )
    {
        try {
            PickUpService service = new PickUpService();
            PickUpFrame frame = new PickUpFrame(service);
            service.setFrame(frame);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
