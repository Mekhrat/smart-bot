package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.dto.ItemParamsDto;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.ItemParam;
import kz.kaznu.smartbot.models.enums.Params;
import kz.kaznu.smartbot.repositories.ItemParamRepository;
import kz.kaznu.smartbot.repositories.ItemRepository;
import kz.kaznu.smartbot.services.ParamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParamsServiceImpl implements ParamsService {
    private final ItemRepository itemRepository;
    private final ItemParamRepository paramRepository;

    @Override
    public List<String> getItemParamValuesByItem(Item item, Params param) {
        List<Item> items = itemRepository.getItemsByName(item.getName());
        Params filter = param.equals(Params.COLOR) ? Params.MEMORY : Params.COLOR;
        return items.stream().filter(i ->
                        paramRepository.getParamValueByItem(i, filter).equals(paramRepository.getParamValueByItem(item, filter)))
                .map(i -> {return paramRepository.getParamValueByItem(i, param);
                }).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getItemParamValuesByItem(String itemName, Params param) {
        List<Item> items = itemRepository.getItemsByName(itemName);
        return items.stream().map(i -> paramRepository.getParamValueByItem(i, param)).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getParamsByItem(Item item) {
        List<ItemParam> allParams = paramRepository.getAllParamsByItem(item);
        return allParams.stream().collect(
                Collectors.toMap(k -> k.getParam().getName().name(), ItemParam::getValue)
        );
    }

    @Override
    public Map<String, List<ItemParamsDto>> getAllSortedItemParams(Item item) {
        List<ItemParam> allParams = paramRepository.getAllParamsByItem(item);
        Map<String, List<ItemParamsDto>> params = new HashMap<>();

        allParams.forEach(i -> {
            List<ItemParamsDto> values = params.get(i.getParam().getType().getName());
            if (values == null) {
                params.put(i.getParam().getType().getName(), new ArrayList<>(Arrays.asList(new ItemParamsDto(i.getParam().getName().getName(), i.getValue()))));
            }
            else {
                values.add(new ItemParamsDto(i.getParam().getName().getName(), i.getValue()));
            }
        });
        return params;
    }

    @Override
    public List<ItemParamsDto> getMainParamsByItem(Item item) {
        return paramRepository.getItemParamsByOrderValue(item, Collections.singletonList(0)).stream()
                .map( i -> new ItemParamsDto(i.getParam().getName().getName(), i.getValue()))
                .collect(Collectors.toList());
    }
}


