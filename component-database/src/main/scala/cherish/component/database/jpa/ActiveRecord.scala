package cherish.component.database.jpa

import javax.persistence._

import cherish.component.`macro`.SQLBuilderMacros
import org.apache.tapestry5.ioc.ObjectLocator
import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable.ArrayBuffer
import scala.language.experimental.macros
import scala.reflect.{ClassTag, classTag}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/11
  */
object ActiveRecord {

    @volatile
    var objectLocator:ObjectLocator =_
    private def entityManager:EntityManager = getService[EntityManager]

    private def getService[T:ClassTag]:T ={
      if(objectLocator == null)
        throw new IllegalArgumentException("object locator is null")
      objectLocator.getService(classTag[T].runtimeClass.asInstanceOf[Class[T]])
    }

    def find[T:ClassTag](key:Any):T={
      entityManager.find(classTag[T].runtimeClass,key).asInstanceOf[T]
    }

    @Transactional
    def save[T](entity:T):T ={
      entityManager.persist(entity)
      entity
    }

    @Transactional
    def update[T](entity:T):T ={
      entityManager.merge(entity)
      entity
    }

    def select(sql:String):Query ={
      entityManager.createNativeQuery(sql)
    }

    def select[T:ClassTag](sql:String,resultClass:Class[_]): java.util.ArrayList[T]={
      entityManager.createNativeQuery(sql,resultClass).getResultList.asInstanceOf[java.util.ArrayList[T]]
    }
}

trait ActiveRecord {
  @Transient
  def save():this.type = {
    ActiveRecord.save(this)
  }

  @Transient
  def update():this.type ={
    ActiveRecord.update[this.type](this)
  }
}

import scala.language.dynamics
abstract class ActiveRecordInstance[A](implicit val clazzTag:ClassTag[A]) extends Dynamic{
  val clazz = clazzTag.runtimeClass.asInstanceOf[Class[A]]
  val tableName = clazz.getAnnotation(classOf[Table]).name()
  protected val field = clazz.getDeclaredFields.find(_.isAnnotationPresent(classOf[Id]))
  val primaryKey = field.getOrElse(throw new IllegalStateException("primary key is null")).getName

  def find(key:Any):A ={
    ActiveRecord.find[A](key)
  }

  def select(sql:String):Query ={
    ActiveRecord.select(sql)
  }

  def get: java.util.ArrayList[A] ={
    ActiveRecord.select("SELECT * FROM " + tableName,classTag[A].runtimeClass)
  }

  //def applyDynamic(name:String)(params:Any*):String = macro SQLBuilderMacros.builderMacro[A]
  def applyDynamic(key:String)(params:Any*):java.util.ArrayList[A]  = {
    var where = ""
    var count = 0

    val buffer = key.split("findBy_").toBuffer
    if (buffer(1).indexOf("not_in") > 0) {
      if (params.size >1) {
        throw new IllegalArgumentException(s"param size > 1")
      }

      val conditionSet = buffer(1).split("_not_in").toSeq
      val sqlColumn = new ArrayBuffer[String]
      clazz.getDeclaredFields.filter(t => t.isAnnotationPresent(classOf[Column]))
        .map(t => sqlColumn += t.getAnnotation(classOf[Column]).name())

      where += sqlColumn(0) + " not in( " + params.toSeq(0) +")"

    } else {
      val conditionSet = buffer(1).split("_and_").toSeq
      if (conditionSet.size != params.size) {
        throw new IllegalArgumentException(s"param size is not equal condition size")
      }

      val sqlColumn = new ArrayBuffer[String]
      clazz.getDeclaredFields.filter(t => t.isAnnotationPresent(classOf[Column]) && conditionSet.contains(t.getName))
        .map(t => sqlColumn += t.getAnnotation(classOf[Column]).name())

      sqlColumn.foreach {
        m =>
          where += m + " = '" + params.toSeq(count) + "' AND "
          count += 1
      }
    }

      where += " 1=1"

      val sql = "SELECT * FROM " + tableName + " WHERE " + where
      ActiveRecord.select(sql, classTag[A].runtimeClass)

  }
}
