package springBot;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import service.SearchKeyword;

@RestController
public class KeyBoardController {
	
	private SearchKeyword searchKeyword;
	
	@RequestMapping("/hello")
	public String hello() {

		return "/WEB-INF/views/keyboard.jsp";
	}

	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	@ResponseBody
	public KeyboardVO keyboard() {
		KeyboardVO keyboardVO = new KeyboardVO(new String[] { "로또", "테스트" });

		return keyboardVO;
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public ResponseMessageVO message(@RequestBody RequestMessageVO vo) {
		ResponseMessageVO res_vo = new ResponseMessageVO();
		MessageVO mes_vo = new MessageVO();

		if (vo.getContent().equals("메인화면") || vo.getContent().equals("시작")) {
			mes_vo.setText("유니봇 시작");

			KeyboardVO keyboard = new KeyboardVO(new String[] { "로또", "테스트" });
			res_vo.setKeyboard(keyboard);
		} else if (vo.getContent().contains("gomgom") || vo.getContent().contains("GOMGOM")
				|| vo.getContent().contains("곰")) {
			MessageButtonVO messageButton = new MessageButtonVO();
			PhotoVO photo = new PhotoVO();

			messageButton.setLabel("GOMGOM");
			messageButton.setUrl("https://www.idus.com/w/artist/bcba0277-af71-40f6-b230-381068bbc39f");

			photo.setUrl("https://image.idus.com/image/files/df7c53b0e4574265affa658a89ea353a_320.jpg");
			photo.setWidth(640);
			photo.setHeight(480);

			mes_vo.setMessage_button(messageButton);
			mes_vo.setPhoto(photo);
			mes_vo.setText("아이디어스 작가 : GOMGOM");
		} else if (vo.getContent().equals("병윤로또"))
		{
			MessageButtonVO messageButton = new MessageButtonVO();
			PhotoVO photo = new PhotoVO();
			
			messageButton.setLabel("지난로또 당첨번호");
			messageButton.setUrl("http://www.nlotto.co.kr/gameResult.do?method=byWin");
			
			photo.setUrl("http://www.nlotto.co.kr/img/contents/index/event/main_banner/1424043086173.jpg");
			photo.setWidth(640);
			photo.setHeight(480);
			
			mes_vo.setMessage_button(messageButton);
			mes_vo.setPhoto(photo);
			mes_vo.setText("7 13 20 26 33 39 | 22");
		} else if (vo.getContent().contains("테스트1"))
		{
			String resultMessage = "null Point";
			
			try {
				resultMessage = searchKeyword.testMethod();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			mes_vo.setText(resultMessage);
		}
		else // 에코메시지
		{
			mes_vo.setText(vo.getContent());
		}

		res_vo.setMessage(mes_vo);
		return res_vo;
	}
	
	
	public SearchKeyword getSearchKeyWord() {
		return searchKeyword;
	}

	public void setSearchKeyWord(SearchKeyword searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
