package love.sola.netsupport.wechat;

import com.google.gson.*;
import love.sola.netsupport.config.Lang;
import love.sola.netsupport.enums.ISP;
import org.junit.Test;

import java.text.MessageFormat;
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
				.registerTypeAdapter(ISP.class, (JsonDeserializer<ISP>) (json, typeOfT, context) -> ISP.fromId(json.getAsJsonPrimitive().getAsInt()))
				.registerTypeAdapter(ISP.class, (JsonSerializer<ISP>) (src, typeOfSrc, context) -> new JsonPrimitive(src.id))
				.create();
		Date date = new Date();
		assert gson.fromJson(gson.toJson(date), Date.class).compareTo(date) == 0;
		assert gson.fromJson(gson.toJson(ISP.TELECOM), ISP.class) == ISP.TELECOM;
		assert gson.toJson(ISP.TELECOM).equals("1");
	}

	@Test
	public void testLong() {
		assert "15838838438".equals(MessageFormat.format("{0,number,#}", 15838838438L));
	}

}
