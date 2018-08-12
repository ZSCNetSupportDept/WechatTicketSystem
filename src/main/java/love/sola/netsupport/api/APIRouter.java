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

package love.sola.netsupport.api;

import com.google.gson.Gson;

import org.hibernate.HibernateException;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@WebServlet(name = "APIRouter", urlPatterns = "/api/*", loadOnStartup = 11)
public class APIRouter extends HttpServlet {

    protected static Gson gson = SQLCore.gson;
    private Map<String, API> nodes = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Reflections reflections = new Reflections(getClass().getPackage().getName());
            Set<Class<? extends API>> set = reflections.getSubTypesOf(API.class);
            for (Class<? extends API> clz : set) {
                try {
                    System.out.println("Loading API: " + clz.getName());
                    API obj = clz.newInstance();
                    System.out.println("Registered API: " + obj);
                    nodes.put(obj.url, obj);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Total " + nodes.size() + " API(s) loaded.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.addHeader("Content-type", "application/json;charset=utf-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        Object obj = null;
        try {
            API api = nodes.get(req.getPathInfo());
            if (api == null) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            WxSession session = getSession(req);
            if (session == null) {
                obj = Error.UNAUTHORIZED;
                return;
            }
            if (api.authorize != null) {
                if (session.getAttribute(Attribute.AUTHORIZED) != api.authorize) {
                    obj = Error.UNAUTHORIZED;
                    return;
                }
                if (api.access == Access.USER) {
                    User u = session.getAttribute(Attribute.USER);
                    if (u == null) {
                        obj = Error.UNAUTHORIZED;
                        return;
                    }
                }
                if (api.access < Access.USER) {
                    Operator op = session.getAttribute(Attribute.OPERATOR);
                    if (op == null) {
                        obj = Error.UNAUTHORIZED;
                        return;
                    }
                    if (op.getAccess() > api.access) {
                        obj = Error.PERMISSION_DENIED;
                        return;
                    }
                }
            }
            obj = api.process(req, session);
        } catch (ParseException | NumberFormatException e) {
            obj = Error.ILLEGAL_PARAMETER;
        } catch (HibernateException e) {
            e.printStackTrace();
            obj = Error.DATABASE_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            obj = Error.INTERNAL_ERROR;
        } finally {
            if (!resp.isCommitted()) {
                try (PrintWriter out = resp.getWriter()) {
                    out.println(gson.toJson(obj));
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Method", "POST, GET, OPTIONS");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private static WxSession getSession(HttpServletRequest req) {
        String t = req.getParameter("token");
        if (t == null || t.isEmpty()) return null;
        return WechatSession.get(t);
    }

}
