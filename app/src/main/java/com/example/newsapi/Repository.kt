package com.example.newsapi

import java.lang.reflect.ParameterizedType

open class Repository<E>(private val endpointService: EndpointService) {

    fun getEndpoint(): E {
        return endpointService[endpointClass()]
    }

    @Suppress("UNCHECKED_CAST")
    private fun endpointClass(): Class<E> {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        return types[0] as Class<E>
    }
}