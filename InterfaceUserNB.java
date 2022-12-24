import java.util.Scanner;

public class InterfaceUserNB{
    
    private String traitement;
    private String path_img;
    private String imgRename;
    private String plusieurTraitement;
    private String stop;
    private String pathFileOut;

    /**
     * ------ CONSTRUCTEUR ------
     */

    public InterfaceUserNB(){
    
        this.traitement="";
        this.path_img="";
        this.imgRename="tamere";
        this.plusieurTraitement="";
        this.stop="";
        this.pathFileOut="";
    }

    /**
     * ------ CHOIX TRAITEMENT ------
     */

    public void traitementUser(){

        Scanner traitementImg = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Quelle traitement voulez vous appliquez à l'image");
        this.traitement = traitementImg.nextLine();  // Read user input
        //System.out.println("Vous avez choisis : " + this.traitement);  // Output user input

    }

    /**
     * ------ CHOIX IMAGE ------
     */

    public void imageUser(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Quelle fichier voulez vous traitez ? ");
        this.path_img = image.nextLine();  // Read user input
        //System.out.println("Vous avez choisis : " + this.path_img);  // Output user input
    }

    /**
     * ------ GESTION DU NOMBRE DE TRAITEMENT ------
     */

    public boolean plusieurTraitement(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("aurez vous plusieur traitement à réaliser ? ");
        this.plusieurTraitement = image.nextLine();  // Read user input
        if(this.plusieurTraitement.equals("oui")){
            return true;
        }
        return false;
    }

    /**
     * ------ GESTION D'ARRET DU MENU ------
     */

    public String stop(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("si vous avez fini tapez stop : ");
        this.stop = image.nextLine();  // Read user input
        return this.stop;
    }

    /**
     * ------ CHOIX FICHIER SORTIE POUR L'ANALYSE EXPOSITION ------
     */

    public void FileOut(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Fichier destination pour l'anayse de l'exposition : ");
        this.pathFileOut = image.nextLine();  // Read user input
    }

    /**
     * ------ RENOMME L'IMAGE ------
     */

    public void renameImg(){

        String[] cheminAcces;
        String nameImg;

        if(this.path_img.contains("/")){

            cheminAcces = this.path_img.split("/"); //on spit autour du / si jamais on a un chemin d'accès
            nameImg=cheminAcces[cheminAcces.length-1]; //recupere le nom img.png
        }
        else{
            nameImg=this.path_img;
        }
        String [] accsName=nameImg.split(".png"); //supprime le .png
        this.imgRename=accsName[0]+this.traitement+".png"; //renommage de l'image
    }

    /**
     * ------ TRAITEMENT IMAGE ------
     * selon le traitement, l'image on applique un certains traitement
     */

    public void GestionTraitement(){

        imageUser();
        traitementUser();
        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(this.path_img);
        

        switch (this.traitement) {
            case "A":
            
                renameImg();
                int img[][] = imageToProcess.imageNoirBlanc(this.path_img); // transforme notre tableau de byte en une matrice d'entier de 0 à 255
                int [][] imgAssombri1=imageToProcess.assombrissement(img); // applique la procdeure d'assombrissement
                byte [] nImg=imageToProcess.imageModifie(imgAssombri1); // écrit l'image assombris dans un tableau de byte
                imageToProcess.saveImage(nImg, this.imgRename); // puis sauvegarde l'image
                break;

            case "E": //applique un traitement d'éclairage sur l'image de base
                
                renameImg();
                int img2[][] = imageToProcess.imageNoirBlanc(this.path_img);
                int [][] imgRestaurer=imageToProcess.eclairage(img2); 
                byte [] nImg2=imageToProcess.imageModifie(imgRestaurer);
                imageToProcess.saveImage(nImg2, this.imgRename);
                break;
            
            case "C": //applique un traitement de contraste sur l'image

                renameImg();
                int img3[][] = imageToProcess.imageNoirBlanc(this.path_img);
                int [][] imgContraster=imageToProcess.contraste(img3); 
                byte [] nImg3=imageToProcess.imageModifie(imgContraster);
                imageToProcess.saveImage(nImg3, this.imgRename);
                break;
            
            case "AE":

                FileOut(); // récupère le fichier dans lequelle l'ae sera enregistré
                imageToProcess.barPlotToFile(this.path_img,this.pathFileOut); // sort le fichier d'ae 
                break;
            
            case "rgbToNb":
                //Mettre le code pour convertir une img couleur vers noir et blnc
                break;
            default:
                break;
        }
    }


}
