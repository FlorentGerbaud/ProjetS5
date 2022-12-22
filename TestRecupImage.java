import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

public class TestRecupImage {
    public static void main(String[] args){

        //test fonction de recuperation classique

        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(args[0]);
        byte t[] = imageToProcess.RecupImageNoirBlanc(args[0]);
        int width = imageToProcess.getWidth();
        int height = imageToProcess.getHEIGHT();

        //---------- boucle de test pour afficher la valeur des pixels en byte --------

        // for (int k=0;k<t.length; k++){
        //     System.out.println(t[k]);
        // }
        
        //---------- boucle de test pour afficher la valeur des pixels en entre 0 et 255 --------

        // for (int l=0; l<width; l++){
        //     for (int c=0; c<height; c++){
        //         System.out.println(img[l][c]);
        //     }
        // }
        
        int img[][] = imageToProcess.imageNoirBlanc(args[0]);
        int [][] imgAssombri1=imageToProcess.assombrissement(img);
        byte [] nImg=imageToProcess.imageModifie(imgAssombri1);
        imageToProcess.saveImage(nImg);


    }
}

//des bouts de code qui peuvent etre utile

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


// int y=56;
        // System.out.printf("res = %08X%n", y);
        // String f =String.format("%08x", y);
        // System.out.println(f);
        // int n = Integer.parseInt(f);
        // System.out.println("c'est un entier " + n);