package com.alpha.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.bean.CalculateBean;
import com.alpha.bean.PlayListBean;
import com.alpha.bean.UpdateBean;
import com.alpha.bean.UserBean;
import com.alpha.bean.WordBean;

@RestController
@CrossOrigin
public class WordCtrl {

	@RequestMapping(value = "/addsingle", method = RequestMethod.GET)
	public ResponseEntity<CalculateBean> getAddSingle() {
		return ResponseEntity.ok(WordUtils.getAddSingle());
	}

	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public ResponseEntity<String> postAddSingle(@RequestBody CalculateBean calcInfo) {
		WordUtils.updateResult(calcInfo);

		return ResponseEntity.noContent().build();
	}

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
	 * user list
	 * 
	 * @return
	 */
	@RequestMapping("/{user}/userprops")
	public ResponseEntity<UserBean> userprops(@PathVariable String user) {
		return ResponseEntity.ok(WordUtils.getUserProps(user));
	}

	/**
	 * user next type's word
	 * 
	 * @param user
	 * @param type
	 * @return
	 */
	@RequestMapping("/{user}/{type}/nextpage")
	public ResponseEntity<List<WordBean>> nextPage(@PathVariable String user, @PathVariable String type,
			@RequestParam(value = "categories") String categories) {
		return ResponseEntity.ok(WordUtils.getNextList(user, type, categories));
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
			@RequestBody List<UpdateBean> reqlist, @RequestParam(value = "categories") String categories) {
		WordUtils.save(user, reqlist);

		return ResponseEntity.ok(WordUtils.getNextList(user, type, categories));
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
	public ResponseEntity<String> upload(@PathVariable String user, MultipartFile file) {
		boolean result = WordUtils.upload(user, file);

		return result ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
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
		boolean result = false;// WordUtils3.updateSettings(file);

		return result ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
	}
}
