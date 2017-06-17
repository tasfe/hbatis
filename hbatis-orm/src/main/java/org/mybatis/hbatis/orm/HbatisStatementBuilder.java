package org.mybatis.hbatis.orm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.mybatis.hbatis.orm.mapper.HbatisMapper;
import org.mybatis.hbatis.orm.sql.KeyGeneratorBuilder;
import org.mybatis.hbatis.orm.sql.ResultMapsBuilder;
import org.mybatis.hbatis.orm.sql.SqlBuilder;
import org.mybatis.hbatis.orm.sql.TableMappingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 声明创建（实现继承)
 * 
 * @author zz
 * @date 2014年9月8日
 * @email zhen.zhang@vipshop.com
 */
public class HbatisStatementBuilder {

	private static final Logger logger = LoggerFactory.getLogger(HbatisStatementBuilder.class);

	private SqlSourceBuilder sqlSourceBuilder;

	private static final Set<String> baseMethodSet = new HashSet<String>();

	/**
	 * 数据库类型
	 */
	private String dialect = "mysql";

	private Collection<Class<?>> mappers;

	private Configuration configuration;
	/**
	 * 模板
	 */
	private Configuration vpmConfiguration;

	private static Class<?> baseMapperClass = HbatisMapper.class;

	public HbatisStatementBuilder(Configuration vpmConfiguration, Configuration configuration) {
		this.vpmConfiguration = vpmConfiguration;
		this.configuration = configuration;
		MapperRegistry r = configuration.getMapperRegistry();

		this.mappers = r.getMappers();

	}

	private KeyGeneratorBuilder keyGeneratorBuilder;
	static {
		Method[] methods = baseMapperClass.getMethods();
		for (Method m : methods) {
			baseMethodSet.add(m.getName());
		}
		logger.info("BaseMapper({}) statements:{}", baseMapperClass, baseMethodSet);

	}

	public void initDao() throws Exception {

		if (this.mappers == null || this.mappers.isEmpty())
			return;
		sqlSourceBuilder = new SqlSourceBuilder(configuration);
		// key generator
		Class<KeyGeneratorBuilder> keyBuilderClass = HBatisConfiguration.getKeyGeneratorBuilder(this.dialect);
		this.keyGeneratorBuilder = keyBuilderClass.newInstance();

		// scan package mapper
		for (Class<?> mapperClass : mappers) {

			if (baseMapperClass.isAssignableFrom(mapperClass) && !baseMapperClass.equals(mapperClass)) {
				logger.info("Preprocessing mapper interface:{}", mapperClass);
				for (String st : baseMethodSet) {
					addStatement(mapperClass, st);

				}
			}
		}
	}

	private Class<?> getEntityClassByInterface(Class<?> mapperClass) {
		ParameterizedType pt = (ParameterizedType) mapperClass.getGenericInterfaces()[0];

		Class<?> entityClass = (Class<?>) (pt.getActualTypeArguments()[0]);
		return entityClass;
	}

	private synchronized String addStatement(Class<?> mapperClass, String methodName) {
		// 定义子mapper的statement1
		String statementId = mapperClass.getName() + "." + methodName;
		if (configuration.hasStatement(statementId)) {
			return statementId;
		}
		String baseStatementId = baseMapperClass.getName() + "." + methodName;
		logger.info("statement id:{},baseStatementId:{}", statementId, baseStatementId);
		System.out.println(vpmConfiguration.hasStatement(baseStatementId, false));
		if (configuration.hasStatement(baseStatementId) || vpmConfiguration.hasStatement(baseStatementId)) {
			// 复制声明
			MappedStatement baseStatement = vpmConfiguration.getMappedStatement(baseStatementId);

			if (!configuration.hasStatement(statementId)) {
				// 创建新的statement,并封装相应的resultType
				MappedStatement mappedSt = buildNewStatement(mapperClass, baseStatement, statementId);
				// 添加进configuration即可
				configuration.addMappedStatement(mappedSt);
			}
		} else {
			// 通过sql builder创建
			Class<?> entityClass = this.getEntityClassByInterface(mapperClass);
			SqlBuilder sqlBuilder;
			try {
				Class<?> sqlSourceClass = HBatisConfiguration.getSqlSourceClass(this.dialect, methodName);
				Constructor<?> constructor = sqlSourceClass.getDeclaredConstructor(SqlSourceBuilder.class, Class.class);
				sqlBuilder = (SqlBuilder) constructor.newInstance(this.sqlSourceBuilder, entityClass);

			} catch (Exception e) {
				throw new RuntimeException("build statement error[id:" + statementId + "]", e);
			}

			MappedStatement mappedSt = this.buildNewStatement(sqlBuilder, statementId, entityClass);

			configuration.addMappedStatement(mappedSt);
		}
		return statementId;
	}

	private MappedStatement buildNewStatement(Class<?> mapperClass, MappedStatement baseStatement, String statementId) {
		Class<?> entityClass = this.getEntityClassByInterface(mapperClass);

		MappedStatement.Builder statementBuilder = new MappedStatement.Builder(baseStatement.getConfiguration(), statementId, baseStatement.getSqlSource(), baseStatement.getSqlCommandType());
		statementBuilder.resultMaps(baseStatement.getResultMaps());
		if (statementId.endsWith("selectByStatement")) {// 特殊处理

			List<ResultMapping> resultMappings = ResultMapsBuilder.buildResultMappings(configuration, TableMappingUtil.getEntityMapping(entityClass));

			ResultMap.Builder builder = new ResultMap.Builder(configuration, statementBuilder.id() + "_ResultMap", entityClass, resultMappings);
			statementBuilder.resultMaps(Arrays.asList(builder.build()));
		}
		return statementBuilder.build();

	}

	private MappedStatement buildNewStatement(SqlBuilder sqlBuilder, String statementId, Class<?> entityClass) {

		MappedStatement.Builder statementBuilder = new MappedStatement.Builder(this.configuration, statementId, sqlBuilder, sqlBuilder.getSqlCommandType());

		if (sqlBuilder.getResultType() != null) {
			List<ResultMap> resultMaps = new ArrayList<ResultMap>();
			ResultMap.Builder resultMapBuilder = new ResultMap.Builder(configuration, statementBuilder.id() + "_Inline", sqlBuilder.getResultType(), sqlBuilder.getResultMappingList());
			resultMaps.add(resultMapBuilder.build());
			statementBuilder.resultMaps(resultMaps);
		}
		
		if (SqlCommandType.INSERT.equals(sqlBuilder.getSqlCommandType()) && sqlBuilder.getResultType()!=null && sqlBuilder.getResultType().isAssignableFrom(entityClass)) {
			keyGeneratorBuilder.build(statementBuilder, entityClass);
		}
		return statementBuilder.build();

	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

}
