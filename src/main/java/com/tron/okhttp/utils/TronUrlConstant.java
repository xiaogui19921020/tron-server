package com.tron.okhttp.utils;

public class TronUrlConstant {
  static String TRON_DOMAIN_URL = "https://api.shasta.trongrid.io";

  /** 账号信息 */
  static String tronGetaccount = TRON_DOMAIN_URL + "/walletsolidity/getaccount";
  /** 创建交易 */
  static String tronCreateTransaction = TRON_DOMAIN_URL + "/wallet/createtransaction";
  /** 交易签名 */
  static String tronGetTransactionSign = TRON_DOMAIN_URL + "/wallet/gettransactionsign";
  /** 广播交易 */
  static String tronBroadcastTransaction = TRON_DOMAIN_URL + "/wallet/broadcasttransaction";
}
