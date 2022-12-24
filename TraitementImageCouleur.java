/** ----- PACKAGES LECTURE IMAGE PNG ----- **/ //comment faire pour ne pas tout import ? peut etre en package ?
import javax.media.jai.RenderedOp;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.PackedColorModel;
import java.awt.image.WritableRaster;
import java.awt.color.ColorSpace;
import java.awt.image.DataBufferByte;
import java.awt.Point;
import java.awt.image.SampleModel;
import java.lang.Math;

/** ----- PACKAGES TRAITEMENTS FICHIERS ----- **/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

public class TraitementImageCouleur extends TraitementImage {
    

    private Path path_img;
    private int IMG_WIDTH;
    private int IMG_HEIGHT;

    /**
     * ---- CONSTRUCTOR ----
     * @param path_img
     */
    public TraitementImageCouleur(String path_img){

        super(path_img); //à regarder c'est bizare ce truc la 
        this.path_img = Paths.get(path_img);

        if (!Files.exists(this.path_img)) {

            this.path_img = null;
        }
        this.IMG_HEIGHT=0;
        this.IMG_WIDTH=0;
    }

    /** ----- METHODES POUR RECUPERER LES IMAGES ----- **/

    public byte [] RecupImageNoirBlanc(String path_img){

        return null;
    }

    public int [] RecupImageCouleur(String path_img){
        String fn=path_img;                                          
        RenderedOp ropimage;                                
        ropimage = JAI.create("fileload", fn);              
        BufferedImage bi = ropimage.getAsBufferedImage();   
        int IMG_WIDTH = ropimage.getWidth();
        int IMG_HEIGHT = ropimage.getHeight();
        ColorModel cm = ropimage.getColorModel();           
        if (cm.getColorSpace().getType() == ColorSpace.TYPE_RGB) {
            int[] pxc = bi.getRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, null, 0, IMG_WIDTH);
            return pxc;
        }
        else{
            return null;
        }
    }

    /** ----- METHODES POUR SAUVEGARDER LES IMAGES ----- **/

    public void saveImage(byte[] pxgs, String nameOut){
        
    }

    /** ----- METHODE POUR TRANSFORMER L'IMAGE SOUS FORME DE MATRICE ----- **/

    public int toRGB(int [] pixelImg){

        return 0;
    }

    public int [][] imageCouleur(String path_img){

        return null;
    }

    /**  ------------------------------------ METHODE TRAITEMENT ------------------------------------*/

    /**
     * ----- METHODES ASSOMBRISSEMENT ----- 
     * Methode qui à pour objectif d'assombrir l'image 
     * en appliquant a chaque pixel pj=pj*pj puis en le normalisant
     * @param img
     * @return
     */

    public int [][] assombrissement(int [][] img){

        return null;
    }

    /**
     * METHODES ECLAIRAGE
     * @param img
     * @return
     */

     public int [][] eclairage(int [][] img){

        return null;
    }

    /**
     * ---- METHODE SORTIE IMAGE
     * cette méthode réécrit l'image sous le format d'un tableau de byte afin de pouvoir sauvegarder l'image
     * @param img
     * @return
     */
    public byte [] imageModifie(int [][] img){

        return null;
    }

    /**
     * ----- METHODE CONTRASTE ------
     * @return
     */

     public int [][] contraste(int [][] img){

        return null;
    }

    /**
     * ----- METHODE TRANSFORMATION IMAGE COULEUR VERS IMAGE GRIS ------
     * @return
     */
    

    /** ----- GETTERS ----- **/

    public int getWidth(){
        return this.IMG_WIDTH;
    }

    public int getHEIGHT(){
        return this.IMG_HEIGHT;
    }


}
