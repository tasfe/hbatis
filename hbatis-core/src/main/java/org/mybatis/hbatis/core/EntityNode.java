package org.mybatis.hbatis.core;

import java.util.List;

import org.mybatis.hbatis.core.meta.EntityMeta;
/**
 * 实体路径
 * @author zz
 * @date 2014年9月8日
 * @email zhen.zhang@vipshop.com
 * @param <T>
 */
public interface EntityNode<T> {
	
	EntityMeta<T> getEntityMeta();
	
	String getAlias();
	
	String getSqlTable();
	
	List<FieldNode<T,?>> getFieldPaths();
}
