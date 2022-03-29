package datatype;

import java.math.BigInteger;

public class ConfigWrapper {
	public final static BigInteger BIGINT_MAX = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935");
	
	public boolean logging = true;
	public int threadCount = 0;
	
	public StrategyType btcStrategy = StrategyType.NOT_SET;
	public StrategyType trxStrategy = StrategyType.NOT_SET;
	public StrategyType ethStrategy = StrategyType.NOT_SET;
	public StrategyType etcStrategy = StrategyType.NOT_SET;
	public StrategyType bscStrategy = StrategyType.NOT_SET;
	public StrategyType polyStrategy = StrategyType.NOT_SET;
	public StrategyType klayStrategy = StrategyType.NOT_SET;
	public StrategyType avaxStrategy = StrategyType.NOT_SET;
	
	public BigInteger btcFrom = BigInteger.ZERO;
	public BigInteger btcTo = BIGINT_MAX;
	public BigInteger trxFrom = BigInteger.ZERO;
	public BigInteger trxTo = BIGINT_MAX;
	public BigInteger ethFrom = BigInteger.ZERO;
	public BigInteger ethTo = BIGINT_MAX;
}
