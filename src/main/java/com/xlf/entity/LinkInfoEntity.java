package com.xlf.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * link info entity
 */
@Data
@Entity
@Table(name = "link_info")
public class LinkInfoEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "short_link")
    private String shortLink;
    @Column(name = "origin_link")
    private String originLink;
    /**
     * status: 0-disable,1-enable
     */
    @Column(name = "status")
    private Integer status;
    @Column(name = "expire_time")
    private Date expireTime;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
}
