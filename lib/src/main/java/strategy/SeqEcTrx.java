package strategy;

import java.math.BigInteger;
import java.util.List;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.util.Arrays;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import config.Config;
import datatype.CryptoKeyPair;
import datatype.StrategyType;
import util.Peeked;

public class SeqEcTrx {
	private static BigInteger Sequence = BigInteger.ONE;
	private static Peeked Peeked = new Peeked("PEEKED_EC_TRX.db");

	static {
		Sequence = Config.Current.trxFrom.subtract(BigInteger.ONE);
	}
	
	private synchronized static byte[] getSeedConcurrent() {
		Sequence = Sequence.add(BigInteger.ONE);
		
		return Numeric.toBytesPadded(Sequence, 32);
	}
	
	public static CryptoKeyPair createKeyPair() {
		byte[] entropy = null;
		BigInteger convertInt = BigInteger.ZERO;

		while (convertInt == BigInteger.ZERO || Peeked.isPeeked(convertInt) == true) {
			entropy = getSeedConcurrent();
			convertInt = Numeric.toBigInt(entropy);
		}

		Peeked.savePeeked(convertInt);

		ECKeyPair keyPair = ECKeyPair.create(convertInt);
		String walletAddress = "";
		
		try {
			byte[] publicKeyBytes = keyPair.getPublicKey().toByteArray();
			byte[] keccakEncoded = new byte[32];
			
			KeccakDigest keccakDigest = new KeccakDigest(256);
			keccakDigest.update(publicKeyBytes, 0, publicKeyBytes.length);
			keccakDigest.doFinal(keccakEncoded, 0);
			
			keccakEncoded = Arrays.copyOfRange(keccakEncoded, 12, 32);
			keccakEncoded = Arrays.prepend(keccakEncoded, (byte) 0x41);
			
			byte[] sha256Encoded = Sha256Hash.hash(Sha256Hash.hash(keccakEncoded));
			byte[] checksumAdded = new byte[25];
			System.arraycopy(keccakEncoded, 0, checksumAdded, 0, 21);
			System.arraycopy(sha256Encoded, 0, checksumAdded, 21, 4);
			
			walletAddress = Base58.encode(checksumAdded);
		} catch (Exception e) {
			walletAddress = "";
		}
		
		return new CryptoKeyPair(
				StrategyType.SEQ_EC_TRX,
				keyPair.getPrivateKey(),
				List.of(walletAddress),
				Numeric.toHexStringWithPrefix(keyPair.getPrivateKey()));
	}
}
