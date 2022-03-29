package strategy;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.KeyUtils;
import util.Peeked;

public class SeqPhraseEth {
	private static BigInteger Sequence = BigInteger.ONE;
	private static Peeked Peeked = new Peeked("PEEKED_PH_ETH.db");

	private static final int[] Bip32ChildPath = new int[] { 44 | Bip32ECKeyPair.HARDENED_BIT,
			60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0 };

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
			
			if (convertInt.compareTo(Config.Current.ethTo) == 1) {
				try {
					Thread.currentThread().stop();
				} catch (Exception e) {}
				
				Sequence = Config.Current.ethFrom;
			}
		}

		Peeked.savePeeked(convertInt);

		String mnemonic = MnemonicUtils.generateMnemonic(entropy);
		Bip32ECKeyPair parentKey = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, null));
		Bip32ECKeyPair childKey = Bip32ECKeyPair.deriveKeyPair(parentKey, Bip32ChildPath);

		String privateKey = Numeric.toHexStringWithPrefix(childKey.getPrivateKey());

		return new CryptoKeyPair(
				StrategyType.SEQ_PH_ETH,
				childKey.getPrivateKey(),
				List.of(Credentials.create(privateKey).getAddress()),
				privateKey);
	}

}
