package cn.dblearn.blog.manage.sys.service.impl;

import cn.dblearn.blog.common.pojo.Constants;
import cn.dblearn.blog.manage.sys.mapper.SysMenuMapper;
import cn.dblearn.blog.manage.sys.mapper.SysUserMapper;
import cn.dblearn.blog.manage.sys.pojo.entity.SysMenu;
import cn.dblearn.blog.manage.sys.pojo.entity.SysUser;
import cn.dblearn.blog.manage.sys.pojo.entity.SysUserToken;
import cn.dblearn.blog.manage.sys.service.ShiroService;
import cn.dblearn.blog.manage.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ShiroServiceImpl
 *
 * @author bobbi
 * @date 2018/10/08 19:58
 * @email 571002217@qq.com
 * @description Shiro Service实现类
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 获取用户的所有权限
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserPermissions(Integer userId) {
         List<String> permsList;

         //系统管理员，拥有最高权限
        if(Constants.SUPER_ADMIN.equals(userId)){
            List<SysMenu> menuList=sysMenuMapper.selectList(null);
            permsList=new ArrayList<>(menuList.size());
            menuList.forEach(menu ->  permsList.add(menu.getPerms()));
        }else {
            permsList = sysUserMapper.queryAllPerms(userId);
        }
        //返回用户权限列表
        return permsList.stream()
                // 过滤空置的字符串
                .filter(perms -> !StringUtils.isEmpty(perms))
                // 把小的list合并成大的list
                .flatMap(perms -> Arrays.stream(perms.split(",")))
                // 转换成set集合
                .collect(Collectors.toSet());
    }

    /**
     * 查询token
     *
     * @param token
     * @return
     */
    @Override
    public SysUserToken queryByToken(String token) {
        return sysUserTokenService.queryByToken(token);
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public SysUser queryUser(Integer userId) {
        return sysUserMapper.selectById(userId);
    }
}
