import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class Test {


  public static String openFileChosser(){

    // on créer l'objet FileChooser
    JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
   
    // Ouvrez le fichier vaut APPROVE_OPTION si fichier bien séléctionner
    int res = choose.showOpenDialog(null);
    String selectedPathFile = null;
    // Enregistrez le fichier en créant un objet de type file

    if (res == JFileChooser.APPROVE_OPTION) {
      choose.setVisible(true);

        File file = choose.getSelectedFile();
        selectedPathFile = file.getAbsolutePath();
        return selectedPathFile;
        
        
    } 
    else{
      choose.setVisible(true);

        selectedPathFile = null;  //erreur aucun fichier sélectionner ou autre erreur
        return selectedPathFile;
    }



    
}
  public static void main(String[] args) {
    String test = openFileChosser();
    System.out.println(test);
    

  }
}

