package cn.succy.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 产线实体类
 *
 * @author Succy
 */
@Table(name = "prod_line", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Entity
@Data
public class ProdLine implements Serializable {
    private static final long serialVersionUID = -1480251159827955775L;
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 产线代号，如lg,zb,lz
     */
    private String code;

    /**
     * 产线系统名称
     */
    private String name;

    /**
     * 产线描述
     */
    private String description;

    @ManyToMany
    @JoinTable(name = "prodline_contact",
            joinColumns = @JoinColumn(name = "prodline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "id")
    )
    private List<Contact> contactList;
}
