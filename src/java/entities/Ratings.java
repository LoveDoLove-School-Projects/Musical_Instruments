/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kai Quan
 */
@Entity
@Table(name = "RATINGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ratings.findAll", query = "SELECT r FROM Ratings r"),
    @NamedQuery(name = "Ratings.findByOkid", query = "SELECT r FROM Ratings r WHERE r.okid = :okid"),
    @NamedQuery(name = "Ratings.findByUserId", query = "SELECT r FROM Ratings r WHERE r.userId = :userId"),
    @NamedQuery(name = "Ratings.findByProductId", query = "SELECT r FROM Ratings r WHERE r.productId = :productId"),
    @NamedQuery(name = "Ratings.findByRatingScore", query = "SELECT r FROM Ratings r WHERE r.ratingScore = :ratingScore"),
    @NamedQuery(name = "Ratings.findByComment", query = "SELECT r FROM Ratings r WHERE r.comment = :comment"),
    @NamedQuery(name = "Ratings.findByTimestamp", query = "SELECT r FROM Ratings r WHERE r.timestamp = :timestamp")})
public class Ratings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OKID")
    private Integer okid;
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private int userId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private int productId;
    @Basic(optional = false)
    @Column(name = "RATING_SCORE")
    private int ratingScore;
    @Column(name = "COMMENT")
    private String comment;
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Ratings() {
    }

    public Ratings(Integer okid) {
        this.okid = okid;
    }

    public Ratings(Integer okid, int userId, int productId, int ratingScore) {
        this.okid = okid;
        this.userId = userId;
        this.productId = productId;
        this.ratingScore = ratingScore;
    }

    public Integer getOkid() {
        return okid;
    }

    public void setOkid(Integer okid) {
        this.okid = okid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (okid != null ? okid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ratings)) {
            return false;
        }
        Ratings other = (Ratings) object;
        if ((this.okid == null && other.okid != null) || (this.okid != null && !this.okid.equals(other.okid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Ratings[ okid=" + okid + " ]";
    }
    
}
