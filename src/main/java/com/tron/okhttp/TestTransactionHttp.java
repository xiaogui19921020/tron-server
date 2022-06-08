package com.tron.okhttp;

import com.alibaba.fastjson.JSONObject;
import com.tron.okhttp.utils.TronHttpUtils;

public class TestTransactionHttp {

  public static void main(String[] args) throws Exception {

    String account = TronHttpUtils.getAccount("TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf");
    System.out.println(account);
    System.out.println("-------------------------------------------");
    String transactionStr =
        TronHttpUtils.createTransaction(
            "TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf",
            "TRivVnAC3BRUvhsXBbUi44yajZRxPQGPEW",
            10000000L,
            true);
    JSONObject transactionJson = JSONObject.parseObject(transactionStr);
    String txID = transactionJson.getString("txID");
    String raw_data = transactionJson.getString("raw_data");
    System.out.println(txID);
    System.out.println(raw_data);
    System.out.println("-------------------------------------------");
    String resultBroadcasttransaction = TronHttpUtils.signBroadcastTransaction(transactionStr);
    System.out.println(resultBroadcasttransaction);
  }
}
