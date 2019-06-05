package cherish.component.jpa

// Generated Mar 25, 2019 9:54:31 AM by Stark Activerecord generator 4.3.1.Final


import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}


/**
  * Personinfo generated by stark activerecord generator
  */
object QualityScore extends ActiveRecordInstance[QualityScore]

@Entity
@Table(name = "quality_score")
class QualityScore extends ActiveRecord {

  @Id
  @Column(name = "id", unique = true, nullable = false, length = 32)
  var id: java.lang.String = _
  @Column(name = "cardid", length = 23)
  var cardid: java.lang.String = _
  @Column(name = "rmp")
  var rmp: java.lang.Float = _
  @Column(name = "rsp")
  var rsp: java.lang.Float = _
  @Column(name = "rzp")
  var rzp: java.lang.Float = _
  @Column(name = "rhp")
  var rhp: java.lang.Float = _
  @Column(name = "rxp")
  var rxp: java.lang.Float = _
  @Column(name = "lmp")
  var lmp: java.lang.Float = _
  @Column(name = "lsp")
  var lsp: java.lang.Float = _
  @Column(name = "lzp")
  var lzp: java.lang.Float = _
  @Column(name = "lhp")
  var lhp: java.lang.Float = _
  @Column(name = "lxp")
  var lxp: java.lang.Float = _
  @Column(name = "rmr")
  var rmr: java.lang.Float = _
  @Column(name = "rsr")
  var rsr: java.lang.Float = _
  @Column(name = "rzr")
  var rzr: java.lang.Float = _
  @Column(name = "rhr")
  var rhr: java.lang.Float = _
  @Column(name = "rxr")
  var rxr: java.lang.Float = _
  @Column(name = "lmr")
  var lmr: java.lang.Float = _
  @Column(name = "lsr")
  var lsr: java.lang.Float = _
  @Column(name = "lzr")
  var lzr: java.lang.Float = _
  @Column(name = "lhr")
  var lhr: java.lang.Float = _
  @Column(name = "lxr")
  var lxr: java.lang.Float = _
  @Column(name = "img_url", length = 64)
  var imgUrl: java.lang.String = _
  @Column(name = "total_score")
  var totalScore: java.lang.Double = _
  @Column(name = "quality_level")
  var qualityLevel: java.lang.Integer = _


  def this(id: java.lang.String) {
    this()
    this.id = id
  }

  def this(id: java.lang.String, cardid: java.lang.String, rmp: java.lang.Float, rsp: java.lang.Float, rzp:java.lang.Float, rhp: java.lang.Float, rxp: java.lang.Float, lmp: java.lang.Float, lsp: java.lang.Float, lzp: java.lang.Float, lhp: java.lang.Float, lxp: java.lang.Float, rmr: java.lang.Float, rsr: java.lang.Float, rzr: java.lang.Float, rhr: java.lang.Float, rxr: java.lang.Float, lmr: java.lang.Float, lsr: java.lang.Float, lzr: java.lang.Float, lhr: java.lang.Float, lxr: java.lang.Float, imgUrl: java.lang.String, totalScore: java.lang.Double, qualityLevel: java.lang.Integer) {
    this()
    this.id = id
    this.cardid = cardid
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
    this.imgUrl = imgUrl
    this.totalScore = totalScore
    this.qualityLevel = qualityLevel
  }
}


