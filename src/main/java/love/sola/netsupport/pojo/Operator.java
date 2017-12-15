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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Entity
@Table(name = "operators")
public class Operator {

    public static final String PROPERTY_WECHAT = "wechat";

    //System Accounts
    public static Operator USER_SELF;
    public static Operator ADMIN;

    @Id
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Integer id;
    @Column(name = "name", nullable = false, insertable = false, updatable = false)
    private String name;
    @Column(name = "access", nullable = false, insertable = false, updatable = false)
    private Integer access;
    @Column(name = "wechat", insertable = false, updatable = false)
    @Expose(serialize = false)
    private String wechat;
    private Integer block;
    private Integer week;
    @Expose(serialize = false)
    private String password;

    public Operator(Integer id, String name, Integer access, String wechat, Integer block, Integer week, String password) {
        this.id = id;
        this.name = name;
        this.access = access;
        this.wechat = wechat;
        this.block = block;
        this.week = week;
        this.password = password;
    }

    public Operator() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", access=" + access +
                ", wechat='" + wechat + '\'' +
                ", block=" + block +
                ", week=" + week +
                '}';
    }
}
