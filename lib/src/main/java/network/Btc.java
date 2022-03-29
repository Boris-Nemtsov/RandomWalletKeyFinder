package network;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

import datatype.CryptoKeyPair;
import datatype.PickerWallet;
import datatype.inf.Network;
import strategy.RandEcBtc;
import strategy.SeqEcBtc;
import util.UrlParser;

public class Btc extends Network {
	public static Btc Provider = null;

	static {
		Provider = new Btc();
		Provider.init();
	}
	
	private Btc() { }

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
				try (Scanner scanner = UrlParser.openAndGetParser(String.format("https://blockstream.info/api/address/%s", walletAddress))) {
					if (scanner.hasNext() == true) {
						List<MatchResult> regexResult = scanner.findAll("funded_txo_sum\":(\\d+),|spent_txo_sum\":(\\d+),").collect(Collectors.toList());
						
						if (regexResult.size() != 0) {
							BigInteger recv = new BigInteger(regexResult.get(0).group(1));
							BigInteger sent = new BigInteger(regexResult.get(1).group(2));
							total = total.add(recv).subtract(sent);
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
				try (Scanner scanner = UrlParser.openAndGetParser(String.format("https://blockstream.info/api/address/%s", walletAddress))) {
					if (scanner.hasNext() == true) {
						List<MatchResult> regexResult = scanner.findAll("funded_txo_count\":(\\d+),").collect(Collectors.toList());
						
						if (regexResult.size() != 0) {
							total = total.add(new BigInteger(regexResult.get(0).group(1)));
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
		return getPickerWallet(RandEcBtc.createKeyPair());
	}
	
	public PickerWallet getSequentialFullWallet() {
		return getPickerWallet(SeqEcBtc.createKeyPair());
	}
}
