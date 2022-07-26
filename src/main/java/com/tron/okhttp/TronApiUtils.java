package com.tron.okhttp;

import com.tron.okhttp.utils.TronConstant;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.bouncycastle.util.encoders.Hex;
import org.tron.trident.abi.FunctionReturnDecoder;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.abi.datatypes.Bool;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.generated.Uint256;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.core.transaction.TransactionBuilder;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;
import org.tron.trident.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class TronApiUtils {

  public static void main(String[] args) throws Exception {
    String toAddress = "TXoDw9PUuBWwp2GmiDvFwdXfja6KREpkyY";

    // TRX操作
    /*Response.Account account = getAccount(TronConstant.privateKey, "TA7weEqvrbyMygQmbNbFuAi2ndEBUAp45h");
    System.out.println(
        "余额：" + new BigDecimal(account.getBalance()).divide(new BigDecimal(1000000)));

    String txid1 = trunsferTRX(TronConstant.privateKey, TronConstant.ownerAddress, toAddress, 100000000L);
    System.out.println("交易ID：" + txid1);
    Thread.sleep(5000);
    String txstatus1 = getTransactionStatusById(TronConstant.privateKey, txid1);
    System.out.println("交易状态：" + txstatus1);*/

    // USDT合约操作
    BigInteger balanceOf =
        getUSDTBalanceOf(TronConstant.privateKey, TronConstant.ownerAddress); // 查询USDT余额
    BigDecimal bd =
        new BigDecimal(balanceOf).divide(new BigDecimal(TronConstant.TRC20_USDT_DECIMALS));
    // System.out.println(balanceOf);
    System.out.println(bd);

    String txid =
        trunsferUSDT(
            TronConstant.privateKey,
            TronConstant.ownerAddress,
            "TKqYgvjPthsyvinQ4Qd2cYvvXaAVzPBskY",
            TronConstant.TRC20_USDT_DECIMALS.multiply(new BigInteger("100"))); // 转账USDT
    System.out.println(txid);
    Thread.sleep(5000);
    String txstatus = getTransactionStatusById(TronConstant.privateKey, txid); // 查询交易状态
    System.out.println("交易状态：" + txstatus);

    // String tranStr = getTransactionsTRC20(address1,true,false);
    // System.out.println(tranStr);

  }

  public static ApiWrapper getApiWrapper(String hexPrivateKey) {
    if (TronConstant.isTest) {
      return ApiWrapper.ofShasta(hexPrivateKey); // 测试网
    } else {
      return ApiWrapper.ofMainnet(hexPrivateKey, TronConstant.apiKey); // 主网
    }
  }

  /**
   * @param address 账号
   * @param only_to 只查询转入交易
   * @param only_from 只查询转出交易
   * @return
   * @throws IOException
   */
  public static String getTransactionsTRC20(String address, boolean only_to, boolean only_from)
      throws IOException {
    OkHttpClient client = new OkHttpClient();
    Request request =
        new Request.Builder()
            .url(
                TronConstant.tronDomainUlr
                    + "/v1/accounts/"
                    + address
                    + "/transactions/trc20?only_to="
                    + only_to
                    + "&only_from="
                    + only_from
                    + "&limit=100&contract_address="
                    + TronConstant.TRC20_USDT_ADDRESS)
            .get()
            .addHeader("Accept", "application/json")
            .build();

    okhttp3.Response response = client.newCall(request).execute();
    return response.body().string();
  }

  /**
   * 转账TRX
   *
   * @param hexPrivateKey
   * @param fromAddress
   * @param toAddress
   * @param amount
   * @return
   * @throws IllegalException
   */
  public static String trunsferTRX(
      String hexPrivateKey, String fromAddress, String toAddress, long amount)
      throws IllegalException {
    ApiWrapper client = getApiWrapper(hexPrivateKey);
    Response.TransactionExtention transactionExtention =
        client.transfer(fromAddress, toAddress, amount);
    Chain.Transaction transaction = client.signTransaction(transactionExtention);
    return client.broadcastTransaction(transaction);
  }

  /**
   * 查询交易状态
   *
   * @param hexPrivateKey
   * @param txid
   * @return
   * @throws IllegalException
   */
  public static String getTransactionStatusById(String hexPrivateKey, String txid)
      throws IllegalException {
    ApiWrapper client = getApiWrapper(hexPrivateKey);
    Chain.Transaction getTransaction = client.getTransactionById(txid);
    return getTransaction.getRet(0).getContractRet().name();
  }

  /**
   * 查询账户信息
   *
   * @param hexPrivateKey
   * @param address
   * @return
   * @throws IllegalException
   */
  public static Response.Account getAccount(String hexPrivateKey, String address) {
    ApiWrapper client = getApiWrapper(hexPrivateKey);

    Response.Account account = client.getAccount(address);
    return account;
  }

  /**
   * 获取USDT余额
   *
   * @param hexPrivateKey
   * @param address
   * @return
   */
  public static BigInteger getUSDTBalanceOf(String hexPrivateKey, String address) {
    ApiWrapper client = getApiWrapper(hexPrivateKey);
    Function balanceOf =
        new Function(
            "balanceOf",
            Arrays.asList(new Address(address)),
            Arrays.asList(new TypeReference<Uint256>() {}));
    Response.TransactionExtention extension =
        client.constantCall(address, TronConstant.TRC20_USDT_ADDRESS, balanceOf);

    String result = Numeric.toHexString(extension.getConstantResult(0).toByteArray());

    BigInteger value =
        (BigInteger)
            FunctionReturnDecoder.decode(result, balanceOf.getOutputParameters()).get(0).getValue();
    return value;
  }

  /**
   * 转账USDT
   *
   * @param hexPrivateKey
   * @param fromAddress
   * @param toAddress
   * @param amount
   * @return
   */
  public static String trunsferUSDT(
      String hexPrivateKey, String fromAddress, String toAddress, BigInteger amount) {
    ApiWrapper client = getApiWrapper(hexPrivateKey);
    Function transfer =
        new Function(
            "transfer",
            Arrays.asList(new Address(toAddress), new Uint256(amount)),
            Arrays.asList(new TypeReference<Bool>() {}));

    TransactionBuilder builder =
        client.triggerCall(fromAddress, TronConstant.TRC20_USDT_ADDRESS, transfer);
    builder.setFeeLimit(10000000);

    Chain.Transaction transaction = client.signTransaction(builder.getTransaction());
    return client.broadcastTransaction(transaction);
  }

  /**
   * 创建账号，并激活
   *
   * @throws IllegalException
   */
  public static void createAccount() throws IllegalException {
    KeyPair keyPair = KeyPair.generate();

    System.out.println(keyPair.toBase58CheckAddress());
    System.out.println(keyPair.toPrivateKey());

    ApiWrapper client = getApiWrapper(TronConstant.privateKey);
    Response.TransactionExtention transactionExtention =
        client.createAccount(TronConstant.ownerAddress, keyPair.toBase58CheckAddress());

    Chain.Transaction transaction = client.signTransaction(transactionExtention);
    String txid = client.broadcastTransaction(transaction);
    System.out.println(txid);
  }

  /** base58 和 hex 互转 */
  public void format() {
    byte[] rawAddr = Hex.decode("4159d3ad9d126e153b9564417d3a05cf51c1964edf");
    String base58 = Base58Check.bytesToBase58(rawAddr);
    System.out.println(base58);
    byte[] base58Byte = Base58Check.base58ToBytes(base58);
    System.out.println(Hex.toHexString(base58Byte));
  }
}
