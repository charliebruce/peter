import java.io.File;

import net.minecraft.client.pd;


public class MiscSnippets {

	/*      */ 
	/*      */   public static File a(String paramString) {
	/*  458 */     String str1 = System.getProperty("user.home", ".");
	/*      */     File localFile;
	/*  460 */     switch (kn.a[y().ordinal()]) {
	/*      */     case 1:
	/*      */     case 2:
	/*  463 */       localFile = new File(str1, '.' + paramString + '/');
	/*  464 */       break;
	/*      */     case 3:
	/*  466 */       String str2 = System.getenv("APPDATA");
	/*  467 */       if (str2 != null) {
	/*  468 */         localFile = new File(str2, "." + paramString + '/');
	/*      */       }
	/*      */       else {
	/*  471 */         localFile = new File(str1, '.' + paramString + '/');
	/*      */       }
	/*  473 */       break;
	/*      */     case 4:
	/*  475 */       localFile = new File(str1, "Library/Application Support/" + paramString);
	/*      */ 
	/*  477 */       break;
	/*      */     default:
	/*  479 */       localFile = new File(str1, paramString + '/');
	/*      */     }
	/*  481 */     if ((!localFile.exists()) && 
	/*  482 */       (!localFile.mkdirs())) {
	/*  483 */       throw new RuntimeException("The working directory could not be created: " + localFile);
	/*      */     }
	/*      */ 
	/*  486 */     return localFile;
	/*      */   }
	
	
	  */ 
	  /*      */   private static pd y() {
	  /*  490 */     String str = System.getProperty("os.name").toLowerCase();
	  /*  491 */     if (str.contains("win"))
	  /*  492 */       return pd.c;
	  /*  493 */     if (str.contains("mac"))
	  /*  494 */       return pd.d;
	  /*  495 */     if (str.contains("solaris"))
	  /*  496 */       return pd.b;
	  /*  497 */     if (str.contains("sunos"))
	  /*  498 */       return pd.b;
	  /*  499 */     if (str.contains("linux"))
	  /*  500 */       return pd.a;
	  /*  501 */     if (str.contains("unix"))
	  /*  502 */       return pd.a;
	  /*  503 */     return pd.e;
	  /*      */   }
}
