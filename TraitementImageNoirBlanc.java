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
import java.io.FileNotFoundException;

/* 

 * #############################################################
 * #                       EXCEPTION                           #
 * #############################################################
 
*/

import java.io.IOException;
import java.io.PrintWriter;

/* 

 * #############################################################
 * #        PACKAGES STRUCTURE DE DONNEES                      #
 * #############################################################
 
*/

import java.util.HashMap;
import java.util.Set;
import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  


/* 

 * #############################################################
 * #                 DEBUT DE LA CLASS                         #
 * #############################################################
 
*/


public class TraitementImageNoirBlanc extends TraitementImage{

    private byte pixels [];                // stocke les pixels après lecture d'une image en niveau de gris

    private int pixels_color [];           // stocke les pixels après lecture d'une image ARGB

    private int [][] pixels_matrix;        // image stockée sous forme de matrice

    private int conv[][];                  // résultat de la convolution

    private int dimConv;                   // dimension de la matrice de convolution

    private int [][] voisins_matrix;       // voisins d'un pixel

    private byte post_process_pixels [];   // stocke le résultat après un traitement donné (forcément en niveau de gris dans cette classe)

    private int IMG_HEIGHT;                // hauteur

    private int IMG_WIDTH;                 // largeur

    private boolean iscolor;               // image couleur ou non

    private int sum;

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
        this.pixels_matrix=new int[this.IMG_HEIGHT][this.IMG_WIDTH];
        this.post_process_pixels = new byte[this.IMG_HEIGHT*this.IMG_WIDTH];
      
        if ((bi.getType() == BufferedImage.TYPE_BYTE_GRAY) && (cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY)) {
            Raster r = ropimage.getData();                              // from the Raster object we retrieve first a DataBufferByte
            DataBufferByte db = (DataBufferByte) (r.getDataBuffer());   // then the real array of bytes
            this.pixels = db.getData();
            this.pixels_color= null;
            this.iscolor = false;
            
            
        }
        else{  //l'image est en couleur           
            this.pixels_color = bi.getRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, null, 0, IMG_WIDTH);
            this.pixels = new byte[this.IMG_HEIGHT*this.IMG_WIDTH];
            this.iscolor = true;
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

    public boolean isColor(){

        return this.iscolor;
    }



/* 

 * #############################################################
 * #    CONSTRCUTION HASHMAP OCCURENCES CHAQUE COULEUR         #
 * #############################################################
 
*/

