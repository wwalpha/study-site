package com.alpha.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.bean.ResultBean;
import com.alpha.bean.UpdateBean;
import com.alpha.bean.WordBean;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WordCtrl {

	@RequestMapping("/users")
	public ResponseEntity<List<String>> users() {
		return ResponseEntity.ok(WordUtils.getUsers());
	}

	@RequestMapping("/{user}/{type}/playlist")
	public ResponseEntity<List<String>> playlist(@PathVariable String user, @PathVariable String type) {
		return null;
	}

	@RequestMapping("/{user}/{type}/nextpage")
	public ResponseEntity<List<WordBean>> nextPage(@PathVariable String user, @PathVariable String type) {
		return ResponseEntity.ok(WordUtils.getNextList(user, type));
	}

	@RequestMapping(value = "/{user}/{type}/save", method = RequestMethod.POST)
	public ResponseEntity<List<WordBean>> save(@PathVariable String user, @PathVariable String type,
			@RequestBody List<UpdateBean> reqlist) {
		WordUtils.save(user, reqlist);

		return ResponseEntity.ok(WordUtils.getNextList(user, type));
	}

	@RequestMapping(value = "/{user}/{type}/download", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<String>> download(@PathVariable String user, @PathVariable String type) {
		List<String> list = new ArrayList<String>();
		list.add(WordUtils.download(user));

		return ResponseEntity.ok(list);
	}

	@RequestMapping(value = "/{user}/upload", method = RequestMethod.POST)
	public HttpStatus upload(@PathVariable String user, @RequestBody Object fileData) {
		WordUtils.upload(user, null);

		return HttpStatus.OK;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public ResponseEntity<ResultBean> settings(MultipartFile file) {
		boolean result = WordUtils.updateSettings(file);

		return result ? ResponseEntity.ok(new ResultBean(true)) : ResponseEntity.noContent().build();
	}
}
