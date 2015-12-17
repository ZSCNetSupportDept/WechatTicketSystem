package love.sola.netsupport.wechat;

import com.google.gson.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import love.sola.netsupport.config.Lang;
import love.sola.netsupport.enums.ISP;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

	@Test
	public void testYaml() {
		assert new Yaml().loadAs("array: \n  - \"err\"\n  - \"ee\"", TestArray.class).array.length == 2;
	}

	@Test
	public void testYamlDump() {
		Map<String, TestArray> map = new HashMap<>();
		map.put("fuck", new TestArray(new String[]{"one", "two", "three"}));
		map.put("you", new TestArray(new String[]{"one", "two", "three"}));
		System.out.println(new Yaml().dumpAs(map, new Tag(map.getClass()), DumperOptions.FlowStyle.BLOCK));
	}

	@Data
	@AllArgsConstructor
	public static class TestArray {
		String[] array;
	}

}
