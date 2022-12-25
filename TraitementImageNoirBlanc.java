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

    private byte pixels [];                // stocke les pixels après lecture d'une image en niveau de gris

    private int pixels_color [];           // stocke les pixels après lecture d'une image ARGB

    private byte post_process_pixels [];   // stocke le résultat après un traitement donné (forcément en niveau de gris dans cette classe)

    private int IMG_HEIGHT;                // hauteur

    private int IMG_WIDTH;                 // largeur

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
        ColorModel cm = ropimage.getColorModel();    
        this.IMG_WIDTH = ropimage.getWidth();
        this.IMG_HEIGHT  = ropimage.getHeight();
        this.post_process_pixels = new byte[this.IMG_HEIGHT*this.IMG_WIDTH];
      
        if ((bi.getType() == BufferedImage.TYPE_BYTE_GRAY) && (cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY)) {
            Raster r = ropimage.getData();                              // from the Raster object we retrieve first a DataBufferByte
            DataBufferByte db = (DataBufferByte) (r.getDataBuffer());   // then the real array of bytes
            this.pixels = db.getData();
            this.pixels_color= null;
            
            
        }
        else{  //l'image est en couleur           
            this.pixels_color = bi.getRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, null, 0, IMG_WIDTH);
            this.pixels = new byte[this.IMG_HEIGHT*this.IMG_WIDTH];
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

    public void barPlotToFile(String destination){

        Path destination_path = Paths.get(destination);

        HashMap<Byte,Integer> sphm = statsHashMap(super.getNameFile());


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

     public void assombrissement(){

        for (int i = 0; i < this.pixels.length; i++) {
            
            this.post_process_pixels[i] = (byte) ((((int) this.pixels[i] & 0xFF)*((int) this.pixels[i] & 0xFF))/255);
        }
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

     public void eclairage(){

        for (int i = 0; i < this.pixels.length; i++) {
            this.post_process_pixels[i] = (byte) (Math.sqrt((double) ((int) this.pixels[i] & 0xFF))*16);
        }

    
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

     public void contraste(){

        for (int i = 0; i < this.pixels.length; i++) {
            this.post_process_pixels[i] = (byte) (255 - this.pixels[i]);
        }
       
    }





/* 

 * #############################################################
 * #       TRANSFORMATION IMG_COULEUR EN ECHELLE DE GRIS       #
 * #############################################################
 
*/

    /**
     * 
     * @param pixel (int) : le pixel codé sur 32 bits avec 4 canaux en ARGB.
     * @return octet (int) : le canal Alpha (sur 1 octet = 8 bits)
     */

    public int getA(int pixel){
        return (pixel >> 24) & 0xFF;
    }



    /**
     * 
     * @param pixel (int) : le pixel codé sur 32 bits avec 4 canaux en ARGB.
     * @return octet (int) : le canal Red (sur 1 octet = 8 bits)
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
     * @brief cette méthode parcours le tableau de (int) de l'image ARGB et rempli le tableau de (byte)
     * en transformant chaque plixel en niveau de GRAY sur un seul canal.
     */

    public void fromColorToGray(){

        int r,g,b = 0;

        int pixel = 0;

        for (int i = 0; i < this.pixels_color.length; i++) {

            r = getR(pixels_color[i]);

            g = getG(pixels_color[i]);

            b = getB(pixels_color[i]);

            pixel = (int) (0.21*r + 0.72*g + 0.07*b);

            this.post_process_pixels[i] = (byte) pixel;

        }
    }




    /**
     * @brief cette méthode trasnsforme une image en ARGB en GRAY et écrit le résultat dans un fichier au format png.
     */

    public void saveImage(String dest_file){

        // Start by defining the type of image we want to save
        // let's create a model of type TYPE_BYTE, to store an image of
        // IMG_WIDTH et IMG_HEIGHT dimensions. Last argument is the number of bands
        // for grayscale = 1 band (no surprises! gray-scale needs only one channel)
        SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,IMG_WIDTH,IMG_HEIGHT,1);
        // The same way we got the image data in a BufferedImage when opening a file
        // we now need to create a BufferedImage where to put the image data
        // The BufferedImage object will be an image of IMG_WIDTH and IMG_HEIGHT
        // dimension. It's also a TYPE_BYTE_GRAY image
        BufferedImage image = new BufferedImage(this.IMG_WIDTH, this.IMG_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        // let's put the data from b (declared as "byte[] b;")
        image.setData(Raster.createRaster(sm, new DataBufferByte(this.post_process_pixels, this.post_process_pixels.length), new Point()));
        // and save it in a file!
        // here, the image will be called "res-gs.png"
        JAI.create("filestore",image,dest_file,"PNG");

    }
}
