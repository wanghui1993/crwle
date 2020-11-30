
package com.wh.yaofangwang.common;

import com.wh.yaofangwang.common.page.PageUtil;
import com.wh.yaofangwang.common.page.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础DAO，用于封装dao操作
 *
 * @param <T> 泛型参数
 * @param <S> 泛型参数
 */
@Component
public class BaseDao<T, S> {
    /**
     * 注入jdbctemp
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * 获取  NamedParameterJdbcTemplate
     *
     * @return NamedParameterJdbcTemplate
     */
    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * 新增
     *
     * @param sql sql
     * @param s   新增对象
     * @return 新增记录值
     */

    protected int insert(String sql, S s) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(s.toString());
        return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(s));
    }

    /**
     * 新增并返回主键
     *
     * @param sql     sql
     * @param s       新增对象
     * @param pkField 主键字段
     * @return 新增记录值
     */
    protected int insertForId(String sql, S s, String pkField) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(s.toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rc = getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(s), keyHolder, new String[]{pkField});
        if (rc > 0) {
            return keyHolder.getKey().intValue();
        } else {
            return 0;
        }
    }

    /**
     * 新增并返回主键-id为String类型
     *
     * @param sql     sql
     * @param s       新增对象
     * @param pkField 主键字段
     * @return 新增记录值
     */
    protected String insertForStringId(String sql, S s, String pkField) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(s.toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rc = getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(s), keyHolder, new String[]{pkField});
        if (rc > 0) {
            return keyHolder.getKeys().get(pkField).toString();
        } else {
            return "";
        }
    }

    /**
     * 修改，参数为model类
     *
     * @param sql sql
     * @param s   修改对象
     * @return 修改记录值
     */
    protected int update(String sql, S s) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(s.toString());
        return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(s));
    }


    /**
     * 按照参数修改
     *
     * @param sql     sql
     * @param objects 多参数
     * @return 修改记录值
     */
    protected int update(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        return jdbcTemplate.update(sql, objects);
    }

    /**
     * 删除
     *
     * @param sql     sql
     * @param objects 多参数
     * @return 删除记录值
     */
    protected int delete(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        return jdbcTemplate.update(sql, objects);
    }

    /**
     * 根据参数获取model
     *
     * @param sql     sql
     * @param objects 多参数
     * @return 返回对象
     */
    protected T get(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        List<T> list = jdbcTemplate.query(sql, objects, BeanPropertyRowMapper.newInstance(getClazz()));
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据参数获取model
     * @param clazz
     * @param sql     sql
     * @param objects 多参数
     * @return 返回对象
     */
    protected T get(String sql,Class<T> clazz, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        List<T> list = jdbcTemplate.query(sql, objects, BeanPropertyRowMapper.newInstance(clazz));
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 取得当前泛型的实际class名
     *
     * @return 类名
     */
    private Class<T> getClazz() {
        return ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    /**
     * 直接根据sql获取列表
     *
     * @param sql sql
     * @return 列表
     */
    protected List<T> list(String sql) {
        LogUtil.info("sql语句：" + sql);
        List<T> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(getClazz()));
        return list;
    }

    /**
     * 根据sql和多参数获取列表
     *
     * @param sql     列表
     * @param objects 多参数
     * @return 列表
     */
    protected List<T> list(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        List<T> list = jdbcTemplate.query(sql, objects, BeanPropertyRowMapper.newInstance(getClazz()));
        return list;
    }


    protected List<Map<String, Object>> listMap(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, objects);
        return list;
    }

    /**
     * 根据查询条件获取列表
     *
     * @param sql sql
     * @param s   对象
     * @return 列表
     */
    protected List<T> list(String sql, S s) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(s.toString());
        List<T> list = getNamedParameterJdbcTemplate().query(sql, new BeanPropertySqlParameterSource(s), BeanPropertyRowMapper.newInstance(getClazz()));
        return list;
    }

    /**
     * 查询总数，无参数
     *
     * @param sql sql
     * @return 总数
     */
    protected int count(String sql) {
        LogUtil.info("sql语句：" + sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 查询总数，带参数
     *
     * @param sql     sql
     * @param objects 多参数
     * @return 总数
     */
    protected int count(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        return jdbcTemplate.queryForObject(sql, objects, Integer.class);
    }

    /**
     * 查询总数，带查询对象
     *
     * @param sql sql
     * @param s   对象
     * @return 总数
     */
    protected int count(String sql, S s) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(s.toString());
        return getNamedParameterJdbcTemplate().queryForObject(sql, new BeanPropertySqlParameterSource(s), Integer.class);
    }

    /**
     * 下拉框列表
     *
     * @param sql sql
     * @return 列表
     */
    /*protected List<ComboboxVO> listCombobox(String sql) {
        LogUtil.info("sql语句：" + sql);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ComboboxVO.class));
    }*/

    /**
     * 下拉框列表
     *
     * @param sql     sql
     * @param objects 多参数
     * @return 列表
     */
    /*protected List<ComboboxVO> listCombobox(String sql, Object... objects) {
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(objects);
        return jdbcTemplate.query(sql, objects, new BeanPropertyRowMapper<>(ComboboxVO.class));
    }*/

    /**
     * 自定义返回对象集合
     * @param sql sql语句
     * @param paramsList 参数集合
     * @param clazz 自定义返回对象class
     * @param <O>   自定义对象
     * @return
     */
    protected <O> List<O> query(String sql,List paramsList,Class<O> clazz){
        LogUtil.info("sql语句：" + sql);
        LogUtil.info(paramsList.toArray());
        List<O> list = this.jdbcTemplate.query(sql,paramsList.toArray(),new BeanPropertyRowMapper<O>(clazz));
        return list;
    }

    /**
     * 批量更新，按名称映射
     * @param sql
     * @param list
     * @param <O>
     * @return
     */
    @Deprecated
    protected <O> int batchUpdate(String sql,List<O> list){
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());
        int[] updatedCountArray = getNamedParameterJdbcTemplate().batchUpdate(sql, batch);
        return updatedCountArray.length;
    }

    /**
     * 返回JSONArray类型结果集，用于自定义列返回结果集
     * @param sql
     * @param params
     * @return
     */
    /*protected JSONArray query(String sql,List<String> params){
        LogUtil.info("sql:"+sql);
        LogUtil.info("参数："+params);
        JSONArray jsonArray =  this.jdbcTemplate.query(sql, params.toArray(), new ResultSetExtractor<JSONArray>() {
            @Override
            public JSONArray extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ResultSetMetaData rsd = resultSet.getMetaData();
                int len = rsd.getColumnCount();
                JSONArray ja = new JSONArray();
                String columnName = "";
                JSONObject jo = null;
                while (resultSet.next()) {
                    jo = new JSONObject();
                    for (int i = 0; i < len; i++) {
                        //columnName = rsd.getColumnLabel(i + 1);
                        //String columnType = rsd.getColumnTypeName(i+1);
                        //System.out.println("================"+columnType);
                        columnName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,rsd.getColumnLabel(i + 1));
                        jo.put(columnName, resultSet.getObject(i + 1));
                    }
                    ja.add(jo);
                }
                return ja;
            }
        });
        return jsonArray;
    }*/
    /**
     * 获取表名
     * @param o 自定义类型(与数据库表名对应)，不局限于baseDao中的T,S
     * @return
     */
    /*protected <O> String getTableName(O o){
        return new ModelToSqlUtil(o).getTableName();
    }*/

    /**
     * 分页公共方法
     * @param sqlColumn sql列
     * @param sqlFrom   from子句
     * @param params     参数集合
     * @param pageIndex
     * @param pageSize
     * @param <O>
     * @return
     */
    public <O> PageVO<O> pageQuery(StringBuilder sqlColumn, StringBuilder sqlFrom, List<Object> params, Integer pageIndex, Integer pageSize, Class<O> clazz){
        String sqlCount = "count(*) ";
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("select ");
        String pageSql = PageUtil.createMysqlPageSql(sqlSelect.append(sqlColumn).append(sqlFrom).toString(),pageIndex,pageSize);
        LogUtil.info("sql语句：" + pageSql);
        LogUtil.info(params.toArray());
        List<O> list = this.jdbcTemplate.query(pageSql,params.toArray(),new BeanPropertyRowMapper<O>(clazz));
        PageVO<O> ret = new PageVO<>();
        ret.setRecords(list);
        ret.setCurrentPageNum(pageIndex);
        ret.setPageSize(pageSize);
        sqlSelect.setLength(7);
        ret.setCount(this.count(sqlSelect.append(sqlCount).append(sqlFrom).toString(),params.toArray()));
        return ret;
    }

    /**
     * 分页查询，返回JSONArray
     * @param sqlColumn
     * @param sqlFrom
     * @param params
     * @param pageIndex
     * @param pageSize
     * @return
     */
    /*public PageVO<JSONObject> pageQueryJson(StringBuilder sqlColumn, StringBuilder sqlFrom, List<String> params, Integer pageIndex, Integer pageSize){
        String sqlCount = "count(*) ";
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("select ");
        String pageSql = PageUtil.createPageSql(sqlSelect.append(sqlColumn).append(sqlFrom).toString(),pageIndex,pageSize);
        sqlSelect.setLength(7);
        int count = this.count(sqlSelect.append(sqlCount).append(sqlFrom).toString(), params.toArray());
        PageVO<JSONObject> ret = new PageVO<>();
        List<JSONObject> list = new ArrayList<>();
        if(count>0){
            JSONArray jsonArray = this.query(pageSql, params);
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.getJSONObject(i));
            }
        }
        ret.setRecords(list);
        ret.setCurrentPageNum(pageIndex);
        ret.setPageSize(pageSize);
        ret.setCount(count);
        return ret;
    }*/
    /**
     * 分页公共方法，返回map类型
     * @param sqlColumn
     * @param sqlFrom
     * @param params
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public  PageVO<Map<String,Object>> pageQuery(StringBuilder sqlColumn, StringBuilder sqlFrom, List<Object> params, Integer pageIndex, Integer pageSize){
        String sqlCount = "count(*) ";
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("select ");
        String pageSql = PageUtil.createMysqlPageSql(sqlSelect.append(sqlColumn).append(sqlFrom).toString(),pageIndex,pageSize);
        LogUtil.info("sql语句：" + pageSql);
        LogUtil.info(params.toArray());
        PageVO<Map<String,Object>> ret = new PageVO<>();
        sqlSelect.setLength(7);
        // order by 可能有别名，所以在count(*)时order by去掉
        if(sqlFrom.toString().toLowerCase().contains("order")){
            sqlFrom = sqlFrom.delete(sqlFrom.indexOf("order"),sqlFrom.length());
        }
        String countSql = sqlSelect.append(sqlCount).append(sqlFrom).toString();
        // 含有group by 的查询都转成小写
        if(countSql.toLowerCase().contains(("group".toLowerCase()))){
            countSql ="select count(*) from ("+countSql+")";
        }
        int count = this.count(countSql,params.toArray());
        if(count>0){
            List<Map<String,Object>> list = this.jdbcTemplate.query(pageSql,params.toArray(),new ColumnMapRowMapper());
            ret.setRecords(list);
        }else{
            ret.setRecords(null);
        }
        ret.setCurrentPageNum(pageIndex);
        ret.setPageSize(pageSize);
        ret.setCount(count);
        return ret;
    }

    /**
     * ---10g返回
     * Oracle Database 10g Enterprise Edition Release 10.2.0.4.0 - 64bit Production
     * With the Partitioning, OLAP, Data Mining and Real Application Testing options
     * 10
     * 2
     * ---18c返回
     * Oracle Database 18c Enterprise Edition Release 18.0.0.0.0 - Production
     * 18
     * 0
     * Oracle Database 18c Enterprise Edition Release 18.0.0.0.0 - Production
     * @author zhaoyh 2020-04-29
     * 获取数据库版本，针对不同的数据库有差异的语法进行特殊处理
     * @return
     */
    public String getDatabaseVersion(){
        Connection connection = null;
        try {
            connection = this.jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData =connection.getMetaData();
            /*System.out.println("-----==-=-=-"+metaData.getDatabaseProductVersion());
            System.out.println(metaData.getDatabaseMajorVersion());
            System.out.println(metaData.getDatabaseMinorVersion());
            System.out.println(metaData.getDatabaseProductVersion());*/
            return metaData.getDatabaseProductVersion();
        } catch (SQLException e) {
            LogUtil.error("查询数据库版本出错："+e.getMessage(),e);
            return "10";//默认返回10，保证生产
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LogUtil.error("关闭连接失败");
            }
        }
    }
}
