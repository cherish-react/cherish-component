package cherish.component.api.support

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/28
  */
object KryoSupport {

  private lazy val  kryo:Kryo = new Kryo()

    def deserializer[T](bytes:Array[Byte],t:T): T ={
        var input:Option[Input] = None
        try{
         input = Some(new Input(bytes))
         kryo.readObject(input.get,t.getClass)
       }finally {
          input.get.close()
        }
    }
}
