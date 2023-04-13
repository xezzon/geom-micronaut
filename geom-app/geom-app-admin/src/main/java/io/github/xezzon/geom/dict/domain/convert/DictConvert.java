package io.github.xezzon.geom.dict.domain.convert;

import io.github.xezzon.geom.dict.DictDTO;
import io.github.xezzon.geom.dict.domain.Dict;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Mapper
public interface DictConvert {

  DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);

  DictDTO toDTO(Dict dict);

  List<DictDTO> toDTOList(List<Dict> dictList);
}
