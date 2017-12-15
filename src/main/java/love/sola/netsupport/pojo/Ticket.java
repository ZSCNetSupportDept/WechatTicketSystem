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

import love.sola.netsupport.sql.TableTicket;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Entity
@Table(name = "tickets")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Ticket {

    public static final String PROPERTY_USER = "user";
    public static final String PROPERTY_STATUS = "status";
    public static final String PROPERTY_SUBMIT_TIME = "submitTime";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = TableTicket.COLUMN_SID)
    private User user;
    private String description;
    @Column(name = TableTicket.COLUMN_SUBMIT_TIME, insertable = false, updatable = false)
    private Date submitTime;
    private String remark;
    private Date updateTime;
    @ManyToOne(optional = true)
    @JoinColumn(name = TableTicket.COLUMN_OPSID)
    private Operator operator;
    private Integer status;

    public Ticket() {
    }

    public Ticket(User user, String description, Date submitTime, String remark, Date updateTime, Operator operator, Integer status) {
        this.user = user;
        this.description = description;
        this.submitTime = submitTime;
        this.remark = remark;
        this.updateTime = updateTime;
        this.operator = operator;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", submitTime=" + submitTime +
                ", remark='" + remark + '\'' +
                ", updateTime=" + updateTime +
                ", operator=" + operator +
                ", status=" + status +
                '}';
    }
}
