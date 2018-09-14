package cn.ipanel.authorization.demo.dao;

import cn.ipanel.authorization.demo.entity.ManagerInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ManagerRepository extends CrudRepository<ManagerInfo,Integer >,JpaSpecificationExecutor {
    //根据姓名查询返回UserInfo实体
    List<ManagerInfo> findAllByUserName(String userName);
    //根据用户登录信息返回UserInfo实体
    ManagerInfo findUserInfoByUserNameAndPassword(String userName, String password);

}
