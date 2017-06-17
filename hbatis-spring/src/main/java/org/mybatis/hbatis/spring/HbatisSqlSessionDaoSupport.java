package org.mybatis.hbatis.spring;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.hbatis.orm.HbatisStatementBuilder;
import org.mybatis.hbatis.orm.mapper.HbatisMapper;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * SqlSessionDaoSupport
 * @author zhen.zhang
 * @date 2014年12月7日
 */
public class HbatisSqlSessionDaoSupport extends SqlSessionDaoSupport {

	private static Logger logger = LoggerFactory.getLogger(HbatisSqlSessionDaoSupport.class);

	private static Configuration vpmConfiguration;

	private List<SqlSessionFactory> factorys;

	public static boolean VPM_STATEMENT_INITILIZED = false;

	public HbatisSqlSessionDaoSupport() {
		factorys = Arrays.asList();
	}

	public HbatisSqlSessionDaoSupport(SqlSessionFactory... factory) {
		factorys = Arrays.asList(factory);
	}

	private synchronized void initVpmStatements() throws Exception{
		vpmConfiguration = this.getSqlSession().getConfiguration();
		if(!vpmConfiguration.hasMapper(HbatisMapper.class)){
			vpmConfiguration.addMapper(HbatisMapper.class);
		}
		HbatisStatementBuilder builder = new HbatisStatementBuilder(vpmConfiguration, vpmConfiguration);
		builder.initDao();
		
	}

	@Override
	protected void initDao() throws Exception {
		if (!VPM_STATEMENT_INITILIZED) {
			logger.info("Init sqlSessionFactory");

			initVpmStatements();

			VPM_STATEMENT_INITILIZED = true;
		}
		for (SqlSessionFactory f : factorys) {
			logger.info("Preprocess sqlSessionFactory {}", f.toString());
			Configuration cfg = f.getConfiguration();
			HbatisStatementBuilder builder = new HbatisStatementBuilder(vpmConfiguration, cfg);
			builder.initDao();
		}

	}

}
