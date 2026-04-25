package cz.okna.kitchenpanel;

public class App 
{
    public static void main( String[] args )
    {
        try {
            KitchenService service = new KitchenService();
            KitchenFrame frame = new KitchenFrame(service);
            service.setFrame(frame);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
