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
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.PackedColorModel;
import java.awt.image.WritableRaster;
import java.awt.color.ColorSpace;
import java.awt.image.DataBufferByte;

/* 

 * #############################################################
 * #           PACKAGES TRAITEMENT DES FICHIERS                #
 * #############################################################
 
*/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;


/* 

 * #############################################################
 * #        PACKAGES STRUCTURE DE DONNEES                      #
 * #############################################################
 
*/



public abstract class TraitementImage {
    
/* 

 * #############################################################
 * #        ATTRIBUTS DE LA CLASSE                             #
 * #############################################################
 
*/
    private Path file_path;                // chemin de l'image

    private String file_name;             // nom du fichier

/* 

 * #############################################################
 * #        CONSTRUCTEUR(S) DE LA CLASSE                       #
 * #############################################################
 
*/

    public TraitementImage(String file_path){

        this.file_path = Paths.get(file_path);

        if (!Files.exists(this.file_path)) {                          // L'image n'existe pas.

            this.file_path = null;

            this.file_name = null;
        }
        else{                                                         // L'image existe

            this.file_path = Paths.get(file_path); 
            
            this.file_name = this.file_path.getFileName().toString(); // uniquement le nom du fichier

        }

    }

    public String getPathFile(){

        return this.file_path.toString();

    }

    public String getNameFile(){

        return this.file_name;

    }



/* 

 * ################################################################################
 * #         METHODES DE TRAITEMENTS POUR LES IMAGES EN NIVEAU DE GRIS            #
 * ################################################################################
 * 
 *  Il s'agit ici des méthodes communes aux images au format ARGB et en niveau de GRIS
 
*/

    abstract public void assombrissement();
   
    abstract public void eclairage();
   
    abstract public void contraste();


}


