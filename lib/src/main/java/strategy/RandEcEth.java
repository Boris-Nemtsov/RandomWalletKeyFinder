package strategy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.bouncycastle.util.BigIntegers;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.Peeked;

public class RandEcEth {
	private static Peeked Peeked = new Peeked("PEEKED_EC_ETH.db");

	public static CryptoKeyPair createKeyPair() {
		BigInteger randomizeInt = BigInteger.ZERO;

		while (randomizeInt == BigInteger.ZERO || Peeked.isPeeked(randomizeInt) == true) {
			randomizeInt = BigIntegers.createRandomInRange(Config.Current.ethFrom, Config.Current.ethTo, new SecureRandom());
		}

		Peeked.savePeeked(randomizeInt);

		ECKeyPair keyPair = ECKeyPair.create(randomizeInt);
		
		return new CryptoKeyPair(
				StrategyType.RAND_EC_ETH,
				keyPair.getPrivateKey(),
				List.of(Credentials.create(keyPair).getAddress()),
				Numeric.toHexStringWithPrefix(keyPair.getPrivateKey()));
	}
}	
