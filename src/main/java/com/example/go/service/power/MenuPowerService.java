package com.example.go.service.power;

import com.example.go.mapper.MenuPowerMapper;
import com.example.go.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuPowerService {

    @Autowired
    private MenuPowerMapper menuPowerMapper;
    
    //根据状态生成菜单
    public List<Menu> getMenu(int status){
        //查询没有子菜单的菜单
        List<Menu> oneMenu = menuPowerMapper.getOneMenu(status);
        //查询有子菜单的菜单
        List<Menu> twoMenu = menuPowerMapper.getTwoMenu(status);
        for(Menu menu : twoMenu){
            menu.setChildren(menuPowerMapper.getChildren(menu.getId()));
        }
        List<Menu> menus = new ArrayList<>();
        menus.addAll(oneMenu);
        menus.addAll(twoMenu);
        return menus;
    }
}
