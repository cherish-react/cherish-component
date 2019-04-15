package cherish.component.database.jpa

import java.util
import java.util.Properties
import javax.persistence.{EntityManager, EntityManagerFactory}
import javax.sql.DataSource

import org.apache.tapestry5.ioc.annotations.{EagerLoad, Local, Match}
import org.apache.tapestry5.ioc.{MethodAdviceReceiver, ObjectLocator}
import org.slf4j.Logger
import org.springframework.orm.jpa.support.SharedEntityManagerBean
import org.springframework.orm.jpa.vendor.{Database, HibernateJpaVendorAdapter}
import org.springframework.orm.jpa.{JpaTransactionManager, LocalContainerEntityManagerFactoryBean}
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.{AnnotationTransactionAttributeSource, Transactional}
import org.springframework.transaction.interceptor.TransactionInterceptor

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/11
  */
object EntityManagerSourceModule {
  private var springBeanFactoryOpt:Option[LocalContainerEntityManagerFactoryBean] =  None

    @EagerLoad
    def buildEntityManagerFactory(dataSource:DataSource
                                  ,configuration: util.Collection[String]
                                  ,objectLocator: ObjectLocator
                                  ,dataBaseConfiguration:DataBaseConfiguration): EntityManagerFactory ={
        val localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean()
        localContainerEntityManagerFactoryBean.setDataSource(dataSource)
        val hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter()
        hibernateJpaVendorAdapter.setDatabase(Database.HSQL)
        hibernateJpaVendorAdapter.setGenerateDdl(true)
      localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter)
      val packages = configuration.toArray(new Array[String](configuration.size()))
      localContainerEntityManagerFactoryBean.setPackagesToScan(packages:_*)
      val properties = new Properties()
      properties.setProperty("hibernate.ejb.naming_strategy","org.hibernate.cfg.ImprovedNamingStrategy")
      if(dataBaseConfiguration.dataBaseType == DataBaseConfiguration.ORACLE){
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.OracleDialect")
      }else if(dataBaseConfiguration.dataBaseType == DataBaseConfiguration.MYSQL){
        properties.setProperty("hibernate.dialect","cherish.component.config.MySQL5DialectUTF8")
      }
      properties.setProperty("hibernate.show_sql","true")
      properties.setProperty("hibernate.format_sql","true")
      properties.setProperty("hibernate.hbm2ddl.auto","update")
      ActiveRecord.objectLocator = objectLocator
      localContainerEntityManagerFactoryBean.setJpaProperties(properties)
      localContainerEntityManagerFactoryBean.setPersistenceUnitName("jpa")
      localContainerEntityManagerFactoryBean.afterPropertiesSet()
      springBeanFactoryOpt = Some(localContainerEntityManagerFactoryBean)
      localContainerEntityManagerFactoryBean.getObject()
    }

    def buildEntityManager(logger: Logger,@Local entityManagerFactory: EntityManagerFactory):EntityManager={
    val shared = new SharedEntityManagerBean()
    shared.setEntityManagerFactory(entityManagerFactory)
    shared.setEntityManagerInterface(classOf[EntityManager])
    shared.afterPropertiesSet()

    shared.getObject
  }

    def buildJpaTransactionManager(entityManagerFactory:EntityManagerFactory,
                                   dataSource: DataSource
                                  ):PlatformTransactionManager={
      val transactionManager = new JpaTransactionManager()
      //保证全局事务使用的key都是自身申明的对象
      transactionManager.setEntityManagerFactory(entityManagerFactory)
      //设置JPA厂商
      springBeanFactoryOpt.foreach(x=>transactionManager.setJpaDialect(x.getJpaDialect))

      transactionManager
    }

    def buildTransactionInterceptor(@Local transactionManager: PlatformTransactionManager): TransactionInterceptor = {
      val transactionAttributeSource = new AnnotationTransactionAttributeSource
      val transactionInterceptor = new TransactionInterceptor(transactionManager, transactionAttributeSource)
      transactionInterceptor.afterPropertiesSet()
      transactionInterceptor
    }

    @Match(Array("*"))
    def adviseTransactional(receiver: MethodAdviceReceiver, @Local transactionInterceptor: TransactionInterceptor) {
      for (m <- receiver.getInterface.getMethods) {
        if (receiver.getMethodAnnotation(m, classOf[Transactional]) != null)
          receiver.adviseMethod(m, new EntityManagerTransactionAdvice(transactionInterceptor, m))
      }
    }
}
