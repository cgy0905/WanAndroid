package com.cgy.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * @author: cgy
 * @date 2020/1/9 14:54
 * @description:
 */
data class ShareResponse (var coinInfo : CoinInfo, var shareArticles : ApiPagerResponse<MutableList<ArticleResponse>>) : Serializable
