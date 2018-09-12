package serviceImpl;

import org.springframework.stereotype.Service;

import service.SearchKeyword;

@Service
public class DefaultSearchKeyword implements SearchKeyword {

	@Override
	public String testMethod() {
		
		return "TEST";
	}

}
