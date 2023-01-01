import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.sound.midi.MidiEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;


/**
 * @brief : Depuis cette classe sera appelé les 4 grandes fonctionnalités du projet :
 * - Traitement d'images en niveau de Gris -> application de filtres
 * - Traitement d'images en niveau de Couleur -> application de filtres
 * - Traitement d'images en niveau de Gris -> Matrice de Convolution
 * - Traitement d'images en niveau de Couleur -> Matrice de Convolution
 */

public class MainWindow extends JFrame
{

    private JButton btnNBFiltre = new JButton("Image Noir & Blanc - Filtres");

    private JButton btnNBConv = new JButton("Image Noir & Blanc - Convolution");

    private JButton btnCFiltre = new JButton("Image Couleur - Filtres");

    private JButton btnCConv = new JButton("Image Couleur - Convolution");



    public MainWindow(){

        super( "Main Menu Project");                          // titre de la fenêtre
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );   // paramètre la fenêtre pour que quand celle-ci se ferme, le programme s'arrête
        this.setSize( 300, 300 );                     // taille par défaut de la fenêtre
        this.setLocationRelativeTo( null );                       // centre la fenêtre par rapport à la résolution de l'écran

        JPanel contentPane = (JPanel) this.getContentPane();        //zone de la windows privée de la barre de menu
        contentPane.setLayout( new FlowLayout());                   // met les éléments de la fenêtre côte à côte et les ajuste en fonction de ce qu'il contient

        contentPane.add(btnNBFiltre);
        contentPane.add(btnNBConv);
        contentPane.add(btnCFiltre);
        contentPane.add(btnCConv);













        this.setVisible(true);                                    // visible dès le lancement de la fenêtre


    }

    public void closeMyWindow(){

        this.dispose();

    }


    public static void main(String args[]) throws Exception{

        try{
            UIManager.setLookAndFeel( new NimbusLookAndFeel() );
        }
        catch(Exception e){  // si jamais le style de l'interface n'est pas trouvé (impossible car inclue depuis java 1.7 d'après la doc)
            System.err.println("Interface non-trouvée");
            return;
        }
        MainWindow main = new MainWindow();

    }

    private class OpenNBFiltre implements ActionListener{


        @Override
        public void actionPerformed(ActionEvent event){


        }
    }

    private class OpenNBConv implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){


        }
        
    }


    private class OpenCFiltre implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){


        }
        
    }

    private class OpenCConv implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){


        }
        
    }
    
}
