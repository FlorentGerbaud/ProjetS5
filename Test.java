import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Test extends JFrame implements ActionListener{

  public static void main(String[] args) {
    /* init + affichage de la fenêtre */
    new Test().setVisible(true);
  }
  
  public Test() {
    /* init de la fenêtre */
    setSize(300, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    /* init du bouton qui va afficher le FileChooser */
    JButton button = new JButton("Open file");
    /* ajout du listener qui prend en charge l'action sur le click */
    button.addActionListener(this);
    
    /* ajout du bouton à la fenêtre */
    JPanel p = new JPanel();
    p.add(button);
    setContentPane(p);
  }
  
  public void actionPerformed(ActionEvent e) {
    /* init du filechooser */
    JFileChooser fc = new JFileChooser();
    /* affichage du dialog et test si le bouton ok est pressé */
    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
      try {
        /* demande au système d'ouvrir le fichier précédemment séléctionné */
        Desktop.getDesktop().open(fc.getSelectedFile());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
  }
}

