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


/* 

 * #############################################################
 * #                 DEBUT DE LA CLASS                         #
 * #############################################################
 
*/

public class TraitementImageNoirBlanc extends TraitementImage{

    private byte pixels [];

    private int IMG_HEIGHT;

    private int IMG_WIDTH;

/* 

 * #############################################################
 * #               CONSTRUCTEUR DE LA CLASSE                   #
 * #############################################################
 
*/

    public TraitementImageNoirBlanc(String file_path){

        super(file_path);

        String fn = super.getPathFile();                    // fn is a String with the filename
        RenderedOp ropimage;                                // a Rendered Operation object will contain the metadata and data
        ropimage = JAI.create("fileload", fn);              // open the file
        BufferedImage bi = ropimage.getAsBufferedImage();   // BufferedImage will contain the data
        this.IMG_WIDTH = ropimage.getWidth();
        this.IMG_HEIGHT  = ropimage.getHeight();
        ColorModel cm = ropimage.getColorModel();  
        if ((bi.getType() == BufferedImage.TYPE_BYTE_GRAY) && (cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY)) {
            Raster r = ropimage.getData();                              // from the Raster object we retrieve first a DataBufferByte
            DataBufferByte db = (DataBufferByte) (r.getDataBuffer());   // then the real array of bytes
            this.pixels = db.getData();
            
        }
        else{
            this.pixels = null;
        }


    }
/* 

 * #############################################################
 * #                        GETTERS                            #
 * #############################################################
 
*/
    public int getHeight(){

        return this.IMG_HEIGHT;
    }

    public int getWidth(){

        return this.IMG_WIDTH;
    }

    public byte [] getTabPixels(){
        return this.pixels;

    }


/* 

 * #############################################################
 * #    CONSTRCUTION HASHMAP OCCURENCES CHAQUE COULEUR         #
 * #############################################################
 
*/

    public HashMap<Byte,Integer> statsHashMap(String name_img){

        HashMap<Byte,Integer> sphm = new HashMap<Byte,Integer>(256); //car 256 nuances de gris

        for (int i = 0; i < 256; i++) {
            sphm.put((byte) i,0);
        }

        int occ = 0;

        for (int i = 0; i < pixels.length; i++) {

            occ = sphm.get(this.pixels[i]); //on recupere l'occurence

            occ = occ + 1; //on l'incremente

            sphm.put(this.pixels[i],occ); //on la remet a sa place
        }

        return sphm; 

    }

/* 

 * #############################################################
 * #                HISTOGRAMME NIVEAU DE GRIS                 #
 * #############################################################
 
*/

    public void barPlotToFile(String name_img, String destination){

        Path destination_path = Paths.get(destination);

        HashMap<Byte,Integer> sphm = statsHashMap(name_img);


        for (int i = 0; i < sphm.size(); i++) {
            
            try (BufferedWriter w = Files.newBufferedWriter(destination_path, StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

                    w.write(String.valueOf(i));
                    w.write(";");
                    w.write(String.valueOf((int) sphm.get((byte) i)));
                    w.newLine(); // sinon, pas de retour à la ligne

            } 
            catch (IOException ioe) {

                System.err.println("Error Write File.");
            }
        }
    }

/* 

 * #############################################################
 * #          METHODES POUR SAUVEGARDER LES IMAGES             #
 * #############################################################
 
*/


    public void saveImage(byte[] pxgs, String nameOut){
        int IMG_WIDTH=getWidth();
        int IMG_HEIGHT=getHeight();
        SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, IMG_WIDTH, IMG_HEIGHT, 1); 
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);           
        image.setData(Raster.createRaster(sm, new DataBufferByte(pxgs, pxgs.length), new Point()));             
        JAI.create("filestore", image, nameOut, "PNG");
    }

