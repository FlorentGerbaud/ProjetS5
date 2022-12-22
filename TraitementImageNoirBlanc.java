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
    private int IMG_WIDTH;
    private int IMG_HEIGHT;

    public TraitementImageNoirBlanc(String path_img){

        super(path_img); //à regarder c'est bizare ce truc la 
        this.path_img = Paths.get(path_img);

        if (!Files.exists(this.path_img)) {

            this.path_img = null;
        }
        this.IMG_HEIGHT=0;
        this.IMG_WIDTH=0;
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
        this.IMG_WIDTH = ropimage.getWidth();
        this.IMG_HEIGHT  = ropimage.getHeight();
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

    /** ----- METHODES POUR SAUVEGARDER LES IMAGES ----- **/

    // public void saveImage(byte [] pxgs){
    //     //byte[] pxgs;
    //     SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, IMG_WIDTH, IMG_HEIGHT, 1); // only one band (no big surprise here)
    //     BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);           // it is a TYPE_BYTE_GRAY image
    //     image.setData(Raster.createRaster(sm, new DataBufferByte(pxgs, pxgs.length), new Point()));             // take the data from pxg
    //     JAI.create("filestore", image, "res-gs.png", "PNG");
    // }

    /** ----- METHODE POUR TRANSFORMER L'IMAGE SOUS FORME DE MATRICE ----- **/

    public int [][] imageNoirBlanc(String path_img){

        int indexPxg=0;
        int n=0; //entier qui contient le byte correspondant à la valeur du pixel
        String f; // string qui contient la valeur hexadecimal du byte
        byte pxg[] = RecupImageNoirBlanc(path_img);
        int [][] img = new int [this.IMG_WIDTH][this.IMG_HEIGHT];
        for (int l=0; l<this.IMG_WIDTH; l++){
            for (int c=0; c<this.IMG_HEIGHT; c++){
                f = String.format("%08x", pxg[indexPxg]);
                n = Integer.parseInt(f,16); ;
                img[l][c]=n;
                indexPxg++;
            }
        }
        return img;
    }

    /** ----- METHODES ASSOMBRISSEMENT ----- **/

    public int [][] assombrissement(int [][] img){

        int width=getWidth();
        int height=getHEIGHT();
        int [][] imgAssombri = new int [width][height];
        int pixelAssombri=0;
        int pixelNormalise=0;

        for (int l=0; l< width; l++){
            for (int c=0; c< height; c++){
                pixelAssombri=img[l][c]*img[l][c];
                pixelNormalise=pixelAssombri%255;
            }
        }

        return imgAssombri;
    }

    /** ----- GETTERS ----- **/

    public int getWidth(){
        return this.IMG_WIDTH;
    }

    public int getHEIGHT(){
        return this.IMG_HEIGHT;
    }

    

    // public void afficheChemin(){
    //     System.out.println(this.path_img);
    // }
}