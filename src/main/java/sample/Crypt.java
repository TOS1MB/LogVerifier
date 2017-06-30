package sample;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;

/**
 * Created by tolgasirvan on 30.06.17.
 */
public class Crypt {
    // http://stackoverflow.com/questions/4895523/java-string-to-sha1

    public static String encryptString(String data) {

        String sha1 = DigestUtils.sha1Hex(data);

        // #Old-Version
        // MessageDigest crypt;
        //
        // try {
        // crypt = MessageDigest.getInstance("SHA-1");
        // crypt.reset();
        // crypt.update(data.getBytes("UTF-8"));
        //
        // } catch (NoSuchAlgorithmException e1) {
        // e1.printStackTrace();
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }

        return sha1;
    }

    public static String generateHmac(String secretKey, String data, int lineCounter
	/* String algorithm e.g. "HmacSHA256" */) {

        // SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(),
        // algorithm);
        //
        // try {
        // Mac mac = Mac.getInstance(algorithm);
        // mac.init(signingKey);
        //
        // byte[] hMac = mac.doFinal(data.getBytes());
        //
        // return hMac;
        // } catch (InvalidKeyException e) {
        // throw new IllegalArgumentException("invalid secret key provided (key
        // not printed for security reasons!)");
        // } catch (NoSuchAlgorithmException e) {
        // throw new IllegalStateException("the system doesn't support algorithm
        // " + algorithm, e);
        // }

        while (lineCounter > 0) {
            secretKey = encryptString(secretKey);
            lineCounter--;
        }

        String hMac = HmacUtils.hmacSha1Hex(secretKey, data);

        return hMac;

    }
}
