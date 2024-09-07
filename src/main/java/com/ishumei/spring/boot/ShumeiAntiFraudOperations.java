/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ishumei.spring.boot;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * 	数美接口集成
 * https://www.ishumei.com/help/documents.html?id=21110
 */
@Slf4j
public abstract class ShumeiAntiFraudOperations {

	public final static String APPLICATION_JSON_VALUE = "application/json";
	public final static String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
	public final static MediaType APPLICATION_JSON = MediaType.parse(APPLICATION_JSON_VALUE);
	public final static MediaType APPLICATION_JSON_UTF8 = MediaType.parse(APPLICATION_JSON_UTF8_VALUE);

	protected ShumeiAntiFraudTemplate template;

	public ShumeiAntiFraudOperations(ShumeiAntiFraudTemplate template) {
		this.template = template;
	}

	public ShumeiAntiFraudTemplate getTemplate() {
		return template;
	}

	public <T> T readValue(String json, Class<T> cls) {
		try {
			if(Objects.isNull(json)){
				return BeanUtils.instantiateClass(cls);
			}
			return JSONObject.parseObject(json, cls);
//			return getTemplate().getObjectMapper().readValue(json, cls);
		} catch (Exception e) {
			log.error(e.getMessage());
			return BeanUtils.instantiateClass(cls);
		}
	}

	public <T> T requestInvoke(String url, Object params, Class<T> cls) {
		long start = System.currentTimeMillis();
		T res = null;
		try {

			String paramStr = getTemplate().getObjectMapper().writeValueAsString(params);
			log.info("iShumei Request Param :  {}", paramStr);

			RequestBody requestBody = RequestBody.create(APPLICATION_JSON_UTF8, paramStr);
			Request request = new Request.Builder().url(url).post(requestBody).build();

			try(Response response = getTemplate().getOkhttp3Client().newCall(request).execute();) {
				if (response.isSuccessful()) {
					String body = response.body().string();
					log.info("iShumei Request Success : url : {}, params : {}, code : {}, body : {} , use time : {} ", url, paramStr, response.code(), body , System.currentTimeMillis() - start);
					res = this.readValue(body, cls);
				} else {
					log.error("iShumei Request Failure : url : {}, params : {}, code : {}, message : {}, use time : {} ", url, paramStr, response.code(), response.message(), System.currentTimeMillis() - start);
					res = BeanUtils.instantiateClass(cls);
				}
			}
		} catch (Exception e) {
			log.error("iShumei Request Error : url : {}, params : {}, use time : {} ,  {}", url, JSONObject.toJSONString(params), e.getMessage(), System.currentTimeMillis() - start);
			res = BeanUtils.instantiateClass(cls);
		}
		return res;
	}

}
