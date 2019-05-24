package cherish.component.jpa

// Generated Mar 25, 2019 9:54:31 AM by Stark Activerecord generator 4.3.1.Final


import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}


/**
  * Personinfo generated by stark activerecord generator
  */
object PersonInfo extends ActiveRecordInstance[PersonInfo]

@Entity
@Table(name = "personinfo")
class PersonInfo extends ActiveRecord {


  @Id
  @Column(name = "id", unique = true, nullable = false, length = 32)
  var id: java.lang.String = _
  @Column(name = "name", length = 40)
  var name: java.lang.String = _
  @Column(name = "aliasname", length = 40)
  var aliasname: java.lang.String = _
  @Column(name = "sex", length = 6)
  var sex: java.lang.String = _
  @Column(name = "birthday", length = 9)
  var birthday: java.lang.String = _
  @Column(name = "idcard", length = 32)
  var idcard: java.lang.String = _
  @Column(name = "birth_address_code", length = 16)
  var birthAddressCode: java.lang.String = _
  @Column(name = "birth_address", length = 70)
  var birthAddress: java.lang.String = _
  @Column(name = "address_code", length = 16)
  var addressCode: java.lang.String = _
  @Column(name = "address", length = 70)
  var address: java.lang.String = _
  @Column(name = "nation", length = 15)
  var nation: java.lang.String = _
  @Column(name = "race", length = 8)
  var race: java.lang.String = _
  @Column(name = "criminal_record")
  var criminalRecord: java.lang.Integer = _
  @Column(name = "personid", length = 23)
  var personid: java.lang.String = _
  @Column(name = "person_type", length = 16)
  var personType: java.lang.String = _
  @Column(name = "case_type1", length = 16)
  var caseType1: java.lang.String = _
  @Column(name = "case_type2", length = 16)
  var caseType2: java.lang.String = _
  @Column(name = "case_type3", length = 16)
  var caseType3: java.lang.String = _
  @Column(name = "print_unit_code", length = 16)
  var printUnitCode: java.lang.String = _
  @Column(name = "print_unit_name", length = 70)
  var printUnitName: java.lang.String = _
  @Column(name = "printer", length = 40)
  var printer: java.lang.String = _
  @Column(name = "printdate", length = 20)
  var printdate: java.lang.String = _
  @Column(name = "printer_tel", length = 32)
  var printerTel: java.lang.String = _
  @Column(name = "printer_idcard", length = 32)
  var printerIdcard: java.lang.String = _
  @Column(name = "person_level")
  var personLevel: java.lang.Integer = _
  @Column(name = "level_id", length = 32)
  var levelId: java.lang.String = _
  @Column(name = "is_compel_pass", length = 20)
  var isCompelPass: java.lang.String = _
  @Column(name = "is_convert", length = 1)
  var isConvert: java.lang.Integer = _
  @Column(name = "is_qualified", length = 1)
  var isQualified: java.lang.Integer = _
  @Column(name = "rmp")
  var rmp: Array[Byte] = _
  @Column(name = "rsp")
  var rsp: Array[Byte] = _
  @Column(name = "rzp")
  var rzp: Array[Byte] = _
  @Column(name = "rhp")
  var rhp: Array[Byte] = _
  @Column(name = "rxp")
  var rxp: Array[Byte] = _
  @Column(name = "lmp")
  var lmp: Array[Byte] = _
  @Column(name = "lsp")
  var lsp: Array[Byte] = _
  @Column(name = "lzp")
  var lzp: Array[Byte] = _
  @Column(name = "lhp")
  var lhp: Array[Byte] = _
  @Column(name = "lxp")
  var lxp: Array[Byte] = _
  @Column(name = "rmr")
  var rmr: Array[Byte] = _
  @Column(name = "rsr")
  var rsr: Array[Byte] = _
  @Column(name = "rzr")
  var rzr: Array[Byte] = _
  @Column(name = "rhr")
  var rhr: Array[Byte] = _
  @Column(name = "rxr")
  var rxr: Array[Byte] = _
  @Column(name = "lmr")
  var lmr: Array[Byte] = _
  @Column(name = "lsr")
  var lsr: Array[Byte] = _
  @Column(name = "lzr")
  var lzr: Array[Byte] = _
  @Column(name = "lhr")
  var lhr: Array[Byte] = _
  @Column(name = "lxr")
  var lxr: Array[Byte] = _


  def this(id: java.lang.String) {
    this()
    this.id = id
  }

  def this(id: java.lang.String, name: java.lang.String, aliasname: java.lang.String, sex: java.lang.String, birthday: java.lang.String, idcard: java.lang.String, birthAddressCode: java.lang.String, birthAddress: java.lang.String, addressCode: java.lang.String, address: java.lang.String, nation: java.lang.String, race: java.lang.String, criminalRecord: java.lang.Integer, personid: java.lang.String, personType: java.lang.String, caseType1: java.lang.String, caseType2: java.lang.String, caseType3: java.lang.String, printUnitCode: java.lang.String, printUnitName: java.lang.String, printer: java.lang.String, printdate: java.lang.String, printerTel: java.lang.String, printerIdcard: java.lang.String, personLevel: java.lang.Integer, levelId: java.lang.String, isCompelPass: java.lang.String, isConvert: java.lang.Integer, isQualified: java.lang.Integer, rmp: Array[Byte], rsp: Array[Byte], rzp: Array[Byte], rhp: Array[Byte], rxp: Array[Byte], lmp: Array[Byte], lsp: Array[Byte], lzp: Array[Byte], lhp: Array[Byte], lxp: Array[Byte], rmr: Array[Byte], rsr: Array[Byte], rzr: Array[Byte], rhr: Array[Byte], rxr: Array[Byte], lmr: Array[Byte], lsr: Array[Byte], lzr: Array[Byte], lhr: Array[Byte], lxr: Array[Byte]) {
    this()
    this.id = id
    this.name = name
    this.aliasname = aliasname
    this.sex = sex
    this.birthday = birthday
    this.idcard = idcard
    this.birthAddressCode = birthAddressCode
    this.birthAddress = birthAddress
    this.addressCode = addressCode
    this.address = address
    this.nation = nation
    this.race = race
    this.criminalRecord = criminalRecord
    this.personid = personid
    this.personType = personType
    this.caseType1 = caseType1
    this.caseType2 = caseType2
    this.caseType3 = caseType3
    this.printUnitCode = printUnitCode
    this.printUnitName = printUnitName
    this.printer = printer
    this.printdate = printdate
    this.printerTel = printerTel
    this.printerIdcard = printerIdcard
    this.personLevel = personLevel
    this.levelId = levelId
    this.isCompelPass = isCompelPass
    this.isConvert = isConvert
    this.isQualified = isQualified
    this.rmp = rmp
    this.rsp = rsp
    this.rzp = rzp
    this.rhp = rhp
    this.rxp = rxp
    this.lmp = lmp
    this.lsp = lsp
    this.lzp = lzp
    this.lhp = lhp
    this.lxp = lxp
    this.rmr = rmr
    this.rsr = rsr
    this.rzr = rzr
    this.rhr = rhr
    this.rxr = rxr
    this.lmr = lmr
    this.lsr = lsr
    this.lzr = lzr
    this.lhr = lhr
    this.lxr = lxr
  }
}


