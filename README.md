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
 
 ## Entity
 ```Java
 @Table(OrgInfo.EntityNode.class)
public class OrgInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	  * ID
	  * nullable:true,length:11
	  */
	@Column(primaryKey = true,autoIncrement = true,comment = "ID")	
	private Integer id;
	/**
	  * 
	  * nullable:false,length:32
	  */
	@Column(comment = "")	
	@NotNull
	private String code;
	/**
	  * 
	  * nullable:false,length:128
	  */
	@Column(comment = "")	
	@NotNull
	private String secret;
	/**
	  * 名称
	  * nullable:false,length:64
	  */
	@Column(comment = "名称")	
	@NotNull
	private String name;
	/**
	  * 图片
	  * nullable:true,length:256
	  */
	@Column(comment = "图片")	
	private String imgSrc;
	/**
	  * 类型(1:个人,2:机构,3:机关单位,4:企业)
	  * nullable:false,length:11
	  */
	@Column(comment = "类型(1:个人,2:机构,3:机关单位,4:企业)")	
	@NotNull
	private Integer type;
	/**
	  * 官网
	  * nullable:true,length:250
	  */
	@Column(comment = "官网")	
	private String website;
	/**
	  * 地址
	  * nullable:true,length:250
	  */
	@Column(comment = "地址")	
	private String address;
	/**
	  * 电话
	  * nullable:true,length:32
	  */
	@Column(comment = "电话")	
	private String tel;
	/**
	  * 邮箱
	  * nullable:true,length:64
	  */
	@Column(comment = "邮箱")	
	private String email;
	/**
	  * 联系人
	  * nullable:true,length:32
	  */
	@Column(comment = "联系人")	
	private String contactor;
	/**
	  * 状态
	  * nullable:false,length:4
	  */
	@Column(comment = "状态")	
	@NotNull
	private Byte status;
	/**
	  * 备注
	  * nullable:true,length:500
	  */
	@Column(comment = "备注")	
	private String remark;
	/**
	  * 
	  * nullable:false,length:19
	  */
	@Column(comment = "")	
	@NotNull
	private Date opOn;
	/**
	  * 
	  * nullable:false,length:11
	  */
	@Column(comment = "")	
	@NotNull
	private Integer opBy;
        
	... get and set 

    public static class EntityNode extends AbstractEntityNode<OrgInfo> {
        public static final EntityNode INSTANCE = new EntityNode("toi");;
    	/** ID */
        public FieldNode<OrgInfo, Integer> id =  createFieldNode("id","id",Integer.class,JdbcType.INTEGER);
    	/**  */
        public FieldNode<OrgInfo, String> code =  createFieldNode("code","code",String.class,JdbcType.VARCHAR);
    	/**  */
        public FieldNode<OrgInfo, String> secret =  createFieldNode("secret","secret",String.class,JdbcType.VARCHAR);
    	/** 名称 */
        public FieldNode<OrgInfo, String> name =  createFieldNode("name","name",String.class,JdbcType.VARCHAR);
    	/** 图片 */
        public FieldNode<OrgInfo, String> imgSrc =  createFieldNode("imgSrc","img_src",String.class,JdbcType.VARCHAR);
    	/** 类型(1:个人,2:机构,3:机关单位,4:企业) */
        public FieldNode<OrgInfo, Integer> type =  createFieldNode("type","type",Integer.class,JdbcType.INTEGER);
    	/** 官网 */
        public FieldNode<OrgInfo, String> website =  createFieldNode("website","website",String.class,JdbcType.VARCHAR);
    	/** 地址 */
        public FieldNode<OrgInfo, String> address =  createFieldNode("address","address",String.class,JdbcType.VARCHAR);
    	/** 电话 */
        public FieldNode<OrgInfo, String> tel =  createFieldNode("tel","tel",String.class,JdbcType.VARCHAR);
    	/** 邮箱 */
        public FieldNode<OrgInfo, String> email =  createFieldNode("email","email",String.class,JdbcType.VARCHAR);
    	/** 联系人 */
        public FieldNode<OrgInfo, String> contactor =  createFieldNode("contactor","contactor",String.class,JdbcType.VARCHAR);
    	/** 状态 */
        public FieldNode<OrgInfo, Byte> status =  createFieldNode("status","status",Byte.class,JdbcType.TINYINT);
    	/** 备注 */
        public FieldNode<OrgInfo, String> remark =  createFieldNode("remark","remark",String.class,JdbcType.VARCHAR);
    	/**  */
        public FieldNode<OrgInfo, Date> opOn =  createFieldNode("opOn","op_on",Date.class,JdbcType.TIMESTAMP);
    	/**  */
        public FieldNode<OrgInfo, Integer> opBy =  createFieldNode("opBy","op_by",Integer.class,JdbcType.INTEGER);
	
        /**
         * @param alias 别名
         */
        public EntityNode(String alias) {
            super(OrgInfo.class,"t_org_info",alias);
        }
    }
    
    // ==== 自定义属性 ====
}
```
 
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

# 代码生成器
> 角色类似 mybatis-generator

![](1.png)

![](2.png)


