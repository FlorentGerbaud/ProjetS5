/** ----- PACKAGES LECTURE IMAGE PNG ----- **/
import javax.media.jai.RenderedOp;


/** ----- PACKAGES TRAITEMENTS FICHIERS ----- **/

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;


/** ----- PACKAGES STRUCTURES DE DONNEES ------ **/



public abstract class TraitementImage {
    
    /** ----- ATTRIBUTS DE CLASSE ------ **/

    private Path path_img;


    /** ----- CONSTRUCTEUR ----- **/

    public TraitementImage(String path_img){

        this.path_img = Paths.get(path_img);

        if (!Files.exists(this.path_img)) {

            this.path_img = null;
        }

    }

    /** ------ METHODES OUVERTURE DES FICHIERS ET VERIFICATIONS D'USAGE ------ **/


    /** ------ METHODES RECUPERATIONS DES PIXELS DE L'IMAGE ------ **/


    /** ------ METHODES TRAITEMENTS DES IMAGES GRIS ------ **/

    /**
     *  ------ METHODES Assombrissement ------
     * @param
     * @param
     */

     /**
     *  ------ METHODES CONTRASTE ------
     * @param
     * @param
     */

     /**
     *  ------ METHODES ECLERAGE ------
     * @param
     * @param
     */

    /** ------ METHODES TRAITEMENTS DES IMAGES RGBA ------ **/

}
