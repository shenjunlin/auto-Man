package org.automan.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by shenjunlin on 2020/05/29 10:19
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WxbArticle {

    /**
     * 应该是封面
     */
    private String fileid;

    private String title;

    private String content;

    private String author;

    private String digest;

    @JSONField(name = "show_cover_pic")
    private String showCoverPic = "";

    @JSONField(name = "add_header")
    private Integer addHeader = 0;

    @JSONField(name = "add_footer")
    private Integer addFooter = 0;

    @JSONField(name = "add_guide")
    private Integer addGuide = 0;

    @JSONField(name = "sourceurl")
    private String sourceUrl = "";

    @JSONField(name = "thumb_media_id")
    private String thumbMediaId = "";

    @JSONField(name = "is_sourceurl")
    private Boolean isSourceurl;
}
