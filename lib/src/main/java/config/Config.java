package config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import datatype.ConfigWrapper;
import datatype.StrategyType;

public class Config {
	public static final ConfigWrapper Current = new ConfigWrapper();
	
	static {
		ObjectMapper mapper = new ObjectMapper();

		try {
			if (new File("config.cfg").exists() == false) {
				System.out.println("Missing file : config.cfg. Try to load default config");
				copyResource("config.cfg");
			}
			
			if (new File("log4j2.xml").exists() == false) {
				System.out.println("Missing file : log4j2.xml. Try to load default config");
				copyResource("log4j2.xml");
			}
			
			JsonNode node = mapper.readTree(new File("config.cfg"));
			
			Current.logging = node.get("logging").asBoolean();
			Current.threadCount = node.get("threadCount").asInt();
			
			JsonNode strategy = node.get("strategy");
			{
				JsonNode btc = strategy.get("BTC");
				{
					Current.btcFrom = new BigInteger(btc.get("rangeFrom").asText());
					
					if (btc.get("rangeTo").asText().equals("0") == false) {
						Current.btcTo = new BigInteger(btc.get("rangeTo").asText());
					}
					
					boolean isUse = btc.get("isUse").asBoolean();
					String strategyType = btc.get("strategy").asText();
					
					Current.btcStrategy = StrategyType.NOT_SET;
					
					if (isUse == true) {
						switch (strategyType) {
							case "RAND_EC":
								Current.btcStrategy = StrategyType.RAND_EC_BTC;
								break;
							case "SEQ_EC":
								Current.btcStrategy = StrategyType.SEQ_EC_BTC;
								break;
						}
					}
				}
				
				JsonNode trx = strategy.get("TRX");
				{
					Current.trxFrom = new BigInteger(trx.get("rangeFrom").asText());
					
					if (trx.get("rangeTo").asText().equals("0") == false) {
						Current.trxTo = new BigInteger(trx.get("rangeTo").asText());
					}
					
					boolean isUse = trx.get("isUse").asBoolean();
					String strategyType = trx.get("strategy").asText();
					
					Current.trxStrategy = StrategyType.NOT_SET;
					
					if (isUse == true) {
						switch (strategyType) {
							case "RAND_EC":
								Current.trxStrategy = StrategyType.RAND_EC_TRX;
								break;
							case "RAND_PH":
								Current.trxStrategy = StrategyType.RAND_PH_TRX;
								break;
							case "SEQ_PH":
								Current.trxStrategy = StrategyType.SEQ_PH_TRX;
								break;
							case "SEQ_EC":
								Current.trxStrategy = StrategyType.SEQ_EC_TRX;
								break;
						}
					}
				}
				
				JsonNode eth = strategy.get("ETH");
				{
					Current.ethFrom = new BigInteger(eth.get("rangeFrom").asText());
					
					if (eth.get("rangeTo").asText().equals("0") == false) {
						Current.ethTo = new BigInteger(eth.get("rangeTo").asText());
					}
					
					boolean isUse = eth.get("isUse").asBoolean();
					String strategyType = eth.get("strategy").asText();
					StrategyType ethStrategy = StrategyType.NOT_SET;
					
					if (isUse == true) {
						switch (strategyType) {
							case "RAND_EC":
								ethStrategy = StrategyType.RAND_EC_ETH;
								break;
							case "RAND_PH":
								ethStrategy = StrategyType.RAND_PH_ETH;
								break;
							case "SEQ_PH":
								ethStrategy = StrategyType.SEQ_PH_ETH;
								break;
							case "SEQ_EC":
								ethStrategy = StrategyType.SEQ_EC_ETH;
								break;
						}
					}
					
					JsonNode ethSideChain = eth.get("multichain");
					boolean isEth = ethSideChain.get("ETH").asBoolean();
					boolean isEtc = ethSideChain.get("ETC").asBoolean();
					boolean isBsc = ethSideChain.get("BSC").asBoolean();
					boolean isPoly = ethSideChain.get("POLY").asBoolean();
					boolean isKlay = ethSideChain.get("KLAY").asBoolean();
					boolean isAvax = ethSideChain.get("AVAX").asBoolean();
					
					if (isEth == true) {
						Current.ethStrategy = ethStrategy;
					}
					
					if (isEtc == true) {
						Current.etcStrategy = ethStrategy;	
					}
					
					if (isBsc == true) {
						Current.bscStrategy = ethStrategy;
					}
					
					if (isPoly == true) {
						Current.polyStrategy = ethStrategy;
					}
					
					if (isKlay == true) {
						Current.klayStrategy = ethStrategy;
					}
					
					if (isAvax == true) {
						Current.avaxStrategy = ethStrategy;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void copyResource(String resourceName) {
		InputStream resourceStream;
		FileOutputStream outputStream;
		
		try {
			resourceStream = ClassLoader.getSystemResourceAsStream(resourceName);
			outputStream = new FileOutputStream(resourceName);
			
			int data;
			while ((data = resourceStream.read()) > 0) {
				outputStream.write(data);
			}
		} catch (Exception e) {
			System.out.println("Cannot create default config : " + resourceName);
			System.exit(-1);
		}
	}
}
