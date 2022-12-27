import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class MyWindow extends JFrame {

    private static final long serialVersionUID = -4939544011287453046L;

    private JCheckBox chkHisto = new JCheckBox("Histogramme");
    
    private JCheckBox chkMethod1 = new JCheckBox("Eclairage");
   
    private JCheckBox chkMethod2 = new JCheckBox("Assombrissement");
   
    private JCheckBox chkMethod3 = new JCheckBox("Contraste");

    private JButton btnConfirmLaunch = new JButton("Appuyer pour lancer");

    private JButton btnOpenFileChooser = new JButton("Appuyer pour choisir un fichier à traiter");

    private JTextArea promptInfos = new JTextArea(" >>> Lancement du programme ");

    private String getselectedfile; // contient le chemin absolue vers le fichier


/* 

 * #############################################################
 * #        CONSTRUCTEUR DE LA CLASSE : DE LA FENETRE          #
 * #############################################################
 
*/

    public MyWindow() {

        super( "Traitement Image Noir & Blanc");             // titre de la fenêtre
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );  // paramètre la fenêtre pour que quand celle-ci se ferme, le programme s'arrête
        this.setSize( 600, 400 ); // taille par défaut de la fenêtre
        this.setLocationRelativeTo( null ); // centre la fenêtre par rapport à la résolution de l'écran

        JPanel contentPane = (JPanel) this.getContentPane(); //zone de la windows privée de la barre de menu
        contentPane.setLayout( new FlowLayout()); // met les éléments de la fenêtre côte à côte et les ajuste en fonction de ce qu'il contient
        // si le setLayout est à null alors c'est au programmeur de placer les layouts lui même.
    
        contentPane.add(chkHisto);
        
        contentPane.add(chkMethod1);

        contentPane.add(chkMethod2);

        contentPane.add(chkMethod3);

        this.promptInfos.setPreferredSize(null);
        contentPane.add(promptInfos);

        contentPane.add(btnConfirmLaunch);
        btnConfirmLaunch.addActionListener(new ConfirmLaunch());

        contentPane.add(btnOpenFileChooser);
        btnOpenFileChooser.addActionListener(new OpenFileChooser());

        this.setVisible( true ); // affichage de la fenêtre


    }
    

    
/* 

 * #############################################################
 * #             MAIN LANCEMENT DE LA FENETRE                 #
 * #############################################################
 
*/

    public static void main(String[] args) throws Exception {
   
        // style de l'interface graphique.
        UIManager.setLookAndFeel( new NimbusLookAndFeel() );
        
        // lancement de la fenêtre.
        MyWindow myWindow = new MyWindow();
        
    }

/* 

 * #############################################################
 * #                  FENETRE DU FILECHOOSER                   #
 * #############################################################
 
*/
    public void openFileChosser(){

        // on créer l'objet FileChooser
        JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        // Ouvrez le fichier vaut APPROVE_OPTION si fichier bien séléctionner
        int res = choose.showOpenDialog(null);

        // Enregistrez le fichier en créant un objet de type file
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = choose.getSelectedFile();
            this.getselectedfile = file.getAbsolutePath();
        } 
        else{
            this.getselectedfile = null;  //erreur aucun fichier sélectionner ou autre erreur
        }
         
    }

/* 

 * #############################################################
 * #         VERIFICATION DE L'EXTENSION DU FICHIER            #
 * #############################################################
 
*/
    public boolean isImgPng(){

        String [] path_to_tab = this.getselectedfile.split("/");

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


/* 

 * #############################################################
 * #       CLASSES INTERNES ACTIONS DE TOUS LES LAYOUTS        #
 * #############################################################
 
*/

    private class OpenFileChooser implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){

            MyWindow.this.openFileChosser();
        }
    }



    private class ConfirmLaunch implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){

            System.out.println(MyWindow.this.getselectedfile);
            System.out.println(MyWindow.this.isImgPng());
        }
    }

}