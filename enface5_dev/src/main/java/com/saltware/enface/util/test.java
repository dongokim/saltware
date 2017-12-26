package com.saltware.enface.util;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		test f = new test();

		System.out.println(f.encrypt("TEST08"));

		String key = "abcd123456789kjd";
		byte[] byteKey = key.getBytes();
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		byteKey = sha.digest(byteKey);
		byteKey = Arrays.copyOf(byteKey, 32); // use only first 256 bit
		SecretKeySpec secretKey = new SecretKeySpec(byteKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

	}

	private final String KEYNAME = "nfaator!plaeemo!";
	private final String ALGORITHM = "AES";
	public static final String AES_ECB_NOPADDING = "AES/ECB/NoPadding";

	public String encrypt(final String source) throws Exception {
		byte[] eArr = null;
		SecretKeySpec skeySpec = new SecretKeySpec(KEYNAME.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ECB_NOPADDING);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		eArr = cipher.doFinal(this.addPadding(source.getBytes()));
		return fromHex(eArr);
	}

	public byte[] encryptToByteArray(final String source) throws Exception {
		byte[] eArr = null;
		SecretKeySpec skeySpec = new SecretKeySpec(KEYNAME.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ECB_NOPADDING);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		eArr = cipher.doFinal(this.addPadding(source.getBytes()));
		return eArr;
	}

	public String GetEncryptString(String source) throws Exception {
		StringBuilder sbResult = new StringBuilder();
		byte[] eArr = null;
		SecretKeySpec skeySpec = new SecretKeySpec(KEYNAME.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ECB_NOPADDING);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		eArr = cipher.doFinal(this.addPadding(source.getBytes()));
		for (byte b : eArr) {
			sbResult.append(String.format("%02x", b));
		}
		return sbResult.toString();
	}

	public String decrypt(final String source) throws Exception {
		Cipher cipher = Cipher.getInstance(AES_ECB_NOPADDING);
		SecretKeySpec skeySpec = new SecretKeySpec(KEYNAME.getBytes(), ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] eArr = this.removePadding(cipher.doFinal(this.toBytes(source)));
		return new String(eArr);
	}

	private byte[] removePadding(final byte[] pBytes) {
		int pCount = pBytes.length;
		int index = 0;
		boolean loop = true;
		while (loop) {
			if (index == pCount || pBytes[index] == 0x00) {
				loop = false;
				index--;
			}
			index++;
		}
		byte[] tBytes = new byte[index];
		System.arraycopy(pBytes, 0, tBytes, 0, index);
		return tBytes;
	}

	private byte[] toBytes(final String pSource) {
		StringBuffer buff = new StringBuffer(pSource);
		int bCount = buff.length() / 2;
		byte[] bArr = new byte[bCount];
		for (int bIndex = 0; bIndex < bCount; bIndex++) {
			bArr[bIndex] = (byte) Long.parseLong(buff.substring(2 * bIndex, (2 * bIndex) + 2), 16);
		}
		return bArr;
	}

	private byte[] addPadding(final byte[] pBytes) {
		int pCount = pBytes.length;
		int tCount = pCount + (16 - (pCount % 16));
		byte[] tBytes = new byte[tCount];
		System.arraycopy(pBytes, 0, tBytes, 0, pCount);
		for (int rIndex = pCount; rIndex < tCount; rIndex++) {
			tBytes[rIndex] = 0x00;
		}
		return tBytes;
	}

	public String fromHex(byte[] pBytes) {
		int pCount = pBytes.length;
		StringBuffer buff = new StringBuffer(pCount * 2);
		for (int pIndex = 0; pIndex < pCount; pIndex++) {
			if (((int) pBytes[pIndex] & 0xff) < 0x10) {
				buff.append(0);
			}
			buff.append(Long.toString((int) pBytes[pIndex] & 0xff, 16));
		}
		return buff.toString();
	}

}
