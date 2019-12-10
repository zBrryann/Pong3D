package pong3dsp;
import org.pong3dsp.vista.Pong3DSPVista;
import com.sun.j3d.utils.applet.MainFrame;
import javax.swing.JOptionPane;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
public class Main {   
    /** Creates a new instance of Main */
    public Main() {
           
    }
    public static void main(String[] args) {
        // TODO code application logic here
        int op=2;      
           MainFrame mf = new MainFrame(new Pong3DSPVista(), 300,300);
        
    }
    
}
