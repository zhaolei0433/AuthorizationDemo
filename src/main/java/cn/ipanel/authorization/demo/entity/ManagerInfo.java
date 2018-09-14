package cn.ipanel.authorization.demo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class ManagerInfo implements Serializable {
    private static final long serialVersionUID = 1731260916334584951L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column
    private String userName ;
    @Column
    private String password;
    @Column
    private Long loginTime;

    public ManagerInfo() {
    }


}
