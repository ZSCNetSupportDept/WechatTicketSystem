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

package love.sola.netsupport.wechat.handler.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import love.sola.netsupport.sql.SQLCore;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

/**
 * @author Sola {@literal <dev@sola.love>}
 * @deprecated limited time only
 */
@Deprecated
public class SignHandler implements WxMpMessageHandler {

    public static Pattern pat = Pattern.compile("^(?i)Auth (\\d{4})");
    public static final int INVALID_ID = -1;
    public static final int SIGNED_ID = -2;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String msg = wxMessage.getContent();
        TextBuilder out = WxMpXmlOutMessage.TEXT().toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName());
        Matcher mat = pat.matcher(msg);

        root:
        if (mat.find()) {
            int id = Integer.parseInt(mat.group(1));
            try (Connection conn = SQLCore.ds.getConnection()) {
                switch (checkID(conn, id)) {
                    case INVALID_ID:
                        out.content("无效ID。");
                        break root;
                    case SIGNED_ID:
                        out.content("该ID已登记过。");
                        break root;
                }
                if (checkDuplicated(conn, wxMessage.getFromUserName())) {
                    out.content("你的微信已经登记过。");
                    break root;
                }
                PreparedStatement ps = conn.prepareStatement("UPDATE auth SET wechat=? WHERE id=?");
                ps.setString(1, wxMessage.getFromUserName());
                ps.setInt(2, id);
                if (ps.executeUpdate() == 1) {
                    out.content("登记成功。");
                } else {
                    out.content("登记失败，请联系管理员。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.content("系统异常，请联系管理员。");
            }
        } else {
            out.content("无效ID。");
        }
        return out.build();
    }

    private static int checkID(Connection conn, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT wechat FROM auth WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("wechat") != null ? SIGNED_ID : 0;
        } else {
            return INVALID_ID;
        }
    }

    private static boolean checkDuplicated(Connection conn, String wechat) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT wechat FROM auth WHERE wechat=?");
        ps.setString(1, wechat);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

}
