package io.github.xezzon.geom.user.domain;

import cn.hutool.crypto.digest.BCrypt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.xezzon.geom.constant.DatabaseConstant;
import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "geom_user")
public class User {

  /**
   * 用户主键
   */
  @Id
  @Column(name = "id", nullable = false, updatable = false, length = DatabaseConstant.ID_LENGTH)
  @GenericGenerator(
      name = HibernateIdGenerator.GENERATOR_NAME,
      type = HibernateIdGenerator.class
  )
  @GeneratedValue(generator = HibernateIdGenerator.GENERATOR_NAME)
  String id;

  /**
   * 用户名
   */
  @Column(name = "username", nullable = false, unique = true)
  String username;

  /**
   * 密码明文
   */
  @Transient
  String plaintext;

  /**
   * 密码密文
   */
  @Column(name = "cipher", nullable = false)
  @Setter(AccessLevel.PRIVATE)
  @JsonIgnore
  String cipher;

  /**
   * 用户昵称
   */
  @Column(name = "nickname")
  String nickname;

  /**
   * 记录创建时间
   */
  @DateCreated
  @Column(name = "create_time", nullable = false, updatable = false)
  LocalDateTime createTime;

  /**
   * 最后更新时间
   */
  @DateUpdated
  @Column(name = "update_time", nullable = false)
  LocalDateTime updateTime;

  public User setPlaintext(String plaintext) {
    this.plaintext = plaintext;
    this.cipher = BCrypt.hashpw(plaintext, BCrypt.gensalt());
    return this;
  }
}
