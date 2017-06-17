package org.mybatis.hbatis.orm.sql.builder;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatis.hbatis.core.meta.FieldMeta;
import org.mybatis.hbatis.orm.sql.AbstractSqlBuilder;
import org.mybatis.hbatis.orm.sql.ResultMapsBuilder;
import org.mybatis.hbatis.orm.sql.TableMapping;

public class SelectByPKSqlBuilder extends AbstractSqlBuilder {

	public SelectByPKSqlBuilder(SqlSourceBuilder sqlSourceBuilder, Class<?> clazz) {
		super(sqlSourceBuilder, clazz);
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	public <E> List<ResultMapping> getResultMappingList(TableMapping<E> mapping) {
		return ResultMapsBuilder.buildResultMappings(this.sqlSourceBuilder.getConfiguration(),mapping);
	}

	@Override
	public <E> String buildSql(TableMapping<E> mapping) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		
		List<FieldMeta<E,?>> fields = mapping.getFieldMetas();
		for(FieldMeta<E,?> fm:fields){
			sb.append(fm.getColumnName()).append(",");
		}
		sb = sb.deleteCharAt(sb.length()-1);
		sb.append(" from ").append(mapping.getEntityMeta().getTableName());
		sb.append(" where ").append(this.buildPrimaryWhere());
		return sb.toString();
	}

	@Override
	public Class<?> getResultType() {
		return this.entityClass;
	}

}
