package pw.cdmi.aws.edu.weixin.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * 提供微信小程序用户信息的加解密接口(UTF8编码的字符串).
 */
public class WXBizDataCrypt {
	static Charset CHARSET = Charset.forName("utf-8");
	Base64 base64 = new Base64();
	byte[] aesKey;
	byte[] ivParameter;

	/**
	 * 构造函数
	 * @param sessionKey 从微信获取的会话密钥
	 * @param iv 消息中携带的IV
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public WXBizDataCrypt(String sessionKey, String iv) throws AesException {
		aesKey = Base64.decodeBase64(sessionKey);
		ivParameter = Base64.decodeBase64(iv);
	}

	/**
	 * 对明文进行加密.
	 * 
	 * @param text 需要加密的明文
	 * @return 加密后base64编码的字符串
	 * @throws AesException aes加密失败
	 */
	public String EncryptData(String text) throws AesException {
		ByteGroup byteCollector = new ByteGroup();
		byte[] textBytes = text.getBytes(CHARSET);

		// randomStr + networkBytesOrder + text + corpid
		byteCollector.addBytes(textBytes);

		// ... + pad: 使用自定义的填充方式对明文进行补位填充
		byte[] padBytes = PKCS7Encoder.encode(textBytes.length);
		byteCollector.addBytes(padBytes);

		// 获得最终的字节流, 未加密
		byte[] unencrypted = byteCollector.toBytes();

		try {
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(ivParameter, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			// 加密
			byte[] encrypted = cipher.doFinal(unencrypted);

			// 使用BASE64对加密后的字符串进行编码
			return base64.encodeToString(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.EncryptAESError);
		}
	}

	/**
	 * 对密文进行解密.
	 * 
	 * @param text 需要解密的密文
	 * @return 解密得到的明文
	 * @throws AesException aes解密失败
	 */
	public String DecryptData(String text) throws AesException {
		byte[] original;
		try {
			// 设置解密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(ivParameter);
			cipher.init(Cipher.DECRYPT_MODE, key_spec, ivSpec);

			// 使用BASE64对密文进行解码
			byte[] encrypted = Base64.decodeBase64(text);

			// 解密
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.DecryptAESError);
		}

		String data;
		try {
			// 去除补位字符
			byte[] bytes = PKCS7Encoder.decode(original);

			data = new String(bytes, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.IllegalBuffer);
		}

		return data;
	}
}