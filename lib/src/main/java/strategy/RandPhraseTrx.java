package strategy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.KeyUtils;
import util.Peeked;

public class RandPhraseTrx {
	private static Peeked Peeked = new Peeked("PEEKED_PH_TRX.db");
	
	private static final int[] Bip32ChildPath = new int[] { 44 | Bip32ECKeyPair.HARDENED_BIT,
			195 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0 };

	public static CryptoKeyPair createKeyPair() {
		byte[] entropy = null;
		BigInteger convertInt = null;
		String mnemonic = "";

		while (true) {
			BigInteger randomizeInt = BigIntegers.createRandomInRange(Config.Current.trxFrom, Config.Current.trxTo, new SecureRandom());
			
			entropy = KeyUtils.arraysCopyOf(randomizeInt.toByteArray(), 16);
			convertInt = Numeric.toBigInt(entropy);

			if (Peeked.isPeeked(convertInt) == true) {
				continue;
			}

			Peeked.savePeeked(convertInt);

			mnemonic = MnemonicUtils.generateMnemonic(entropy);
			
			if (KeyUtils.isNotDuplicatedMnemonic(mnemonic) == true) {
				break;
			}
		}
		
		Bip32ECKeyPair parentKey = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, null));
		Bip32ECKeyPair childKey = Bip32ECKeyPair.deriveKeyPair(parentKey, Bip32ChildPath);

		byte[] publicKeyBytes = childKey.getPublicKey().toByteArray();
		byte[] keccakEncoded = new byte[32];
		
		KeccakDigest keccakDigest = new KeccakDigest(256);
		keccakDigest.update(publicKeyBytes, 0, publicKeyBytes.length);
		keccakDigest.doFinal(keccakEncoded, 0);
		
		keccakEncoded = Arrays.copyOfRange(keccakEncoded, 12, 32);
		keccakEncoded = Arrays.prepend(keccakEncoded, (byte) 0x41);
		
		byte[] sha256Encoded = Sha256Hash.hash(Sha256Hash.hash(keccakEncoded));
		byte[] checksumAdded = new byte[25];
		System.arraycopy(keccakEncoded, 0, checksumAdded, 0, 21);
		System.arraycopy(sha256Encoded, 0, checksumAdded, 21, 4);
		
		String address = Base58.encode(checksumAdded);
		
		return new CryptoKeyPair(
				StrategyType.RAND_PH_TRX,
				childKey.getPrivateKey(),
				List.of(address),
				Numeric.toHexStringWithPrefix(childKey.getPrivateKey()));
	}

}
