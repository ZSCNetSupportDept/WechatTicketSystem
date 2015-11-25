package love.sola.netsupport.api;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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

		OK(0, "OK"),
		PARAMETER_REQUIRED(-1, "Parameter Required"),
		ILLEGAL_PARAMETER(-2, "Illegal parameter"),
		USER_NOT_FOUND(-11, "User not found"),
		;

		private static final Map<Integer, ResponseCode> ID_MAP = new HashMap<>();

		static {
			for (ResponseCode type : values()) {
				if (type.id > 0) {
					ID_MAP.put(type.id, type);
				}
			}
		}

		public final String info;
		public final int id;

		ResponseCode(int id, String info) {
			this.info = info;
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