## 生成的mapper xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.kingseok.cirms.biz.dao.OrgInfoMapper" >
	<resultMap id="BaseResultMap" type="com.kingseok.cirms.biz.domain.OrgInfo" >
	    <id column="id" property="id" jdbcType="INTEGER" />	
	  	<result column="code" property="code" jdbcType="VARCHAR" />
	  	<result column="secret" property="secret" jdbcType="VARCHAR" />
	  	<result column="name" property="name" jdbcType="VARCHAR" />
	  	<result column="img_src" property="imgSrc" jdbcType="VARCHAR" />
	  	<result column="type" property="type" jdbcType="INTEGER" />
	  	<result column="website" property="website" jdbcType="VARCHAR" />
	  	<result column="address" property="address" jdbcType="VARCHAR" />
	  	<result column="tel" property="tel" jdbcType="VARCHAR" />
	  	<result column="email" property="email" jdbcType="VARCHAR" />
	  	<result column="contactor" property="contactor" jdbcType="VARCHAR" />
	  	<result column="status" property="status" jdbcType="TINYINT" />
	  	<result column="remark" property="remark" jdbcType="VARCHAR" />
	  	<result column="op_on" property="opOn" jdbcType="TIMESTAMP" />
	  	<result column="op_by" property="opBy" jdbcType="INTEGER" />
    </resultMap>
    <resultMap id="BaseResultMap_OrgInfo" type="com.kingseok.cirms.biz.domain.OrgInfo" >
	    <id column="toi_id" property="id" jdbcType="INTEGER" />	
	  	<result column="toi_code" property="code" jdbcType="VARCHAR" />
	  	<result column="toi_secret" property="secret" jdbcType="VARCHAR" />
	  	<result column="toi_name" property="name" jdbcType="VARCHAR" />
	  	<result column="toi_img_src" property="imgSrc" jdbcType="VARCHAR" />
	  	<result column="toi_type" property="type" jdbcType="INTEGER" />
	  	<result column="toi_website" property="website" jdbcType="VARCHAR" />
	  	<result column="toi_address" property="address" jdbcType="VARCHAR" />
	  	<result column="toi_tel" property="tel" jdbcType="VARCHAR" />
	  	<result column="toi_email" property="email" jdbcType="VARCHAR" />
	  	<result column="toi_contactor" property="contactor" jdbcType="VARCHAR" />
	  	<result column="toi_status" property="status" jdbcType="TINYINT" />
	  	<result column="toi_remark" property="remark" jdbcType="VARCHAR" />
	  	<result column="toi_op_on" property="opOn" jdbcType="TIMESTAMP" />
	  	<result column="toi_op_by" property="opBy" jdbcType="INTEGER" />
    </resultMap>
    <sql id="Base_Column_List" >
  		id,code,secret,name,img_src,type,website,address,tel,email,contactor,status,remark,op_on,op_by
	</sql>
  	<sql id="Base_Column_List_OrgInfo" >
  		toi.id toi_id,toi.code toi_code,toi.secret toi_secret,toi.name toi_name,toi.img_src toi_img_src,toi.type toi_type,toi.website toi_website,toi.address toi_address,toi.tel toi_tel,toi.email toi_email,toi.contactor toi_contactor,toi.status toi_status,toi.remark toi_remark,toi.op_on toi_op_on,toi.op_by toi_op_by
	</sql>
	
	<!-- 系统自定义 -->
	<!-- 实体参数查询结果[START] -->
	<resultMap id="ResultMap_FindByQueryParam" type="com.kingseok.cirms.biz.domain.OrgInfo" extends="BaseResultMap_OrgInfo">
		
	</resultMap>

	<sql id="Condition_FindByQueryParam">
		<if test="queryParam.param!=null">
			<bind name="p" value="queryParam.param"/>
			<if test="p.id!=null ">
		    	and toi.id=#{p.id}
		    </if>
			<if test="p.code!=null and p.code!=''">
		    	and toi.code=#{p.code}
		    </if>
			<if test="p.secret!=null and p.secret!=''">
		    	and toi.secret=#{p.secret}
		    </if>
			<if test="p.name!=null and p.name!=''">
		    	and toi.name=#{p.name}
		    </if>
			<if test="p.imgSrc!=null and p.imgSrc!=''">
		    	and toi.img_src=#{p.imgSrc}
		    </if>
			<if test="p.type!=null ">
		    	and toi.type=#{p.type}
		    </if>
			<if test="p.website!=null and p.website!=''">
		    	and toi.website=#{p.website}
		    </if>
			<if test="p.address!=null and p.address!=''">
		    	and toi.address=#{p.address}
		    </if>
			<if test="p.tel!=null and p.tel!=''">
		    	and toi.tel=#{p.tel}
		    </if>
			<if test="p.email!=null and p.email!=''">
		    	and toi.email=#{p.email}
		    </if>
			<if test="p.contactor!=null and p.contactor!=''">
		    	and toi.contactor=#{p.contactor}
		    </if>
			<if test="p.status!=null ">
		    	and toi.status=#{p.status}
		    </if>
			<if test="p.remark!=null and p.remark!=''">
		    	and toi.remark=#{p.remark}
		    </if>
			<if test="p.opOn!=null ">
		    	and toi.op_on=#{p.opOn}
		    </if>
			<if test="p.opBy!=null ">
		    	and toi.op_by=#{p.opBy}
		    </if>
		</if>
		<bind name="rp" value="queryParam.ruleParam" />
		<include refid="org.mybatis.hbatis.orm.mapper.HbatisMapper.RuleParam"/>
	</sql>
	<select id="findByQueryParam" resultMap="ResultMap_FindByQueryParam">
		select
		<include refid="Base_Column_List_OrgInfo"></include>
		from t_org_info toi where 1=1
		<include refid="Condition_FindByQueryParam"></include>
		<if test="queryParam.limit gt 0">
	  		limit ${queryParam.start},${queryParam.limit}
	  	</if>
	</select>
	<select id="countByQueryParam" resultType="long">
		select count(1) from t_org_info toi where 1=1
		<include refid="Condition_FindByQueryParam"></include>
	</select>
	
	<!-- 实体参数查询结果[END] -->
	<!-- 用户自定义 -->
	
</mapper>
```
> 如果只是单表的CRUD操作，xml内容可为空。但为了减少后期人工添加的麻烦，于是就在模块里生成...
