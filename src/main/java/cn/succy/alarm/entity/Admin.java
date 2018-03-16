package cn.succy.alarm.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Table
@Entity
@Data
public class Admin implements Serializable {
    private static final long serialVersionUID = -7833084603030978789L;
    @Id
    private String id;

    private String name;

    private String pwd;

    private String desc;

    @CreationTimestamp
    private Timestamp createTime;

    @UpdateTimestamp
    private Timestamp updateTime;

}
