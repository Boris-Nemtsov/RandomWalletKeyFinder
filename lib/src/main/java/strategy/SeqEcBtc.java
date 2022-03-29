package strategy;

import java.math.BigInteger;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.Script.ScriptType;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.Peeked;

public class SeqEcBtc {
	private static BigInteger Sequence = BigInteger.ONE;
	private static Peeked Peeked = new Peeked("PEEKED_EC_BTC.db");

	static {
		Sequence = Config.Current.btcFrom.subtract(BigInteger.ONE);
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
		
		ECKey keyPair = ECKey.fromPrivate(convertInt);

		String addressP2pkh = Address.fromKey(NetworkParameters.fromID(NetworkParameters.ID_MAINNET), keyPair, ScriptType.P2PKH).toString();
		String addressP2wpkh = Address.fromKey(NetworkParameters.fromID(NetworkParameters.ID_MAINNET), keyPair, ScriptType.P2WPKH).toString();
		
		return new CryptoKeyPair(
				StrategyType.SEQ_EC_BTC,
				keyPair.getPrivKey(),
				List.of(addressP2pkh, addressP2wpkh),
				Numeric.toHexStringWithPrefix(keyPair.getPrivKey()));
	}
}
