package cherish.component.config

import org.hibernate.dialect.MySQL5InnoDBDialect

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */
class MySQL5DialectUTF8 extends MySQL5InnoDBDialect{
  override def getTableTypeString: String = " ENGINE=InnoDB DEFAULT CHARSET=utf8"
}
