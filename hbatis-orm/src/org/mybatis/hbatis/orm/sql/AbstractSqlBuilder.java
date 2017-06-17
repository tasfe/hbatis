package org.mybatis.hbatis.orm.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatis.hbatis.core.meta.FieldMeta;
import org.mybatis.hbatis.core.metaDescriber.EntityClassDescriber;
import org.mybatis.hbatis.core.metaDescriber.EntityFieldDescriber;
import org.mybatis.hbatis.orm.util.EntityClassDescriberHelper;
/**
 * 抽象SqlBuilder
 * @author zhen.zhang
 * @date 2014年10月23日
 */
public abstract class AbstractSqlBuilder implements SqlBuilder {
	protected SqlSourceBuilder sqlSourceBuilder;
	
	protected Class<?> entityClass;
	
	protected TableMapping<?> mapping;
	/**
	 * SQL模板
	 */
	protected String sqlTpl;
	public AbstractSqlBuilder(SqlSourceBuilder sqlSourceBuilder,Class<?> clazz){
		this.entityClass = clazz;
		this.sqlSourceBuilder = sqlSourceBuilder;
		mapping = TableMappingUtil.getEntityMapping(clazz);
		this.sqlTpl = this.buildSql(mapping);
	}
	@Override
	public BoundSql getBoundSql(Object parameter){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("_parameter", parameter);
		return sqlSourceBuilder.parse(sqlTpl, parameter.getClass(), map).getBoundSql(parameter);
		
	}
	public void setSqlSourceBuilder(SqlSourceBuilder sqlSourceBuilder) {
		this.sqlSourceBuilder = sqlSourceBuilder;
	}
	public abstract <E> String buildSql(TableMapping<E> mapping);
	
	@Override
	public abstract SqlCommandType getSqlCommandType();
	
	public abstract <E> List<ResultMapping> getResultMappingList(TableMapping<E> mapping);
	
	@Override
	public List<ResultMapping> getResultMappingList(){
		return this.getResultMappingList(mapping);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String buildPrimaryWhere(){
		StringBuilder sb = new StringBuilder();
		EntityClassDescriber entityDescriber = EntityClassDescriberHelper.getEntityClassDescriber(this.entityClass);
		List<EntityFieldDescriber> fieldDescribers = entityDescriber.getFieldDescribers();
		int keyIndex = 0;
		for(EntityFieldDescriber fd:fieldDescribers){
			if(fd.isPrimaryKey()){
				if(keyIndex>0){
					sb.append(" and ");
				}
				String prop = fd.getField().getName();
				FieldMeta fm = (FieldMeta) TableMappingUtil.getFieldMeta(this.mapping, prop);
				sb.append(fm.getColumnName()).append("=");
				sb.append("#{").append(prop).append(",jdbcType=").append(fm.getJdbcType()).append("}");
				keyIndex++;
			}
		}
		return sb.toString();
	}
	@Override
	public Class<?> getResultType() {
		return null;
	}
}
