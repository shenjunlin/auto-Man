package org.automan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.automan.model.WxbArticle;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
//        System.out.println("输入微小宝账号：");
//        Scanner scanner = new Scanner(System.in); // 输入键盘
//        String wxbName = scanner.next();
//        System.out.println("输入微小宝密码：");
//        String wxbPwd = scanner.next();
        WeixiaoBao weixiaoBao = new WeixiaoBao();
//        String cookie = weixiaoBao.loginWxbGetCookie(wxbName, wxbPwd);
//        System.out.println(cookie);
//        if (StringUtils.isEmpty(cookie)) {
//            System.out.println("登录微小宝失败");
//        } else {
//            // TODO: 2020/5/29
//           System.out.println("登录微小宝成功---->开始获取公众号列表");
//        }
        String cookie = "Hm_lvt_5859c7e2fd49a1739a0b0f5a28532d91=1590464401,1590550801,1590637201,1590723601; Hm_lpvt_5859c7e2fd49a1739a0b0f5a28532d91=1590723606; aliyungf_tc=AQAAAAIC9WcGLgEAranrMVDLLfXvbVW4; RMT=ec36cfb77eef23667eb3c86f5c096dbd; PHPSESSID=d1967394c3c8afcfaf41cd97f613b56b; RMU=153795";
        System.out.println("开始发送文章");
        Integer authorizerId = 8355; // TODO: 2020/5/29
        List<WxbArticle> wxbArticles = Lists.newArrayList();
        wxbArticles.add(WxbArticle.builder()
            .fileid("https://tvax2.sinaimg.cn/crop.0.0.1000.1000.180/8843eac0ly8fxmtlqd2akj20rs0rst9x.jpg")
            .title("热点GIF图，要你好笑")
            .content("    <p style=\"text-align: center;\">\n"
                + "        <img src=\"https://wx2.sinaimg.cn/large/79a00895ly1gf6szv1k6fg206o0a67wh.gif\" style=\"width: 60%;display: block;\"/>\n"
                + "    </p>\n"
                + "    <br>\n")
            .author("每天都好笑").digest("好好学习，天天向上").addHeader(0).addFooter(1).addGuide(0).build());

        weixiaoBao.postArticle(wxbArticles, authorizerId, cookie);
    }
}
