package au.com.myarticles.news.common.network.services

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest

open class NetworkServiceRequest(method: Int,
                                 url: String?,
                                 listener: Response.Listener<String>?,
                                 errorListener: Response.ErrorListener?) :
        StringRequest(method, url, listener, errorListener) {

    var statusCode: Int? = null
    var responseTime: Long? = null
    var responseHeaders: Map<String, String>? = null

    override fun parseNetworkResponse(response: NetworkResponse?): Response<String>? {
        statusCode = response?.statusCode
        responseTime = response?.networkTimeMs
        responseHeaders = response?.headers
        return super.parseNetworkResponse(response)
    }

    override fun parseNetworkError(volleyError: VolleyError?): VolleyError? {
        statusCode = volleyError?.networkResponse?.statusCode
        responseTime = volleyError?.networkResponse?.networkTimeMs
        responseHeaders = volleyError?.networkResponse?.headers

        return super.parseNetworkError(volleyError)
    }

}

