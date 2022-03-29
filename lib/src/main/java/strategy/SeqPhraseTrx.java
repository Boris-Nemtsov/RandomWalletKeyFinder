package strategy;

import java.math.BigInteger;
import java.util.List;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.util.Arrays;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.KeyUtils;
import util.Peeked;

public class SeqPhraseTrx {
	private static BigInteger Sequence = BigInteger.ONE;
	private static Peeked Peeked = new Peeked("PEEKED_PH_TRX.db");
	
	private static final int[] Bip32ChildPath = new int[] { 44 | Bip32ECKeyPair.HARDENED_BIT,
			195 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0 };
	
	static {
		Sequence = Config.Current.ethFrom.subtract(BigInteger.ONE);
		Sequence = Numeric.toBigInt(KeyUtils.arraysCopyOf(Sequence.toByteArray(), 16));
	}
	
	private synchronized static byte[] getSeedConcurrent() {
		Sequence = Sequence.add(BigInteger.ONE);
		
		return KeyUtils.arraysCopyOf(Sequence.toByteArray(), 16);
	}

	@SuppressWarnings("deprecation")
	public static CryptoKeyPair createKeyPair() {
		byte[] entropy = null;
		BigInteger convertInt = null;
		
		while (convertInt == null || Peeked.isPeeked(convertInt) == true) {
			entropy = getSeedConcurrent();
			convertInt = Numeric.toBigInt(entropy);
			
			if (convertInt.compareTo(Config.Current.trxTo) == 1) {
				try {
					Thread.currentThread().stop();
				} catch (Exception e) {}
				
				Sequence = Config.Current.trxFrom;
			}
		}

		Peeked.savePeeked(convertInt);
		
		String mnemonic = MnemonicUtils.generateMnemonic(entropy);
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
				StrategyType.SEQ_PH_TRX,
				childKey.getPrivateKey(),
				List.of(address),
				Numeric.toHexStringWithPrefix(childKey.getPrivateKey()));
	}

}
