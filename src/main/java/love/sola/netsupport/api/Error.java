package love.sola.netsupport.api;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/11/5.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Error {

	public static final Error ALREADY_SUBMITTED = new Error(1);
	public static final Error OK = new Error(0);
	public static final Error PARAMETER_REQUIRED = new Error(-1);
	public static final Error ILLEGAL_PARAMETER = new Error(-2);
	public static final Error REQUEST_FAILED = new Error(-3);
	public static final Error LENGTH_LIMIT_EXCEEDED = new Error(-4);
	public static final Error INVALID_PARAMETER = new Error(-5);
	public static final Error USER_NOT_FOUND = new Error(-11);
	public static final Error TICKET_NOT_FOUND = new Error(-12);
	public static final Error OPERATOR_NOT_FOUND = new Error(-13);
	public static final Error UNAUTHORIZED = new Error(-20);
	public static final Error WRONG_PASSWORD = new Error(-22);
	public static final Error PERMISSION_DENIED = new Error(-24);
	public static final Error INTERNAL_ERROR = new Error(-90);
	public static final Error DATABASE_ERROR = new Error(-91);

	public int errCode;
	public String errMsg;

	private Error(int code) {
		this(code, lang("ERR_" + code));
	}

	public Error(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public Error withMsg(String msg) {
		return new Error(errCode, msg);
	}

}