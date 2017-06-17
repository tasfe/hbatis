package org.mybatis.hbatis.orm.sql.builder;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatis.hbatis.core.meta.FieldMeta;
import org.mybatis.hbatis.orm.sql.AbstractSqlBuilder;
import org.mybatis.hbatis.orm.sql.ResultMapsBuilder;
import org.mybatis.hbatis.orm.sql.TableMapping;

/**
 * 新增
 * 
 * @author zz
 * @date 2014年10月3日
 * @email zhen.zhang@vipshop.com
 */
public class InsertSqlBuilder extends AbstractSqlBuilder {

	public InsertSqlBuilder(SqlSourceBuilder sqlSourceBuilder, Class<?> clazz) {
		super(sqlSourceBuilder, clazz);
	}

	@Override
	public <E> String buildSql(TableMapping<E> tableMapping) {
		StringBuilder sb = new StringBuilder();

		String tableName = tableMapping.getEntityMeta().getTableName();
		sb.append("insert into ").append(tableName).append(" (");

		List<FieldMeta<E, ?>> fields = tableMapping.getFieldMetas();
		for (FieldMeta<E, ?> f : fields) {
			sb.append(f.getColumnName()).append(",");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(") values (");
		for (FieldMeta<E, ?> f : fields) {
			sb.append("#{").append(f.getPropertyName()).append(",jdbcType=").append(f.getJdbcType()).append("},");
		}
		sb = sb.deleteCharAt(sb.length() - 1).append(")");
		
		return sb.toString();
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public <E> List<ResultMapping> getResultMappingList(TableMapping<E> mapping) {
		return ResultMapsBuilder.buildResultMappings(this.sqlSourceBuilder.getConfiguration(),mapping);
	}
	@Override
	public Class<?> getResultType() {
		return this.entityClass;
	}
	
}
