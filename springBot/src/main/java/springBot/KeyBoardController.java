package springBot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.ImmutableList;

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
		KeyboardVO keyboardVO = new KeyboardVO(new String[] { "기능" });
		
		return keyboardVO;
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public ResponseMessageVO message(@RequestBody RequestMessageVO vo) {
		ResponseMessageVO res_vo = new ResponseMessageVO();
		MessageVO mes_vo = new MessageVO();

		if (vo.getContent().equals("기능"))
		{
			
			String resultMessage = "번역";
			
			mes_vo.setText(resultMessage);
			
		} else if (vo.getContent().substring(0, 2).equals("번역"))
		{
			String nat = "en";
			final String natCode = vo.getContent().substring(3, 5);
			final List<String> natCodeList = new ArrayList<String>();
			natCodeList.add("ko");
			natCodeList.add("en");
			natCodeList.add("fr");
			natCodeList.add("zh");
			natCodeList.add("ja");
			natCodeList.add("en");
			natCodeList.add("es");
			natCodeList.add("vi");
			natCodeList.add("ru");
			natCodeList.add("de");
			
			if(natCodeList.contains(natCode))
			{
				nat = natCode;
				if(!vo.getContent().substring(6).isEmpty() && !(vo.getContent().substring(6).length() < 1))
				{
					mes_vo.setText(autoDetectTranslate(vo.getContent().substring(6), nat));
				}else 
				{
					mes_vo.setText("번역기능:\n ex) 번역 (default:en) input word..\n 지원언어:ko,en,fr,zh,ja,en,es,vi,ru,de");
				}
			}else
			{
				if(!vo.getContent().substring(2).isEmpty() && !(vo.getContent().substring(2).length() < 1))
				{
					mes_vo.setText(autoDetectTranslate(vo.getContent().substring(2), nat));
				}else 
				{
					mes_vo.setText("번역기능:\n ex) 번역 (default:en) input word..\n 지원언어:ko,en,fr,zh,ja,en,es,vi,ru,de");
				}
			}
			
		}
		else // 에코메시지
		{
			
			mes_vo.setText(vo.getContent()+"\n '기능' 이라고 물어봐");
			
		}

		res_vo.setMessage(mes_vo);
		return res_vo;
	}
	

	/////////////////////////////////////////////////
	///////     번역 기능 start 				  ///////
	/////////////////////////////////////////////////
    private static Translate getTranslateService() {
        return TranslateOptions.getDefaultInstance().getService();
    }

    private static String translate(String text, String source, String target) {
        Translate translate = getTranslateService();

        Translation translation = translate.translate(text,        // 바꾸고자 하는 텍스트
                Translate.TranslateOption.sourceLanguage(source),  // 원본 언어
                Translate.TranslateOption.targetLanguage(target)); // 번역할 언어

        return translation.getTranslatedText();
    }

    // 어떤 언어인지 감지를 해서 언어 코드를 반환합니다 예) 한국어 -> ko
    private static String detectLanguage(String text) {
        Translate translate = getTranslateService();
        List<Detection> detections = translate.detect(ImmutableList.of(text));

        String sourceLanguage = null;
        for (Detection detection : detections) {
            sourceLanguage = detection.getLanguage();
        }

        return sourceLanguage;
    }

    // https://jungwoon.github.io/google%20cloud/2018/01/03/Translation-Api/
    private static String autoDetectTranslate(String text, String target) {
        return translate(text, detectLanguage(text), target);
    }
	/////////////////////////////////////////////////
	///////     번역 기능 end  				  ///////
	/////////////////////////////////////////////////

	
	
	public SearchKeyword getSearchKeyWord() {
		return searchKeyword;
	}

	public void setSearchKeyWord(SearchKeyword searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
