import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
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

    private JTextArea promptInfos = new JTextArea(" >>> Lancement de la fenêtre GUI \n",40,40);

    private String getselectedfile = null; // contient le chemin absolue vers le fichier

    private String name_file = null; // nom du fichier


/* 

 * #############################################################
 * #        CONSTRUCTEUR DE LA CLASSE : DE LA FENETRE          #
 * #############################################################
 
*/

    public MyWindow() {

        super( "Traitement Image Noir & Blanc");             // titre de la fenêtre
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );  // paramètre la fenêtre pour que quand celle-ci se ferme, le programme s'arrête
        this.setSize( 800, 600 ); // taille par défaut de la fenêtre
        this.setLocationRelativeTo( null ); // centre la fenêtre par rapport à la résolution de l'écran

        JPanel contentPane = (JPanel) this.getContentPane(); //zone de la windows privée de la barre de menu
        contentPane.setLayout( new FlowLayout()); // met les éléments de la fenêtre côte à côte et les ajuste en fonction de ce qu'il contient
        // si le setLayout est à null alors c'est au programmeur de placer les layouts lui même.
        //FlowLayout.CENTER,20,20
        contentPane.add(chkHisto);
        
        contentPane.add(chkMethod1);

        contentPane.add(chkMethod2);

        contentPane.add(chkMethod3);



        contentPane.add(btnConfirmLaunch);
        btnConfirmLaunch.addActionListener(new ConfirmLaunch());

        contentPane.add(btnOpenFileChooser);
        btnOpenFileChooser.addActionListener(new OpenFileChooser());


        contentPane.add(promptInfos);
        //barre de scroll pour la texte field
        JScrollPane scroll = new JScrollPane(this.promptInfos,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scroll);



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
 * #                        TOOLBAR                            #
 * #############################################################
 
*/

    private JToolBar createToolBar(){

        JToolBar toolBar = new JToolBar();

        toolBar.add(this.btnConfirmLaunch);

        toolBar.add(this.btnOpenFileChooser);

        return toolBar;
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

    /**
     * 
     * @return (String) : l'extension du fichier sélectionné.
     * @todo vérifier si la chaîne est vide sinon renvoyer une erreur
     */
    public boolean isImgPng(){

        String [] path_to_tab = this.getselectedfile.split("/");

        String name_file = path_to_tab[path_to_tab.length-1];

        String [] name_and_extension = name_file.split("\\.");

        String extension = name_and_extension[name_and_extension.length-1];

        if ((extension.equals("png")) || (extension.equals("PNG")) ){

            this.name_file = name_file;

            return true;
        }
        else{
            this.name_file = name_file;
            return false;
        }

    }

    public String getName_File(){

        return this.name_file;
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

            if(MyWindow.this.getselectedfile == null)
            {
                MyWindow.this.promptInfos.append(" >>> No file selected "+"\n");
                MyWindow.this.promptInfos.append(" >>> Please select a file"+"\n");
            }
            else{


            
                if(isImgPng() == true){ //on peut exécuter les méthodes de traitements

                    //création de l'objet image
                    TraitementImageNoirBlanc obj = new TraitementImageNoirBlanc(MyWindow.this.getselectedfile);


                    MyWindow.this.promptInfos.append(" >>> Correct File : "+MyWindow.this.getName_File()+"\n");
                    MyWindow.this.promptInfos.append(" >>> BEGIN OF PROCESS "+"\n");

                    if(MyWindow.this.chkHisto.isSelected()){


                        MyWindow.this.promptInfos.append(" >>> Process Histogramme was successful"+"\n");

                    }

                    if(MyWindow.this.chkMethod1.isSelected()){

                        obj.eclairage();
                        obj.saveImage("eclairage.png");

                        MyWindow.this.promptInfos.append(" >>> Process Eclairage was successful"+"\n");
                    }

                    if(MyWindow.this.chkMethod2.isSelected()){

                        obj.assombrissement();
                        obj.saveImage("assombrissement.png");

                        MyWindow.this.promptInfos.append(" >>> Process Assombrissement was successful"+"\n");

                        
                    }

                    if(MyWindow.this.chkMethod3.isSelected()){

                        obj.contraste();
                        obj.saveImage("contraste.png");

                        MyWindow.this.promptInfos.append(" >>> Process Contraste was successful"+"\n");
                        
                    }
                }
                else{ // on ne peut rien faire avec l'image, on redonne la main
                    MyWindow.this.promptInfos.append(" >>> Bad File : "+MyWindow.this.getName_File()+" is not .png"+"\n");
                    MyWindow.this.promptInfos.append(" >>> Select Another File .png "+"\n");
                }
                MyWindow.this.promptInfos.append(" >>> END OF PROCESS"+"\n");
            }
        }
    }
}