package io.github.xezzon.geom.dict.domain;

import static io.github.xezzon.geom.dict.domain.Dict.CODE;
import static io.github.xezzon.geom.dict.domain.Dict.TAG;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
        @UniqueConstraint(columnNames = {TAG, CODE})
    }
)
@Entity
public class Dict implements IDict, TreeNode<Dict, String> {

  static final String TAG = "tag";
  static final String CODE = "code";

  @Id
  @Column(name = "id", nullable = false, updatable = false, length = DatabaseConstant.ID_LENGTH)
  @GenericGenerator(
      name = HibernateIdGenerator.GENERATOR_NAME,
      type = HibernateIdGenerator.class
  )
  @GeneratedValue(generator = HibernateIdGenerator.GENERATOR_NAME)
  private String id;
  /**
   * 字典目
   */
  @Column(name = TAG, nullable = false, updatable = false)
  @NotNull(message = "字典目不能为空")
  private String tag;
  /**
   * 字典值
   */
  @NotNull(message = "字典编码不能为空")
  @Pattern(regexp = "[\\w-]+", message = "字典编码只允许英文字母、下划线、短横线")
  @Column(name = CODE, nullable = false, updatable = false)
  private String code;
  /**
   * 字典描述
   */
  @Column(name = "label")
  private String label;
  /**
   * 排序号
   */
  @Column(name = "ordinal", nullable = false)
  private Integer ordinal;
  /**
   * 上级字典ID
   */
  @Column(name = "parent_id", nullable = false, updatable = false)
  private String parentId;

  @Transient
  private List<Dict> children;

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
