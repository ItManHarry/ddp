package com.doosan.ddp.pm.dao.domain.base
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import org.hibernate.annotations.GeneratorType
import org.hibernate.annotations.GenericGenerator
/**
 * 基础DAO Model
 */
@MappedSuperclass
class TableEntityBaseModel implements Serializable {
	
	/*唯一标识 使用UUID作为主键*/
	@Id
	@GenericGenerator(name="jpa-uuid",strategy="uuid")
	@GeneratedValue(generator="jpa-uuid")
	@Column(name="tid",length=36)
	String tid;
	/*创建者*/
	@Column(name="createuserid",length=30)
	String createuserid;
	@Column(name="createtime")
	Date createtime;
	/*最后修改人*/
	@Column(name="modifyuserid",length=30)
	String modifyuserid;
	/*修改时间*/
	@Column(name="modifytime")
	Date modifytime;
	/*删除标记(1 : 正常  0 : 删除)*/
	@Column(name="status")
	int status;
}