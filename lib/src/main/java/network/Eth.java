package network;

import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.PickerWallet;
import datatype.StrategyType;
import datatype.inf.Network;
import strategy.RandEcEth;
import strategy.RandPhraseEth;
import strategy.SeqEcEth;
import strategy.SeqPhraseEth;
import util.UrlParser;

public class Eth extends Network {
	private Web3j PROVIDER_ETH;
	private Web3j PROVIDER_ETC;
	private Web3j PROVIDER_BSC;
	private Web3j PROVIDER_POLYGON;
	private Web3j PROVIDER_KLAYTN;
	private Web3j PROVIDER_AVAX;

	public static Eth Provider = null;

	static {
		Provider = new Eth();
		Provider.init();
	}
	
	private Eth() { }

	@Override
	public void init() {
		PROVIDER_ETH = Web3j.build(new HttpService("https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"));
		PROVIDER_ETC = Web3j.build(new HttpService("https://www.ethercluster.com/etc"));
		PROVIDER_BSC = Web3j.build(new HttpService("https://bsc-dataseed.binance.org"));
		PROVIDER_POLYGON = Web3j.build(new HttpService("https://matic-mainnet.chainstacklabs.com"));
		PROVIDER_KLAYTN = Web3j.build(new HttpService("https://public-node-api.klaytnapi.com/v1/cypress"));
		PROVIDER_AVAX = Web3j.build(new HttpService("https://api.avax.network/ext/bc/C/rpc"));
	}

	@Override
	public void dispose() {
		PROVIDER_ETH.shutdown();
		PROVIDER_ETC.shutdown();
		PROVIDER_BSC.shutdown();
		PROVIDER_POLYGON.shutdown();
		PROVIDER_KLAYTN.shutdown();
		PROVIDER_AVAX.shutdown();
	}

	@Override
	protected BigInteger getBalance(CryptoKeyPair cryptoKeyPair) {
		BigInteger amount = BigInteger.ZERO;
		
		try {
			String walletAddress = cryptoKeyPair.getWalletAddresses().get(0);
			
			if (Config.Current.ethStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger balanceEth = 
							UrlParser.openAndGetParser(
									PROVIDER_ETH.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST))
							.getBalance();
					
					amount = amount.add(balanceEth);	
				} catch (Exception e) {}
			}
			
			if (Config.Current.etcStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger balanceEtc =
							UrlParser.openAndGetParser(
								PROVIDER_ETC.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST))
							.getBalance();
					
					amount = amount.add(balanceEtc);	
				} catch (Exception e) {}
			}
			
			if (Config.Current.bscStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger balanceBsc = 
							UrlParser.openAndGetParser(
									PROVIDER_BSC.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST))
							.getBalance();
					
					amount = amount.add(balanceBsc);
				} catch (Exception e) {}
			}
			
			if (Config.Current.polyStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger balancePolygon = 
							UrlParser.openAndGetParser(
									PROVIDER_POLYGON.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST))
							.getBalance();
					
					amount = amount.add(balancePolygon);
				} catch (Exception e) {}
			}
			
			if (Config.Current.klayStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger balanceKlaytn =
							UrlParser.openAndGetParser(
									PROVIDER_KLAYTN.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST))
							.getBalance();
					
					amount = amount.add(balanceKlaytn);
				} catch (Exception e) {}
			}
			
			if (Config.Current.avaxStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger balanceAvax = 
							UrlParser.openAndGetParser(
									PROVIDER_AVAX.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST))
							.getBalance();
					
					amount = amount.add(balanceAvax);
				} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
			amount = BigInteger.ZERO;
		}
		
		return amount;
	}

	@Override
	protected BigInteger getTransactionCount(CryptoKeyPair cryptoKeyPair) {
		BigInteger amount = BigInteger.ZERO;
		
		try {
			String walletAddress = cryptoKeyPair.getWalletAddresses().get(0);
			
			if (Config.Current.ethStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger transactionEth = 
							UrlParser.openAndGetParser(
									PROVIDER_ETH.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST))
							.getTransactionCount();
					
					amount = amount.add(transactionEth);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (Config.Current.etcStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger transactionEtc = 
							UrlParser.openAndGetParser(
									PROVIDER_ETC.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST))
							.getTransactionCount();
					
					amount = amount.add(transactionEtc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (Config.Current.bscStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger transactionBsc = 
							UrlParser.openAndGetParser(
									PROVIDER_BSC.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST))
							.getTransactionCount();
					
					amount = amount.add(transactionBsc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (Config.Current.polyStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger transactionPolygon = 
							UrlParser.openAndGetParser(
									PROVIDER_POLYGON.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST))
							.getTransactionCount();
					
					amount = amount.add(transactionPolygon);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (Config.Current.klayStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger transactionKlaytn = 
							UrlParser.openAndGetParser(
									PROVIDER_KLAYTN.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST))
							.getTransactionCount();
					
					amount = amount.add(transactionKlaytn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (Config.Current.avaxStrategy != StrategyType.NOT_SET) {
				try {
					BigInteger transactionAvax = 
							UrlParser.openAndGetParser(
									PROVIDER_AVAX.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST))
							.getTransactionCount();
					
					amount = amount.add(transactionAvax);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			amount = BigInteger.ZERO;
		}
		
		return amount;
	}
	
	@Override
	protected PickerWallet getPickerWallet(CryptoKeyPair cryptoKeyPair) {
		try {
			String walletAddress = cryptoKeyPair.getWalletAddresses().get(0);
			BigInteger totalBalance = BigInteger.ZERO;
			BigInteger totalTransactionCount = getTransactionCount(cryptoKeyPair);

			if (totalTransactionCount.toString().equals("0") == false) {
				totalBalance = getBalance(cryptoKeyPair);
			}

			return new PickerWallet(
					cryptoKeyPair.getStrategy(),
					cryptoKeyPair.getFormattedPrivateKey(),
					walletAddress,
					totalTransactionCount,
					totalBalance);
		} catch (Exception e) {
			return PickerWallet.DEFUALT;
		}
	}

	public PickerWallet getRandomFullScan() {
		return getPickerWallet(RandEcEth.createKeyPair());
	}

	public PickerWallet getRandomPhraseWallet() {
		return getPickerWallet(RandPhraseEth.createKeyPair());
	}

	public PickerWallet getSequentialPhraseWallet() {
		return getPickerWallet(SeqPhraseEth.createKeyPair());
	}
	
	public PickerWallet getSequentialFullWallet() {
		return getPickerWallet(SeqEcEth.createKeyPair());
	}
}
