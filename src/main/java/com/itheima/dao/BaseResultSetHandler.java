package com.itheima.dao;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用的ResultSetHandler基类
 * 为所有实体类提供基本的结果集处理功能
 */
public abstract class BaseResultSetHandler<T> implements ResultSetHandler<T> {
    /**
     * 安全地处理可能为NULL的整数类型字段
     * @param rs 结果集
     * @param columnName 字段名
     * @return Integer对象，如果数据库值为NULL则返回null
     * @throws SQLException SQL异常
     */
    protected Integer getIntegerValue(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * 安全地处理可能为NULL的字符串类型字段
     * @param rs 结果集
     * @param columnName 字段名
     * @return String对象，如果数据库值为NULL则返回null
     * @throws SQLException SQL异常
     */
    protected String getStringValue(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * 安全地处理可能为NULL的日期类型字段
     * @param rs 结果集
     * @param columnName 字段名
     * @return java.util.Date对象，如果数据库值为NULL则返回null
     * @throws SQLException SQL异常
     */
    protected java.util.Date getDateValue(ResultSet rs, String columnName) throws SQLException {
        java.sql.Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp != null ? new java.util.Date(timestamp.getTime()) : null;
    }
}