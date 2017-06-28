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

import com.alpha.bean.PlayListBean;
import com.alpha.bean.UpdateBean;
import com.alpha.bean.WordBean;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WordCtrl {

	/**
	 * user list
	 * 
	 * @return
	 */
	@RequestMapping("/users")
	public ResponseEntity<List<String>> users() {
		return ResponseEntity.ok(WordUtils.getUsers());
	}

	/**
	 * user next type's word
	 * 
	 * @param user
	 * @param type
	 * @return
	 */
	@RequestMapping("/{user}/{type}/nextpage")
	public ResponseEntity<List<WordBean>> nextPage(@PathVariable String user, @PathVariable String type) {
		return ResponseEntity.ok(WordUtils.getNextList(user, type));
	}

	/**
	 * user word save
	 * 
	 * @param user
	 * @param type
	 * @param reqlist
	 * @return
	 */
	@RequestMapping(value = "/{user}/{type}/save", method = RequestMethod.POST)
	public ResponseEntity<List<WordBean>> save(@PathVariable String user, @PathVariable String type,
			@RequestBody List<UpdateBean> reqlist) {
		WordUtils.save(user, reqlist);

		return ResponseEntity.ok(WordUtils.getNextList(user, type));
	}

	/**
	 * user download
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/{user}/download", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<String>> download(@PathVariable String user) {
		List<String> list = new ArrayList<String>();
		list.add(WordUtils.download(user));

		return ResponseEntity.ok(list);
	}

	/**
	 * user upload
	 * 
	 * @param user
	 * @param fileData
	 * @return
	 */
	@RequestMapping(value = "/{user}/upload", method = RequestMethod.POST)
	public HttpStatus upload(@PathVariable String user, @RequestBody Object fileData) {
		WordUtils.upload(user, null);

		return HttpStatus.OK;
	}

	/**
	 * user playlist
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/{user}/playlist")
	public ResponseEntity<List<PlayListBean>> playlist(@PathVariable String user) {
		return ResponseEntity.ok(WordUtils.getPlayList(user));
	}

	/**
	 * settings
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public ResponseEntity<String> settings(MultipartFile file) {
		boolean result = WordUtils.updateSettings(file);

		return result ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
	}
}
