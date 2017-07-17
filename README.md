# hbatis
> 基于mybatis的crud解决方案


## hbatis是什么

> 建立在mybatis之上的 单表通用操作（新增、修改、删除、按字段查、按字段更新等）。减小重复性的、逻辑简单的xml配置，在不侵入mybatis的成本下，以几乎等同的代码量，
完成单表的CRUD操作，可通过代码构建Statement。大大提高代码的可维护性。

## hbatis的由来
>- mybatis generator 生成的代码及xml十分臃肿 及难以维护。
>- mybatis generator 生成的代码功能较弱 且 xml几乎不可复用
***
# 开始之旅
## POM.xml
```xml
<dependency>
	<groupId>org.mybatis.hbatis</groupId>
	<artifactId>hbatis-spring</artifactId>
	<version>2.0</version>
</dependency> 
```    
## 配置文件
 ### 不使用hbatis
 ```xml
 <bean id="sessionManager" class="org.mybatis.spring.support.SqlSessionDaoSupport" lazy-
      init="false" scope="singleton">
		  <property name="sqlSessionFactory" ref="sqlSessionFactory"></property>
 </bean>
```
 ### 使用hbatis
 ```xml
  <bean id="sessionManager" 
         class="org.mybatis.hbatis.spring.HbatisSqlSessionDaoSupport" lazy-
         init="false" scope="singleton">
	<property name="sqlSessionFactory" ref="sqlSessionFactory"></property>
  </bean>
```
 > 如此即开启了hbatis支持，是不是 So easy ?!
 
 ## DAO
 > 继承HbatisMapper
 ```Java
 /**
 * OrgInfoMapper
 * @author generator
 * @date 2017年07月11日
 */
public interface OrgInfoMapper extends HbatisMapper<OrgInfo, Integer> {
	
	
	//-- 按实体参数查询[START] 
	List<OrgInfo> findByQueryParam(@Param("queryParam")OrgInfo.QueryParam queryParam);
	
	long countByQueryParam(@Param("queryParam")OrgInfo.QueryParam queryParam);
	//-- 按实体参数查询[END] 
	
	//-- 自定义业务方法，请写在下方 -->
}
```
## Service 
> 在Service 中调用Mapper接口

```Java
/**
	 * 删除
	 * 
	 * @param id
	 */
	public void deleteById(Integer pk) {
		repo.deleteByPK(pk);
	}

	/**
	 * 通过id获取
	 * 
	 * @param id
	 * @return
	 */
	public OrgInfo findById(Integer pk) {
		return repo.selectByPK(pk);
	}

	/**
	 * 通过非空属性查询
	 * 
	 * @param OrgInfo
	 * @return
	 */
	public List<OrgInfo> findByNotNullProps(OrgInfo entity) {

		SelectStatement<OrgInfo> st = StatementBuilder.buildSelectSelective(entity);
		return repo.selectByStatement(st);
	}
  /**
	 * 单表通用查询样例
	 * 
	 * @param OrgInfo
	 * @return
	 */
  @Override
	public OrgInfo findByCode(String code) {
		OrgInfo.EntityNode n = OrgInfo.EntityNode.INSTANCE;
		SelectStatement<OrgInfo> st = StatementBuilder.buildSelect(n);
		st.restrictions().add(n.code.eq(code));
		return CollectionUtil.uniqueResult(this.repo.selectByStatement(st));
	}
  
  ```


