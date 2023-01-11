/* 

 * #############################################################
 * #        PACKAGES LECTURE ET ECRITURE IMAGE PNG             #
 * #############################################################
 
*/

import javax.media.jai.RenderedOp;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.PackedColorModel;
import java.awt.image.WritableRaster;
import java.awt.color.ColorSpace;
import java.awt.image.DataBufferByte;
import java.awt.image.SampleModel;
import java.awt.image.DirectColorModel;


/* 

 * #############################################################
 * #           PACKAGES TRAITEMENT DES FICHIERS                #
 * #############################################################
 
*/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;

/* 

 * #############################################################
 * #                       EXCEPTION                           #
 * #############################################################
 
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/* 

 * #############################################################
 * #        PACKAGES STRUCTURE DE DONNEES                      #
 * #############################################################
 
*/

import java.util.HashMap;
import java.util.Set;

/**
 * FLORENT LIT BIEN CE PASSAGE POUR COMPRENDRE COMMENT ON VA STRUCTURER CETTE PARTIE : 
 * On fait exactement comme avant avec le tableau de pixels qui contient les pixels de l'image.
 * Le constructeur se charge de le remplir directement.
 * Par contre, comme tu peux le voir, j'ai mit le tableau de pixels en 2D en attribut de la classe.
 * En gros, au lieu de renvoyer après chaque traitement, une matrice, on modifie directement l'attribut matrice.
 * Mais comme tu l'aura devinés à chaque appel d'une nouvelle méthode de traitement, on perd le résultat de la
 * méthode précédente. 
 * En réalité, ça n'a aucune conséquence puisque que dans l'interface on appel les méthodes les unes après les
 * autres. Donc à chaque appel on va écraser le résultat de la méthode précédente.
 * Voilà c'est tout. 
 * De cette manière ça rend le code plus propre et ça évite des returns dans tous les sens. 
 * On finira par transférer le résultat stocker dans la matrice dans l'attribut post_process_pixels.
 */

public class TraitementImageCouleur extends TraitementImage {
    
    private int [] pixels;

    private int [][] pixels_matrix;

    private int [] post_process_pixels;



    private int conv[][];

    private int dimConv;

    private int sum;



    private int [][] voisins_matrixR;

    private int [][] voisins_matrixG;

    private int [][] voisins_matrixB;



    private int pixelConvolerR;

    private int pixelConvolerG;

    private int pixelConvolerB;



    private int IMG_HEIGHT;

    private int IMG_WIDTH;

    private boolean iscolor = false;

