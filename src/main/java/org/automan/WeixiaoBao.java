package org.automan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.commons.compress.utils.Lists;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.automan.model.WxbArticle;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by shenjunlin on 2020/04/25 10:00 登录微小宝
 */
public class WeixiaoBao {

    public String loginWxbGetCookie(String userName, String password) {
        WebDriver driver = null;
        try {
            WebDriverManager.phantomjs().useMirror().forceCache().setup();
            driver = new PhantomJSDriver();
            driver.get("http://account.wxb.com/?from=http%3A%2F%2Fwww.wxb.com%2Fhome");
            Thread.sleep(2000);
            WebElement loginTypeBtn = driver.findElement(By.xpath("//div[@class='login-type-toggle___2e_eO']/span[2]"));
            loginTypeBtn.click();
            Thread.sleep(1000);
            WebElement userNameInput = driver.findElement(By.id("email"));
            userNameInput.sendKeys(userName);
            WebElement passElement = driver.findElement(By.id("password"));
            passElement.sendKeys(password);
            WebElement loginBtn = driver.findElement(By.xpath("//button[@type='button']"));
            loginBtn.click();
            Thread.sleep(5000);

            StringBuilder cookieStr = new StringBuilder();
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                cookieStr.append(cookie.getName()).append("=").append(cookie.getValue()).append(";").append(" ");
            }
            return cookieStr.substring(0, cookieStr.lastIndexOf(";"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return null;
    }

    /**
     * 获取已登录账号列表
     */
    // TODO: 2020/5/29

    public void postArticle(List<WxbArticle> wxbArticles, Integer authorizerId, String cookie)
        throws InterruptedException, IOException {
        String article = JSON.toJSONString(wxbArticles);
        postArticle(article, authorizerId, cookie);
    }

    private void postArticle(String article, Integer authorizerId, String cookie)
        throws IOException, InterruptedException {
        HttpPost post = new HttpPost("https://www.wxb.com/resarticle/updateNews");
        addHeader(post, cookie);
        List<NameValuePair> nameValuePairs = Lists.newArrayList();
        nameValuePairs.add(new BasicNameValuePair("article", article));
        nameValuePairs.add(new BasicNameValuePair("authorizer_id", authorizerId.toString()));
        nameValuePairs.add(new BasicNameValuePair("newsId", "0"));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        String responseJSON = executeRequest(post);
        System.out.println("发送文章到微小宝，结果：" + responseJSON);
        JSONObject jsonObject = JSONObject.parseObject(responseJSON);
        if (jsonObject.getInteger("errcode") != 0) {
            System.out.println("发送文章到微小宝失败");
            return;
        }
        String newsId = jsonObject.getString("newsId");
        System.out.println("发送文章到微小宝成功，newsId:" + newsId + "，开始同步文章到微信公众平台");
        Thread.sleep(2000);
        synchronizeToWxMp(cookie, newsId, authorizerId);
    }

    private void synchronizeToWxMp(String cookie, String newsId, Integer authorizerId)
        throws IOException {
        HttpPost post = new HttpPost("https://www.wxb.com/resarticle/synchronize");
        addHeader(post, cookie);
        List<NameValuePair> nameValuePairs = Lists.newArrayList();
        nameValuePairs.add(new BasicNameValuePair("type", "sync"));
        nameValuePairs.add(new BasicNameValuePair("newsId", newsId));
        nameValuePairs.add(new BasicNameValuePair("authorizerIds[]", authorizerId.toString()));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        String responseJSON = executeRequest(post);
        System.out.println("同步文章到微信公众平台，结果：" + responseJSON);
        JSONObject respObj = JSONObject.parseObject(responseJSON);
        if (respObj.getInteger("errcode") != 0) {
            System.out.println("同步文章到微信公众平台 失败");
            return;
        }
        System.out.println("同步文章到微信公众平台 成功");
    }

    private void addHeader(HttpRequestBase requestBase, String cookie) {
        requestBase.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestBase.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        requestBase.addHeader("Origin", "http://www.wxb.com");
        requestBase.addHeader("referer", "http://www.wxb.com/home");
        requestBase.addHeader("x-requested-with", "XMLHttpRequest");
        requestBase.addHeader("Host", "www.wxb.com");
        requestBase.addHeader("cookie", cookie);
    }

    private String executeRequest(HttpRequestBase requestBase) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpResponse response = httpClient.execute(requestBase);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                throw new RuntimeException("请求出错");
            }
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
