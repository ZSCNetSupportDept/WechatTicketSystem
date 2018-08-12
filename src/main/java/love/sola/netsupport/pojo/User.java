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

package love.sola.netsupport.pojo;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.enums.ISPConverter;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Entity
@Table(name = "users")
public class User {

    //System Accounts
    public static User OFFICIAL_CHINA_UNICOM_XH;
    public static User OFFICIAL_CHINA_MOBILE_XH;
    public static User OFFICIAL_CHINA_MOBILE_FX;

    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_WECHAT = "wechatId";
    public static final String PROPERTY_BLOCK = "block";

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "name", updatable = false, nullable = false)
    private String name;
    @Convert(converter = ISPConverter.class)
    private ISP isp;
    @Column(name = "netaccount")
    private String netAccount;
    @Expose(serialize = false)
    @Column(name = "wechat")
    private String wechatId;
    private Integer block;
    private Integer room;
    private Long phone;

    public User() {
    }

    public User(Long id, String name, ISP isp, String netAccount, String wechatId, Integer block, Integer room, Long phone) {
        this.id = id;
        this.name = name;
        this.isp = isp;
        this.netAccount = netAccount;
        this.wechatId = wechatId;
        this.block = block;
        this.room = room;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ISP getIsp() {
        return isp;
    }

    public void setIsp(ISP isp) {
        this.isp = isp;
    }

    public String getNetAccount() {
        return netAccount;
    }

    public void setNetAccount(String netAccount) {
        this.netAccount = netAccount;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isp=" + isp +
                ", netAccount='" + netAccount + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", block=" + block +
                ", room=" + room +
                ", phone=" + phone +
                '}';
    }
}