    /**
     * 
     * @param file_path (String) / chemin vers le fichier à traiter.
     * @todo Vérifier que le fichier reçu est bien en couleur sinon déclencher une erreur.
     */
    public TraitementImageCouleur(String file_path){

        super(file_path);                // récupération du chemin d'accès à l'image


        String fn = super.getPathFile(); // récupération du nom de l'image                                       
        RenderedOp ropimage;                                
        ropimage = JAI.create("fileload", fn);              
        BufferedImage bi = ropimage.getAsBufferedImage();   
        ColorModel cm = ropimage.getColorModel();    


        this.IMG_WIDTH = ropimage.getWidth();                          // Init de la longueur de l'image
        this.IMG_HEIGHT = ropimage.getHeight();                        // Init de la hauteur de l'image
        this.pixels_matrix = new int[this.IMG_HEIGHT][this.IMG_WIDTH]; // Init de la matrice de pixels
        this.post_process_pixels = new int[this.IMG_HEIGHT*IMG_WIDTH]; // Init du tableau qui contiendra les résultats de la transformation
    
        if (cm.getColorSpace().getType() == ColorSpace.TYPE_RGB) { // tout se passe bien
            this.pixels = bi.getRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, null, 0, IMG_WIDTH);
            this.setPixelsInMatrice();
            this.iscolor = true;
        }
        else{   // Erreur : l'image n'est pas en couleur
            this.pixels = null;
            this.IMG_HEIGHT = -1;
            this.IMG_WIDTH = -1;
            this.iscolor = false;
        }

    }


    public boolean isColor(){

        return this.iscolor; 
    }



 /* 

 * ##########################################################################################
 * #                        TRANSFORMATION SOUS FORME DE MATRICE                            #
 * ##########################################################################################
 
*/

    /**
     * Fonction appelée dans le constructeur pour initialiser la matrice de pixels.
     */
    public void setPixelsInMatrice(){

        int k = 0;
        for (int i = 0; i < this.IMG_HEIGHT; i++) {
            for (int j = 0; j < this.IMG_WIDTH; j++) {
                this.pixels_matrix[i][j] = this.pixels[k];
                k++;
            }
        }
    }

    /**
     * Fonction d'affichage de la matrice pour comprendre un peu florent.
     */
    public void toAffiche(){

  
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(this.voisins_matrixB[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * méthode d'affichage du tableau contenant les pixels.
     */
    public void toSimple(){

        for (int i = 0; i < pixels.length; i++) {
            System.out.println(pixels[i]);
        }
    }



/* 

 * ##########################################################################################
 * #                        HISTOGRAMME CANAUX ARGB (FLORENT)                           #
 * ##########################################################################################
 
*/

   /**
    * @brief Methode qui vas se charger de remplir un Hashmap composé de tableau [R,G,B] avec pour
    *clé la nuance de couleur entre [0;255] afin de savoir conbien de fois chaque nuance apparait
    * @param name_img (String) qui est le nom de l'image
    * @return HashMap<Integer,int []> avec en clé les nuance [0,255] en valeur des tableau [R,G,B]
    */
   public HashMap<Integer,int []> statsHashMapCol(){


        int r=0; //initialisation des 3 couleurs
        int g=0;
        int b=0;
        int [] occ = new int[3]; //on initialise un tableau d'occurence [R,G,B]

        HashMap<Integer,int []> sphm = new HashMap<Integer,int []>(256); //car 256 nuances pour R, G et B

       for (int i = 0; i < 256; i++) {
           sphm.put(i,new int[3]); //on initialise la boucle avec 256 tableau tous instancier avec new pour pouvoir modifier les valeurs de chaque tableau de manière indépendante
       }

       for (int i = 0; i < pixels.length; i++) {

            r = getR(this.pixels[i]); // on récupère les composantes R,G,B d'un certains pixels pi

            g = getG(this.pixels[i]);

            b = getB(this.pixels[i]);
    
            occ=sphm.get(r); //on recupere le tableau des occurance en position de la nuance rouge
            occ[0]=occ[0]+1; // on incrémente la composante R
            sphm.put(r,occ); // on réinjecte le tableau modifié dans e Hashmap

            occ=sphm.get(g); // On fait de meme avec les autres couleurs
            occ[1]=occ[1]+1;
            sphm.put(g,occ);

            occ=sphm.get(b);
            occ[2]=occ[2]+1;
            sphm.put(b,occ);
       }

       return sphm; 

   }

   /**
    * 
    * @param destination fichier .txt de sortie
    */
   public void barPlotToFileCol(String destination) throws FileNotFoundException{

    Path destination_path = Paths.get(destination);

    HashMap<Integer,int []> sphm = statsHashMapCol();

    boolean iscreated = super.createFile(destination); // file doesn't exist , so it's created

    if(iscreated == false){                            // file already exist so clear file

        PrintWriter writer = new PrintWriter(destination);
        writer.print("");
        writer.close();
    }

    for (int occRGB = 0; occRGB < sphm.size(); occRGB++) {
        
        try (BufferedWriter w = Files.newBufferedWriter(destination_path, StandardOpenOption.CREATE,
            StandardOpenOption.APPEND)) {

                w.write(String.valueOf(occRGB)); //écriture de chaque intensité du pixel
                w.write(";");
                w.write(String.valueOf(sphm.get(occRGB)[0])); // puis de chaque occurance de la composante
                w.write(";");
                w.write(String.valueOf(sphm.get(occRGB)[1]));
                w.write(";");
                w.write(String.valueOf(sphm.get(occRGB)[2]));
                w.newLine(); // sinon, pas de retour à la ligne

        } 
        catch (IOException ioe) {

            System.err.println("Error Write File.");
        }
    }
}


/* 

 * ##########################################################################################
 * #   METHODE DE TRAITEMENT DE L'IMAGE -- COMMUNE A LA CLASSE TRAITEMENTNOITETBLANC.JAVA   #
 * ##########################################################################################
 
*/

/* 

 * #############################################################
 * #                       ASSOMBRISSEMENT                     #
 * #############################################################
 
*/

    public void assombrissement(){

        int a=0;
        int r=0;
        int g=0;
        int b=0;

        
        for (int i = 0; i < this.pixels.length; i++) {
            a=getA(this.pixels[i]);
            r=getR(this.pixels[i]);
            g=getG(this.pixels[i]);
            b=getB(this.pixels[i]);
            this.post_process_pixels[i]=getPixel(a, (r*r)/255, (g*g)/255, (b*b)/255);
        }
    }

    /* 

 * #############################################################
 * #                       ECLAIRAGE                     #
 * #############################################################
 
*/
   
    public void eclairage(){

        int a=0;
        int r=0;
        int g=0;
        int b=0;

        
        for (int i = 0; i < this.pixels.length; i++) {
            a=getA(this.pixels[i]);
            r=getR(this.pixels[i]);
            g=getG(this.pixels[i]);
            b=getB(this.pixels[i]);
            this.post_process_pixels[i]=getPixel(a,(int) Math.sqrt((double) r)*16, (int) Math.sqrt((double) g)*16, (int) Math.sqrt((double) b)*16);
        }

    }

    /* 

 * #############################################################
 * #                       CONTRASTE                     #
 * #############################################################
 
*/
    
    /**
     * methode qui recupere les composantes de chaque pixel, et applique 
     * le complément sur chacune d'entre-elle
     */

    public void contraste(){

        int a=0;
        int r=0;
        int g=0;
        int b=0;

        
        for (int i = 0; i < this.pixels.length; i++) {
            a=getA(this.pixels[i]);
            r=getR(this.pixels[i]);
            g=getG(this.pixels[i]);
            b=getB(this.pixels[i]);
            this.post_process_pixels[i]=getPixel(a,255-r, 255-g, 255-b);
        }
    }


       /* 

 * #############################################################
 * #                       CONVOLUTION                     #
 * #############################################################
 
*/

    /**
     * Methode qui vas se charger de recupérer une latrice de convolution dans un fichier .csv
     * @param file (String) nom du fichier csv
     */

     public void getConv(String file){

        int l=0; //on initialise l'indice des ligne
        String line = "";  
        String splitBy = ";";  //on initialise le separateur

        try{  
            BufferedReader br = new BufferedReader(new FileReader(file)); 
            line = br.readLine(); //lecture un premier coup en dehors de la boucle
            String[] ligneConv = line.split(splitBy); 
            this.dimConv=ligneConv.length; //qui nous permet d'obtenir la dimensions de la matrice de convolution
            this.conv=new int[this.dimConv][this.dimConv]; // que l'on peut donc initialiser

            for(int c=0;c<this.dimConv; c++){ //maintenant qu'on connais la taille, on recupere la premiere ligne
                this.conv[l][c]=Integer.parseInt(ligneConv[c]);
            }
            l++; //on passe à la ligne suivante
            while ((line = br.readLine()) != null) {  //puis on re applique n fois le procédé pour recuperer les autres lignes
                ligneConv = line.split(splitBy);
                for(int c=0;c<this.dimConv; c++){
                    this.conv[l][c]=Integer.parseInt(ligneConv[c]);
                }
                l++;
            }  
        }   
        catch (IOException e){  
            e.printStackTrace();  
        }  
    } 


    /**
     * permet à l'utilisateur de choisir le traitement qu'il veut faire
     * @param choix
     */

    public void choixConv(String choix){
        switch (choix) {
            case "TB":
                getConv("MatricesConv/TranslationBas.csv");
                break;
            case "F" :
                getConv(("MatricesConv/Flou.csv"));
                break;
            case "C" :
                getConv("MatricesConv/Contour.csv");
                break;
            case "N" :
                getConv("MatricesConv/Pique.csv");
                break;
            default:
                System.err.println("le traitement choisis n'éxiste pas");
                break;
        }
    }

    /**
     * transforme e tavleau de byte en matrice de int
     */
    

    public void convOnePixel(){

        this.pixelConvolerR=0;
        this.pixelConvolerG=0;
        this.pixelConvolerB=0;
        for(int l=0;l<this.dimConv;l++){
            for (int c=0;c<this.dimConv;c++){

                this.pixelConvolerR=this.pixelConvolerR+this.voisins_matrixR[l][c]*this.conv[l][c];


                this.pixelConvolerG=this.pixelConvolerG+this.voisins_matrixG[l][c]*this.conv[l][c];


                this.pixelConvolerB=this.pixelConvolerB+this.voisins_matrixB[l][c]*this.conv[l][c];


            }
        }

        this.pixelConvolerB = this.pixelConvolerB / this.sum ;
        this.pixelConvolerG = this.pixelConvolerG / this.sum ;
        this.pixelConvolerR = this.pixelConvolerR / this.sum ;

        if(pixelConvolerR>255 || pixelConvolerR<0){
            this.pixelConvolerR = ((this.pixelConvolerR >> 0) & 0xFF);
        }

        if(pixelConvolerG>255 || pixelConvolerG<0){
            this.pixelConvolerG = ((this.pixelConvolerG >> 0) & 0xFF);
        }

        if(pixelConvolerB>255 || pixelConvolerB<0){
            this.pixelConvolerB = ((this.pixelConvolerB>> 0) & 0xFF);
        }



    }

    public int calculPas(int seuil){

        int pas=1;
        int cpt=3;

        while (cpt<seuil){
            pas++;
            cpt=cpt+2;
        }
        return pas;
    }


    public void sumFiltre(){


        for (int i = 0; i < this.dimConv; i++) {
            for (int j = 0; j < this.dimConv; j++) {
                this.sum = this.sum + this.conv[i][j];
            }
        }
        if(this.sum == 0){
            this.sum = 1;
        }  
       
    }

    public void recupVoisins(int lp, int cp){

        this.voisins_matrixR=new int[this.dimConv][this.dimConv];
        this.voisins_matrixG=new int[this.dimConv][this.dimConv];
        this.voisins_matrixB=new int[this.dimConv][this.dimConv];

        for (int l=0;l<this.dimConv; l++){ 
            for(int c=0;c<this.dimConv; c++){
                try {
                    this.voisins_matrixR[l][c]=getR(this.pixels_matrix[lp+l-calculPas(this.dimConv)][cp+c-calculPas(this.dimConv)]);
                    this.voisins_matrixG[l][c]=getG(this.pixels_matrix[lp+l-calculPas(this.dimConv)][cp+c-calculPas(this.dimConv)]);
                    this.voisins_matrixB[l][c]=getB(this.pixels_matrix[lp+l-calculPas(this.dimConv)][cp+c-calculPas(this.dimConv)]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.voisins_matrixR[l][c]=0;
                    this.voisins_matrixG[l][c]=0;
                    this.voisins_matrixB[l][c]=0;
                }
            }
        }
    }

    public void traitementConvolution(String Traitement){

        choixConv(Traitement);
        this.sumFiltre(); //calcul de la somme pour normalisation
        int k=0;
        for (int l=0;l<this.IMG_HEIGHT; l++){
            for(int c=0;c<this.IMG_WIDTH;c++){
                recupVoisins(l, c);
                convOnePixel();
                this.post_process_pixels[k]=getPixel(getA(this.pixels_matrix[l][c]), this.pixelConvolerR, this.pixelConvolerG, this.pixelConvolerB);
                k++;
            }
        }
    }


/* 

 * ##########################################################################################
 * #   METHODE ENREGISTREMENT IMAGE --   #
 * ##########################################################################################
 
*/

    public void saveImgColor(String nameOut){


    DataBufferInt dataBuffer = new DataBufferInt(this.post_process_pixels, this.post_process_pixels.length);
    int samplesPerPixel = 4;
    ColorModel colorModel = new DirectColorModel(32,0xFF0000,0xFF00,0xFF,0xFF000000);
    WritableRaster raster = Raster.createPackedRaster(dataBuffer, this.IMG_WIDTH, this.IMG_HEIGHT, this.IMG_WIDTH,((DirectColorModel) colorModel).getMasks(), null);
    BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
    RenderedOp op1 = JAI.create("filestore", image, nameOut, "png");
    }
    

    /* 

 * ##########################################################################################
 * #   METHODE UTILE POUR LES TRAITEMENTS --   #
 * ##########################################################################################
 
*/

    public int getA(int pixel){
        return (pixel >> 24) & 0xFF;
    }


    public int getR(int pixel){
        return (pixel >> 16) & 0xFF;
    }



    /**
     * 
     * @param pixel (int) : le pixel codé sur 32 bits avec 4 canaux en ARGB.
     * @return octet (int) : le canal Green (sur 1 octet = 8 bits)
     */

    public int getG(int pixel){
        return (pixel >> 8) & 0xFF;
    }




    /**
     * 
     * @param pixel (int) : le pixel codé sur 32 bits avec 4 canaux en ARGB.
     * @return octet (int) : le canal Blue (sur 1 octet = 8 bits)
     */

    public int getB(int pixel){
        return pixel & 0xFF;
    }

    /**
     * @brief cette méthode récupère les 4 canaux de l'image en type int et les regroupent pour former un pixel
     * au format ARGB
     * @param a (int) : canal Alpha
     * @param r (int) : canal Red
     * @param g (int) : canal Green
     * @param b (int) : canal Blue
     * @return
     */

     public int getPixel(int a, int r, int g, int b){

        return ((a << 24) | (r << 16) | (g << 8) | b);
    }



}
