package love.sola.netsupport.wechat;

import com.google.gson.*;
import love.sola.netsupport.config.Lang;
import org.junit.Test;

import java.util.Date;

/**
 * ***********************************************
 * Created by Sola on 2015/12/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TestMessageFormat {

	@Test
	public void testLang() {
		assert Lang.messages != null;
	}

	@Test
	public void testJsonDate() {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
				.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()))
				.create();
		Date date = new Date();
		assert gson.fromJson(gson.toJson(date), Date.class).compareTo(date) == 0;
	}

}