    private HashMap<Byte,Integer> statsHashMap(String name_img){

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

    public void barPlotToFile(String destination) throws FileNotFoundException{

        Path destination_path = Paths.get(destination);

        HashMap<Byte,Integer> sphm = statsHashMap(super.getNameFile());

        boolean iscreated = super.createFile(destination); // file doesn't exist , so it's created

        if(iscreated == false){                            // file already exist so clear file

            PrintWriter writer = new PrintWriter(destination);
            writer.print("");
            writer.close();
        }

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

    /* 

 * #############################################################
 * #                       CONVOLUTION                     #
 * #############################################################
 
*/

    /**
     * Methode qui vas se charger de recupérer une matrice de convolution dans un fichier .csv
     * @param file (String) nom du fichier csv
     */

    public void getConv(String file){

        int l=0; //on initialise l'indice des ligne
        String line = "";  
        String splitBy = ";";  //on initialise le separateur

        try{  
            BufferedReader br = new BufferedReader(new FileReader(file)); 
            line = br.readLine(); //lecture un premier coup en dehors de la boucle
            String[] ligneConv = line.split(splitBy); // on récupère chaque valeur de la premiere ligne de la matrice 
            this.dimConv=ligneConv.length; //On récupère la dimension de la matrice de convolution
            this.conv=new int[this.dimConv][this.dimConv]; // que l'on peut donc initialiser

            for(int c=0;c<this.dimConv; c++){ //maintenant qu'on connais la taille, on recupere la premiere ligne
                this.conv[l][c]=Integer.parseInt(ligneConv[c]);
            }
            l++; //on passe à la ligne suivante
            while ((line = br.readLine()) != null) {  //puis on applique n fois le procédé pour recuperer les autres lignes
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
     * transforme le tableau de byte en matrice de int
     */
    public void setPixelsInMatrice(){

        int k = 0;
        for (int i = 0; i < this.IMG_HEIGHT; i++) {
            for (int j = 0; j < this.IMG_WIDTH; j++) {
                this.pixels_matrix[i][j] = (int) (this.pixels[k] & 0xFF);
                k++;
            }
        }
    }

    /**
     * Methode qui vas se charger de calculer le nombre de colonne et ligne nécessaire pour calculer le voisinage d'un point 
     * auquel on applique une matrice de convolution
     * par exemple pour une MDC 3x3, on aura un voisisnage de 1, pour une MDC de 5x5 on aura un voisinage de 2 
     * ect
     * @param seuil (int) taille de la matrice de conv donc nécessairement impaire
     * @return (int) taille du voisinage
     */
    public int calculPas(int seuil){

        int pas=1;
        int cpt=3;

        while (cpt<seuil){
            pas++;
            cpt=cpt+2;
        }
        return pas;
    }

    /**
     * Méthode qui retourne le pixel convoler
     * @return
     */
    public int convOnePixel(){

        int pixelConvoler=0; //on initialise le pixel convoler à 0
        for(int l=0;l<this.dimConv;l++){
            for (int c=0;c<this.dimConv;c++){

                pixelConvoler=pixelConvoler+this.voisins_matrix[l][c]*this.conv[l][c]; // on applique la formule de convolution

            }
        }
        pixelConvoler = pixelConvoler / this.sum; // normalisation du pixel convoler

        if(pixelConvoler>255 || pixelConvoler<0){
                    
            pixelConvoler = (pixelConvoler >> 0) & 0xFF; // on remet la valeur sur [0,255] si on n'est plus dazns cet intervalles
        }
        return pixelConvoler;
    }

    



    /**
     * Méthode qui récupère le voisinage d'un point en fonction de la taille de la matrice de convolution
     * @param lp (int) indice de la ligne du pixel
     * @param cp (int) indice de la colonne du pixel
     */
    public void recupVoisins(int lp, int cp){

        this.voisins_matrix=new int[this.dimConv][this.dimConv]; //initialisation de la matrice qui contient le voisinage
        for (int l=0;l<this.dimConv; l++){ //on parcours le voisinage de la matrice
            for(int c=0;c<this.dimConv; c++){
                try {
                    this.voisins_matrix[l][c]=this.pixels_matrix[lp+l-calculPas(this.dimConv)][cp+c-calculPas(this.dimConv)]; //si on ne sort pas da la matrice, on stock la valeur recuperer.
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.voisins_matrix[l][c]=0; //si on sort de la matrice, on met un 0 dans la matrice
                }
            }
        }
    }

    /**
     * Méthode qui retourne la somme des coefficients de la matrice de convolution
     */
    public void sumFiltre(){


        for (int i = 0; i < this.dimConv; i++) {
            for (int j = 0; j < this.dimConv; j++) {
                this.sum = this.sum + this.conv[i][j];
            }
        }
        if(this.sum == 0){
            this.sum = 1;
        }  

        System.out.println(this.sum);
       
    }

    /**
     * Méthodes qui gère toute la convolution
     * @param Traitement (String) Traitement que l'utilisateur souhaite appliquer
     */
    public void traitementConvolution(String Traitement){

        choixConv(Traitement); // choix de la matrice de convolution
        sumFiltre(); // calcul somme des coefficients de la matrice de convolution
        setPixelsInMatrice(); // mise sous forme matricielle du tableau de byte
        int k=0;
        for (int l=0;l<this.IMG_HEIGHT; l++){ //parcours de l'image
            for(int c=0;c<this.IMG_WIDTH;c++){
                recupVoisins(l, c); // recupere le voisinage du pixel
                this.post_process_pixels[k]=(byte) convOnePixel(); // stock la convolution dans le tableau de sortie 
                k++;
            }
        }
    }




    /**
     * @brief cette méthode trasnsforme une image en ARGB en GRAY et écrit le résultat dans un fichier au format png.
     */

    public void saveImage(String dest_file){

        SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,IMG_WIDTH,IMG_HEIGHT,1);
        BufferedImage image = new BufferedImage(this.IMG_WIDTH, this.IMG_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        image.setData(Raster.createRaster(sm, new DataBufferByte(this.post_process_pixels, this.post_process_pixels.length), new Point()));
        JAI.create("filestore",image,dest_file,"PNG");

    }
}
