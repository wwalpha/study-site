package com.alpha.word.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alpha.entity.WordEntity;
import com.alpha.tools.Constants.WordType;
import com.alpha.tools.XUtils;
import com.alpha.word.service.IWordService;

@Controller
@CrossOrigin
public class WordController {

	@Autowired
	private IWordService wordService;

	@RequestMapping("test")
	public ResponseEntity<Long> getTest() {
		return ResponseEntity.ok(wordService.countReviews("Alpha"));
	}

	/**
	 * user next type's word
	 * 
	 * @param user
	 * @param type
	 * @return
	 */
	@RequestMapping("{userId}/{type}/nextpage")
	public ResponseEntity<List<WordEntity>> nextPage(@PathVariable String userId, @PathVariable String type) {

		List<WordEntity> retList = new ArrayList<>();

		if (StringUtils.equals(type, WordType.New)) {
			retList = wordService.getNewwordList(userId);
		} else if (StringUtils.equals(type, WordType.Review)) {
			retList = wordService.getReviewList(userId);
		}

		return ResponseEntity.ok(XUtils.getRandomList(retList, 7));
	}

	/**
	 * user word save
	 *
	 * @param user
	 * @param type
	 * @param reqlist
	 * @return
	 */
	@PutMapping(value = "{userId}/{wordNo}")
	public ResponseEntity<Object> save(@PathVariable String userId, @PathVariable int wordNo, @RequestBody WordEntity entity) {
		wordService.save(entity);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "{userId}/{word}")
	public ResponseEntity<Object> reset(@PathVariable String userId, @PathVariable int wordNo) {
		wordService.reset(userId, wordNo);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	//
	// /**
	// * user playlist
	// *
	// * @param user
	// * @return
	// */
	// @RequestMapping("/{user}/playlist")
	// public ResponseEntity<List<PlayListBean>> playlist(@PathVariable String
	// user)
	// {
	// return ResponseEntity.ok(Utils.getPlayList(user));
	// }
}
