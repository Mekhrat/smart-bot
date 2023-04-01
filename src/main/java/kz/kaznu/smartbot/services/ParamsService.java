package kz.kaznu.smartbot.services;


import kz.kaznu.smartbot.models.dto.ItemParamsDto;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.enums.Params;

import java.util.List;
import java.util.Map;

public interface ParamsService {

    List<String> getItemParamValuesByItem(Item item, Params param);
    List<String> getItemParamValuesByItem(String itemName, Params param);
    Map<String, String> getParamsByItem(Item item);
    Map<String, List<ItemParamsDto>> getAllSortedItemParams(Item item);
    List<ItemParamsDto> getMainParamsByItem(Item item);
}
