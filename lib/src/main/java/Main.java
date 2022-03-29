import config.Config;
import datatype.StrategyType;

public class Main {
	public static void main(String[] args) {
		Config.Current.btcStrategy.start();
		Config.Current.trxStrategy.start();
		
		if (Config.Current.ethStrategy != StrategyType.NOT_SET) {
			Config.Current.ethStrategy.start();
		} else if (Config.Current.bscStrategy != StrategyType.NOT_SET) {
			Config.Current.bscStrategy.start();
		} else if (Config.Current.polyStrategy != StrategyType.NOT_SET) {
			Config.Current.polyStrategy.start();
		} else if (Config.Current.klayStrategy != StrategyType.NOT_SET) {
			Config.Current.klayStrategy.start();
		} else if (Config.Current.avaxStrategy != StrategyType.NOT_SET) {
			Config.Current.avaxStrategy.start();
		}
	}
}
