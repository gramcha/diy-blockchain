/**
 * @author gramcha
 * 22-Dec-2017 2:40:37 PM
 * 
 */
package com.gramcha.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.Block;

@RestController
public class Index {
	@RequestMapping(value="/")
	public ResponseEntity<String> ping() throws Exception{
		return ResponseEntity.ok("pong - ");
	}
	
}
