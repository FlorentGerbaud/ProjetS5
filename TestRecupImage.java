import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

public class TestRecupImage {
    public static void main(String[] args){

        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(args[0]);
        byte t[] = imageToProcess.RecupImageNoirBlanc(args[0]);

        for (int k=0;k<t.length; k++){
            System.out.println(t[k]);
        }
        //imageToProcess.afficheChemin();
        int y=56;
        System.out.printf("res = %08X%n", y);


    }
}

// short x = -1;
        // short res = (short)(x & 0xFF00);
        // System.out.println("res = " + res);
        // System.out.printf("res = %d%n", res); // res en decimal
        // // res en hexadecimal et autant de zéros que nécessaire pour l'avoir sur 4 chiffres
        // System.out.printf("res = %04X%n", res); 
        // // deplacement vers la droite (pour un deplacement vers la gauche, utilisez "<<")
        // res = (short)(res >> 8);
        // System.out.println("res = " + res);
        // System.out.printf("res = %d%n", res);
        // System.out.printf("res = %04X%n", res);
