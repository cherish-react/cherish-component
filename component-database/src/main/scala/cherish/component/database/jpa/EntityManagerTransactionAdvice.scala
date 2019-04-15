package cherish.component.database.jpa

import java.lang.reflect.{AccessibleObject, Method}

import org.apache.tapestry5.plastic.{MethodAdvice, MethodInvocation}
import org.springframework.transaction.interceptor.TransactionInterceptor

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/11
  */
class EntityManagerTransactionAdvice(interceptor: TransactionInterceptor, method:Method) extends  MethodAdvice {
  def advise (methodInvocation: MethodInvocation) {
    interceptor.invoke (new org.aopalliance.intercept.MethodInvocation{
      def getMethod: Method = method
      def getArguments: Array[AnyRef] = {
        throw new UnsupportedOperationException
      }
      @throws (classOf[Throwable] )
      def proceed: AnyRef = {
        methodInvocation.proceed
        methodInvocation.getReturnValue
      }
      def getThis: AnyRef = {
        methodInvocation.getInstance()
      }
      def getStaticPart: AccessibleObject = {
        throw new UnsupportedOperationException
      }
    })
  }
}
