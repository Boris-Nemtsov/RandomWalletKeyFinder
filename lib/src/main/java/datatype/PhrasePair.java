package datatype;

import java.math.BigInteger;

public class PhrasePair {
	private String _Mnemonic;
	private String _PrivateKey;
	private String _Address;
	private BigInteger _RandomizedInt;

	public PhrasePair(String mnemonic, String privateKey, String address, BigInteger randomizedInt) {
		_Mnemonic = mnemonic;
		_PrivateKey = privateKey;
		_Address = address;
		_RandomizedInt = randomizedInt;
	}

	public String getMnemonic() {
		return _Mnemonic;
	}

	public String getPrivateKey() {
		return _PrivateKey;
	}

	public String getAddress() {
		return _Address;
	}

	public BigInteger getRandomizedInt() {
		return _RandomizedInt;
	}
}