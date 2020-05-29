package org.automan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.automan.model.WxbArticle;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        System.out.println("输入微小宝账号：");
        Scanner scanner = new Scanner(System.in); // 输入键盘
        String wxbName = scanner.next();
        System.out.println("输入微小宝密码：");
        String wxbPwd = scanner.next();
        WeixiaoBao weixiaoBao = new WeixiaoBao();
        String cookie = weixiaoBao.loginWxbGetCookie(wxbName, wxbPwd);
        if (StringUtils.isEmpty(cookie)) {
            System.out.println("登录微小宝失败");
        } else {
            // TODO: 2020/5/29  
           System.out.println("登录微小宝成功---->开始获取公众号列表");
        }

        Integer authorizerId = 8355; // TODO: 2020/5/29
        WxbArticle wxbArticle = WxbArticle.builder().author("这里是作者")
            .content("这里是内容")
            .fileid("https://ws3.sinaimg.cn/large/005BYqpggy1fyx997vj3bj30c805zwev.jpg")
            .digest("这里是摘要")
            .title("这里是标题")
            .build();

        weixiaoBao.postOneArticle(wxbArticle, authorizerId, cookie);
    }
}
