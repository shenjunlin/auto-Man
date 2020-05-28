package org.automan;

import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        System.out.println("输入微小宝账号：");
        Scanner scanner = new Scanner(System.in); // 输入键盘
        String wxbName = scanner.next();
        System.out.println("输入微小宝密码：");
        String wxbPwd = scanner.next();
        String cookie = WeixiaoBao.loginWxbGetCookie(wxbName, wxbPwd);
        if (StringUtils.isEmpty(cookie)) {
            System.out.println("登录微小宝失败");
        } else {
           System.out.println("登录微小宝成功---->开始获取公众号列表");
        }
        
    }
}
