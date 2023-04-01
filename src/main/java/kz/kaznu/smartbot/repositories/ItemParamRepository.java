package kz.kaznu.smartbot.repositories;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.ItemParam;
import kz.kaznu.smartbot.models.enums.Params;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ItemParamRepository extends JpaRepository<ItemParam, Long> {

    @Query("select ip.value from ItemParam ip join Param p on ip.param = p where ip.item = ?1 and p.name = ?2")
    String getParamValueByItem(Item item, Params param);

    @Query("select ip from ItemParam ip where ip.item = ?1")
    List<ItemParam> getAllParamsByItem(Item item);

    @Query("select i from ItemParam i where i.item = ?1 and i.orderValue in ?2")
    List<ItemParam> getItemParamsByOrderValue(Item item, List<Integer> orderValue);
}
