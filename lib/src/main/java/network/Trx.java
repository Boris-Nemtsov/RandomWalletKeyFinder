package network;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

import datatype.CryptoKeyPair;
import datatype.PickerWallet;
import datatype.inf.Network;
import strategy.RandEcTrx;
import strategy.RandPhraseTrx;
import strategy.SeqEcTrx;
import strategy.SeqPhraseTrx;
import util.UrlParser;

public class Trx extends Network {
	public static Trx Provider = null;
	
	static {
		Provider = new Trx();
		Provider.init();
	}
	
	private Trx() { }
	
	@Override
	public void init() {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	protected BigInteger getBalance(CryptoKeyPair cryptoKeyPair) {
		BigInteger total = BigInteger.ZERO;
		
		try {
			for (String walletAddress : cryptoKeyPair.getWalletAddresses()) {
				try (Scanner scanner = UrlParser.openAndGetParser(String.format("https://api.trongrid.io/v1/accounts/%s", walletAddress))) {
					if (scanner.hasNext() == true) {
						List<MatchResult> regexResult = scanner.findAll("\"balance\":([0-9.]+),").collect(Collectors.toList());
						
						if (regexResult.size() != 0) {
							total = total.add(new BigInteger(Long.toString((long)(Double.parseDouble(regexResult.get(0).group(1))))));
						}
					} else {
						if (scanner != null) {
							scanner.close();
						}
						
						throw new Exception("Failed to load pages");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return total;
	}

	@Override
	protected BigInteger getTransactionCount(CryptoKeyPair cryptoKeyPair) {
		BigInteger total = BigInteger.ZERO;
		
		try {
			for (String walletAddress : cryptoKeyPair.getWalletAddresses()) {
				try (Scanner scanner = UrlParser.openAndGetParser(String.format("https://api.trongrid.io/v1/accounts/%s/transactions", walletAddress))) {
					if (scanner.hasNext() == true) {
						List<MatchResult> regexResult = scanner.findAll("\"net_fee\":").collect(Collectors.toList());
						
						if (regexResult.size() != 0) {
							total = total.add(BigInteger.valueOf(regexResult.size()));
						}
					} else {
						if (scanner != null) {
							scanner.close();
						}
						
						throw new Exception("Failed to load pages");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return total;
	}

	@Override
	protected PickerWallet getPickerWallet(CryptoKeyPair cryptoKeyPair) {
		try {
			BigInteger totalBalance = BigInteger.ZERO;
			BigInteger totalTransactionCount = getTransactionCount(cryptoKeyPair);

			if (totalTransactionCount.toString().equals("0") == false) {
				totalBalance = getBalance(cryptoKeyPair);
			}

			return new PickerWallet(
					cryptoKeyPair.getStrategy(),
					cryptoKeyPair.getFormattedPrivateKey(),
					String.join("\t", cryptoKeyPair.getWalletAddresses()),
					totalTransactionCount,
					totalBalance);
		} catch (Exception e) {
			return PickerWallet.DEFUALT;
		}
	}

	public PickerWallet getRandomFullScan() {
		return getPickerWallet(RandEcTrx.createKeyPair());
	}
	
	public PickerWallet getRandomPhraseWallet() {
		return getPickerWallet(RandPhraseTrx.createKeyPair());
	}
	
	public PickerWallet getSequentialPhraseWallet() {
		return getPickerWallet(SeqPhraseTrx.createKeyPair());
	}

	public PickerWallet getSequentialFullWallet() {
		return getPickerWallet(SeqEcTrx.createKeyPair());
	}
}
