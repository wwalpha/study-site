package com.alpha.words.app;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.words.bean.PlayListBean;
import com.alpha.words.bean.StatisticBean;
import com.alpha.words.bean.UpdateBean;
import com.alpha.words.bean.UserBean;
import com.alpha.words.bean.WordBean;

@RestController
@CrossOrigin
public class WordCtrl {

	/**
	 * user list
	 * 
	 * @return
	 */
	@RequestMapping("/users")
	public ResponseEntity<List<String>> users() {
		return ResponseEntity.ok(Utils.getUsers());
	}

	/**
	 * user list
	 * 
	 * @return
	 */
	@RequestMapping("/{user}/userprops")
	public ResponseEntity<UserBean> userprops(@PathVariable String user) {
		return ResponseEntity.ok(Utils.getUserProps(user));
	}

	@RequestMapping("/{user}/statistic")
	public ResponseEntity<List<StatisticBean>> statistic(@PathVariable String user) {
		return ResponseEntity.ok(Utils.getStatistic(user));
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
			@RequestParam(value = "categories", required = false) String categories) {
		return ResponseEntity.ok(Utils.getNextList(user, type, categories));
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
			@RequestBody List<UpdateBean> reqlist,
			@RequestParam(value = "categories", required = false) String categories) {
		Utils.save(user, reqlist);

		return ResponseEntity.ok(Utils.getNextList(user, type, categories));
	}

	/**
	 * user playlist
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/{user}/playlist")
	public ResponseEntity<List<PlayListBean>> playlist(@PathVariable String user) {
		return ResponseEntity.ok(Utils.getPlayList(user));
	}
}
