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

public class TraitementImageNoirBlanc extends TraitementImage{

    private Path path_img;
    private int IMG_WIDTH;
    private int IMG_HEIGHT;

    /**
     * ---- CONSTRUCTOR ----
     * @param path_img
     */
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

    public byte [] RecupImageNoirBlanc(String path_img){

        String fn=path_img;                
        RenderedOp ropimage;                                
        ropimage = JAI.create("fileload", fn);             
        BufferedImage bi = ropimage.getAsBufferedImage();   
        this.IMG_WIDTH = ropimage.getWidth();
        this.IMG_HEIGHT  = ropimage.getHeight();
        ColorModel cm = ropimage.getColorModel();  
        if ((bi.getType() == BufferedImage.TYPE_BYTE_GRAY) && (cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY)) {
            Raster r = ropimage.getData();                              
            DataBufferByte db = (DataBufferByte) (r.getDataBuffer());   
            byte[] pxg = db.getData();
            return pxg;
        }
        else{
            return null;
        }
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
        int IMG_WIDTH=getWidth();
        int IMG_HEIGHT=getHEIGHT();
        SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, IMG_WIDTH, IMG_HEIGHT, 1); 
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);           
        image.setData(Raster.createRaster(sm, new DataBufferByte(pxgs, pxgs.length), new Point()));             
        JAI.create("filestore", image, nameOut, "PNG");
    }

    /** ----- METHODE POUR TRANSFORMER L'IMAGE SOUS FORME DE MATRICE ----- **/

    public int [][] imageNoirBlanc(String path_img){

        int indexPxg=0;
        // int pixInt=0; //entier qui contient le byte correspondant à la valeur du pixel
        // String pixHexa; // string qui contient la valeur hexadecimal du byte
        byte pxg[] = RecupImageNoirBlanc(path_img);
        int [][] img = new int [this.IMG_WIDTH][this.IMG_HEIGHT]; //initialisation du tableau image
        for (int l=0; l<this.IMG_WIDTH; l++){
            for (int c=0; c<this.IMG_HEIGHT; c++){
                // pixHexa = String.format("%08x", pxg[indexPxg]); //tranforme le byte en hexa decimal sous forme de String
                // pixInt = Integer.parseInt(pixHexa,16); // transforme l'hexa en int
                img[l][c]=(int) pxg[indexPxg] & 0xFF;; // puis on le stock
                indexPxg++;
            }
        }
        return img;
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

        int width=getWidth(); // on recupere la taille de l'image
        int height=getHEIGHT();
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

    /**
     * METHODES ECLAIRAGE
     * @param img
     * @return
     */

     public int [][] eclairage(int [][] img){

        int width=getWidth(); // on recupere la taille de l'image
        int height=getHEIGHT();
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

    /**
     * ---- METHODE SORTIE IMAGE
     * cette méthode réécrit l'image sous le format d'un tableau de byte afin de pouvoir sauvegarder l'image
     * @param img
     * @return
     */
    public byte [] imageModifie(int [][] img){

        int width=getWidth(); //on recupere la taille de l'image
        int height=getHEIGHT();
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

    /**
     * ----- METHODE CONTRASTE ------
     * @return
     */

     public int [][] contraste(int [][] img){

        int width=getWidth(); // on recupere la taille de l'image
        int height=getHEIGHT();
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

    /**
     * ----- METHODE TRANSFORMATION IMAGE COULEUR VERS IMAGE ------
     * @return
     */
    

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