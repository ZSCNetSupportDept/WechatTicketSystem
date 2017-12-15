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

package love.sola.netsupport.sql;

import love.sola.netsupport.pojo.Operator;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TableOperator extends SQLCore {

    public static boolean has(String wechat) {
        try (Session s = SQLCore.sf.openSession()) {
            return (long) s.createCriteria(Operator.class)
                    .add(Restrictions.eq(Operator.PROPERTY_WECHAT, wechat))
                    .setProjection(Projections.rowCount())
                    .uniqueResult() > 0;
        }
    }


    public static Operator get(String wechat) {
        try (Session s = SQLCore.sf.openSession()) {
            return (Operator) s.createCriteria(Operator.class)
                    .add(Restrictions.eq(Operator.PROPERTY_WECHAT, wechat))
                    .uniqueResult();
        }
    }

    public static Operator get(int id) {
        try (Session s = SQLCore.sf.openSession()) {
            return s.get(Operator.class, id);
        }
    }

    protected static void init() {
        try (Session s = SQLCore.sf.openSession()) {
            Operator.USER_SELF = s.get(Operator.class, -1);
            Operator.ADMIN = s.get(Operator.class, 0);
        }
    }

}
