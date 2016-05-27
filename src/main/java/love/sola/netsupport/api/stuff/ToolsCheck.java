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

package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.wechat.Command;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Sola
 */
public class ToolsCheck extends API {

	public ToolsCheck() {
		url = "/admin/toolscheck";
		access = Access.MEMBER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		if (req.getMethod().equals("GET")) {
			return query(req, session);
		} else if (req.getMethod().equals("POST")) {
			return submit(req, session);
		}
		return null;
	}

	private Object submit(HttpServletRequest req, WxSession session) {
		Operator op = session.getAttribute(Attribute.OPERATOR);
		int status = Integer.valueOf(getParameterWithDefault(req.getParameter("status"), "0"));
		String remark = req.getParameter("remark");
		if (status != 0 && StringUtils.isBlank(remark)) {
			return Error.PARAMETER_REQUIRED;
		}
		try (Session s = SQLCore.sf.openSession()) {
			s.beginTransaction();
			s.save(new love.sola.netsupport.pojo.ToolsCheck(
							null,
							op,
							op.getBlock(),
							new Date(),
							status,
							remark
					)
			);
			s.getTransaction().commit();
			return Error.OK;
		}
	}

	private Object query(HttpServletRequest req, WxSession session) {
		int status = Integer.valueOf(getParameterWithDefault(req.getParameter("status"), "0"));
		Date after = getDay(getParameterAsDate(req.getParameter("after"), getToday()));
		Date before = getDay(getParameterAsDate(req.getParameter("before"), getToday()));
		before = DateUtils.addDays(before, 1);
		int block = Integer.valueOf(getParameterWithDefault(req.getParameter("block"), "0"));
		try (Session s = SQLCore.sf.openSession()) {
			Criteria query = s.createCriteria(love.sola.netsupport.pojo.ToolsCheck.class);
			query.add(
					Restrictions.sqlRestriction(
							"{alias}.status & ? = ?",
							new Object[]{status, status},
							new Type[]{IntegerType.INSTANCE, IntegerType.INSTANCE}
					)
			);
			query.add(Restrictions.between("checkTime", after, before));
			if (block != 0) query.add(Restrictions.eq("block", block));
			return query.list();
		}
	}

}
