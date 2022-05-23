package edu.sau.NetworkLotterySystem.mapper;

import edu.sau.NetworkLotterySystem.entity.PageData;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    void saveUser(PageData pd);

    int findNumByPhone(String phone);

    void insertUserByPhone(PageData pd);
}
