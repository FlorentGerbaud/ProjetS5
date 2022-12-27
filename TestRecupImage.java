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

        //imageToProcess.eclairage();
        //imageToProcess.assombrissement();
        //imageToProcess.fromColorToGray();
        //imageToProcess.saveImage(args[1]);

        //imageToProcessColor.toAffiche();
        //imageToProcessColor.toSimple();

        /**
         * Utilisation AE : 
         * TestRecupImage <nom/de/image.png> <nom_fichier_destination.txt>
         * si tu veux verifier tu peux le faire avec matlab
         */

        imageToProcessColor.barPlotToFileCol(args[1]);

        /**
         * Utilisation : 
         * TestRecupImage <nom/de/image.png> <nom_fichier_destination.png (uniquement pour imageNB pour l'instant)>
         */
        imageToProcessColor.assombrissement();
        imageToProcessColor.eclairage();
        imageToProcessColor.contraste();
        imageToProcessColor.saveImgColor(args[1]);
        


            
    
    }
}





