package com.tron.okhttp;

import com.tron.okhttp.utils.TronConstant;
import org.bouncycastle.util.encoders.Hex;
import org.tron.trident.abi.FunctionReturnDecoder;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.Utf8String;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;
import org.tron.trident.utils.Numeric;

import java.util.Arrays;
import java.util.Collections;

public class ApiWapperTransactionHttp {

  public static ApiWrapper getApiWrapper() {
    //return ApiWrapper.ofMainnet(TronConstant.privateKey, TronConstant.apiKey);
     return ApiWrapper.ofShasta(TronConstant.privateKey);
  }

  public static void trunsferTRX() throws IllegalException, InterruptedException {
    ApiWrapper client = getApiWrapper();
    Response.TransactionExtention transactionExtention =
        client.transfer(
            "TA7weEqvrbyMygQmbNbFuAi2ndEBUAp45h", "TQaiH8b4LBFPBfd78i7mS2RZNpPWSaFx3B", 1000000L);
    Chain.Transaction transaction = client.signTransaction(transactionExtention);
    String txid = client.broadcastTransaction(transaction);
    System.out.println(txid);

    Thread.sleep(2000);

    Chain.Transaction getTransaction = client.getTransactionById(txid);
    System.out.println(getTransaction.getRet(0).getContractRet().name());
  }

  public static void trunsferUSDT() {
    ApiWrapper client = getApiWrapper();

    Response.Account account = client.getAccount("TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf");
    System.out.println(account.getBalance());

    Function name =
        new Function(
            "name", Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));
    // Function name = new Function("balanceOf", Arrays.asList(new
    // Address("TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf")), Arrays.asList(new TypeReference<Utf8String>()
    // {}));
    Response.TransactionExtention extension =
        client.constantCall(
            "TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf", "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t", name);

    String result = Numeric.toHexString(extension.getConstantResult(0).toByteArray());

    String value =
        (String) FunctionReturnDecoder.decode(result, name.getOutputParameters()).get(0).getValue();
    System.out.println(result);
    System.out.println(value);
  }

  public static void main(String[] args) throws Exception {
    // trunsferUSDT();
     //trunsferTRX();

    //createAccount();



  }

  /**
   * 创建账号，并激活
   * @throws IllegalException
   */
  public static void createAccount() throws IllegalException {
    KeyPair keyPair = KeyPair.generate();

    System.out.println(keyPair.toBase58CheckAddress());
    System.out.println(keyPair.toPrivateKey());

    ApiWrapper client = getApiWrapper();
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
