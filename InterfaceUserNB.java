import java.util.Scanner;

public class InterfaceUserNB{
    
    private String traitement;
    private String path_img;
    private String imgRename;
    private String plusieurTraitement;
    private String stop;

    public InterfaceUserNB(){
    
        this.traitement="";
        this.path_img="";
        this.imgRename="tamere";
        this.plusieurTraitement="";
        this.stop="";
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

    public boolean plusieurTraitement(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("aurez vous plusieur traitement à réaliser ? ");
        this.plusieurTraitement = image.nextLine();  // Read user input
        if(this.plusieurTraitement.equals("oui")){
            return true;
        }
        return false;
    }

    public String stop(){

        Scanner image = new Scanner(System.in);  // Create a Scanner object
        System.out.println("si vous avez fini tapez stop : ");
        this.stop = image.nextLine();  // Read user input
        return this.stop;
    }

    public void renameImg(){

        String[] cheminAcces = this.path_img.split("/");
        String nameImg=cheminAcces[cheminAcces.length-1];
        System.out.println("test : "+ cheminAcces[cheminAcces.length-1]);
        String [] accsName=nameImg.split(".png");
        System.out.println(accsName.length);
        this.imgRename=accsName[0]+this.traitement+".png";
        System.out.println(this.imgRename);
    }

    public void GestionTraitement(){

        imageUser();
        traitementUser();
        renameImg();
        TraitementImageNoirBlanc imageToProcess = new TraitementImageNoirBlanc(this.path_img);
        byte t[] = imageToProcess.RecupImageNoirBlanc(this.path_img);

        switch (this.traitement) {
            case "A":
            System.out.println("Ta mere je suis la");
                int img[][] = imageToProcess.imageNoirBlanc(this.path_img); // transforme notre tableau de byte en une matrice d'entier de 0 à 255
                int [][] imgAssombri1=imageToProcess.assombrissement(img); // applique la procdeure d'assombrissement
                byte [] nImg=imageToProcess.imageModifie(imgAssombri1); // écrit l'image assombris dans un tableau de byte
                imageToProcess.saveImage(nImg, this.imgRename); // puis sauvegarde l'image
                break;

            case "E": //applique un traitement d'éclairage sur l'image de base
                int img2[][] = imageToProcess.imageNoirBlanc(this.path_img);
                int [][] imgRestaurer=imageToProcess.eclairage(img2); 
                byte [] nImg2=imageToProcess.imageModifie(imgRestaurer);
                imageToProcess.saveImage(nImg2, this.imgRename);
                break;
            
            case "C": //applique un traitement de contraste sur l'image
                int img3[][] = imageToProcess.imageNoirBlanc(this.path_img);
                int [][] imgContraster=imageToProcess.contraste(img3); 
                byte [] nImg3=imageToProcess.imageModifie(imgContraster);
                imageToProcess.saveImage(nImg3, this.imgRename);
                break;
            
            case "AE":
                //Mettre le code pour lancer l'analyse de l'exposition
                break;
            
            case "rgbToNb":
                //Mettre le code pour convertir une img couleur vers noir et blnc
                break;
            default:
                break;
        }
    }


}
