package io.github.xezzon.geom.dict.adaptor.rpc;

import io.github.xezzon.geom.dict.DictDTO;
import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.domain.convert.DictConvert;
import io.github.xezzon.geom.dict.service.DictService;
import io.github.xezzon.tao.logger.LogRecord;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@RestController
@RequestMapping("/rpc/dict")
public class DictServer {

  private final transient DictService dictService;

  public DictServer(DictService dictService) {
    this.dictService = dictService;
  }


  /**
   * 查询字典目下的字典集合
   */
  @GetMapping("/{tag}")
  @LogRecord
  public List<DictDTO> dictListByTag(@PathVariable String tag) {
    List<Dict> treeNodes = dictService.dictListByTag(tag);
    return DictConvert.INSTANCE.toDTOList(treeNodes);
  }

  /**
   * 通过字典目与字典码查询字典
   * @param tag 字典目
   * @param code 字典码
   * @return 字典信息
   */
  @GetMapping("/{tag}/{code}")
  @LogRecord
  public DictDTO dictByTagAndCode(@PathVariable String tag, @PathVariable String code) {
    Dict dict = dictService.dictByTagAndCode(tag, code);
    return DictConvert.INSTANCE.toDTO(dict);
  }
}
