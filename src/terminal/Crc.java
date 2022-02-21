package terminal;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * This program computes the CRC checksum of a file. <br>
 */
public class Crc {
    
    public static void checkCRCfiles(){
        
    }

    public static void createCRCfiles(){
        
    }
    
//    public static long checksumBufferedInputStream(String filename) throws IOException {
//        InputStream in = new BufferedInputStream(new FileInputStream(filename));
//        CRC32 crc = new CRC32();
//
//        int c;
//        while ((c = in.read()) != -1) {
//            crc.update(c);
//        }
//
//        System.out.println("Buffered Input Stream:");
//        long start = System.currentTimeMillis();
//        long crcValue = checksumBufferedInputStream(filename);
//        long end = System.currentTimeMillis();
//        System.out.println(Long.toHexString(crcValue));
//        System.out.println((end - start) + " milliseconds");
//        
//        return crc.getValue();
//    }
    
    public static long checksum(String filename) throws IOException {
//        FileInputStream file = new FileInputStream(filename);
        CheckedInputStream check = new CheckedInputStream(new FileInputStream(filename), new CRC32());
        BufferedInputStream in = new BufferedInputStream(check);
        while (in.read() != -1) {
            // Read file in completely
        }
        in.close();
//        System.out.println("Checksum is " + check.getChecksum().getValue());
        return check.getChecksum().getValue();
    }    
}
