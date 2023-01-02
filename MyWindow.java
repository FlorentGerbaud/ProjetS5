import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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



/* 

 * ###################################################################################################
 * #                                          FENETRE PRINCIPALE                                     #
 * ###################################################################################################
 
*/



public class MyWindow extends MainWindow{

    private static final long serialVersionUID = -4939544011287453046L;

    private JCheckBox chkHisto = new JCheckBox("Histogramme");
    
    private JCheckBox chkMethod1 = new JCheckBox("Eclairage");
   
    private JCheckBox chkMethod2 = new JCheckBox("Assombrissement");
   
    private JCheckBox chkMethod3 = new JCheckBox("Contraste");

    private JCheckBox chkFromColorToGray = new JCheckBox("Color to Gray");


    private JButton btnConfirmLaunch = new JButton("Appuyer pour lancer");

    private JButton btnOpenFileChooser = new JButton("Appuyer pour choisir un fichier à traiter");

    private JButton btnOpenDirectoryChooser = new JButton("Appuyer pour choisir le dossier de sauvegarde");


    private JTextArea promptInfos = new JTextArea(" >>> Lancement de la fenêtre GUI \n",40,40);


    private String selectedPathFile = null; // contient le chemin absolue vers le fichier

    private String name_file = null; // nom du fichier

    private String selectedPathDirectory = null;




/* 

 * #############################################################
 * #        CONSTRUCTEUR DE LA CLASSE : DE LA FENETRE          #
 * #############################################################
 
*/

    public MyWindow() {
    

        super(false);

        


        this.setTitle("Filtre Image Niveau Gris");// titre de la fenêtre
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );  // paramètre la fenêtre pour que quand celle-ci se ferme, le programme s'arrête
        this.setSize( 800, 800 ); // taille par défaut de la fenêtre
        this.setLocationRelativeTo( null ); // centre la fenêtre par rapport à la résolution de l'écran

        JPanel contentPane = (JPanel) this.getContentPane(); //zone de la windows privée de la barre de menu

        contentPane.setLayout( new FlowLayout()); // met les éléments de la fenêtre côte à côte et les ajuste en fonction de ce qu'il contient

        contentPane.add(chkHisto);
        
        contentPane.add(chkMethod1);

        contentPane.add(chkMethod2);

        contentPane.add(chkMethod3);

        contentPane.add(chkFromColorToGray);



        contentPane.add(btnConfirmLaunch);
        btnConfirmLaunch.addActionListener(new ConfirmLaunch());

        contentPane.add(btnOpenFileChooser);
        btnOpenFileChooser.addActionListener(new OpenFileChooser());

        contentPane.add(btnOpenDirectoryChooser);
        btnOpenDirectoryChooser.addActionListener(new OpenDirectoryChooser());

        this.promptInfos.setEditable(false); // impossible de modifier le prompt
        contentPane.add(promptInfos);
        //barre de scroll pour la texte field (indiquant les entrées sorties du programme)
        JScrollPane scroll = new JScrollPane(this.promptInfos,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scroll);


        this.setVisible( true ); // affichage de la fenêtre
        


    }

/* 

 * #########################################################################################
 * #                                  SETTERS ET GETTERS                                   #
 * #########################################################################################
 
*/

    public String getName_File(){

        return this.name_file;
    }

    public void setNameFile(String name_file){
        this.name_file = name_file;
    }

    public void setPathDirectory(String path){

        this.selectedPathDirectory = path;
    }

    public void setPathFile(String path){

        this.selectedPathFile = path;
    }



/* 

 * ###############################################################################################################
 * #                                 ECOUTEURS D'EVENEMENTS                                                      #
 * ###############################################################################################################
 
*/


/* 

 * #########################################################################################
 * #                 OUVERTURE BOITE DE DIALOGUE EXPORATEUR DE FICHIER                     #
 * #########################################################################################
 
*/

    private class OpenFileChooser extends MainWindow implements ActionListener {
        
        public OpenFileChooser(){
            super(false);
        }

        @Override
        public void actionPerformed(ActionEvent event){

            String name = super.openFileChosser();
            setPathFile(name);
            if(name != null)
                MyWindow.this.promptInfos.append(" >>> A file is actually selected \n");
        }

    } //FIN DE LA CLASSE INTERNE

/* 

 * #########################################################################################
 * #            OUVERTURE BOITE DE DIALOGUE OUVERTURE EXPLORATEUR DE DOSSIER               #
 * #########################################################################################
 
*/

    private class OpenDirectoryChooser extends MainWindow implements ActionListener{

        public OpenDirectoryChooser(){
            super(false);
        }

        @Override
        public void actionPerformed(ActionEvent event){

            String name = super.openDirectoryChooser();
            setPathDirectory(name);
            if(name != null)
                MyWindow.this.promptInfos.append(" >>> A directory is actually selected \n");
        }

    }//FIN DE LA CLASSE INTERNE

