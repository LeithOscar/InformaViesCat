

package ioc.informaviescat;

import com.formdev.flatlaf.FlatLightLaf;
import ioc.informaviescat.Vista.MainScreen;
import ioc.informaviescat.Vista.guiLogin;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

/**
 *
 * @author Pau Cors Bardolet
 */
public class InformaViesCAT {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        
        //Es crea l'objecte ImageIcon amb la icona del programa
        ImageIcon img = new ImageIcon("ICONS/icon.png");
        
        //Es crea l'objecta finestra de login
        guiLogin guilogin = new guiLogin();
        //Es fica en posició centrada a la pantalla
        guilogin.setLocationRelativeTo(null);
        //Se li assigna la icona
        guilogin.setIconImage(img.getImage());
        //Es fa visible
        guilogin.setVisible(true);
        
        
        MainScreen gui = new MainScreen();
        gui.setSessionType(0);
        gui.setVisible(true);
        
        MainScreen gui2 = new MainScreen();
        gui2.setSessionType(1);
        gui2.setVisible(true);
    }
}
