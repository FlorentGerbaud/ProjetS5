public class TraitementImageNB_C {
    public static void main(String[] args){

        InterfaceUserNB imageToProcess = new InterfaceUserNB();
        boolean pt=imageToProcess.plusieurTraitement(); //variable qui definit si l'utilisateur veut faire plusieur traitement sur des images
        String stop;

        if(pt==true){

            stop=imageToProcess.stop(); //variable pour arreter le traitement
            while (!stop.equals("stop")){
                imageToProcess.GestionTraitement(); //applique le menu de traitement image
                stop=imageToProcess.stop();
            }
        }
        else{

            imageToProcess.GestionTraitement();
        }
    }
}
