package com.amore.springboot.explore.mapper;

import com.amore.springboot.explore.bean.App;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AppMapper {

    public List<App> getAllApp();

    public boolean insert(App app);

    public boolean batchInsert(List<App> apps);

}
