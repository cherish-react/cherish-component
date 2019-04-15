package cherish.component.database.jpa

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/1
  */
object DataBaseConfiguration{
  final val ORACLE = 1
  final val MYSQL = 2
}
class DataBaseConfiguration {
  var dataBaseType:Int = _
}
