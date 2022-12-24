public class TraitementImageNB_C {
    public static void main(String[] args){

        InterfaceUserNB imageToProcess = new InterfaceUserNB();
        boolean pt=imageToProcess.plusieurTraitement();
        String stop;

        if(pt==true){

            stop=imageToProcess.stop();
            while (!stop.equals("stop")){
                imageToProcess.GestionTraitement();
                stop=imageToProcess.stop();
            }
        }
        else{

            imageToProcess.GestionTraitement();
        }
    }
}
