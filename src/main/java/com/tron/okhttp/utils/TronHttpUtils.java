package com.tron.okhttp.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.Protocol;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;

public class TronHttpUtils {

  /**
   * 查询账号信息
   *
   * @param address
   * @return String
   * @throws IOException
   */
  public static String getAccount(String address) throws IOException {
    JSONObject json = new JSONObject();
    json.put("address", address);
    json.put("visible", true);
    return OKHttpUtils.post(TronUrlConstant.tronGetaccount, json.toJSONString());
  }

  /**
   * 创建一笔转账 TRX 的 Transaction.
   *
   * @param owner_address 转账转出地址
   * @param to_address 转账转入地址
   * @param amount 转账金额, 单位为 SUN
   * @param visible 账户地址是否为 Base58check 格式, 默认为 false, 使用 HEX 地址
   * @return String
   * @throws IOException
   */
  public static String createTransaction(
      String owner_address, String to_address, Long amount, boolean visible) throws IOException {
    JSONObject json = new JSONObject();
    json.put("owner_address", owner_address);
    json.put("to_address", to_address);
    json.put("amount", amount);
    json.put("visible", visible);
    return OKHttpUtils.post(TronUrlConstant.tronCreateTransaction, json.toJSONString());
  }

  public static String getTransactionSign(String transaction) throws IOException {
    JSONObject json = new JSONObject();
    json.put("transaction", transaction);
    json.put("privateKey", TronConstant.privateKey);
    return OKHttpUtils.post(TronUrlConstant.tronGetTransactionSign, json.toJSONString());
  }

  public static String broadcastTransaction(String content) throws IOException {
    return OKHttpUtils.post(TronUrlConstant.tronBroadcastTransaction, content);
  }

  public static String signBroadcastTransaction(String transaction) throws IOException {
    return broadcastTransaction(getTransactionSign(transaction));
  }




}
