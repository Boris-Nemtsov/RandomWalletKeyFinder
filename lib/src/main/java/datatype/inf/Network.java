package datatype.inf;

import java.math.BigInteger;

import datatype.CryptoKeyPair;
import datatype.PickerWallet;

public abstract class Network {

	public abstract void init();

	public abstract void dispose();

	protected abstract BigInteger getBalance(CryptoKeyPair cryptoKeyPair);

	protected abstract BigInteger getTransactionCount(CryptoKeyPair cryptoKeyPair);
	
	protected abstract PickerWallet getPickerWallet(CryptoKeyPair cryptoKeyPair);

}
