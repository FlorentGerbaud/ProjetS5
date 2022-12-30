import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.accessibility.Accessible;
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




public class MyWindowForColor extends JFrame{

    private JCheckBox chkHisto = new JCheckBox("Histogramme");

    private JCheckBox chkEclair = new JCheckBox("Eclairage");

    private JCheckBox chkAssomb  = new JCheckBox("Assombrissement");

    private JCheckBox chkContrast = new JCheckBox("Contraste");


    private JButton btnConfirmLaunch = new JButton("Appuyer pour lancer");

    private JButton btnOpenFileChooser = new JButton("Appuyer pour choisir un fichier à traiter");

    private JButton btnOpenDirectoryChooser = new JButton("Appuyer pour choisir le dossier de sauvegarde");

    private JButton btnOpenGrayProcess = new JButton("Appuyer ici pour les traitements en niveau de gris");


    private JTextArea promptInfos = new JTextArea(" >>> Lancement de la fenêtre GUI \n",40,40);

    private String selectedPathFile = null; // contient le chemin absolue vers le fichier

    private String name_file = null; // nom du fichier

    private String selectedPathDirectory = null;




    public MyWindowForColor(){

        super("Traitement Image Couleur");
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE ); 
        this.setSize( 800, 800 ); 
        this.setLocationRelativeTo( null ); 

        JPanel contentPane = (JPanel) this.getContentPane(); 

        contentPane.setLayout( new FlowLayout()); 


        contentPane.add(this.chkHisto);
        contentPane.add(this.chkEclair);
        contentPane.add(this.chkAssomb);
        contentPane.add(this.chkContrast);

        contentPane.add(this.btnConfirmLaunch);
        btnConfirmLaunch.addActionListener(new ConfirmLaunchForColor());

        contentPane.add(this.btnOpenFileChooser);
        btnOpenFileChooser.addActionListener(new OpenFileChooser());

        contentPane.add(this.btnOpenDirectoryChooser);
        btnOpenDirectoryChooser.addActionListener(new OpenDirectoryChooser());

        contentPane.add(this.btnOpenGrayProcess);
        btnOpenGrayProcess.addActionListener(new OpenProcessGray());


        this.promptInfos.setEditable(false); // impossible de modifier le prompt
        contentPane.add(promptInfos);
        //barre de scroll pour la texte field (indiquant les entrées sorties du programme)
        JScrollPane scroll = new JScrollPane(this.promptInfos,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scroll);


    }
    /* 

 * #############################################################
 * #               FENETRE DU DIRECTORY CHOOSER                #
 * #############################################################
 
*/
    public void openDirectoryChooser(){

        JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int res = choose.showOpenDialog(this);

        if(res == JFileChooser.APPROVE_OPTION){
            File directory = choose.getSelectedFile();
            this.selectedPathDirectory = directory.toString();
        }
        else{

            this.selectedPathDirectory = null;
        }
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
            this.selectedPathFile = file.getAbsolutePath();
            this.promptInfos.append(" >>> A File is actually selected"+"\n");
            
        } 
        else{
            this.selectedPathFile = null;  //erreur aucun fichier sélectionner ou autre erreur
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

        String [] path_to_tab = this.selectedPathFile.split("/");

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

    public void closeMyWindow(){

        this.dispose();

    }

    private class OpenFileChooser implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){

            MyWindowForColor.this.openFileChosser();
        }
    }


    private class OpenDirectoryChooser implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){

            MyWindowForColor.this.openDirectoryChooser();
        }
    }

    private class OpenProcessGray implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){

            MyWindow windows_gray = new MyWindow();
            windows_gray.setVisible(true);
            MyWindowForColor.this.closeMyWindow();

        }
    }

    private class ConfirmLaunchForColor implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){

            boolean done = false;
            // si aucun dossier de sauvegarde , on arrête directement
            if(MyWindowForColor.this.selectedPathDirectory == null){

                MyWindowForColor.this.promptInfos.append(" >>> No directory selected "+"\n");
                MyWindowForColor.this.promptInfos.append(" >>> Please select a directory "+"\n");
            }
            else{
            
                // si aucun fichier sélectionner on arrête tout
                if(MyWindowForColor.this.selectedPathFile == null){
                    MyWindowForColor.this.promptInfos.append(" >>> No file selected "+"\n");
                    MyWindowForColor.this.promptInfos.append(" >>> Please select a file"+"\n");
                }
                else{
                
                    if(isImgPng() == true){ //on peut exécuter les méthodes de traitements si png

                        //création de l'objet image
                        TraitementImageCouleur obj = new TraitementImageCouleur(MyWindowForColor.this.selectedPathFile);

                        if(obj.isColor() == false){ //verification du type de couleur
                            MyWindowForColor.this.promptInfos.append(" >>> Selected Image is not in Color, select another one "+"\n");
                        }
                        else{

                            MyWindowForColor.this.promptInfos.append(" >>> Correct File : "+MyWindowForColor.this.getName_File()+"\n");
                            MyWindowForColor.this.promptInfos.append(" >>> BEGIN OF PROCESS "+"\n");

                            if(MyWindowForColor.this.chkHisto.isSelected()){

                                try{
                                    obj.barPlotToFileCol(selectedPathDirectory+"/histogramme.txt");
                                    MyWindowForColor.this.promptInfos.append(" >>> Process Histogramme was successful"+"\n");
                                }
                                catch(FileNotFoundException e){
                                    System.out.println("Error while creating file in histogramme");
                                }
                                done = true;

                            }

                            if(MyWindowForColor.this.chkEclair.isSelected()){

                                obj.eclairage();
                                obj.saveImgColor(MyWindowForColor.this.selectedPathDirectory+"/eclairage_"+MyWindowForColor.this.name_file);

                                MyWindowForColor.this.promptInfos.append(" >>> Process Eclairage was successful"+"\n");
                                done = true;
                            }

                            if(MyWindowForColor.this.chkAssomb.isSelected()){

                                obj.assombrissement();
                                obj.saveImgColor(MyWindowForColor.this.selectedPathDirectory+"/assombrissement_"+MyWindowForColor.this.name_file);

                                MyWindowForColor.this.promptInfos.append(" >>> Process Assombrissement was successful"+"\n");

                                
                            }

                            if(MyWindowForColor.this.chkContrast.isSelected()){

                                obj.contraste();
                                obj.saveImgColor(MyWindowForColor.this.selectedPathDirectory+"/contraste_"+MyWindowForColor.this.name_file);

                                MyWindowForColor.this.promptInfos.append(" >>> Process Contraste was successful"+"\n");
                                done = true;
                                
                            }
                        }
                    }
                    else{ // on ne peut rien faire avec l'image, on redonne la main
                        MyWindowForColor.this.promptInfos.append(" >>> Bad File : "+MyWindowForColor.this.getName_File()+" is not .png"+"\n");
                        MyWindowForColor.this.promptInfos.append(" >>> Select Another File .png "+"\n");
                    }
                    if(done == false){
                        MyWindowForColor.this.promptInfos.append(" >>> No process have been performed "+"\n");
                    }
                    MyWindowForColor.this.promptInfos.append(" >>> END OF PROCESS"+"\n");
                    MyWindowForColor.this.promptInfos.append("\n");

                }   
            }
        }
    }
} //FIN DE LA CLASSE PRINCIPALE

