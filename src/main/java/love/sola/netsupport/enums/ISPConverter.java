package love.sola.netsupport.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ISPConverter implements AttributeConverter<ISP, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ISP attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.id;
	}

	@Override
	public ISP convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		return ISP.fromId(dbData);
	}

}