package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Menu;
import com.example.go.entity.MenuPower;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 菜单权限
 */
@Mapper
public interface MenuPowerMapper extends BaseMapper<MenuPower> {

    //获得没有子菜单的菜单
    List<Menu> getOneMenu(int status);
    //获得有子菜单的菜单
    List<Menu> getTwoMenu(int status);

    //获得一个菜单的子菜单
    List<Menu> getChildren(int id);
}
