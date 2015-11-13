package love.sola.netsupport.api;

import lombok.AllArgsConstructor;
import love.sola.netsupport.enums.ResponseCode;

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

}
