package cherish.component.`macro`

import sun.reflect.generics.tree.Tree

import scala.language.experimental.macros
import scala.reflect.macros.whitebox
/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/13
  */
object SQLBuilderMacros {

    def builderMacro[E:c.WeakTypeTag,String](c:whitebox.Context)
                    (name:c.Expr[String])
                    (params:c.Expr[Any]*):c.Expr[String] = { //
      import c.universe._

      val expectedNames = findMembers[E](c).map(_.name.toString.trim)
      val condital = name.tree.toString().split("findBy_").toBuffer
      val attrs = condital(1).split("_and_").toSeq
      if(attrs.length != params.length){
        c.error(c.enclosingPosition,s"name's length ${attrs.length} != parameter's length ${params.length}.")
      }

      attrs.zipWithIndex.foreach{
        case (attr,index) =>
          if(expectedNames.contains(attr)){
            c.error(c.enclosingPosition,s"${c.weakTypeOf[E]}#$attr not found. Expected fields are ${expectedNames.mkString("#", ", #", "")}.")
          }
      }

      var where = ""
      var count = 0
      attrs.foreach{
        t =>
          where += t + " = '" + params(count) + "' AND "
          count +=1
      }

      where += " 1=1"


      c.Expr[String](Literal(Constant(("SELECT * FROM " + c.weakTypeOf[E].getClass.getSimpleName + " WHERE " + where))))
      //"SELECT * FROM " + c.weakTypeOf[E].getClass.getSimpleName + " WHERE " + where
    }


  /**
    * find method
    */
  def findField[E: c.WeakTypeTag,R](c: whitebox.Context)
                                   (fieldName: c.Expr[String]):c.Expr[R]  = {
    import c.universe._
    val Literal(Constant(field:String)) = fieldName.tree
    //gather class field

    val expectedNames =  findMembers[E](c)

    //validate field name
    //    c.error(c.enclosingPosition,s"field: $field")
    val termOpt =  expectedNames.find(_.name.decodedName.toString.trim == field)
    termOpt match{
      case Some(term) =>
        val termType = term.typeSignature
        c.Expr[R](q"stark.activerecord.services.DSL.column[$termType]($field)")
      case None=>
        c.error(c.enclosingPosition, s"${c.weakTypeOf[E]}#$field not found. Expected fields are ${expectedNames.mkString("#", ", #", "")}.")
        c.Expr[R](Literal(Constant(Nil)))
    }
  }

  private def findMembers[E:c.WeakTypeTag](c:whitebox.Context):Seq[c.universe.TermSymbol]={
    c.weakTypeOf[E].members.filter(_.isTerm).filter(_.asTerm.isAccessor)
      .map(_.asTerm.accessed.asTerm).toStream.distinct
  }
}
