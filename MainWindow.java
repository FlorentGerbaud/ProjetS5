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
import java.math.BigInteger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.awt.Frame;
/**
 * @brief : Depuis cette classe sera appelé les 4 grandes fonctionnalités du projet :
 * - Traitement d'images en niveau de Gris -> application de filtres
 * - Traitement d'images en niveau de Couleur -> application de filtres
 * - Traitement d'images en niveau de Gris -> Matrice de Convolution
 * - Traitement d'images en niveau de Couleur -> Matrice de Convolution
 */

public class MainWindow extends JFrame {

    private JButton btnNBFiltre = new JButton("Image Noir & Blanc - Filtres");

    private JButton btnNBConv = new JButton("Image Noir & Blanc - Convolution");

    private JButton btnCFiltre = new JButton("Image Couleur - Filtres");

    private JButton btnCConv = new JButton("Image Couleur - Convolution");

    public boolean EXIST = false;



    public MainWindow(){

        this.setTitle("Main Menu Project");                        // titre de la fenêtre
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );   // paramètre la fenêtre pour que quand celle-ci se ferme, le programme s'arrête
        this.setSize( 300, 300 );                     // taille par défaut de la fenêtre
        this.setLocationRelativeTo( null );                       // centre la fenêtre par rapport à la résolution de l'écran

        JPanel contentPane = (JPanel) this.getContentPane();        //zone de la windows privée de la barre de menu
        contentPane.setLayout( new FlowLayout());                   // met les éléments de la fenêtre côte à côte et les ajuste en fonction de ce qu'il contient

        contentPane.add(btnNBFiltre);
        btnNBFiltre.addActionListener(new OpenNBFiltre());

        contentPane.add(btnNBConv);

        contentPane.add(btnCFiltre);
        btnCFiltre.addActionListener(new OpenCFiltre());

        contentPane.add(btnCConv);

        this.setVisible(true);                                    // visible dès le lancement de la fenêtre


    }


/**
 * 
 * @param not_appear (false ou true)
 * @brief Ce constructeur par défaut est primordial. Il permet de ne pas initialiser les attributs 
 * de la classe parente ici MainWindow dans les classes héritantes. Ainsi les boutons de la fenêtre
 * principal n'apparaitront pas dans les sous-fenêtres. 
 * En fait, l'objectif de classe MainWindow est d'hériter uniquement des méthodes et non pas des attributs
 * d'après quelques recherches google, on appelle ce type de classe des "Mixin Classes" mais leur implémentation
 * est délicate et ne convient pas ici au interface graphique. Nous avons donc "bricoler" cette solution 
 * qui suffit emplement puisque, initialisé à null, les boutons n'apparaissent pas. 
 */

    public MainWindow(boolean cst){

        btnCConv = null;
        btnCFiltre = null;
        btnNBConv = null;
        btnNBFiltre = null;
        EXIST = true;
        this.setVisible(false);
        this.closeMyWindow();

    }


/* 

 * ##################################################################################################################
 * #                         METHODES COMMUNES A TOUTES LES FENETRES ET SOUS FENETRES                               #
 * ##################################################################################################################
 
*/

/* 

 * #############################################################
 * #               FENETRE DU DIRECTORY CHOOSER                #
 * #############################################################
 
*/


    public String openDirectoryChooser(){

        JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int res = choose.showOpenDialog(null);

        String selectedPathDirectory = null;

        if(res == JFileChooser.APPROVE_OPTION){
            File directory = choose.getSelectedFile();
            selectedPathDirectory = directory.toString();
            return selectedPathDirectory;
        }
        else{

            selectedPathDirectory = null;
            return selectedPathDirectory;
        }
    }
/* 

* #############################################################
* #                  FENETRE DU FILECHOOSER                   #
* #############################################################

*/
    public String openFileChosser(){

        // on créer l'objet FileChooser
        JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        // Ouvrez le fichier vaut APPROVE_OPTION si fichier bien séléctionner
        int res = choose.showOpenDialog(null);

        String selectedPathFile = null;
        // Enregistrez le fichier en créant un objet de type file
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = choose.getSelectedFile();
            selectedPathFile = file.getAbsolutePath();
            return selectedPathFile;

        } 
        else{
            selectedPathFile = null;  //erreur aucun fichier sélectionner ou autre erreur
            return selectedPathFile;
        }
        
    }

/* 

* #############################################################
* #         VERIFICATION DE L'EXTENSION DU FICHIER            #
* #############################################################

*/

/**
 * 
 * @return (String) : l'extension du fichier sélectionné.
 * @todo vérifier si la chaîne est vide sinon renvoyer une erreur
 */
    public boolean isImgPng(String selected_file){

        String [] path_to_tab = selected_file.split("/");

        String name_file = path_to_tab[path_to_tab.length-1];

        String [] name_and_extension = name_file.split("\\.");

        String extension = name_and_extension[name_and_extension.length-1];

        if ((extension.equals("png")) || (extension.equals("PNG")) ){

            return true;
        }
        else{

            return false;
        }

    }

    public String getNameFileToString(String selected_file){

        String [] path_to_tab = selected_file.split("/");

        String name_file = path_to_tab[path_to_tab.length-1];

        String [] name_and_extension = name_file.split("\\.");

        String extension = name_and_extension[name_and_extension.length-1];

        if ((extension.equals("png")) || (extension.equals("PNG")) ){

            return name_file;
        }
        else{

            return null;
        }

    }

/* 

* #############################################################
* #         FERMETURE DE LA FENETRE ACTUELLE                  #
* #############################################################

*/



    public void closeMyWindow(){

        this.dispose();

    }




/* 

 * ##################################################################################################################
 * #               ACTIONS DES BOUTONS PRINCIPAUX -> OUVERTURE DES DIFFERENTES FENETRES DE TRAITEMENT               #
 * ##################################################################################################################
 
*/
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
            

                if(EXIST == false){
                    MyWindow t = new MyWindow();
                    System.out.println("hein");
                }
                else{
                    System.out.println("existe deja");
                }

        
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

            MyWindowForColor col_filtre = new MyWindowForColor();

            col_filtre.setVisible(true);

            //MainWindow.this.closeMyWindow();

        }
        
    }

    private class OpenCConv implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){


        }
        
    }
    
}
