/*
 * This file is generated by jOOQ.
 */
package com.scomarlf.generated.tables.interfaces;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * VIEW
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IVEnroll extends Serializable {

    /**
     * Setter for <code>indexBot.v_enroll.id</code>. 主键
     */
    public void setId(String value);

    /**
     * Getter for <code>indexBot.v_enroll.id</code>. 主键
     */
    public String getId();

    /**
     * Setter for <code>indexBot.v_enroll.recordId</code>. 频道/群组/bot  ID
     */
    public void setRecordid(Long value);

    /**
     * Getter for <code>indexBot.v_enroll.recordId</code>. 频道/群组/bot  ID
     */
    public Long getRecordid();

    /**
     * Setter for <code>indexBot.v_enroll.username</code>. 频道\群组\机器人 username
     */
    public void setUsername(String value);

    /**
     * Getter for <code>indexBot.v_enroll.username</code>. 频道\群组\机器人 username
     */
    public String getUsername();

    /**
     * Setter for <code>indexBot.v_enroll.inviteLink</code>. 私有链接
     */
    public void setInvitelink(String value);

    /**
     * Getter for <code>indexBot.v_enroll.inviteLink</code>. 私有链接
     */
    public String getInvitelink();

    /**
     * Setter for <code>indexBot.v_enroll.title</code>. 名称
     */
    public void setTitle(String value);

    /**
     * Getter for <code>indexBot.v_enroll.title</code>. 名称
     */
    public String getTitle();

    /**
     * Setter for <code>indexBot.v_enroll.remark</code>. 简介
     */
    public void setRemark(String value);

    /**
     * Getter for <code>indexBot.v_enroll.remark</code>. 简介
     */
    public String getRemark();

    /**
     * Setter for <code>indexBot.v_enroll.memberNumber</code>. 成员数量
     */
    public void setMembernumber(Long value);

    /**
     * Getter for <code>indexBot.v_enroll.memberNumber</code>. 成员数量
     */
    public Long getMembernumber();

    /**
     * Setter for <code>indexBot.v_enroll.tag</code>. 标签
     */
    public void setTag(String value);

    /**
     * Getter for <code>indexBot.v_enroll.tag</code>. 标签
     */
    public String getTag();

    /**
     * Setter for <code>indexBot.v_enroll.type</code>. 收录类型 字典-recordType   频道\群组\机器人
     */
    public void setType(String value);

    /**
     * Getter for <code>indexBot.v_enroll.type</code>. 收录类型 字典-recordType   频道\群组\机器人
     */
    public String getType();

    /**
     * Setter for <code>indexBot.v_enroll.classification</code>. 分类
     */
    public void setClassification(String value);

    /**
     * Getter for <code>indexBot.v_enroll.classification</code>. 分类
     */
    public String getClassification();

    /**
     * Setter for <code>indexBot.v_enroll.placardId</code>. 公告ID
     */
    public void setPlacardid(Long value);

    /**
     * Getter for <code>indexBot.v_enroll.placardId</code>. 公告ID
     */
    public Long getPlacardid();

    /**
     * Setter for <code>indexBot.v_enroll.recordStatus</code>. 是否展示
     */
    public void setRecordstatus(Boolean value);

    /**
     * Getter for <code>indexBot.v_enroll.recordStatus</code>. 是否展示
     */
    public Boolean getRecordstatus();

    /**
     * Setter for <code>indexBot.v_enroll.status</code>. 是否提交
     */
    public void setStatus(Boolean value);

    /**
     * Getter for <code>indexBot.v_enroll.status</code>. 是否提交
     */
    public Boolean getStatus();

    /**
     * Setter for <code>indexBot.v_enroll.createUser</code>. 用户ID
     */
    public void setCreateuser(Long value);

    /**
     * Getter for <code>indexBot.v_enroll.createUser</code>. 用户ID
     */
    public Long getCreateuser();

    /**
     * Setter for <code>indexBot.v_enroll.createTime</code>. 申请时间
     */
    public void setCreatetime(LocalDateTime value);

    /**
     * Getter for <code>indexBot.v_enroll.createTime</code>. 申请时间
     */
    public LocalDateTime getCreatetime();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IVEnroll
     */
    public void from(IVEnroll from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface IVEnroll
     */
    public <E extends IVEnroll> E into(E into);
}