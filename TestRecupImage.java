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

        //imageToProcessColor.barPlotToFileCol(args[1]);

        /**
         * Utilisation : 
         * TestRecupImage <nom/de/image.png> <nom_fichier_destination.png (uniquement pour imageNB pour l'instant)>
         */
        //imageToProcessColor.assombrissement();
        //imageToProcessColor.eclairage();
        // imageToProcessColor.contraste();
        //imageToProcessColor.saveImgColor(args[1]);


        // ca me servira pour la covolution c'est le cast
        // int [][] conv = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
        // for(int l=0;l<3;l++){
        //     for(int c=0;c<3;c++){
        //         System.out.print(conv[l][c]+" ");
        //     }
        //     System.out.println("");
        // }
        // int x=258;
        // int y=-345;
        // System.out.println((int) (x >> 0) & 0xff);
        // System.out.println((int) (y >> 0) & 0xff);

        // imageToProcess.setPixelsInMatrice();
        // imageToProcess.recupVoisins(0, 0);
        // imageToProcess.toAffiche();
        // System.out.println(imageToProcess.convOnePixel());

        //--------- Convolution NB
        //imageToProcess.traitementConvolution("C");
        //imageToProcess.saveImage(args[1]);
        //---------------------------------

        imageToProcessColor.traitementConvolution("C");
        imageToProcessColor.saveImgColor(args[1]);

        //imageToProcessColor.setPixelsInMatrice();
        imageToProcessColor.recupVoisins(0, 0);
        imageToProcessColor.convOnePixel();
        System.out.println(imageToProcessColor.getPixel(0, 1, 1, 255));
        imageToProcessColor.toAffiche();
        System.out.println(imageToProcess.convOnePixel());

    }
}





