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
  public static void main(String[] args) {

    TraitementImageCouleur color = new TraitementImageCouleur(args[0]);
    color.assombrissement();
    color.saveImgColor(args[1]);
  }
}

