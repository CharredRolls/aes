import java.util.Arrays;
import java.util.Iterator;

public class App {
    public static void main(String[] args) throws Exception {

        byte[] cipherText = { (byte) 0x17, (byte) 0x0E, (byte) 0x2E, (byte) 0x69, (byte) 0x80, (byte) 0xEF, (byte) 0x52,
                (byte) 0xBA, (byte) 0x32, (byte) 0x46, (byte) 0x47, (byte) 0xBC, (byte) 0x51, (byte) 0x6C, (byte) 0x89,
                (byte) 0x09,

                (byte) 0x1A, (byte) 0xB6, (byte) 0x21, (byte) 0x20, (byte) 0x81, (byte) 0xC7, (byte) 0x0C, (byte) 0x7E,
                (byte) 0x7D, (byte) 0x6E, (byte) 0xEB, (byte) 0x27, (byte) 0xB6, (byte) 0x03, (byte) 0x3A, (byte) 0xF1,

                (byte) 0xC4, (byte) 0x13, (byte) 0x25, (byte) 0xDA, (byte) 0x93, (byte) 0xB5, (byte) 0xBD, (byte) 0xA3,
                (byte) 0x81, (byte) 0x7D, (byte) 0xE3, (byte) 0x45, (byte) 0xAD, (byte) 0x94, (byte) 0x36, (byte) 0xE4,

                (byte) 0x5B, (byte) 0x7D, (byte) 0x77, (byte) 0xF9, (byte) 0x13, (byte) 0xDF, (byte) 0x93, (byte) 0x0C,
                (byte) 0x27, (byte) 0x96, (byte) 0x00, (byte) 0xB9, (byte) 0xA7, (byte) 0x07, (byte) 0x86, (byte) 0xFB,

                (byte) 0x06, (byte) 0xB4, (byte) 0xF2, (byte) 0xB2, (byte) 0x63, (byte) 0x3D, (byte) 0x5A, (byte) 0xAE,
                (byte) 0x3D, (byte) 0x83, (byte) 0xFD, (byte) 0x8C, (byte) 0xEE, (byte) 0x32, (byte) 0x6B,
                (byte) 0x1A };

        int arg1 = Integer.parseInt(args[0]);

        byte[] fullKey = { (byte) arg1, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x60, // Unknown bytes (0 through
                                                                                            // 4)
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x03 };

        boolean found = false;

        do  {
            do  {
                do  {
                    do {
                        do  {

                            Object decryptRoundKeys = Rijndael_Algorithm.makeKey(Rijndael_Algorithm.DECRYPT_MODE,
                                    fullKey); //
                            int numOfCiphertextBlocks = cipherText.length / 16 - 1; // Each AES block has 16 bytes and
                                                                                    // we need to
                                                                                    // exclude
                                                                                    // the IV
                            byte[] cleartextBlocks = new byte[numOfCiphertextBlocks * 16];

                            byte[] receivedIV = new byte[16];
                            for (int i = 0; i < 16; i++)
                                receivedIV[i] = cipherText[i];
                            byte[] currentDecryptionBlock = new byte[16];

                            boolean b = true;

                            for (int i = 0; i < numOfCiphertextBlocks; i++) {
                                if (b) {
                                for (int j = 0; j < 16; j++)
                                    currentDecryptionBlock[j] = cipherText[(i + 1) * 16 + j]; // Note that the first
                                                                                              // block is the IV

                                byte[] thisDecryptedBlock = Rijndael_Algorithm.blockDecrypt2(currentDecryptionBlock, 0,
                                        decryptRoundKeys);

                                for (int j = 0; j < 16; j++) {
                                    cleartextBlocks[i * 16
                                            + j] = (byte) (thisDecryptedBlock[j] ^ cipherText[i * 16 + j]);
                                    if (cleartextBlocks[i*16 + j] < 32 || cleartextBlocks[i*16 + j] >= 127) {
                                        b = false;
                                    }
                                }
                                }
                            }

                            String recoveredString = new String(cleartextBlocks);

                            byte[] test = recoveredString.getBytes();

                            boolean within = true;
                            for (byte a : test) {
                                int num = Byte.toUnsignedInt(a);
                                if (num < 32 || num >= 127) {
                                    within = false;
                                }
                            }

                            if (within) {
                                System.out.println(recoveredString);
                                System.out.println("KEY IS: " + AESExample.convertToString(fullKey));
                                found = true;
                            }
                            fullKey[4]++;
                            //System.out.println("Testing Key:" + AESExample.convertToString(fullKey));
                        }while(!found && fullKey[4] != (byte) 0x00);
                        fullKey[3]++;
                        fullKey[4] = 0;
                        //System.out.println("Testing Key:" + AESExample.convertToString(fullKey));
                    }while(!found && fullKey[3] != (byte) 0x00); 
                    fullKey[2]++;
                    fullKey[3] = 0;
                    fullKey[4] = 0;
                    //System.out.println("Testing Key:" + AESExample.convertToString(fullKey));
                }while(!found && fullKey[2] != (byte) 0x00);
                fullKey[1]++;
                fullKey[2] = 0;
                fullKey[3] = 0;
                fullKey[4] = 0;
                System.out.println("Testing Key:" + AESExample.convertToString(fullKey));
            }while(!found && fullKey[1] != (byte) 0x00);
            fullKey[0]++;
            fullKey[1] = 0;
            fullKey[2] = 0;
            fullKey[3] = 0;
            fullKey[4] = 0;
            System.out.println("Testing Key:" + AESExample.convertToString(fullKey));
        }while(!found && fullKey[0] != (byte) 0x00);

    }
}
