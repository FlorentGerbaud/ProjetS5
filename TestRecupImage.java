import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

public class TestRecupImage {
    public static void main(String[] args){

        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(args[0]);
        imageToProcess.barPlotToFile(args[0],args[1]);

        int y=-1447447;
        String f =String.format("%08x", y);
        String A=f.substring(0,2);
        String R=f.substring(2,4);
        String G=f.substring(4,6);
        String B=f.substring(6,8);
        System.out.println("A : "+A);
        System.out.println("R : "+R);
        System.out.println("G : "+G);
        System.out.println("B : "+B);
        System.out.println(f);
        int a = Integer.parseInt(A,16);
        int r = Integer.parseInt(R,16);
        int g = Integer.parseInt(G,16);
        int b = Integer.parseInt(B,16);
        System.out.println("alpha : " + a + " r : " + r + " g : " + g + " b : "+b);
        int decimal =Long.valueOf(f,16).intValue();
        System.out.println(decimal);

        

        //applique un traitement d'assombrissement sur l'image de base
        int img[][] = imageToProcess.imageNoirBlanc(args[0]); // transforme notre tableau de byte en une matrice d'entier de 0 à 255
        int [][] imgAssombri1=imageToProcess.assombrissement(img); // applique la procdeure d'assombrissement
        byte [] nImg=imageToProcess.imageModifie(imgAssombri1); // écrit l'image assombris dans un tableau de byte
        imageToProcess.saveImage(nImg, "MaGeuleAssombrie.png"); // puis sauvegarde l'image

        //applique un traitement d'éclairage sur l'image de base
        int [][] imgEclairer1=imageToProcess.eclairage(img); 
        byte [] nImg2=imageToProcess.imageModifie(imgEclairer1);
        imageToProcess.saveImage(nImg2, "MaGeuleEclairer.png");

        //applique un traitement de contraste sur l'image de base
        int [][] imgContraster=imageToProcess.contraste(img); 
        byte [] nImg4=imageToProcess.imageModifie(imgContraster);
        imageToProcess.saveImage(nImg4, "MaGeuleContraster.png");

        //applique un eclairage sur l'image assombris
        int img2[][] = imageToProcess.imageNoirBlanc("MaGeuleAssombrie.png");
        int [][] imgRestaurer=imageToProcess.eclairage(img2); 
        byte [] nImg3=imageToProcess.imageModifie(imgRestaurer);
        imageToProcess.saveImage(nImg3, "MaGeuleRestaurer.png");


    }
}





