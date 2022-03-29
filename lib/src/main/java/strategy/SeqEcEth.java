package strategy;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.Peeked;

public class SeqEcEth {
	private static BigInteger Sequence = BigInteger.ONE;
	private static Peeked Peeked = new Peeked("PEEKED_EC_ETH.db");

	static {
		Sequence = Config.Current.ethFrom.subtract(BigInteger.ONE);
	}
	
	private synchronized static byte[] getSeedConcurrent() {
		Sequence = Sequence.add(BigInteger.ONE);
		
		return Numeric.toBytesPadded(Sequence, 32);
	}
	
	public static CryptoKeyPair createKeyPair() {
		byte[] entropy = null;
		BigInteger convertInt = BigInteger.ZERO;

		while (convertInt == BigInteger.ZERO || Peeked.isPeeked(convertInt) == true) {
			entropy = getSeedConcurrent();
			convertInt = Numeric.toBigInt(entropy);
		}

		Peeked.savePeeked(convertInt);

		ECKeyPair keyPair = ECKeyPair.create(convertInt);
		
		return new CryptoKeyPair(
				StrategyType.SEQ_EC_ETH,
				keyPair.getPrivateKey(),
				List.of(Credentials.create(keyPair).getAddress()),
				Numeric.toHexStringWithPrefix(keyPair.getPrivateKey()));
	}
}
