/**
 * Copyright (c) 2010 Saltware, Inc.
 * 
 * http://www.saltware.co.kr
 * 
 * Kolon Science Valley Bldg 2th. 901, Guro-dong 811, Guro-gu,
 * Seoul, 152-878, South Korea.
 * All Rights Reserved.
 * 
 * This software is the Java based Enterprise Portal of Saltware, Inc.
 * Making any change or distributing this without permission from us is out of law.
 */
package com.saltware.enface.security.passcodec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enview.security.PasswordEncoder;

/**
 * SHA 해시 인코더 구현체.
 * 
 * @author <${developer}> (${email})
 * @author Last changed by: ${developer}
 * @version 3.2.2
 * @since 1.0-SNAPSHOT
 */
public final class ShaPasswordEncoder implements PasswordEncoder {
	
    MessageDigest digester;
    boolean simpleEncryption = false;
    protected Log log = LogFactory.getLog(this.getClass());

    public ShaPasswordEncoder() throws NoSuchAlgorithmException {
        this.digester = MessageDigest.getInstance("SHA-512");
    }

    public ShaPasswordEncoder( String algorithmn) throws NoSuchAlgorithmException {
        this.digester = MessageDigest.getInstance(algorithmn);
    }
    
    public String encode(final String password) {
        return encode(null, password);
    }    

    public String encode(final String username, final String password) {
        if (username == null) {
            return null;
        }
        if (password == null) {
            return null;
        }

        //log.info("algorithm=[SHA-1]::password=["+password+"]");
        byte[] value;
        synchronized(digester) {
            digester.reset();
            value = digester.digest(password.getBytes());
            if (!simpleEncryption)
            {
                // don't allow copying of encoded passwords
                digester.update(username.getBytes());
            }
            value = digester.digest(value);
        }
        return new String(Base64.encodeBase64(value));
    }    
}