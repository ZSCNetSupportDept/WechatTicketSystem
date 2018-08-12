package love.sola.netsupport.pojo;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Entity
@Table(name = "toolschk", indexes = {
        @Index(columnList = "block,chktime,status"),
        @Index(columnList = "chktime,status")
})
@DynamicInsert
public class ToolsCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "opsid", nullable = false)
    private Operator operator;
    @Column(nullable = false)
    private Integer block;
    @Column(name = "chktime", nullable = false)
    private Date checkTime = new Date();
    @ColumnDefault("0")
    private Integer status = 0;
    private String remark;


    public ToolsCheck() {
    }

    public ToolsCheck(Operator operator, Integer block, Date checkTime, Integer status, String remark) {
        this.operator = operator;
        this.block = block;
        this.checkTime = checkTime;
        this.status = status;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ToolsCheck{" +
                "id=" + id +
                ", operator=" + operator +
                ", block=" + block +
                ", checkTime=" + checkTime +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}
