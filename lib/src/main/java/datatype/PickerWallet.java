package datatype;

import java.math.BigInteger;

public class PickerWallet {
	private StrategyType _Strategy = StrategyType.NOT_SET;
	private String _PrivateKey = "";
	private String _Address = "";
	private BigInteger _TransactionCount = BigInteger.ZERO;
	private BigInteger _Balance = BigInteger.ZERO;

	public static PickerWallet DEFUALT = new PickerWallet(StrategyType.NOT_SET, "", "", BigInteger.ZERO, BigInteger.ZERO);

	public PickerWallet(StrategyType strategy, String privateKey, String address, BigInteger transactionCount, BigInteger balance) {
		_Strategy = strategy;
		_PrivateKey = privateKey;
		_Address = address;
		_TransactionCount = transactionCount;
		_Balance = balance;
	}
	
	public StrategyType getStrategy() {
		return _Strategy;
	}

	public String getPrivateKey() {
		return _PrivateKey;
	}

	public String getAddress() {
		return _Address;
	}

	public BigInteger getTransactionCount() {
		return _TransactionCount;
	}

	public BigInteger getBalance() {
		return _Balance;
	}

	@Override
	public String toString() {
		return String.format("%-13s\t| %-67s\t| %-80s\t| %-15s | %s", _Strategy, _PrivateKey, _Address, _TransactionCount.toString(), _Balance.toString());
	}
}
