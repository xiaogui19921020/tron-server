package com.tron.okhttp.utils;

import java.math.BigInteger;

public class TronConstant {
  public static Boolean isTest = true; // 是否测试网
  public static String ownerAddress = "TA7weEqvrbyMygQmbNbFuAi2ndEBUAp45h"; // 钱包地址
  /** 钱包私钥，创建一个钱包，用这个钱包去发行usdt合约 */
  public static String privateKey =
      "e088f372ffaf11b33900e5fd36963abdc64c4819386436541347bb4d9af17dd708";
  /** TRC20合约地址 */
  public static String TRC20_USDT_ADDRESS = "TAgaqV6dV6Fevud6CZGDFoie7P6NAoujHo";
  /** TRC20合约精度 */
  public static BigInteger TRC20_USDT_DECIMALS = new BigInteger("1000000");


  public static String tronDomainUlr = "https://api.shasta.trongrid.io";
  public static String apiKey = "68aea214-f593-4946-8cb2-c5ecb7e9e67f";
}
