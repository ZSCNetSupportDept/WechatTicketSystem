/*
 * This file is part of WechatTicketSystem.
 *
 * WechatTicketSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WechatTicketSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WechatTicketSystem.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.sola.netsupport.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
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