package io.github.xezzon.geom.auth.adaptor;

import io.github.xezzon.geom.auth.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@RestController
@RequestMapping("/group")
public class GroupController {

  private final transient GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }
}
