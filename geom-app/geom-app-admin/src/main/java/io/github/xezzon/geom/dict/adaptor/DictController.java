package io.github.xezzon.geom.dict.adaptor;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.service.DictService;
import io.github.xezzon.tao.logger.LogRecord;
import io.github.xezzon.tao.retrieval.CommonQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@RestController
@RequestMapping("/dict")
public class DictController {

  private final transient DictService dictService;

  public DictController(DictService dictService) {
    this.dictService = dictService;
  }

  /**
   * 分页查询字典目
   */
  @GetMapping("/tag")
  @LogRecord
  public Page<Dict> dictTagPage(CommonQuery params) {
    return dictService.dictTagPage(params);
  }

  /**
   * 新增字典/字典目
   * @param dict 字典信息
   */
  @PostMapping("")
  @LogRecord
  public void addDict(@RequestBody @Validated Dict dict) {
    dictService.addDict(dict);
  }

  /**
   * 查询字典目下的字典集合
   */
  @GetMapping("/{tag}")
  @LogRecord
  public List<Dict> dictListByTag(@PathVariable String tag) {
    return dictService.dictListByTag(tag);
  }

  /**
   * 通过字典目与字典码查询字典
   * @param tag 字典目
   * @param code 字典码
   * @return 字典信息
   */
  @GetMapping("/{tag}/{code}")
  @LogRecord
  public Dict dictByTagAndCode(@PathVariable String tag, @PathVariable String code) {
    return null;
  }
}
