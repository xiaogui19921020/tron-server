package com.tron.okhttp;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.tron.trident.abi.FunctionEncoder;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.abi.datatypes.Type;
import org.tron.trident.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class OnlineTransactionHttp {
  static String tronDomainUlr = "https://api.trongrid.io";
  static String ownerAddress = "TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf";
  static String apiKey = "68aea214-f593-4946-8cb2-c5ecb7e9e67f";
  static String privateKey = "e21b9b74f0ea0d8b82b8fab1e981afdb35332a6aef2c0ed4e19a96552a9789ce";

  public static String getAccounts(String account) throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body =
        RequestBody.create(mediaType, "{\"visible\":true,\"address\":\"" + account + "\"}");
    Request request =
        new Request.Builder()
            .url(tronDomainUlr + "/walletsolidity/getaccount")
            .post(body)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("TRON-PRO-API-KEY", apiKey)
            .build();

    Response response = client.newCall(request).execute();

    return response.body().string();
  }

  public static String createtransaction() throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body =
        RequestBody.create(
            mediaType,
            "{\"owner_address\":\"TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf\",\"to_address\":\"TRivVnAC3BRUvhsXBbUi44yajZRxPQGPEW\",\"amount\":10000000,\"visible\":true}");
    Request request =
        new Request.Builder()
            .url(tronDomainUlr + "/wallet/createtransaction")
            .post(body)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build();
    Response response = client.newCall(request).execute();
    return response.body().string();
  }

  public static String gettransactionsign(String content) throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    // RequestBody body = RequestBody.create(mediaType,
    // "{\"transaction\":{\"raw_data\":\"{\\\"contract\\\":[{\\\"parameter\\\":{\\\"value\\\":{\\\"amount\\\":1000,\\\"owner_address\\\":\\\"TRivVnAC3BRUvhsXBbUi44yajZRxPQGPEW\\\",\\\"to_address\\\":\\\"TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf\\\"},\\\"type_url\\\":\\\"type.googleapis.com/protocol.TransferContract\\\"},\\\"type\\\":\\\"TransferContract\\\"}],\\\"ref_block_bytes\\\":\\\"30f4\\\",\\\"ref_block_hash\\\":\\\"d5c235b175eac469\\\",\\\"expiration\\\":1654423236000,\\\"timestamp\\\":1654423176903}\",\"visible\":true,\"txID\":\"858c50035edf5f317a9c027058a0ec0bf6c8ab309b236817f0e8bab5ee40893a\"},\"privateKey\":\"a466062d529c259c8055574dd1723b92cb2f6ce53f7e937962ac5a03dbdc8971\"}");
    RequestBody body = RequestBody.create(mediaType, content);
    Request request =
        new Request.Builder()
            .url("https://api.shasta.trongrid.io/wallet/gettransactionsign")
            .post(body)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build();

    Response response = client.newCall(request).execute();

    return response.body().string();
  }

  public static String broadcasttransaction(String content) throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, content);
    Request request =
        new Request.Builder()
            .url(tronDomainUlr + "/wallet/broadcasttransaction")
            .post(body)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build();

    Response response = client.newCall(request).execute();

    return response.body().string();
  }

  public static String triggersmartcontract() throws IOException {
    String address = "TRivVnAC3BRUvhsXBbUi44yajZRxPQGPEW";
    BigInteger amount = new BigInteger("1000000");
    //BigInteger amount = new BigInteger("1000000000");

    String parameter = encoderAbi(address,amount);

    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body =
        RequestBody.create(
            mediaType,
            "{\"owner_address\":\"TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf\",\"contract_address\":\"TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t\",\"function_selector\":\"transfer(address,uint256)\",\"fee_limit\":10000000,\"visible\":true,\"parameter\":\""+parameter+"\"}");
    Request request =
        new Request.Builder()
            .url("https://api.trongrid.io/wallet/triggersmartcontract")
            .post(body)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build();

    Response response = client.newCall(request).execute();

    return response.body().string();
  }

  public static void main(String[] args) throws Exception {

    String account = getAccounts(ownerAddress);
    System.out.println("account info:" + account);
    System.out.println("-------------------------------------------");
    /*String transactionStr = createtransaction();
    JSONObject transactionJson = JSONObject.parseObject(transactionStr);
    String txID = transactionJson.getString("txID");
    String raw_data = transactionJson.getString("raw_data");
    System.out.println(transactionJson.toJSONString());
    System.out.println(txID);
    System.out.println(raw_data);

    JSONObject gettransactionsignJson = new JSONObject();
    gettransactionsignJson.put("transaction", transactionJson);
    gettransactionsignJson.put("privateKey", privateKey);

    System.out.println("-------------------------------------------");
    String gettransactionsignStr = gettransactionsign(gettransactionsignJson.toJSONString());
    System.out.println(gettransactionsignStr);
    JSONObject resultTransactionsignJson = JSONObject.parseObject(gettransactionsignStr);

    System.out.println("-------------------------------------------");*/
    // String resultBroadcasttransaction = broadcasttransaction(gettransactionsignStr);
    // System.out.println(resultBroadcasttransaction);

    System.out.println("-------------------------------------------");
    String triggersmartcontractStr = triggersmartcontract();
    System.out.println(triggersmartcontractStr);

    JSONObject triggersmartcontractJson = JSONObject.parseObject(triggersmartcontractStr);
    String txID = triggersmartcontractJson.getJSONObject("transaction").getString("txID");
    String raw_data = triggersmartcontractJson.getJSONObject("transaction").getString("raw_data");
    System.out.println(txID);
    System.out.println(raw_data);
    System.out.println("-------------------------------------------");
    JSONObject gettransactionsignJson = new JSONObject();
    gettransactionsignJson.put("transaction", triggersmartcontractJson.getJSONObject("transaction"));
    gettransactionsignJson.put("privateKey", privateKey);


    String gettransactionsignStr = gettransactionsign(gettransactionsignJson.toJSONString());
    System.out.println(gettransactionsignStr);

    System.out.println("-------------------------------------------");
    String resultBroadcasttransaction = broadcasttransaction(gettransactionsignStr);
    System.out.println(resultBroadcasttransaction);

  }

  public static String encoderAbi(String address, BigInteger amount) {

    List<Type> parameters = new ArrayList<>();

    parameters.add(new Address(address));
    parameters.add(new Uint256(amount));

    return FunctionEncoder.encodeConstructor(parameters);
  }
}
