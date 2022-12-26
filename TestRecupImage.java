import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.lang.String;

import java.util.HashMap;
import java.util.Set;

public class TestRecupImage {
    public static void main(String[] args){

        /**
         * Utilisation : 
         * TestRecupImage <nom/de/image.png> <nom_fichier_destination.png (uniquement pour imageNB pour l'instant)>
         */

        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(args[0]);

        TraitementImageCouleur imageToProcessColor = new TraitementImageCouleur(args[0]);

        // imageToProcess.eclairage();
        // imageToProcess.assombrissement();
        // imageToProcess.saveImage(args[1]);

        //imageToProcessColor.toAffiche();
        //imageToProcessColor.toSimple();

        //imageToProcessColor.barPlotToFileCol(args[1]);


            int [] occ = {0,0,0}; 
            int [] occR = {0,0,0};
            int [] occG = {0,0,0};
            int [] occB = {0,0,0};
            int incr=0;
    
            HashMap<Integer,int []> sphm = new HashMap<Integer,int []>(256); //car 256 nuances de gris
    
           for (int i = 0; i < 3; i++) {
               sphm.put(i,occ);
           }
    
           for (int i = 0; i < 3; i++) {
            System.out.println(sphm.get(i));
        }
    
    }
}





