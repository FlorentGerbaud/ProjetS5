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

        imageToProcess.eclairage();
        //imageToProcess.assombrissement();
        imageToProcess.saveImage(args[1]);

        //imageToProcessColor.toAffiche();
        //imageToProcessColor.toSimple();

        //imageToProcessColor.barPlotToFileCol(args[1]);


            
    
    }
}





