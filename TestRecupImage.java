import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.lang.String;

public class TestRecupImage {
    public static void main(String[] args){

        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(args[0]);

        TraitementImageCouleur imageToProcessColor = new TraitementImageCouleur(args[0]);

        // imageToProcess.eclairage();
        // imageToProcess.assombrissement();
        // imageToProcess.saveImage(args[1]);

        imageToProcessColor.toAffiche();



    }
}





