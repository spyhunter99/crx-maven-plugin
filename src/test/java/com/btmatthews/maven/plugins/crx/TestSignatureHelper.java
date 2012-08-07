package com.btmatthews.maven.plugins.crx;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 07/08/12
 * Time: 00:52
 * To change this template use File | Settings | File Templates.
 */
public class TestSignatureHelper {

    /**
     * The private key used to generate the signature.
     */
    private static final byte[] PRIVATE_KEY = new byte[]{
            48, -126, 1, 84, 2, 1, 0, 48,
            13, 6, 9, 42, -122, 72, -122, -9,
            13, 1, 1, 1, 5, 0, 4, -126,
            1, 62, 48, -126, 1, 58, 2, 1,
            0, 2, 65, 0, -26, -82, 89, 10,
            13, -22, 29, 4, 127, 11, 7, -33,
            49, 7, -44, 49, 31, 59, -60, -39,
            -20, 6, 71, 91, 63, 97, 2, 51,
            2, -68, -19, 32, -51, -20, 61, 111,
            41, 17, -121, 98, 63, 114, -109, -121,
            62, 69, -120, -14, -17, 38, 18, 111,
            -24, -89, -97, -15, -44, 67, -126, 109,
            23, 106, 53, 47, 2, 3, 1, 0,
            1, 2, 64, 115, 86, -71, 46, 77,
            -58, -64, -69, 126, -54, 5, 37, -109,
            94, 86, -70, 56, -29, -87, -119, 76,
            121, -41, 51, -56, 124, -72, -100, -56,
            -63, -83, -51, -58, 34, 18, 12, 28,
            -67, 47, 0, 8, 123, 4, -61, -9,
            -55, -32, -20, 26, 74, -115, -86, 76,
            74, 114, -20, 45, 45, 41, -28, 94,
            59, 0, 1, 2, 33, 0, -11, -106,
            71, -67, -124, -102, 11, -4, -1, 81,
            24, -68, 66, -7, -123, 111, 110, -120,
            -36, 74, -87, -90, -110, 21, 101, -47,
            -122, -89, -78, 8, -7, -51, 2, 33,
            0, -16, 118, 69, 85, 70, -28, -29,
            -22, -47, 43, 96, 77, 2, 48, 82,
            23, -118, 47, -26, 59, -117, 23, -113,
            111, 46, 63, 77, -94, 85, 34, 126,
            -21, 2, 32, 64, 10, -102, 86, -97,
            119, -108, 92, -69, 47, -88, -91, -97,
            73, 118, 52, 98, -68, -87, -59, -122,
            -117, -5, 88, 61, 30, -16, 96, 86,
            -102, -28, -103, 2, 32, 5, -119, 99,
            -99, 76, 13, -105, 37, -33, -3, 20,
            -79, -111, 60, 85, -105, -113, 41, -3,
            46, 31, -52, -62, 19, -72, 71, -30,
            -36, -69, 3, 25, 83, 2, 33, 0,
            -94, -90, 26, -93, 25, 62, -109, -125,
            118, 92, -45, 119, -40, -95, 38, -92,
            -25, 121, 36, -19, -89, -102, 33, 70,
            -72, -7, 29, -12, -90, 124, 30, 65
    };

    /**
     * The public key used to verify the signature.
     */
    private static final byte[] PUBLIC_KEY = new byte[]{
            48, 92, 48, 13, 6, 9, 42, -122,
            72, -122, -9, 13, 1, 1, 1, 5,
            0, 3, 75, 0, 48, 72, 2, 65,
            0, -26, -82, 89, 10, 13, -22, 29,
            4, 127, 11, 7, -33, 49, 7, -44,
            49, 31, 59, -60, -39, -20, 6, 71,
            91, 63, 97, 2, 51, 2, -68, -19,
            32, -51, -20, 61, 111, 41, 17, -121,
            98, 63, 114, -109, -121, 62, 69, -120,
            -14, -17, 38, 18, 111, -24, -89, -97,
            -15, -44, 67, -126, 109, 23, 106, 53,
            47, 2, 3, 1, 0, 1
    };

    /**
     * Sampe data used for testing.
     */
    private static final byte[] DATA = new byte[]{
            -49, -9, 58, -26, 2, -119, -39, -24,
            -128, -77, -63, 36, 25, 108, -112, -7,
            110, 118, -87, -123, 15, 51, -33, 77,
            62, -48, 59, -19, 45, -76, 103, -49
    };

    /**
     * The expected signature for DATA.
     */
    private static final byte[] SIGNATURE = {
            -62, -75, -82, -115, -94, 3, -81, -58,
            -47, 72, -92, 18, 39, 71, 91, -62,
            -68, 115, 97, -127, 82, -52, -93, -51,
            54, -34, -102, -88, -78, 64, 98, 66,
            17, -13, 14, 116, 118, -21, -126, -48,
            97, 102, -51, 46, 110, 5, 127, -41,
            74, 6, -109, -44, -13, 50, -62, 17,
            100, 0, 69, -91, 123, 73, -82, -34
    };

    /**
     * The {@link CRXSignatureHelper} being unit tested.
     */
    private SignatureHelper signatureHelper;

    /**
     * The key factory is used to convert binary data to public and private keys.
     */
    private KeyFactory keyFactory;

    @Before
    public void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        signatureHelper = new CRXSignatureHelper();
        keyFactory = KeyFactory.getInstance("RSA", "BC");
    }

    /**
     * Verify the {@link SignatureHelper#sign(byte[], java.security.PrivateKey)} method.
     *
     * @throws Exception If there was an unexpected problem.
     */
    @Test
    public void testSign() throws Exception {
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(PRIVATE_KEY);
        final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        final byte[] signature = signatureHelper.sign(DATA, privateKey);
        assertNotNull(signature);
        assertArrayEquals(SIGNATURE, signature);
    }


    /**
     * Verify the {@link SignatureHelper#check(byte[], java.security.PublicKey, byte[])}  method.
     *
     * @throws Exception If there was an unexpected problem.
     */
    public void testCheck() throws Exception {
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(PUBLIC_KEY);
        final PublicKey publicKey = keyFactory.generatePublic(keySpec);
        final boolean result = signatureHelper.check(DATA, publicKey, SIGNATURE);
        assertTrue(result);
    }
}
