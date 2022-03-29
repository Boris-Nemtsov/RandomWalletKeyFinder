package datatype;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.Config;
import network.Btc;
import network.Eth;
import network.Trx;

public enum StrategyType {
	NOT_SET,
	RAND_EC_BTC,
	RAND_EC_ETH,
	RAND_EC_TRX,
	RAND_PH_ETH,
	RAND_PH_TRX,
	SEQ_PH_ETH,
	SEQ_PH_TRX,
	SEQ_EC_BTC,
	SEQ_EC_ETH,
	SEQ_EC_TRX;
	
	public void start() {
		switch (this) {
			case NOT_SET:
				break;
			case RAND_EC_BTC:
				newJob(() -> Btc.Provider.getRandomFullScan());
				break;
			case RAND_EC_ETH:
				newJob(() -> Eth.Provider.getRandomFullScan());
				break;
			case RAND_EC_TRX:
				newJob(() -> Trx.Provider.getRandomFullScan());
				break;
			case RAND_PH_ETH:
				newJob(() -> Eth.Provider.getRandomPhraseWallet());
				break;
			case RAND_PH_TRX:
				newJob(() -> Trx.Provider.getRandomPhraseWallet());
				break;
			case SEQ_PH_ETH:
				newJob(() -> Eth.Provider.getSequentialPhraseWallet());
				break;
			case SEQ_PH_TRX:
				newJob(() -> Trx.Provider.getSequentialPhraseWallet());
				break;
			case SEQ_EC_BTC:
				newJob(() -> Btc.Provider.getSequentialFullWallet());
				break;
			case SEQ_EC_ETH:
				newJob(() -> Eth.Provider.getSequentialFullWallet());
				break;
			case SEQ_EC_TRX:
				newJob(() -> Trx.Provider.getSequentialFullWallet());
				break;
			default:
				break;
		}
	}
	
	private void newJob(Supplier<PickerWallet> walletMaker) {
		Logger logger = LoggerFactory.getLogger("Main");
		
		for (int i = 0; i < Config.Current.threadCount; i++) {
			new Thread(() -> {
				while (true) {
					PickerWallet wallet = walletMaker.get();
					
					try {
						if (wallet.getAddress().trim().equals("")) {
							return;
						}
						
						if (Config.Current.logging == true) {
							logger.info(wallet.toString());
						} else {
							System.out.println(wallet.toString());
						}

						if (wallet.getTransactionCount().toString().equals("0") == false) {
							util.Log.writeFoundWallet(wallet.toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
