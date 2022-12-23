import java.util.Scanner;

public class InterfaceUserNB extends TraitementImageNoirBlanc{
    
    private String traitement;
    private String path_img;

    public InterfaceUserNB(String path_img){
        super(path_img);
        this.traitement="";
        this.path_img="";
    }

    public void traitementUser(){

        Scanner traitementImg = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Quelle traitement voulez vous appliquez à l'image");
        this.traitement = traitementImg.nextLine();  // Read user input
        System.out.println("Vous avez choisis : " + this.traitement);  // Output user input

    }

    public void imageUser(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Quelle fichier voulez vous traitez ? ");
        this.path_img = image.nextLine();  // Read user input
        System.out.println("Vous avez choisis : " + this.path_img);  // Output user input
    }

    public void GestionTraitement(){

        imageUser();
        traitementUser();
        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(this.path_img);
        byte t[] = imageToProcess.RecupImageNoirBlanc(this.path_img);

        switch (this.traitement) {
            case "A":
                int img[][] = imageToProcess.imageNoirBlanc(this.path_img); // transforme notre tableau de byte en une matrice d'entier de 0 à 255
                int [][] imgAssombri1=imageToProcess.assombrissement(img); // applique la procdeure d'assombrissement
                byte [] nImg=imageToProcess.imageModifie(imgAssombri1); // écrit l'image assombris dans un tableau de byte
                imageToProcess.saveImage(nImg, "MaGeuleAssombrie"); // puis sauvegarde l'image

            case "E": //applique un traitement d'éclairage sur l'image de base
                int img2[][] = imageToProcess.imageNoirBlanc(this.path_img);
                int [][] imgRestaurer=imageToProcess.eclairage(img2); 
                byte [] nImg2=imageToProcess.imageModifie(imgRestaurer);
                imageToProcess.saveImage(nImg2, "MaGeuleEclairer");
            
            case "C": //applique un traitement de contraste sur l'image
                int img3[][] = imageToProcess.imageNoirBlanc(this.path_img);
                int [][] imgContraster=imageToProcess.contraste(img3); 
                byte [] nImg3=imageToProcess.imageModifie(imgContraster);
                imageToProcess.saveImage(nImg3, "MaGeuleContraster");
            
            case "AE":
                //Mettre le code pour lancer l'analyse de l'exposition
            
            case "rgbToNb"
                //Mettre le code pour convertir une img couleur vers noir et blnc
            default:
                break;
        }
    }


}