/* 

 * #########################################################################################
 * #                         LANCEMENT DES TRAITEMENTS SELECTIONNES                        #
 * #########################################################################################
 
*/



    private class ConfirmLaunch  extends MainWindow implements ActionListener{

        public ConfirmLaunch(){
            super(false);
        }

        @Override
        public void actionPerformed(ActionEvent event){

            boolean done = false;
            // si aucun dossier de sauvegarde , on arrête directement
            if(MyWindow.this.selectedPathDirectory == null){

                MyWindow.this.promptInfos.append(" >>> No directory selected "+"\n");
                MyWindow.this.promptInfos.append(" >>> Please select a directory "+"\n");
            }
            else{

                // si aucun fichier sélectionner on arrête tout
                if(MyWindow.this.selectedPathFile == null){
                    MyWindow.this.promptInfos.append(" >>> No file selected "+"\n");
                    MyWindow.this.promptInfos.append(" >>> Please select a file"+"\n");
                }
                else{

                    String name_file = super.getNameFileToString(MyWindow.this.selectedPathFile); //on récupère le nom du fichier avec extension
                    MyWindow.this.setNameFile(name_file); // on met à jour l'attribut

                    if(super.isImgPng(MyWindow.this.selectedPathFile) == true){ //on peut exécuter les méthodes de traitements

                        //création de l'objet image
                        TraitementImageNoirBlanc obj = new TraitementImageNoirBlanc(MyWindow.this.selectedPathFile);

                        if(obj.isColor() == false){ // l'image est en noir et blanc

                            MyWindow.this.promptInfos.append(" >>> Select Image is Gray -> All Processes are supported except ColoToGray"+"\n");
                            MyWindow.this.promptInfos.append(" >>> Correct File : "+MyWindow.this.getName_File()+"\n");
                            MyWindow.this.promptInfos.append(" >>> BEGIN OF PROCESS "+"\n");

                            if(MyWindow.this.chkHisto.isSelected()){

                                try{
                                    obj.barPlotToFile(selectedPathDirectory+"/histogramme.txt");
                                    MyWindow.this.promptInfos.append(" >>> Process Histogramme was successful"+"\n");
                                }
                                catch(FileNotFoundException e){
                                    System.out.println("Error while creating file in histogramme");
                                }
                                done = true;

                            }

                            if(MyWindow.this.chkMethod1.isSelected()){

                                obj.eclairage();
                                obj.saveImage(MyWindow.this.selectedPathDirectory+"/eclairage_"+MyWindow.this.name_file);

                                MyWindow.this.promptInfos.append(" >>> Process Eclairage was successful"+"\n");
                                done = true;
                            }

                            if(MyWindow.this.chkMethod2.isSelected()){

                                obj.assombrissement();
                                obj.saveImage(MyWindow.this.selectedPathDirectory+"/assombrissement_"+MyWindow.this.name_file);

                                MyWindow.this.promptInfos.append(" >>> Process Assombrissement was successful"+"\n");
                                done = true;

                                
                            }

                            if(MyWindow.this.chkMethod3.isSelected()){

                                obj.contraste();
                                obj.saveImage(MyWindow.this.selectedPathDirectory+"/contraste_"+MyWindow.this.name_file);

                                MyWindow.this.promptInfos.append(" >>> Process ColorToGray was successful"+"\n");
                                done = true;
                                
                            }
                        }
                        else{ // l'image est en couleur donc 1 seul traitement dispo

                            if(MyWindow.this.chkFromColorToGray.isSelected()){

                                MyWindow.this.promptInfos.append(" >>> Select Image is in Color -> Only ColorToGray is supported "+"\n");


                                obj.fromColorToGray();
                                obj.saveImage(MyWindow.this.selectedPathDirectory+"/noir&blanc_"+MyWindow.this.name_file);

                                MyWindow.this.promptInfos.append(" >>> Process ColorTogray was successful"+"\n");
                                done = true;
                            }
                        }
                    }
                    else{ // on ne peut rien faire avec l'image, on redonne la main
                        MyWindow.this.promptInfos.append(" >>> Bad File : "+MyWindow.this.getName_File()+" is not .png"+"\n");
                        MyWindow.this.promptInfos.append(" >>> Select Another File .png "+"\n");
                    }
                    if(done == false){
                        MyWindow.this.promptInfos.append(" >>> No process have been performed "+"\n");
                    }
                    MyWindow.this.promptInfos.append(" >>> END OF PROCESS"+"\n");
                    MyWindow.this.promptInfos.append("\n");

                }   
            }
        }
    } //FIN DE LA CLASSE INTERNE


} //FIN DE CLASSE EXTERNE





