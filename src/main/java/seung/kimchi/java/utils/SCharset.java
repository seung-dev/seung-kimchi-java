package seung.kimchi.java.utils;

import java.nio.charset.Charset;

/**
 * <h1>Description</h1>
 * <pre>
 * Korean charsets.
 * 
 * {@value SCharset#_EUC_KR}
 * {@value SCharset#_ISO_2022_KR}
 * {@value SCharset#_ISO_8859_1}
 * {@value SCharset#_UTF_8}
 * {@value SCharset#_X_JOHAB}
 * {@value SCharset#_X_WINDOWS_949}
 * </pre>
 * <hr>
 * @author seung
 * @since 2020.12.21
 * @version 1.0.0
 */
public class SCharset {

//	public static final String _BIG5 = "Big5";
//	public static final String _BIG5_HKSCS = "Big5-HKSCS";
//	public static final String _CESU_8 = "CESU-8";
//	public static final String _EUC_JP = "EUC-JP";
	/**
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(Charset.forName(SCharset._EUC_KR).aliases());
	 * </pre>
	 * <h1>Aliase</h1>
	 * <pre>
	 * ["ksc5601-1987","csEUCKR","ksc5601_1987","ksc5601","5601","euc_kr","ksc_5601","ks_c_5601-1987","euckr"]
	 * </pre>
	 * <hr>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static final String _EUC_KR = "EUC-KR";
//	public static final String _GB18030 = "GB18030";
//	public static final String _GB2312 = "GB2312";
//	public static final String _GBK = "GBK";
//	public static final String _IBM_THAI = "IBM-Thai";
//	public static final String _IBM00858 = "IBM00858";
//	public static final String _IBM01140 = "IBM01140";
//	public static final String _IBM01141 = "IBM01141";
//	public static final String _IBM01142 = "IBM01142";
//	public static final String _IBM01143 = "IBM01143";
//	public static final String _IBM01144 = "IBM01144";
//	public static final String _IBM01145 = "IBM01145";
//	public static final String _IBM01146 = "IBM01146";
//	public static final String _IBM01147 = "IBM01147";
//	public static final String _IBM01148 = "IBM01148";
//	public static final String _IBM01149 = "IBM01149";
//	public static final String _IBM037 = "IBM037";
//	public static final String _IBM1026 = "IBM1026";
//	public static final String _IBM1047 = "IBM1047";
//	public static final String _IBM273 = "IBM273";
//	public static final String _IBM277 = "IBM277";
//	public static final String _IBM278 = "IBM278";
//	public static final String _IBM280 = "IBM280";
//	public static final String _IBM284 = "IBM284";
//	public static final String _IBM285 = "IBM285";
//	public static final String _IBM290 = "IBM290";
//	public static final String _IBM297 = "IBM297";
//	public static final String _IBM420 = "IBM420";
//	public static final String _IBM424 = "IBM424";
//	public static final String _IBM437 = "IBM437";
//	public static final String _IBM500 = "IBM500";
//	public static final String _IBM775 = "IBM775";
//	public static final String _IBM850 = "IBM850";
//	public static final String _IBM852 = "IBM852";
//	public static final String _IBM855 = "IBM855";
//	public static final String _IBM857 = "IBM857";
//	public static final String _IBM860 = "IBM860";
//	public static final String _IBM861 = "IBM861";
//	public static final String _IBM862 = "IBM862";
//	public static final String _IBM863 = "IBM863";
//	public static final String _IBM864 = "IBM864";
//	public static final String _IBM865 = "IBM865";
//	public static final String _IBM866 = "IBM866";
//	public static final String _IBM868 = "IBM868";
//	public static final String _IBM869 = "IBM869";
//	public static final String _IBM870 = "IBM870";
//	public static final String _IBM871 = "IBM871";
//	public static final String _IBM918 = "IBM918";
//	public static final String _ISO_2022_CN = "ISO-2022-CN";
//	public static final String _ISO_2022_JP = "ISO-2022-JP";
//	public static final String _ISO_2022_JP_2 = "ISO-2022-JP-2";
	/**
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(Charset.forName(SCharset._ISO_2022_KR).aliases());
	 * </pre>
	 * <h1>Aliase</h1>
	 * <pre>
	 * ["csISO2022KR","ISO2022KR"]
	 * </pre>
	 * <hr>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static final String _ISO_2022_KR = "ISO-2022-KR";
	/**
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(Charset.forName(SCharset._ISO_8859_1).aliases());
	 * </pre>
	 * <h1>Aliase</h1>
	 * <pre>
	 * ["819","ISO8859-1","l1","ISO_8859-1:1987","ISO_8859-1","8859_1","iso-ir-100","latin1","cp819","ISO8859_1","IBM819","ISO_8859_1","IBM-819","csISOLatin1"]
	 * </pre>
	 * <hr>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static final String _ISO_8859_1 = "ISO-8859-1";
//	public static final String _ISO_8859_13 = "ISO-8859-13";
//	public static final String _ISO_8859_15 = "ISO-8859-15";
//	public static final String _ISO_8859_2 = "ISO-8859-2";
//	public static final String _ISO_8859_3 = "ISO-8859-3";
//	public static final String _ISO_8859_4 = "ISO-8859-4";
//	public static final String _ISO_8859_5 = "ISO-8859-5";
//	public static final String _ISO_8859_6 = "ISO-8859-6";
//	public static final String _ISO_8859_7 = "ISO-8859-7";
//	public static final String _ISO_8859_8 = "ISO-8859-8";
//	public static final String _ISO_8859_9 = "ISO-8859-9";
//	public static final String _JIS_X0201 = "JIS_X0201";
//	public static final String _JIS_X0212_1990 = "JIS_X0212-1990";
//	public static final String _KOI8_R = "KOI8-R";
//	public static final String _KOI8_U = "KOI8-U";
//	public static final String _SHIFT_JIS = "Shift_JIS";
//	public static final String _TIS_620 = "TIS-620";
	public static final String _US_ASCII = "US-ASCII";
	public static final String _UTF_16 = "UTF-16";
	public static final String _UTF_16BE = "UTF-16BE";
	public static final String _UTF_16LE = "UTF-16LE";
	public static final String _UTF_32 = "UTF-32";
	public static final String _UTF_32BE = "UTF-32BE";
	public static final String _UTF_32LE = "UTF-32LE";
	/**
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(Charset.forName(SCharset._UTF_8).aliases());
	 * </pre>
	 * <h1>Aliase</h1>
	 * <pre>
	 * ["unicode-1-1-utf-8","UTF8"]
	 * </pre>
	 * <hr>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static final String _UTF_8 = "UTF-8";
//	public static final String _WINDOWS_1250 = "windows-1250";
//	public static final String _WINDOWS_1251 = "windows-1251";
//	public static final String _WINDOWS_1252 = "windows-1252";
//	public static final String _WINDOWS_1253 = "windows-1253";
//	public static final String _WINDOWS_1254 = "windows-1254";
//	public static final String _WINDOWS_1255 = "windows-1255";
//	public static final String _WINDOWS_1256 = "windows-1256";
//	public static final String _WINDOWS_1257 = "windows-1257";
//	public static final String _WINDOWS_1258 = "windows-1258";
//	public static final String _WINDOWS_31J = "windows-31j";
//	public static final String _X_BIG5_HKSCS_2001 = "x-Big5-HKSCS-2001";
//	public static final String _X_BIG5_SOLARIS = "x-Big5-Solaris";
//	public static final String _X_EUC_JP_LINUX = "x-euc-jp-linux";
//	public static final String _X_EUC_TW = "x-EUC-TW";
//	public static final String _X_EUCJP_OPEN = "x-eucJP-Open";
//	public static final String _X_IBM1006 = "x-IBM1006";
//	public static final String _X_IBM1025 = "x-IBM1025";
//	public static final String _X_IBM1046 = "x-IBM1046";
//	public static final String _X_IBM1097 = "x-IBM1097";
//	public static final String _X_IBM1098 = "x-IBM1098";
//	public static final String _X_IBM1112 = "x-IBM1112";
//	public static final String _X_IBM1122 = "x-IBM1122";
//	public static final String _X_IBM1123 = "x-IBM1123";
//	public static final String _X_IBM1124 = "x-IBM1124";
//	public static final String _X_IBM1166 = "x-IBM1166";
//	public static final String _X_IBM1364 = "x-IBM1364";
//	public static final String _X_IBM1381 = "x-IBM1381";
//	public static final String _X_IBM1383 = "x-IBM1383";
//	public static final String _X_IBM300 = "x-IBM300";
//	public static final String _X_IBM33722 = "x-IBM33722";
//	public static final String _X_IBM737 = "x-IBM737";
//	public static final String _X_IBM833 = "x-IBM833";
//	public static final String _X_IBM834 = "x-IBM834";
//	public static final String _X_IBM856 = "x-IBM856";
//	public static final String _X_IBM874 = "x-IBM874";
//	public static final String _X_IBM875 = "x-IBM875";
//	public static final String _X_IBM921 = "x-IBM921";
//	public static final String _X_IBM922 = "x-IBM922";
//	public static final String _X_IBM930 = "x-IBM930";
//	public static final String _X_IBM933 = "x-IBM933";
//	public static final String _X_IBM935 = "x-IBM935";
//	public static final String _X_IBM937 = "x-IBM937";
//	public static final String _X_IBM939 = "x-IBM939";
//	public static final String _X_IBM942 = "x-IBM942";
//	public static final String _X_IBM942C = "x-IBM942C";
//	public static final String _X_IBM943 = "x-IBM943";
//	public static final String _X_IBM943C = "x-IBM943C";
//	public static final String _X_IBM948 = "x-IBM948";
//	public static final String _X_IBM949 = "x-IBM949";
//	public static final String _X_IBM949C = "x-IBM949C";
//	public static final String _X_IBM950 = "x-IBM950";
//	public static final String _X_IBM964 = "x-IBM964";
//	public static final String _X_IBM970 = "x-IBM970";
//	public static final String _X_ISCII91 = "x-ISCII91";
//	public static final String _X_ISO_2022_CN_CNS = "x-ISO-2022-CN-CNS";
//	public static final String _X_ISO_2022_CN_GB = "x-ISO-2022-CN-GB";
//	public static final String _X_ISO_8859_11 = "x-iso-8859-11";
//	public static final String _X_JIS0208 = "x-JIS0208";
//	public static final String _X_JISAUTODETECT = "x-JISAutoDetect";
	/**
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(Charset.forName(SCharset._X_JOHAB).aliases());
	 * </pre>
	 * <h1>Aliase</h1>
	 * <pre>
	 * ["ms1361","ksc5601_1992","johab","ksc5601-1992"]
	 * </pre>
	 * <hr>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static final String _X_JOHAB = "x-Johab";
//	public static final String _X_MACARABIC = "x-MacArabic";
//	public static final String _X_MACCENTRALEUROPE = "x-MacCentralEurope";
//	public static final String _X_MACCROATIAN = "x-MacCroatian";
//	public static final String _X_MACCYRILLIC = "x-MacCyrillic";
//	public static final String _X_MACDINGBAT = "x-MacDingbat";
//	public static final String _X_MACGREEK = "x-MacGreek";
//	public static final String _X_MACHEBREW = "x-MacHebrew";
//	public static final String _X_MACICELAND = "x-MacIceland";
//	public static final String _X_MACROMAN = "x-MacRoman";
//	public static final String _X_MACROMANIA = "x-MacRomania";
//	public static final String _X_MACSYMBOL = "x-MacSymbol";
//	public static final String _X_MACTHAI = "x-MacThai";
//	public static final String _X_MACTURKISH = "x-MacTurkish";
//	public static final String _X_MACUKRAINE = "x-MacUkraine";
//	public static final String _X_MS932_0213 = "x-MS932_0213";
//	public static final String _X_MS950_HKSCS = "x-MS950-HKSCS";
//	public static final String _X_MS950_HKSCS_XP = "x-MS950-HKSCS-XP";
//	public static final String _X_MSWIN_936 = "x-mswin-936";
//	public static final String _X_PCK = "x-PCK";
//	public static final String _X_SJIS_0213 = "x-SJIS_0213";
//	public static final String _X_UTF_16LE_BOM = "x-UTF-16LE-BOM";
//	public static final String _X_UTF_32BE_BOM = "X-UTF-32BE-BOM";
//	public static final String _X_UTF_32LE_BOM = "X-UTF-32LE-BOM";
//	public static final String _X_WINDOWS_50220 = "x-windows-50220";
//	public static final String _X_WINDOWS_50221 = "x-windows-50221";
//	public static final String _X_WINDOWS_874 = "x-windows-874";
	/**
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(Charset.forName(SCharset._X_WINDOWS_949).aliases());
	 * </pre>
	 * <h1>Aliase</h1>
	 * <pre>
	 * ["windows949","ms949","windows-949","ms_949"]
	 * </pre>
	 * <hr>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static final String _X_WINDOWS_949 = "x-windows-949";
//	public static final String _X_WINDOWS_950 = "x-windows-950";
//	public static final String _X_WINDOWS_ISO2022JP = "x-windows-iso2022jp";
	
}
