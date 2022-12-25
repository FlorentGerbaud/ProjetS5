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
