package love.sola.netsupport.api;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/11/5.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@AllArgsConstructor
public class Response {

	public int code;
	public String info;
	public Object result;

	public Response(ResponseCode code) {
		this(code, null);
	}

	public Response(ResponseCode code, Object result) {
		this.code = code.id;
		this.info = code.info;
		this.result = result;
	}


	public enum ResponseCode {

		ALREADY_SUBMITTED(1),
		OK(0),
		PARAMETER_REQUIRED(-1),
		ILLEGAL_PARAMETER(-2),
		AUTHORIZE_FAILED(-9),
		USER_NOT_FOUND(-11),
		TICKET_NOT_FOUND(-12),
		UNAUTHORIZED(-20),
		REQUEST_EXPIRED(-21),
		INTERNAL_ERROR(-90),
		DATABASE_ERROR(-91),
		;

		private static final Map<Integer, ResponseCode> ID_MAP = new HashMap<>();

		static {
			for (ResponseCode type : values()) {
				ID_MAP.put(type.id, type);
			}
		}

		public final String info;
		public final int id;

		ResponseCode(int id) {
			this.info = lang("RC_" + name());
			this.id = id;
		}

		public static ResponseCode fromId(int id) {
			return ID_MAP.get(id);
		}

		@Override
		public String toString() {
			return info;
		}

	}

}