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
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableOperator;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.Crypto;
import love.sola.netsupport.wechat.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Sola {@literal <dev@sola.love>}
 */

@WebServlet(name = "Login", urlPatterns = "/api/admin/login", loadOnStartup = 12)
public class Login extends HttpServlet {

    private Gson gson = SQLCore.gson;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-type", "application/json;charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(login(request)));
        out.close();
    }

    private Object login(HttpServletRequest request) {
        try {
            int oid = Integer.parseInt(request.getParameter("id"));
            String password = request.getParameter("pass");
            boolean bypass = request.getParameter("bypass") != null;
            Operator op = TableOperator.get(oid);
            if (op == null)
                return Error.OPERATOR_NOT_FOUND;
            else if (op.getAccess() >= Access.NO_LOGIN)
                return Error.PERMISSION_DENIED;

            if (!Crypto.check(password, op.getPassword())) {
                return Error.WRONG_PASSWORD;
            }

            WxSession session = WechatSession.create();
            if (bypass) {
                session.setAttribute(Attribute.AUTHORIZED, Command.fromId(Integer.parseInt(request.getParameter("bypass"))));
            } else {
                session.setAttribute(Attribute.AUTHORIZED, Command.LOGIN);
            }

            session.setAttribute(Attribute.WECHAT, op.getWechat());
            session.setAttribute(Attribute.OPERATOR, op);

            if (request.getParameter("bypassuser") != null) {
                User u = TableUser.getById(Long.parseLong(request.getParameter("bypassuser")));
                session.setAttribute(Attribute.USER, u);
                session.setAttribute(Attribute.WECHAT, u.getWechatId());
            }
            if (request.getParameter("bypasswechat") != null) {
                session.setAttribute(Attribute.WECHAT, request.getParameter("bypasswechat"));
            }
            return session.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return Error.INTERNAL_ERROR;
        }
    }
}