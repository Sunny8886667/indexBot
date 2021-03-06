/*
 * This file is generated by jOOQ.
 */
package com.scomarlf.generated.tables;


import com.scomarlf.generated.Indexbot;
import com.scomarlf.generated.Keys;
import com.scomarlf.generated.tables.records.ApproveRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Approve extends TableImpl<ApproveRecord> {

    private static final long serialVersionUID = -47993775;

    /**
     * The reference instance of <code>indexBot.approve</code>
     */
    public static final Approve APPROVE = new Approve();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ApproveRecord> getRecordType() {
        return ApproveRecord.class;
    }

    /**
     * The column <code>indexBot.approve.id</code>. 主键
     */
    public final TableField<ApproveRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "主键");

    /**
     * The column <code>indexBot.approve.enrollId</code>. 申请ID
     */
    public final TableField<ApproveRecord, String> ENROLLID = createField(DSL.name("enrollId"), org.jooq.impl.SQLDataType.VARCHAR(50), this, "申请ID");

    /**
     * The column <code>indexBot.approve.status</code>. 审核状态 通过不通过
     */
    public final TableField<ApproveRecord, Boolean> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.BIT, this, "审核状态 通过不通过");

    /**
     * The column <code>indexBot.approve.createUser</code>. 用户ID
     */
    public final TableField<ApproveRecord, Long> CREATEUSER = createField(DSL.name("createUser"), org.jooq.impl.SQLDataType.BIGINT, this, "用户ID");

    /**
     * The column <code>indexBot.approve.createTime</code>. 审核时间
     */
    public final TableField<ApproveRecord, LocalDateTime> CREATETIME = createField(DSL.name("createTime"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "审核时间");

    /**
     * Create a <code>indexBot.approve</code> table reference
     */
    public Approve() {
        this(DSL.name("approve"), null);
    }

    /**
     * Create an aliased <code>indexBot.approve</code> table reference
     */
    public Approve(String alias) {
        this(DSL.name(alias), APPROVE);
    }

    /**
     * Create an aliased <code>indexBot.approve</code> table reference
     */
    public Approve(Name alias) {
        this(alias, APPROVE);
    }

    private Approve(Name alias, Table<ApproveRecord> aliased) {
        this(alias, aliased, null);
    }

    private Approve(Name alias, Table<ApproveRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Approve(Table<O> child, ForeignKey<O, ApproveRecord> key) {
        super(child, key, APPROVE);
    }

    @Override
    public Schema getSchema() {
        return Indexbot.INDEXBOT;
    }

    @Override
    public UniqueKey<ApproveRecord> getPrimaryKey() {
        return Keys.KEY_APPROVE_PRIMARY;
    }

    @Override
    public List<UniqueKey<ApproveRecord>> getKeys() {
        return Arrays.<UniqueKey<ApproveRecord>>asList(Keys.KEY_APPROVE_PRIMARY);
    }

    @Override
    public Approve as(String alias) {
        return new Approve(DSL.name(alias), this);
    }

    @Override
    public Approve as(Name alias) {
        return new Approve(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Approve rename(String name) {
        return new Approve(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Approve rename(Name name) {
        return new Approve(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, Boolean, Long, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
