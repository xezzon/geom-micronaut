package io.github.xezzon.geom.group;

import io.github.xezzon.geom.group.domain.Group;
import io.github.xezzon.geom.group.domain.QGroup;
import io.github.xezzon.geom.group.repository.GroupRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;

/**
 * @author xezzon
 */
@Repository
public class GroupDAO extends BaseJpaWrapper<Group, QGroup, GroupRepository> {

  protected GroupDAO(GroupRepository repository) {
    super(repository);
  }

  @Override
  protected QGroup getQuery() {
    return QGroup.group;
  }

  @Override
  protected Class<Group> getBeanClass() {
    return Group.class;
  }
}
