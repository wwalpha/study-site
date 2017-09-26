package com.alpha.calculate.app;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.calculate.bean.CalculateBean;
import com.alpha.calculate.bean.ScoreBean;

@RestController
@CrossOrigin
public class CalcCtrl {

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	public ResponseEntity<CalculateBean> next(@RequestParam("options") String options) {
		return ResponseEntity.ok(Utils.getNext(options));
	}
	
	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public ResponseEntity<String> answer(@RequestBody CalculateBean calcInfo) {
		Utils.updateResult(calcInfo);

		return ResponseEntity.noContent().build();
	}

//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	public ResponseEntity<CalculateBean> getAdd(@RequestParam("options") String options) {
//		return ResponseEntity.ok(Utils.getAddSingle());
//	}
//	
//	@RequestMapping(value = "/minus", method = RequestMethod.GET)
//	public ResponseEntity<CalculateBean> g(@RequestParam("options") String options) {
//		return ResponseEntity.ok(Utils.getMinusSingle());
//	}



	@RequestMapping(value = "/score", method = RequestMethod.GET)
	public ResponseEntity<List<ScoreBean>> score() {
		return ResponseEntity.ok(Utils.score());
	}
}
