package strategy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.bouncycastle.util.BigIntegers;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.KeyUtils;
import util.Peeked;

public class RandPhraseEth {
	private static Peeked Peeked = new Peeked("PEEKED_PH_ETH.db");

	private static final int[] Bip32ChildPath = new int[] { 44 | Bip32ECKeyPair.HARDENED_BIT,
			60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0 };

	public static CryptoKeyPair createKeyPair() {
		byte[] entropy = null;
		BigInteger convertInt = null;
		String mnemonic = "";

		while (true) {
			BigInteger randomizeInt = BigIntegers.createRandomInRange(Config.Current.ethFrom, Config.Current.ethTo, new SecureRandom());
			
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

		String privateKey = Numeric.toHexStringWithPrefix(childKey.getPrivateKey());

		return new CryptoKeyPair(
				StrategyType.RAND_PH_ETH,
				childKey.getPrivateKey(),
				List.of(Credentials.create(privateKey).getAddress()),
				privateKey);
	}

}
