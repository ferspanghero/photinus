package sample;

/**
 * Java illustration of the GoTo fail bug in Apple SSL API.
 *   
 *   The failure comprise a lack of error message for signature validation, 
 *   which is not checked because a if statement is never reached.
 *   
 * Converted the C code to Java. 
 * Explanation of the bug can be found at the following links:
 *  http://avandeursen.com/2014/02/22/gotofail-security/ 
 *  https://www.imperialviolet.org/2014/02/22/applebug.html
 * 
 * Original source code can be found at http://opensource.apple.com/source/Security/Security-55471/libsecurity_ssl/lib/sslKeyExchange.c
 * 
 * @author Christian Adriano
 *
 */
public class AppleOpenSSL {

	/**public static OSStatus SSLVerifySignedServerKeyExchange(SSLContext ctx, boolean isRsa, SSLBuffer signedParams,
	                                 int signature, UInt16 signatureLen)
	{
	    
	    ...
	 
	    if ((err = SSLHashSHA1.update(&hashCtx, &serverRandom)) != 0)
	        return fail(err);
	    if ((err = SSLHashSHA1.update(&hashCtx, &signedParams)) != 0)
	        return fail(err);
	        return fail(err);
	    if ((err = SSLHashSHA1.final(&hashCtx, &hashOut)) != 0)
	        return fail(err);
	    ...
	 
	private void fail(OssStatus err){	
	       
	    SSLFreeBuffer(signedHashes);
	    SSLFreeBuffer(hashCtx);
	    return err;
	}
	
	   */
	
	private void SSFreeBuffer(StringBuffer buffer){
		//buffer.clean();
	
	}

    private class OSSStatus{}
	
	private class SSLContext{}
	
	private class SSLBuffer{}
	
	private class UInt16{}
	
	
	
}
