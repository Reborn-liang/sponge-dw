package cn.nearf.ggz.api;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import org.jeecgframework.core.annotation.JeecgEntityTitle;
import org.jeecgframework.core.common.dao.ICommonDao;
import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.DetachedCriteriaUtil;
import org.jeecgframework.core.common.hibernate.qbc.HqlQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.hibernate.qbc.PagerUtil;
import org.jeecgframework.core.common.model.common.DBTable;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGridReturn;
import org.jeecgframework.core.common.model.json.ImportFile;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.extend.swftools.SwfToolsUtil;
import org.jeecgframework.core.extend.template.DataSourceMap;
import org.jeecgframework.core.extend.template.Template;
import org.jeecgframework.core.util.*;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.DataTableReturn;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * 
 * @author Chris
 *
 */
@Repository
public class ApiDao<T, PK extends Serializable> {

	/**
	 * ?????????Log4j???????????????
	 */
	private static final Logger logger = Logger.getLogger(ApiDao.class);
	/**
	 * ????????????sessionFactory??????,??????????????????(HibernateDaoSupport)
	 **/
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;


	/**
	 * ??????????????????????????????
	 *
	 * @param entityName
	 *            ??????????????????
	 */
	private <T> void getProperty(Class entityName) {
		ClassMetadata cm = sessionFactory.getClassMetadata(entityName);
		String[] str = cm.getPropertyNames(); // ?????????????????????????????????
		for (int i = 0; i < str.length; i++) {
			String property = str[i];
			String type = cm.getPropertyType(property).getName(); // ????????????????????????
			org.jeecgframework.core.util.LogUtil.info(property + "---&gt;" + type);
		}
	}

	/**
	 * ?????????????????????
	 *
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		List<DBTable> resultList = new ArrayList<DBTable>();
		SessionFactory factory = session.getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		for (String key : (Set<String>) metaMap.keySet()) {
			DBTable dbTable = new DBTable();
			AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap.get(key);
			dbTable.setTableName(classMetadata.getTableName());
			dbTable.setEntityName(classMetadata.getEntityName());
			Class<?> c;
			try {
				c = Class.forName(key);
				JeecgEntityTitle t = c.getAnnotation(JeecgEntityTitle.class);
				dbTable.setTableTitle(t != null ? t.name() : "");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			resultList.add(dbTable);
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return resultList;
	}

	/**
	 * ?????????????????????
	 *
	 * @return
	 */
	public Integer getAllDbTableSize() {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		SessionFactory factory = session.getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return metaMap.size();
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		Assert.hasText(propertyName);
		
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		SimpleExpression criterions = Restrictions.eq(propertyName, value);
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterions);
		
