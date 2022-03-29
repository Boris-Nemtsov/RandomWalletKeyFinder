package datatype;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class CryptoKeyPair {
	private StrategyType _Strategy = StrategyType.NOT_SET;
	private BigInteger _PrivateKey = BigInteger.ZERO;
	private List<String> _WalletAddresses = Collections.unmodifiableList(List.of(""));
	private String _FormattedPrivateKey = "";

	public static CryptoKeyPair DEFUALT = new CryptoKeyPair(StrategyType.NOT_SET, BigInteger.ZERO, Collections.unmodifiableList(List.of("")), "");

	public CryptoKeyPair(StrategyType strategy, BigInteger privateKey, List<String> walletAddresses, String formattedPrivateKey) {
		_Strategy = strategy;
		_PrivateKey = privateKey;
		_WalletAddresses = Collections.unmodifiableList(walletAddresses);
		_FormattedPrivateKey = formattedPrivateKey;
	}
	
	public StrategyType getStrategy() {
		return _Strategy;
	}

	public BigInteger getPrivateKey() {
		return _PrivateKey;
	}
	
	public List<String> getWalletAddresses() {
		return _WalletAddresses;
	}

	public String getFormattedPrivateKey() {
		return _FormattedPrivateKey;
	}
}
