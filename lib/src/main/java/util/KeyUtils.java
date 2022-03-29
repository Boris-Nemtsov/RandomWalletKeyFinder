package util;

import java.util.HashSet;

import org.bouncycastle.util.Arrays;

import com.google.common.primitives.Bytes;

public class KeyUtils {
	public static boolean isNotDuplicatedMnemonic(String mnemonic) {
		String[] mnemonicSplit = mnemonic.split(" ");
		HashSet<String> words = new HashSet<String>();
		
		for (String word : mnemonicSplit) {
			if (words.contains(word) == true) {
				break;
			}
			words.add(word);
		}
		
		return words.size() == mnemonicSplit.length;
	}
	
	public static byte[] arraysCopyOf(byte[] initBytes, int ensureLength) {
		if (initBytes.length >= ensureLength) {
			return Arrays.copyOf(initBytes, ensureLength);
		}
		
		byte[] padBytes = new byte[ensureLength - initBytes.length];
		return Bytes.concat(padBytes, initBytes);
	}
}
