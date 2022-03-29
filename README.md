# RandomWalletKeyFinder

## 개요

무차별 대입법으로 활성화된 암호화폐 지갑을 찾는 라이브러리 입니다.

## 네트워크 지원

- Bitcoin(BTC)
- Ethereum(ETH)
- Tron(TRX)
- Ethereum Classic(ETC)
- Binance Smart Chain(BSC)
- Polygon(MATIC)
- Klaytn(KLAY)
- Avalanche(AVAX)

## 전략

### Range

- Full Scan
    
    사용자가 설정한 범위와 ECDSA 범위 내에서 개인키를 추출합니다.
    
- 12 Mnemonic Words Scan
    
    BIP-39 표준 Mnemonic 단어 12개를 사용하여 개인키를 추출합니다.
    
    - 유명한 지갑 어플리케이션이(Metamask, Tronlink) 기본적으로 12 Mnemonic words 를 사용합니다.
    - 각 단어는 중복이 허용되지 않습니다.

### Rule

- Random
    
    무작위로 엔트로피를 생성합니다.
    
- Sequential
    
    한 단계씩 증가하는 엔트로피를 생성합니다.
    


## 빌드환경

JDK 9+

## 시작하기

### 소스 다운로드

1. Git

```xml
$ git clone https://github.com/Boris-Nemtsov/RandomWalletKeyFinder.git
$ git checkout -t origin/master
```

2. [Release](https://github.com/Boris-Nemtsov/RandomWalletKeyFinder/releases) 페이지에서 다운로드

### 빌드

```xml
$ cd RandomPrivateKeyPicker
$ gradlew clean build
```

### 설정

- config.cfg (Program)

```xml
$ vi lib\build\libs\config.cfg
```

- log4j2.xml (Log4j)

```xml
$ vi lib\build\libs\log4j2.xml
```

### 실행

```xml
$ cd lib\build\libs
$ java -jar RandomPrivateKeyPicker.jar
```

## 설정 (config.cfg)

| Property | Comment | Value |
| --- | --- | --- |
| logging | 탐색결과를 로그파일로 기록합니다. | true / false |
| threadCount | 탐색에 사용할 스레드 갯수를 설정합니다. <br/>ex) threadCount = 5 <br/>BTC * 5 + TRX * 5 + ETH * 5 = 15 threads | Integer |
| strategy | 탐색 전략을 설정합니다. |  |
| strategy.isUse | 사용여부를 설정합니다. | true / false |
| strategy.multichain | 동시에 탐색할 네트워크를 설정합니다. | true / false |
| strategy.rangeFrom | 탐색에 사용할 엔트로피(혹은 개인키) 범위를 설정합니다. | Integer |
| strategy.rangeTo | 탐색에 사용할 엔트로피(혹은 개인키) 범위를 설정합니다. | Integer |
| strategy.strategy | 탐색 세부전략을 설정합니다. | String |

### strategy.strategy

| strategy.strategy | Network | Entropy | Rule |
| --- | --- | --- | --- |
| RAND_EC | BTC / TRX / ETH | Private Key | Random |
| SEQ_EC | BTC / TRX / ETH | Private Key | Sequential |
| RAND_PH | TRX / ETH | 12 Mnemonic Words | Random |
| SEQ_PH | TRX / ETH | 12 Mnemonic Words | Sequential |

## 탐색

### 활성화 조건

지갑 트랜잭션 수 ≥ 1

## 출력

| Path | Comment |
| --- | --- |
| db/*.db | 엔트로피 데이터 |
| log/*.log | 로그 데이터 |
| log/FOUND_WALLET.log | 활성화된 지갑 로그 |

### Log Format

로그는 아래 형식으로 저장합니다.

```xml
Strategy | Private Key | Wallet Address | Transaction | Balance
```

ex)

```xml
SEQ_PH_TRX   	| 0xfe5166c6093289eb9dc7e1572313125f071515e099589d8aed5b8b0b1a49a32a 	| TKZeqLE11aHhxXZ9oLFuaLBYfNe3eEp8dC                                              	| 0               | 0
RAND_EC_BTC  	| 0x6d56e0966bf6efed7372ecfdb64102cd81c5681f08860111b9bebf762130603b 	| 1F6Me2oibZDUEvoBuBJyVnvU1harAKKbev	bc1qn223rgl9gw6gpltkq22d49u0t7wr88wh96yekd   	| 0               | 0
```