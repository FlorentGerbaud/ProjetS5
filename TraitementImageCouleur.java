/* 

 * #############################################################
 * #        PACKAGES LECTURE ET ECRITURE IMAGE PNG             #
 * #############################################################
 
*/

import javax.media.jai.RenderedOp;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import java.awt.image.BufferedImage;
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

/* 

 * #############################################################
 * #                       EXCEPTION                           #
 * #############################################################
 
*/

import java.io.IOException;

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

    private int IMG_HEIGHT;

    private int IMG_WIDTH;

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
        }
        else{   // Erreur : l'image n'est pas en couleur
            this.pixels = null;
            this.IMG_HEIGHT = -1;
            this.IMG_WIDTH = -1;
        }

    }



 /* 

 * ##########################################################################################
 * #                        TRANSFORMATION SOUS FORME DE MATRICE                            #
 * ##########################################################################################
 
*/

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

  
        for (int i = 0; i < this.IMG_HEIGHT; i++) {
            for (int j = 0; j < this.IMG_WIDTH; j++) {
                System.out.print(this.pixels_matrix[i][j]);
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


   public HashMap<Integer,int []> statsHashMapCol(String name_img){


        int r=0; //initialisation des 3 couleurs
        int g=0;
        int b=0;
        int [] occ = {0,0,0}; 
        int [] occR = {0,0,0};
        int [] occG = {0,0,0};
        int [] occB = {0,0,0};

        HashMap<Integer,int []> sphm = new HashMap<Integer,int []>(256); //car 256 nuances de gris

       for (int i = 0; i < 256; i++) {
           sphm.put(i,occ);
       }

       for (int i = 0; i < pixels.length; i++) {

            System.out.println("test : "+occ[0]+";"+occ[1]+";"+occ[2]);

            r = getR(this.pixels[i]);
            //System.out.println(r);

            g = getG(this.pixels[i]);

            b = getB(this.pixels[i]);

            occ = sphm.get(r); //on recupere l'occurence du rouge
            //System.out.println("test : "+occ[0]+";"+occ[1]+";"+occ[2]);
            for (int R=0; R<3; R++){
                if(R==0){
                    occR[R]=occ[R]+1;
                }
                occR[R]=occ[R];
            }
            sphm.put(r,occR); //on remet le tableau d'incrément à sa place
            
            occ = sphm.get(g);
            //System.out.println("test2 : "+occ[0]+";"+occ[1]+";"+occ[2]);
            for (int G=0; G<3; G++){
                if(G==1){
                    occG[G]=occ[G]+1;
                }
                occG[G]=occ[G];
            }
            sphm.put(g,occG); //on remet le tableau d'incrément à sa place

            occ = sphm.get(b);
            for (int B=0; B<3; B++){
                if(B==2){
                    occB[B]=occ[B]+1;
                }
                occB[B]=occ[B];
            }
            sphm.put(b,occB); //on remet le tableau d'incrément à sa place

       }

       return sphm; 

   }

   public void barPlotToFileCol(String destination){

    Path destination_path = Paths.get(destination);

    HashMap<Integer,int []> sphm = statsHashMapCol(super.getNameFile());


    for (int occRGB = 0; occRGB < sphm.size(); occRGB++) {
        
        try (BufferedWriter w = Files.newBufferedWriter(destination_path, StandardOpenOption.CREATE,
            StandardOpenOption.APPEND)) {

                w.write(String.valueOf(occRGB));
                w.write(";");
                w.write(String.valueOf(sphm.get(occRGB)[0]));
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

    public void assombrissement(){


    }
   
    public void eclairage(){


    }
   
    public void contraste(){



    }




}
