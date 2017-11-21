import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.io.*;
class pulkit
{

	public static void main(String args[])
	{
		String s="hello",a,b;
		a=bytesToHex(s.getBytes());
		System.out.println(a);
		b=(hexToBytes(a));
		System.out.println(b);
	}
	private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
    public static byte[] hexToBytes(String s) {
    byte[] b = new byte[s.length() / 2];
    for (int i = 0; i < b.length; i++) {
      int index = i * 2;
      int v = Integer.parseInt(s.substring(index, index + 2), 16);
      b[i] = (byte) v;
    }
    return b;
  }
}