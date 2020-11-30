
package com.wh.yaofangwang.common.page;

/**
 * 分页工具类，用于生成分页语句
 *
 */
public class PageUtil {

    /**
     * 分页，判断mysql和oracle
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @return
     */
    /*public static String createPageSql(String sql, int pageIndex, int pageSize){
        String driverClassName = SpringContextUtil.getProperty("spring.datasource.driverClassName");
        if(StringUtil.isNotNullOrEmpty(driverClassName) && driverClassName.contains("mysql")){
            return createMysqlPageSql(sql,pageIndex,pageSize);
        }
        return createOraclePageSql(sql,pageIndex,pageSize);
    }*/

    /**
     * 生成mysql分页查询语句
     *
     * @param sql       sql
     * @param pageIndex 当前页
     * @param pageSize  每页数
     * @return 分页sql
     */
    public static String createMysqlPageSql(String sql, int pageIndex, int pageSize) {
        return sql += " limit " + (pageIndex - 1) * pageSize + "," + pageSize;
    }

    /**
     * 生成mysql分页查询语句
     *
     * @param sql       sql
     * @param pageIndex 当前页
     * @return 分页sql
     */
    public static String createMysqlPageSql(String sql, int pageIndex) {
        return sql += " limit " + (pageIndex - 1) * 10 + "," + 10;
    }

    /**
     * 创建Oracle分页查询语句
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static String createOraclePageSql(String sql, int pageIndex, int pageSize) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("SELECT * FROM ( ");
        sqlBuilder.append("SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TMP_PAGE) WHERE ");
        sqlBuilder.append("ROW_ID > ");
        sqlBuilder.append((pageIndex-1)*pageSize);
        sqlBuilder.append(" AND ");
        sqlBuilder.append("ROW_ID <= ");
        sqlBuilder.append((pageIndex-1)*pageSize+pageSize);
        return sqlBuilder.toString();
    }

    /**
     * 创建SqlServer分页查询语句
     * @param sql
     * @param pageOrderColumn
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static String createSqlServerPageSql(String sql, String pageOrderColumn, int pageIndex, int pageSize) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 160);
        sqlBuilder.append("SELECT top  ");
        sqlBuilder.append(pageSize);
        sqlBuilder.append(" * FROM (");
        sqlBuilder.append("SELECT row_number() over(order by ");
        sqlBuilder.append(pageOrderColumn);
        sqlBuilder.append(") as row_number,* FROM (");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) t1) t2 WHERE ");
        sqlBuilder.append("row_number > ");
        sqlBuilder.append((pageIndex-1)*pageSize);
        return sqlBuilder.toString();
    }

}
