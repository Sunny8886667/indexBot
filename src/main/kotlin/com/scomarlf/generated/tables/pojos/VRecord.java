/*
 * This file is generated by jOOQ.
 */
package com.scomarlf.generated.tables.pojos;


import com.scomarlf.generated.tables.interfaces.IVRecord;

import java.time.LocalDateTime;


/**
 * VIEW
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VRecord implements IVRecord {

    private static final long serialVersionUID = -274966206;

    private Long          id;
    private String        username;
    private String        invitelink;
    private String        title;
    private String        remark;
    private Long          membernumber;
    private String        tag;
    private String        type;
    private String        typename;
    private String        classification;
    private String        classificationname;
    private Long          placardid;
    private Boolean       status;
    private LocalDateTime createtime;
    private Long          createuser;
    private LocalDateTime updatetime;
    private LocalDateTime updateuser;

    public VRecord() {}

    public VRecord(IVRecord value) {
        this.id = value.getId();
        this.username = value.getUsername();
        this.invitelink = value.getInvitelink();
        this.title = value.getTitle();
        this.remark = value.getRemark();
        this.membernumber = value.getMembernumber();
        this.tag = value.getTag();
        this.type = value.getType();
        this.typename = value.getTypename();
        this.classification = value.getClassification();
        this.classificationname = value.getClassificationname();
        this.placardid = value.getPlacardid();
        this.status = value.getStatus();
        this.createtime = value.getCreatetime();
        this.createuser = value.getCreateuser();
        this.updatetime = value.getUpdatetime();
        this.updateuser = value.getUpdateuser();
    }

    public VRecord(
        Long          id,
        String        username,
        String        invitelink,
        String        title,
        String        remark,
        Long          membernumber,
        String        tag,
        String        type,
        String        typename,
        String        classification,
        String        classificationname,
        Long          placardid,
        Boolean       status,
        LocalDateTime createtime,
        Long          createuser,
        LocalDateTime updatetime,
        LocalDateTime updateuser
    ) {
        this.id = id;
        this.username = username;
        this.invitelink = invitelink;
        this.title = title;
        this.remark = remark;
        this.membernumber = membernumber;
        this.tag = tag;
        this.type = type;
        this.typename = typename;
        this.classification = classification;
        this.classificationname = classificationname;
        this.placardid = placardid;
        this.status = status;
        this.createtime = createtime;
        this.createuser = createuser;
        this.updatetime = updatetime;
        this.updateuser = updateuser;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getInvitelink() {
        return this.invitelink;
    }

    @Override
    public void setInvitelink(String invitelink) {
        this.invitelink = invitelink;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Long getMembernumber() {
        return this.membernumber;
    }

    @Override
    public void setMembernumber(Long membernumber) {
        this.membernumber = membernumber;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getTypename() {
        return this.typename;
    }

    @Override
    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public String getClassification() {
        return this.classification;
    }

    @Override
    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String getClassificationname() {
        return this.classificationname;
    }

    @Override
    public void setClassificationname(String classificationname) {
        this.classificationname = classificationname;
    }

    @Override
    public Long getPlacardid() {
        return this.placardid;
    }

    @Override
    public void setPlacardid(Long placardid) {
        this.placardid = placardid;
    }

    @Override
    public Boolean getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getCreatetime() {
        return this.createtime;
    }

    @Override
    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    @Override
    public Long getCreateuser() {
        return this.createuser;
    }

    @Override
    public void setCreateuser(Long createuser) {
        this.createuser = createuser;
    }

    @Override
    public LocalDateTime getUpdatetime() {
        return this.updatetime;
    }

    @Override
    public void setUpdatetime(LocalDateTime updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public LocalDateTime getUpdateuser() {
        return this.updateuser;
    }

    @Override
    public void setUpdateuser(LocalDateTime updateuser) {
        this.updateuser = updateuser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VRecord (");

        sb.append(id);
        sb.append(", ").append(username);
        sb.append(", ").append(invitelink);
        sb.append(", ").append(title);
        sb.append(", ").append(remark);
        sb.append(", ").append(membernumber);
        sb.append(", ").append(tag);
        sb.append(", ").append(type);
        sb.append(", ").append(typename);
        sb.append(", ").append(classification);
        sb.append(", ").append(classificationname);
        sb.append(", ").append(placardid);
        sb.append(", ").append(status);
        sb.append(", ").append(createtime);
        sb.append(", ").append(createuser);
        sb.append(", ").append(updatetime);
        sb.append(", ").append(updateuser);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IVRecord from) {
        setId(from.getId());
        setUsername(from.getUsername());
        setInvitelink(from.getInvitelink());
        setTitle(from.getTitle());
        setRemark(from.getRemark());
        setMembernumber(from.getMembernumber());
        setTag(from.getTag());
        setType(from.getType());
        setTypename(from.getTypename());
        setClassification(from.getClassification());
        setClassificationname(from.getClassificationname());
        setPlacardid(from.getPlacardid());
        setStatus(from.getStatus());
        setCreatetime(from.getCreatetime());
        setCreateuser(from.getCreateuser());
        setUpdatetime(from.getUpdatetime());
        setUpdateuser(from.getUpdateuser());
    }

    @Override
    public <E extends IVRecord> E into(E into) {
        into.from(this);
        return into;
    }
}