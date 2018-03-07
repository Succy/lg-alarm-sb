package cn.succy.alarm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity
@Data
public class Contact implements Serializable{
    private static final long serialVersionUID = -3915701227614746080L;
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    /**
     * 科室
     */
    private String office;

    private String email;

    private String wxId;

}
