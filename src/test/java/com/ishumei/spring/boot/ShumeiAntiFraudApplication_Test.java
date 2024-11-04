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

import java.util.ArrayList;
import java.util.List;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishumei.spring.boot.model.AntiFraudResponse;
import com.ishumei.spring.boot.model.BatchAntiFraudImageResponse;

@SpringBootApplication
public class ShumeiAntiFraudApplication_Test {
	
	@Autowired
	private ShumeiAntiFraudTemplate template;
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void testText() {

		try {

			AntiFraudResponse response = template.opsForText().antiFraud("SOCIAL", "00001", "你妈的");
			System.out.println(objectMapper.writeValueAsString(response));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@PostConstruct
	public void testImage() {

		try {
			
			List<String> imgs = new ArrayList<String>();

			imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603775417273&di=d07584c12801a0a097634667ba944eb2&imgtype=0&src=http%3A%2F%2Fimg011.hc360.cn%2Fhb%2FMTQ2MDYwODAzMjY0ODE5NzM2NTMxMDE%3D.jpg");

			BatchAntiFraudImageResponse response2 = template.opsForImage().antiFraud("POLITICS_PORN_AD", "00001", imgs);
			System.out.println(objectMapper.writeValueAsString(response2));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ShumeiAntiFraudApplication_Test.class, args);
	}

}
