package io.github.xezzon.geom.dict.domain;

import static io.github.xezzon.geom.dict.domain.Dict.CODE_COLUMN;
import static io.github.xezzon.geom.dict.domain.Dict.TAG_COLUMN;

import io.github.xezzon.geom.constant.DatabaseConstant;
import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.github.xezzon.tao.dict.IDict;
import io.github.xezzon.tao.tree.TreeNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Table(
    name = "geom_dict",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {TAG_COLUMN, CODE_COLUMN})
    }
)
@Entity
public class Dict implements IDict, TreeNode<Dict, String> {

  public static final String ROOT_ID = "0";
  public static final String ROOT_CODE = "dict";
  static final String TAG_COLUMN = "tag";
  static final String CODE_COLUMN = "code";

  @Id
  @Column(name = "id", nullable = false, updatable = false, length = DatabaseConstant.ID_LENGTH)
  @GenericGenerator(
      name = HibernateIdGenerator.GENERATOR_NAME,
      type = HibernateIdGenerator.class
  )
  @GeneratedValue(generator = HibernateIdGenerator.GENERATOR_NAME)
  String id;
  /**
   * 字典目
   */
  @Column(name = TAG_COLUMN, nullable = false, updatable = false)
  String tag;
  /**
   * 字典值
   */
  @Column(name = CODE_COLUMN, nullable = false, updatable = false)
  String code;
  /**
   * 字典描述
   */
  @Column(name = "label")
  String label;
  /**
   * 排序号
   */
  @Column(name = "ordinal", nullable = false)
  Integer ordinal;
  /**
   * 上级字典ID
   */
  @Column(name = "parent_id", nullable = false, updatable = false)
  String parentId;

  @Transient
  List<Dict> children;

  @Override
  public int getOrdinal() {
    return Optional.ofNullable(ordinal).orElse(0);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Dict dict = (Dict) o;
    return Objects.equals(id, dict.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
