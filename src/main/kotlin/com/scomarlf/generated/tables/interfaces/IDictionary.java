/*
 * This file is generated by jOOQ.
 */
package com.scomarlf.generated.tables.interfaces;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IDictionary extends Serializable {

    /**
     * Setter for <code>indexBot.dictionary.id</code>. 主键
     */
    public void setId(String value);

    /**
     * Getter for <code>indexBot.dictionary.id</code>. 主键
     */
    public String getId();

    /**
     * Setter for <code>indexBot.dictionary.parentId</code>. 父级主键
     */
    public void setParentid(String value);

    /**
     * Getter for <code>indexBot.dictionary.parentId</code>. 父级主键
     */
    public String getParentid();

    /**
     * Setter for <code>indexBot.dictionary.label</code>. 名
     */
    public void setLabel(String value);

    /**
     * Getter for <code>indexBot.dictionary.label</code>. 名
     */
    public String getLabel();

    /**
     * Setter for <code>indexBot.dictionary.sort</code>. 排序
     */
    public void setSort(Integer value);

    /**
     * Getter for <code>indexBot.dictionary.sort</code>. 排序
     */
    public Integer getSort();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IDictionary
     */
    public void from(IDictionary from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface IDictionary
     */
    public <E extends IDictionary> E into(E into);
}
