package strategy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.Script.ScriptType;
import org.bouncycastle.util.BigIntegers;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.Peeked;

public class RandEcBtc {
	private static Peeked Peeked = new Peeked("PEEKED_EC_BTC.db");

	public static CryptoKeyPair createKeyPair() {
		BigInteger randomizeInt = BigInteger.ZERO;

		while (randomizeInt == BigInteger.ZERO || Peeked.isPeeked(randomizeInt) == true) {
			randomizeInt = BigIntegers.createRandomInRange(Config.Current.btcFrom, Config.Current.btcTo, new SecureRandom());
		}
		
		Peeked.savePeeked(randomizeInt);
		
		ECKey keyPair = ECKey.fromPrivate(randomizeInt);

		String addressP2pkh = Address.fromKey(NetworkParameters.fromID(NetworkParameters.ID_MAINNET), keyPair, ScriptType.P2PKH).toString();
		String addressP2wpkh = Address.fromKey(NetworkParameters.fromID(NetworkParameters.ID_MAINNET), keyPair, ScriptType.P2WPKH).toString();
		
		return new CryptoKeyPair(
				StrategyType.RAND_EC_BTC,
				keyPair.getPrivKey(),
				List.of(addressP2pkh, addressP2wpkh),
				Numeric.toHexStringWithPrefix(keyPair.getPrivKey()));
	}
}
