package com.tron.okhttp;

import com.tron.okhttp.utils.TronConstant;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.utils.Base58Check;

public class PrivateTest {
  public static void main(String[] args) throws Exception {
    // 通过私钥获取钱包地址
    byte[] rawAddr =
        KeyPair.publicKeyToAddress(
            SECP256K1.PublicKey.create(SECP256K1.PrivateKey.create(TronConstant.privateKey)));
    String str = Base58Check.bytesToBase58(rawAddr);
    System.out.println(str);
  }
}
