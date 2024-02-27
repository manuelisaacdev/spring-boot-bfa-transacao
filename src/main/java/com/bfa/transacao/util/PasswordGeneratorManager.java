package com.bfa.transacao.util;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

@Component
public class PasswordGeneratorManager {
	private final PasswordGenerator passwordGenerator;
	
	public PasswordGeneratorManager() {
		this.passwordGenerator = new PasswordGenerator();
	}
	
	public static PasswordGeneratorManager builder() {
		return new PasswordGeneratorManager();
	}
	
	public String generate(int length, int variation) {
		CharacterRule lcr = new CharacterRule(EnglishCharacterData.LowerCase);  
        lcr.setNumberOfCharacters(variation);
  
        CharacterRule ucr = new CharacterRule(EnglishCharacterData.UpperCase);  
        ucr.setNumberOfCharacters(variation);
        
		return passwordGenerator.generatePassword(length, lcr, ucr);
	}
	
	public String generateOnlyDigits(int length, int variation) {
  
        CharacterRule dr = new CharacterRule(EnglishCharacterData.Digit);  
        dr.setNumberOfCharacters(variation); 
        
		return passwordGenerator.generatePassword(length, dr);
	}
	
	public String generateWithDigit(int length, int variation) {
		CharacterRule lcr = new CharacterRule(EnglishCharacterData.LowerCase);  
        lcr.setNumberOfCharacters(variation);
  
        CharacterRule ucr = new CharacterRule(EnglishCharacterData.UpperCase);  
        ucr.setNumberOfCharacters(variation);
  
        CharacterRule dr = new CharacterRule(EnglishCharacterData.Digit);  
        dr.setNumberOfCharacters(variation); 
        
		return passwordGenerator.generatePassword(length, lcr, ucr, dr);
	}
	
	public String generateWithSpecial(int length, int variation) {
		CharacterRule lcr = new CharacterRule(EnglishCharacterData.LowerCase);  
        lcr.setNumberOfCharacters(variation);
  
        CharacterRule ucr = new CharacterRule(EnglishCharacterData.UpperCase);  
        ucr.setNumberOfCharacters(variation);
  
        CharacterRule sr = new CharacterRule(EnglishCharacterData.Special);  
        sr.setNumberOfCharacters(variation); 
        
		return passwordGenerator.generatePassword(length, lcr, ucr, sr);
	}
	
	public String generateWithDigitAndSpecial(int length, int variation) {
		CharacterRule lcr = new CharacterRule(EnglishCharacterData.LowerCase);  
        lcr.setNumberOfCharacters(variation);
  
        CharacterRule ucr = new CharacterRule(EnglishCharacterData.UpperCase);  
        ucr.setNumberOfCharacters(variation);
  
        CharacterRule dr = new CharacterRule(EnglishCharacterData.Digit);  
        dr.setNumberOfCharacters(variation); 
  
        CharacterRule sr = new CharacterRule(EnglishCharacterData.Special);  
        sr.setNumberOfCharacters(variation); 
        
		return passwordGenerator.generatePassword(length, lcr, ucr, dr, sr);
	}
}
