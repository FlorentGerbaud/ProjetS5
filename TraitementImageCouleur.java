public class TraitementImageCouleur extends TraitementImage {
    

    /**
     * ---- CONSTRUCTOR ------
     * @param img_path
     */
    public TraitementImageCouleur(String img_path){

        super(img_path);
    }

    /**
     * ---- METHODE TO GET IMAGE COLOR
     */
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


}
