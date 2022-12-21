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

/** ----- PACKAGES TRAITEMENTS FICHIERS ----- **/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

public class TraitementImageNoirBlanc extends TraitementImage{

    private Path path_img;

    public TraitementImageNoirBlanc(String path_img){

        super(path_img); //à regarder c'est bizare ce truc la 
        this.path_img = Paths.get(path_img);

        if (!Files.exists(this.path_img)) {

            this.path_img = null;
        }
    }

    /** ----- METHODES POUR RECUPERER LES IMAGES ----- **/

    public int [] RecupImageCouleur(String path_img){
        String fn=path_img;                                          // fn is a String with the filename
        RenderedOp ropimage;                                // a Rendered Operation object will contain the metadata and data
        ropimage = JAI.create("fileload", fn);              // open the file
        BufferedImage bi = ropimage.getAsBufferedImage();   // BufferedImage will contain the data
        int IMG_WIDTH = ropimage.getWidth();
        int IMG_HEIGHT = ropimage.getHeight();
        ColorModel cm = ropimage.getColorModel();           // get the color mode
        if (cm.getColorSpace().getType() == ColorSpace.TYPE_RGB) {
            int[] pxc = bi.getRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, null, 0, IMG_WIDTH);
            return pxc;
        }
        else{
            return null;
        }
    }

    public byte [] RecupImageNoirBlanc(String path_img){

        String fn=path_img;                                          // fn is a String with the filename
        RenderedOp ropimage;                                // a Rendered Operation object will contain the metadata and data
        ropimage = JAI.create("fileload", fn);              // open the file
        BufferedImage bi = ropimage.getAsBufferedImage();   // BufferedImage will contain the data
        int IMG_WIDTH = ropimage.getWidth();
        int IMG_HEIGHT = ropimage.getHeight();
        ColorModel cm = ropimage.getColorModel();  
        if ((bi.getType() == BufferedImage.TYPE_BYTE_GRAY) && (cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY)) {
            Raster r = ropimage.getData();                              // from the Raster object we retrieve first a DataBufferByte
            DataBufferByte db = (DataBufferByte) (r.getDataBuffer());   // then the real array of bytes
            byte[] pxg = db.getData();
            return pxg;
        }
        else{
            return null;
        }
    }

    /** ----- METHODE POUR TRANSFORMER L'IMAGE SOUS FORME DE MATRICE ----- **/

    /** ----- METHODES ASSOMBRISSEMENT ----- **/

    // public void afficheChemin(){
    //     System.out.println(this.path_img);
    // }
}