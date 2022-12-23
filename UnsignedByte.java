public class UnsignedByte {
    public static void main (String args[]) {
      byte b1 = 127;
      byte b2 = -128;
      byte b3 = -1;
  
      System.out.println(b1);
      System.out.println(b2);
      System.out.println(b3);
      System.out.println(unsignedByteToInt(b1));
      System.out.println(unsignedByteToInt(b2));
      System.out.println(unsignedByteToInt(b3));
      int t = unsignedByteToInt(b3)*b3;
      int i=255;
      byte k = (byte) i;
      System.out.println("i : "+ i + k);
      /*
      127
      -128
      -1
      127
      128
      255
      */
      }
  
    public static int unsignedByteToInt(byte b) {
      return (int) b & 0xFF;
    }
  }