		T res = (T) criteria.uniqueResult();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return res;
	}

	/**
	 * ???????????????????????????.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
		Assert.hasText(propertyName);
		
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, value));
		
		List<T> res = criteria.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return res;
	}

	/**
	 * ????????????????????????????????????
	 */
	public <T> Serializable save(T entity) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		try {
			Serializable id = session.save(entity);
			session.flush();
			if (logger.isDebugEnabled()) {
				logger.debug("??????????????????," + entity.getClass().getName());
			}
			
			if (isOpenSession) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
			
			return id;
		} catch (RuntimeException e) {
			logger.error("??????????????????", e);
			throw e;
		}

	}

	/**
	 * ??????????????????
	 *
	 * @param <T>
	 * @param entitys
	 *            ???????????????????????????????????????
	 */
	public <T> void batchSave(List<T> entitys) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		for (int i = 0; i < entitys.size(); i++) {
			session.save(entitys.get(i));
			if (i % 20 == 0) {
				// 20?????????????????????????????????????????????
				session.flush();
				session.clear();
			}
		}
		// ??????????????????----????????????20??????40????????????
		session.flush();
		session.clear();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}
	
	public <T> void batchSaveOrUpdate(List<T> entitys) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		for (int i = 0; i < entitys.size(); i++) {
			session.saveOrUpdate(entitys.get(i));
			if (i % 20 == 0) {
				// 20?????????????????????????????????????????????
				session.flush();
				session.clear();
			}
		}
		// ??????????????????----????????????20??????40????????????
		session.flush();
		session.clear();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ??????????????????????????????????????????
	 *
	 * @param <T>
	 *
	 * @param entity
	 */

	public <T> void saveOrUpdate(T entity) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		try {
			session.saveOrUpdate(entity);
			session.flush();
			if (logger.isDebugEnabled()) {
				logger.debug("?????????????????????," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("?????????????????????", e);
			throw e;
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ?????????????????????????????????
	 */
	public <T> void delete(T entity) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		try {
			session.delete(entity);
			session.flush();
			if (logger.isDebugEnabled()) {
				logger.debug("????????????," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("????????????", e);
			throw e;
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @param <T>
	 * @param pojo
	 */
	public <T> void deleteEntityById(Class entityName, Serializable id) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		delete(get(entityName, id));
		session.flush();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ?????????????????????
	 *
	 * @param <T>
	 *
	 * @param entitys
	 */
	public <T> void deleteAllEntitie(Collection<T> entitys) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		for (Object entity : entitys) {
			session.delete(entity);
			session.flush();
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ??????Id???????????????
	 */
	public <T> T get(Class<T> entityClass, final Serializable id) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		T t = (T) session.get(entityClass, id);
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return t;
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @param lock
	 * @return
	 */
	public <T> T getEntity(Class entityName, Serializable id) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		T t = (T) session.get(entityName, id);
		if (t != null) {
			session.flush();
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return t;
	}

	/**
	 * ?????????????????????
	 *
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(T pojo) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		session.update(pojo);
		session.flush();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ?????????????????????
	 *
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(String className, Object id) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		session.update(className, id);
		session.flush();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ????????????????????????
	 */
	public <T> void updateEntityById(Class entityName, Serializable id) {
		updateEntitie(get(entityName, id));
	}

	/**
	 * ??????hql ????????????????????????
	 *
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findByQueryString(final String query) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query queryObject = session.createQuery(query);
		List<T> list = queryObject.list();
		if (list.size() > 0) {
			session.flush();
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return list;
	}

	/**
	 * ??????hql??????????????????
	 *
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> T singleResult(String hql) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		T t = null;
		Query queryObject = session.createQuery(hql);
		List<T> list = queryObject.list();
		if (list.size() == 1) {
			session.flush();
			t = list.get(0);
		} else if (list.size() > 0) {
			throw new BusinessException("???????????????:" + list.size() + "??????1");
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return t;
	}

	/**
	 * ??????hql ??????????????????HashMap??????
	 *
	 * @param <T>
	 * @param query
	 * @return
	 */
	public Map<Object, Object> getHashMapbyQuery(String hql) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query query = session.createQuery(hql);
		List list = query.list();
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] tm = (Object[]) iterator.next();
			map.put(tm[0].toString(), tm[1].toString());
		}
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return map;
	}

	/**
	 * ??????sql????????????
	 *
	 * @param <T>
	 * @param query
	 * @return
	 */
	public int updateBySqlString(final String query) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query querys = session.createSQLQuery(query);
		int result = querys.executeUpdate();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????sql????????????????????????
	 *
	 * @param <T>
	 * @param query
	 * @return
	 */
	public List<T> findListbySql(final String sql) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query querys = session.createSQLQuery(sql);
		List<T> result = querys.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}



	public <T> List<T> loadAll(final Class<T> entityClass) {
		
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = session.createCriteria(entityClass);
		
		List<T> res = criteria.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return res;
	}

	/**
	 * ????????????Criteria??????
	 *
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = session.createCriteria(entityClass);
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return criteria;
	}

	/**
	 * ?????????????????????????????????. ?????????
	 *
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc) {
		Assert.hasText(propertyName);
		
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, value));
		
		if (isAsc) {
			criteria.addOrder(Order.asc("asc"));
		} else {
			criteria.addOrder(Order.desc("desc"));
		}
		
		List<T> res = criteria.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return res;
	}

	/**
	 * ???????????????????????????????????????Query??????
	 *
	 * @param session
	 *            Hibernate??????
	 * @param hql
	 *            HQL??????
	 * @param objects
	 *            ????????????
	 * @return Query??????
	 */
	public Query createQuery(Session session, String hql, Object... objects) {
		Query query = session.createQuery(hql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}

	/**
	 * ??????????????????
	 *
	 * @param clas
	 * @param values
	 * @return
	 */
	public <T> int batchInsertsEntitie(List<T> entityList) {
		int num = 0;
		for (int i = 0; i < entityList.size(); i++) {
			save(entityList.get(i));
			num++;
		}
		return num;
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 */
	/**
	 * ????????????????????????????????? ????????????like?????????????????????"%"+username+"%" Hibernate Query
	 *
	 * @param hibernateTemplate
	 * @param hql
	 * @param valus
	 *            ???????????????????????????????????????????????????? ??????????????????????????????????????????null
	 * @return 2008-07-19 add by liuyang
	 */
	public List<T> executeQuery(final String hql, final Object[] values) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query query = session.createQuery(hql);
		// query.setCacheable(true);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}

		List<T> result = query.list();

		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}
	
	public List<T> executeQuery(final String hql, final Object[] values, int offset, int maxResults) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query query = session.createQuery(hql);
		// query.setCacheable(true);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}

		query.setFirstResult(offset);
		query.setMaxResults(maxResults);
		
		List<T> result = query.list();

		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ????????????????????????
	 *
	 * @param entityName
	 * @param exampleEntity
	 * @return
	 */

	public List findByExample(final String entityName, final Object exampleEntity) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Assert.notNull(exampleEntity, "Example entity must not be null");
		Criteria executableCriteria = (entityName != null ? session.createCriteria(entityName)
				: session.createCriteria(exampleEntity.getClass()));
		executableCriteria.add(Example.create(exampleEntity));
		List result = executableCriteria.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	// ?????????????????????????????????????????????????????????
	public Integer getRowCount(DetachedCriteria criteria) {
		return oConvertUtils.getInt(((Criteria) criteria.setProjection(Projections.rowCount())).uniqueResult(), 0);
	}

	/**
	 * ??????????????????
	 */
	public void callableStatementByName(String proc) {
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @param clazz
	 * @return
	 */
	public int getCount(Class<T> clazz) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		int count = DataAccessUtils
				.intResult(session.createQuery("select count(*) from " + clazz.getName()).list());
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return count;
	}

	/**
	 * ??????????????????CriteriaQuery ?????????final int allCounts =
	 * oConvertUtils.getInt(criteria
	 * .setProjection(Projections.rowCount()).uniqueResult(), 0);
	 *
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(session);
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// ??????Projection???OrderBy???????????????,?????????????????????Count??????
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		// ???????????????????????????
		if (cq.getOrdermap() != null) {
			cq.setOrder(cq.getOrdermap());
		}
		int pageSize = cq.getPageSize();// ???????????????
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(), pageSize);// ?????????
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		String toolBar = "";
		if (isOffset) {// ????????????
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getPageSize());
			if (cq.getIsUseimage() == 1) {
				toolBar = PagerUtil.getBar(cq.getMyAction(), cq.getMyForm(), allCounts, curPageNO, pageSize,
						cq.getMap());
			} else {
				toolBar = PagerUtil.getBar(cq.getMyAction(), allCounts, curPageNO, pageSize, cq.getMap());
			}
		} else {
			pageSize = allCounts;
		}
		PageList result = new PageList(criteria.list(), toolBar, offset, curPageNO, allCounts);
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????JQUERY datatables DataTableReturn????????????
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq, final boolean isOffset) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(session);
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// ??????Projection???OrderBy???????????????,?????????????????????Count??????
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		// ???????????????????????????
		if (cq.getOrdermap() != null) {
			cq.setOrder(cq.getOrdermap());
		}
		int pageSize = cq.getPageSize();// ???????????????
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(), pageSize);// ?????????
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		if (isOffset) {// ????????????
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getPageSize());
		} else {
			pageSize = allCounts;
		}
		DetachedCriteriaUtil.selectColumn(cq.getDetachedCriteria(), cq.getField().split(","), cq.getEntityClass(),
				false);
		DataTableReturn result = new DataTableReturn(allCounts, allCounts, cq.getDataTables().getEcho(), criteria.list());
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????easyui datagrid DataGridReturn????????????
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq, final boolean isOffset) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(session);
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// ??????Projection???OrderBy???????????????,?????????????????????Count??????
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (StringUtils.isNotBlank(cq.getDataGrid().getSort())) {
			cq.addOrder(cq.getDataGrid().getSort(), cq.getDataGrid().getOrder());
		}

		// ???????????????????????????
		if (!cq.getOrdermap().isEmpty()) {
			cq.setOrder(cq.getOrdermap());
		}
		int pageSize = cq.getPageSize();// ???????????????
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(), pageSize);// ?????????
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		if (isOffset) {// ????????????
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getPageSize());
		} else {
			pageSize = allCounts;
		}
		// DetachedCriteriaUtil.selectColumn(cq.getDetachedCriteria(),
		// cq.getField().split(","), cq.getClass1(), false);
		List list = criteria.list();
		cq.getDataGrid().setResults(list);
		cq.getDataGrid().setTotal(allCounts);
		DataGridReturn result = new DataGridReturn(allCounts, list);
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????????????????SqlQuery
	 *
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList getPageListBySql(final HqlQuery hqlQuery, final boolean isToEntity) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query query = session.createSQLQuery(hqlQuery.getQueryString());

		// query.setParameters(hqlQuery.getParam(), (Type[])
		// hqlQuery.getTypes());
		int allCounts = query.list().size();
		int curPageNO = hqlQuery.getCurPage();
		int offset = PagerUtil.getOffset(allCounts, curPageNO, hqlQuery.getPageSize());
		query.setFirstResult(offset);
		query.setMaxResults(hqlQuery.getPageSize());
		List list = null;
		if (isToEntity) {
			list = ToEntityUtil.toEntityList(query.list(), hqlQuery.getClass1(),
					hqlQuery.getDataGrid().getField().split(","));
		} else {
			list = query.list();
		}
		PageList result = new PageList(hqlQuery, list, offset, curPageNO, allCounts);
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????????????????HqlQuery
	 *
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageList getPageList(final HqlQuery hqlQuery, final boolean needParameter) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query query = session.createQuery(hqlQuery.getQueryString());
		if (needParameter) {
			query.setParameters(hqlQuery.getParam(), (Type[]) hqlQuery.getTypes());
		}
		int allCounts = query.list().size();
		int curPageNO = hqlQuery.getCurPage();
		int offset = PagerUtil.getOffset(allCounts, curPageNO, hqlQuery.getPageSize());
		String toolBar = PagerUtil.getBar(hqlQuery.getMyaction(), allCounts, curPageNO, hqlQuery.getPageSize(),
				hqlQuery.getMap());
		query.setFirstResult(offset);
		query.setMaxResults(hqlQuery.getPageSize());
		PageList result = new PageList(query.list(), toolBar, offset, curPageNO, allCounts);
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????CriteriaQuery??????List
	 *
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(session);
		// ???????????????????????????
		if (cq.getOrdermap() != null) {
			cq.setOrder(cq.getOrdermap());
		}
		if (ispage) {
			criteria.setFirstResult((cq.getCurPage() - 1) * cq.getPageSize());
			criteria.setMaxResults(cq.getPageSize());
		}
		List<T> result = criteria.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * ????????????????????????????????????????????????????????????
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		// ????????????SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		return this.jdbcTemplate.queryForList(sql);
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
		List<T> rsList = new ArrayList<T>();
		// ????????????SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

		T po = null;
		for (Map<String, Object> m : mapList) {
			try {
				po = clazz.newInstance();
				MyBeanUtils.copyMap2Bean_Nobig(po, m);
				rsList.add(po);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rsList;
	}

	/**
	 * ????????????????????????????????????????????????????????????-?????????????????????
	 *
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs) {
		// ????????????SQL
		sql = JdbcDao.jeecgCreatePageSql(sql, page, rows);
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	/**
	 * ????????????????????????????????????????????????????????????For JDBC
	 */
	public Long getCountForJdbc(String sql) {
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * ????????????????????????????????????????????????????????????For JDBC-?????????????????????
	 *
	 */
	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return this.jdbcTemplate.queryForLong(sql, objs);
	}

	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	public Integer executeSql(String sql, List<Object> param) {
		return this.jdbcTemplate.update(sql, param);
	}

	public Integer executeSql(String sql, Object... param) {
		return this.jdbcTemplate.update(sql, param);
	}

	public Integer executeSql(String sql, Map<String, Object> param) {
		return this.namedParameterJdbcTemplate.update(sql, param);
	}

	public Object executeSqlReturnKey(final String sql, Map<String, Object> param) {
		Object keyValue = null;
		KeyHolder keyHolder = null;
		SqlParameterSource sqlp = new MapSqlParameterSource(param);
		if (StringUtil.isNotEmpty(param.get("id"))) {// ?????????????????????id(UUID),????????????????????????????????????????????????
			this.namedParameterJdbcTemplate.update(sql, sqlp);
		} else {// NATIVE or SEQUENCE
			keyHolder = new GeneratedKeyHolder();
			this.namedParameterJdbcTemplate.update(sql, sqlp, keyHolder, new String[] { "id" });
			Number number = keyHolder.getKey();
			if (oConvertUtils.isNotEmpty(number)) {
				keyValue = keyHolder.getKey().longValue();
			}
		}
		return keyValue;
	}

	public Integer countByJdbc(String sql, Object... param) {
		return this.jdbcTemplate.queryForInt(sql, param);
	}

	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		try {
			return this.jdbcTemplate.queryForMap(sql, objs);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * ??????hql ????????????????????????
	 *
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findHql(String hql, Object... param) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query q = session.createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		List<T> result = q.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????HQL??????????????????
	 *
	 * @param hql
	 * @return
	 */
	public Integer executeHql(String hql) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Query q = session.createQuery(hql);
		Integer result = q.executeUpdate();
		
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		Criteria criteria = dc.getExecutableCriteria(session);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		List<T> result = criteria.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ????????????
	 */
	public <T> List<T> findByDetached(DetachedCriteria dc) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		List<T> result = dc.getExecutableCriteria(session).list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * ??????????????????
	 */
	@SuppressWarnings({ "unchecked", })
	public <T> List<T> executeProcedure(String executeSql, Object... params) {
		Session session = null;
		boolean isOpenSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception exp) {
			session = sessionFactory.openSession();
			isOpenSession = true;
		}
		
		SQLQuery sqlQuery = session.createSQLQuery(executeSql);

		for (int i = 0; i < params.length; i++) {
			sqlQuery.setParameter(i, params[i]);
		}

		List<T> result = sqlQuery.list();
		
		if (isOpenSession) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		
		return result;
	}

}
