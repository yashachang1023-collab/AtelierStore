package com.atelier.atelierstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data//自动生成 Getter, Setter, toString, equals, hashCode
@AllArgsConstructor // 自动生成全参数构造函数
@NoArgsConstructor
@Entity
@Table(name = "illustrations")
@EqualsAndHashCode(callSuper = true) // 【重要】Lombok 处理继承类时需要
@ToString(callSuper = true)          // 【重要】打印日志时能看到父类字段
public class Illustration extends BaseItem{
    private String info;
    private String imageUrl;
    // JPA 自动处理这些时间字段
    @Column(updatable = false)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

  /*  protected Illustration() {
        super();
    }
    // 你之前手写的构造函数可以保留
    public Illustration(String id, String name, String info, String imageUrl) {
        super(id, name);
        this.info = info;
        this.imageUrl = imageUrl;
    }*/
    @Override
    public void displayInfo() {
        System.out.println("illustration's id:" + getId() + "name:" + getName());
    }
}
