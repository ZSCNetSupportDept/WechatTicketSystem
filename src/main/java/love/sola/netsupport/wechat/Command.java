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

package love.sola.netsupport.wechat;

import love.sola.netsupport.wechat.handler.*;
import love.sola.netsupport.wechat.handler.admin.LoginHandler;
import love.sola.netsupport.wechat.handler.admin.OperatorInfoHandler;
import love.sola.netsupport.wechat.handler.admin.SignHandler;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public enum Command {

    REGISTER(0, RegisterHandler.class),
    QUERY(1, QueryHandler.class),
    SUBMIT(2, SubmitHandler.class),
    CANCEL(3, CancelHandler.class),
    PROFILE(4, ProfileHandler.class),
    LOGIN(10, LoginHandler.class),
    OPERATOR_INFO(11, OperatorInfoHandler.class),
    SIGN(12, SignHandler.class), //FIXME
    ;

    private static final Map<Integer, Command> ID_MAP = new HashMap<>();

    static {
        for (Command type : values()) {
            if (type.id >= 0) {
                ID_MAP.put(type.id, type);
            }
        }
    }

    public final String regex;
    public final Class<? extends WxMpMessageHandler> handler;
    public final int id;

    Command(int id, Class<? extends WxMpMessageHandler> handler) {
        this.id = id;
        this.regex = lang("REGEX_" + name());
        this.handler = handler;
    }

    public static Command fromId(int id) {
        return ID_MAP.get(id);
    }

    @Override
    public String toString() {
        return name();
    }

}
