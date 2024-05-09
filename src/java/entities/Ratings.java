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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RATINGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ratings.findAll", query = "SELECT r FROM Ratings r"),
    @NamedQuery(name = "Ratings.findByPkid", query = "SELECT r FROM Ratings r WHERE r.pkid = :pkid"),
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
    @Column(name = "PKID")
    private Integer pkid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_ID")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCT_ID")
    private int productId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RATING_SCORE")
    private int ratingScore;
    @Size(max = 5000)
    @Column(name = "COMMENT")
    private String comment;
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Ratings() {
    }

    public Ratings(Integer pkid) {
        this.pkid = pkid;
    }

    public Ratings(Integer pkid, int userId, int productId, int ratingScore) {
        this.pkid = pkid;
        this.userId = userId;
        this.productId = productId;
        this.ratingScore = ratingScore;
    }

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
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
        hash += (pkid != null ? pkid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ratings)) {
            return false;
        }
        Ratings other = (Ratings) object;
        if ((this.pkid == null && other.pkid != null) || (this.pkid != null && !this.pkid.equals(other.pkid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ratings{");
        sb.append("pkid=").append(pkid);
        sb.append(", userId=").append(userId);
        sb.append(", productId=").append(productId);
        sb.append(", ratingScore=").append(ratingScore);
        sb.append(", comment=").append(comment);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
