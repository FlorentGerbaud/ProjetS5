import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.lang.String;

public class TestRecupImage {
    public static void main(String[] args){

        //test fonction de recuperation classique

        // recupe info de l'image de base
        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(args[0]);
        byte t[] = imageToProcess.RecupImageNoirBlanc(args[0]); // tableau qui contient l'image dans un tableau de byte
        //int c[] = imageToProcess.RecupImageCouleur(args[0]);

        int width = imageToProcess.getWidth(); // on recupere les dimensions de l'image
        int height = imageToProcess.getHEIGHT();
        

        //System.out.println("w : "+ width+ "h : "+height + " l : " );

        // for (int k=0;k<t.length; k++){
        //     System.out.println(t[k]);
        // }

        // -----------convertion RGB Test -----------------
        // ffe9e9e9
        // int y=-1447447;
        // String f =String.format("%08x", y);
        // String A=f.substring(0,2);
        // String R=f.substring(2,4);
        // String G=f.substring(4,6);
        // String B=f.substring(6,8);
        // System.out.println("A : "+A);
        // System.out.println("R : "+R);
        // System.out.println("G : "+G);
        // System.out.println("B : "+B);
        // System.out.println(f);
        // int a = Integer.parseInt(A,16);
        // int r = Integer.parseInt(R,16);
        // int g = Integer.parseInt(G,16);
        // int b = Integer.parseInt(B,16);
        // System.out.println("alpha : " + a + " r : " + r + " g : " + g + " b : "+b);
        // int decimal =Long.valueOf(f,16).intValue();
        // System.out.println(decimal);

        

            // --------------- test img noir blanc ----------------
        
        //applique un traitement d'assombrissement sur l'image de base
        int img[][] = imageToProcess.imageNoirBlanc(args[0]); // transforme notre tableau de byte en une matrice d'entier de 0 à 255
        int [][] imgAssombri1=imageToProcess.assombrissement(img); // applique la procdeure d'assombrissement
        byte [] nImg=imageToProcess.imageModifie(imgAssombri1); // écrit l'image assombris dans un tableau de byte
        imageToProcess.saveImage(nImg, "MaGeuleAssombrie"); // puis sauvegarde l'image

        //applique un traitement d'éclairage sur l'image de base
        int [][] imgEclairer1=imageToProcess.eclairage(img); 
        byte [] nImg2=imageToProcess.imageModifie(imgEclairer1);
        imageToProcess.saveImage(nImg2, "MaGeuleEclairer");

        //applique un traitement de contraste sur l'image de base
        int [][] imgContraster=imageToProcess.contraste(img); 
        byte [] nImg4=imageToProcess.imageModifie(imgContraster);
        imageToProcess.saveImage(nImg4, "MaGeuleContraster");

        //applique un eclairage sur l'image assombris
        int img2[][] = imageToProcess.imageNoirBlanc("MaGeuleAssombrie");
        int [][] imgRestaurer=imageToProcess.eclairage(img2); 
        byte [] nImg3=imageToProcess.imageModifie(imgRestaurer);
        imageToProcess.saveImage(nImg3, "MaGeuleRestaurer");


    }
}

// int x;
// double z=5.8;
// double y;
// y =(double) z;
// System.out.println((int)y);

//des bouts de code qui peuvent etre utile

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