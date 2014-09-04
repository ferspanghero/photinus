package sample;

/**
 * This is Java translation of the heartbleed bug.
 * 
 * Explanation of the bug and the fix
 * http://codequirksnrants.wordpress.com/2014/04/09/heartbleed-bug-the-source-code/
 * 
 * The diffs 
 * file: d1_both.c
 * http://git.openssl.org/gitweb/?p=openssl.git;a=blobdiff;f=ssl/d1_both.c;h=2e8cf681ed0976e2b16460170fda27c77cfec6cc;hp=7a5596a6b373aeabbd6d8d674f0e20b1618c5012;hb=96db9023b881d7cd9f379b0c154650d6c108e9a3;hpb=0d7717fc9c83dafab8153cbd5e2180e6e04cc802
 * 
 * file: t1_lib.c
 * http://git.openssl.org/gitweb/?p=openssl.git;a=blobdiff;f=ssl/t1_lib.c;h=bddffd92cc045ae920d63e6e140c78b4d96c3425;hp=b82fadace66e764b47ab2d854621ad89b804e8d2;hb=96db9023b881d7cd9f379b0c154650d6c108e9a3;hpb=0d7717fc9c83dafab8153cbd5e2180e6e04cc802
 * 
 * 
 * @author Christian Adriano
 *
 */
public class SSLHeartbleed {

	private StringBuffer payload;
	private StringBuffer padding;
	
	public StringBuffer getRequest(String word,int length){
		
		return null;
	}
	
	
	
	
}
