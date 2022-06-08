package com.tron.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.tron.okhttp.utils.TronConstant;
import com.tron.okhttp.utils.TronHttpUtils;
import org.tron.trident.abi.FunctionReturnDecoder;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.Utf8String;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Numeric;

import java.util.Arrays;
import java.util.Collections;

public class ApiWapperTransactionHttp {

  public static void main(String[] args) throws Exception {

    //ApiWrapper client = ApiWrapper.ofShasta(TronConstant.privateKey);
    ApiWrapper client = ApiWrapper.ofMainnet(TronConstant.privateKey,TronConstant.apiKey);

    Response.Account account = client.getAccount("TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf");
    System.out.println(account.getBalance());

    Function name = new Function("name",
            Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));
    Response.TransactionExtention extension = client.constantCall("TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf", "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t", name);

    String result = Numeric.toHexString(extension.getConstantResult(0).toByteArray());

    String value = (String) FunctionReturnDecoder.decode(result, name.getOutputParameters()).get(0).getValue();
    System.out.println(result);
    System.out.println(value);

    /*Response.TransactionExtention transactionExtention = client.transfer("TNzeyjDYagHSBXJYMKvAtwRBrDgGpwGrMf",
            "TRivVnAC3BRUvhsXBbUi44yajZRxPQGPEW",
            10000000L);
    Chain.Transaction  transaction= client.signTransaction(transactionExtention);
    String txid = client.broadcastTransaction(transaction);
    System.out.println(txid);*/


    //client.triggerCall()
  }
}
