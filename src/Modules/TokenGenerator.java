package Modules;

import java.security.SecureRandom;

public class TokenGenerator {

    /**
     * Generates a random 8-digit token.
     * @return A string representing the 8-digit token.
     */
    public static String generarToken() {
        SecureRandom random = new SecureRandom();
        int token = 10000000 + random.nextInt(90000000);
        return String.valueOf(token);
    }
}
