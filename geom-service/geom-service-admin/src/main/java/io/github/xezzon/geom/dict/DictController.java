package io.github.xezzon.geom.dict;

import io.github.xezzon.geom.dict.domain.AddDictReq;
import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.domain.ModifyDictReq;
import io.github.xezzon.geom.domain.CommonQueryBean;
import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.tao.logger.LogRecord;
import io.github.xezzon.tao.tree.TreeNode;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.RequestBean;
import jakarta.validation.Valid;
import java.util.List;

/**
 * @author xezzon
 */
@Controller("/dict")
public class DictController {

  private final DictService dictService;

  public DictController(DictService dictService) {
    this.dictService = dictService;
  }

  /**
   * 分页查询字典目
   */
  @Get("/tag")
  @LogRecord
  public Page<Dict> dictTagPage(@RequestBean CommonQueryBean params) {
    return dictService.dictTagPage(params.into());
  }

  /**
   * 新增字典/字典目
   * @param dict 字典信息
   */
  @Post()
  @LogRecord
  public Id addDict(@Body @Valid AddDictReq req) {
    Dict dict = req.into();
    dictService.addDict(dict);
    return Id.of(dict.getId());
  }

  /**
   * 查询字典目下的字典集合
   */
  @Get()
  @LogRecord
  public List<? extends TreeNode<Dict, ?>> dictListByTag(@QueryValue String tag) {
    return dictService.dictListByTag(tag);
  }

  /**
   * 通过字典目与字典码查询字典
   * @param tag 字典目
   * @param code 字典码
   * @return 字典信息
   */
  @Get("/tag-code")
  @LogRecord
  public Dict dictByTagAndCode(@QueryValue String tag, @QueryValue String code) {
    return dictService.dictByTagAndCode(tag, code);
  }

  /**
   * 从字典服务中删除指定id的字典项
   * @param id 要删除的字典项的id
   */
  @Delete("/{id}")
  public void remove(@PathVariable String id) {
    dictService.removeDict(id);
  }

  /**
   * 修改字典项
   * @param dict 要修改的字典项对象
   * @return 返回修改后的字典项ID
   */
  @Put()
  public Id modifyDict(@Body @Valid ModifyDictReq req) {
    Dict dict = req.into();
    dictService.modifyDict(dict);
    return Id.of(dict.getId());
  }
}
