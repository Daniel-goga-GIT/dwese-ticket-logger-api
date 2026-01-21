package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyConfig {

    @Value("${jwt.keystore.path}") // Ruta del keystore
    private String keystorePath;

    @Value("${jwt.keystore.password}") // Contraseña del keystore
    private String keystorePassword;

    @Value("${jwt.keystore.alias}") // Alias del par de claves
    private String keystoreAlias;

    /**
     * Carga el par de claves (privada y pública) desde el keystore configurado.
     *
     * @return KeyPair con la clave privada y pública.
     * @throws GeneralSecurityException si falla la carga del keystore o las claves.
     * @throws IOException si no se puede leer el fichero del keystore.
     */
    @Bean
    public KeyPair jwtKeyPair() throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keystoreAlias, keystorePassword.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(keystoreAlias).getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }
}
