package love.sola.netsupport.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BlockConverter implements AttributeConverter<Block, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Block attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.id;
	}

	@Override
	public Block convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		return Block.fromId(dbData);
	}

}