/* 

 * #############################################################
 * #   METHODE POUR TRANSFORMER L'IMAGE SOUS FORME DE MATRICE  #
 * #############################################################
 
*/

    public int [][] imageNoirBlanc(String path_img){

        int indexPxg=0;
        int [][] img = new int [this.IMG_WIDTH][this.IMG_HEIGHT]; //initialisation du tableau image
        for (int l=0; l<this.IMG_WIDTH; l++){
            for (int c=0; c<this.IMG_HEIGHT; c++){
                img[l][c]=(int) this.pixels[indexPxg] & 0xFF;; // puis on le stock
                indexPxg++;
            }
        }
        return img;
    }


/* 

 * ###########################################################################################################
 * #                                METHODES TRAITEMENTS DES IMAGES                                          #
 * ###########################################################################################################
 
*/

/* 

 * #############################################################
 * #                       ASSOMBRISSEMENT                     #
 * #############################################################
 
*/

    /**
     * ----- METHODES ASSOMBRISSEMENT ----- 
     * Methode qui à pour objectif d'assombrir l'image 
     * en appliquant a chaque pixel pj=pj*pj puis en le normalisant
     * @param img
     * @return
     */

     public int [][] assombrissement(int [][] img){

        int width=getWidth(); // on recupere la taille de l'image
        int height=getHeight();
        int [][] imgAssombri = new int [width][height]; //on défini notre matrice du tableau assombris
        int pixelAssombri=0;
        int pixelNormalise=0;

        for (int l=0; l< width; l++){
            for (int c=0; c< height; c++){
                pixelAssombri=img[l][c]*img[l][c]; //on applique la methode pour assombrir le pixel
                pixelNormalise=pixelAssombri/255; //on le normalise
                imgAssombri[l][c]=pixelNormalise; // puis on le stock
            }
        }

        return imgAssombri;
    }


/* 

 * #############################################################
 * #                       ECLAIRAGE                           #
 * #############################################################
 
*/
    /**
     * METHODES ECLAIRAGE
     * @param img
     * @return
     */

     public int [][] eclairage(int [][] img){

        int width=getWidth(); // on recupere la taille de l'image
        int height=getHeight();
        int [][] imgEclairer = new int [width][height]; //on défini notre matrice du tableau assombris
        double pixelEclairer=0;
        double pixelNormalise=0;

        for (int l=0; l< width; l++){
            for (int c=0; c< height; c++){
                pixelEclairer=Math.sqrt((double)img[l][c]); //on applique la methode pour assombrir le pixel
                pixelNormalise=pixelEclairer*16; //on le normalise
                imgEclairer[l][c]=(int)pixelNormalise; // puis on le stock
            }
        }

        return imgEclairer;
    }

/* 

 * #############################################################
 * #             REECRITURE IMAGE POUR SAUVEGARDE              #
 * #############################################################
 
*/
    /**
     * ---- METHODE SORTIE IMAGE
     * cette méthode réécrit l'image sous le format d'un tableau de byte afin de pouvoir sauvegarder l'image
     * @param img
     * @return
     */

    public byte [] imageModifie(int [][] img){

        int width=getWidth(); //on recupere la taille de l'image
        int height=getHeight();
        byte [] imgModif = new byte [width*height]; //on definit notre tableau de sortie
        int indexTabImg=0;
        byte b;

        for(int l=0; l<width; l++){
            for (int c=0; c<height; c++){
                b=(byte) img[l][c]; //on caste notre int pour avoir un sa valeur en byte signé
                imgModif[indexTabImg]=b; //puis on le stock
                indexTabImg++;
            }
        }
        return imgModif;
    }


/* 

 * #############################################################
 * #                        CONTRASTE                          #
 * #############################################################
 
*/
    /**
     * ----- METHODE CONTRASTE ------
     * @return
     */

     public int [][] contraste(int [][] img){

        int width=getWidth(); // on recupere la taille de l'image
        int height=getHeight();
        int [][] imgContraster = new int [width][height]; //on défini notre matrice du tableau contraste
        int pixelContraster=0;

        for (int l=0; l< width; l++){
            for (int c=0; c< height; c++){
                pixelContraster=255-img[l][c]; //on applique la methode pour contraster le pixel
                imgContraster[l][c]=pixelContraster; // puis on le stock
            }
        }

        return imgContraster;
    }

}
