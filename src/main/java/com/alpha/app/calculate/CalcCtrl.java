package com.alpha.app.calculate;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.bean.CalculateBean;
import com.alpha.bean.ScoreBean;

@RestController
@CrossOrigin
public class CalcCtrl {

	@RequestMapping(value = "/addsingle", method = RequestMethod.GET)
	public ResponseEntity<CalculateBean> getAddSingle() {
		return ResponseEntity.ok(Utils.getAddSingle());
	}
	
	@RequestMapping(value = "/minsingle", method = RequestMethod.GET)
	public ResponseEntity<CalculateBean> getMinusSingle() {
		return ResponseEntity.ok(Utils.getMinusSingle());
	}

	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public ResponseEntity<String> answer(@RequestBody CalculateBean calcInfo) {
		Utils.updateResult(calcInfo);

		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/score", method = RequestMethod.GET)
	public ResponseEntity<List<ScoreBean>> score() {
		return ResponseEntity.ok(Utils.score());
	}
}
