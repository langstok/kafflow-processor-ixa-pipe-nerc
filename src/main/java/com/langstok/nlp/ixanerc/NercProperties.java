package com.langstok.nlp.ixanerc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.langstok.nlp.ixanerc.enumeration.DictTag;
import com.langstok.nlp.ixanerc.enumeration.Language;

@ConfigurationProperties("module.ixanerc")
public class NercProperties {
	
	/**
	 * "Choose language "de", "en", "es", "eu", "it", "nl"
	 */
	Language language = Language.en;
	
	/**
	 * Pass the model to do the tagging as a parameter
	 */
	String model;
	
	/**
	 * Use lexer rules 'numeric' for NERC tagging; it defaults to false.
	 */
	String lexer = "off";
	
	/**
	 * 	"Choose to directly tag entities by dictionary look-up; 
	 *  if option 'tag' is chosen, only tags entities found in the dictionary; 
	 *  if option 'post' is chosen, it will post-process the results of the statistical model.
	 */
	DictTag dictTag = DictTag.off;
	
	/**
	 * 	"Provide the path to the dictionaries for direct dictionary tagging; it ONLY WORKS if --dictTag option is activated.;
	 */
	String dictPath = "off";
	
	/**
	 * "Reset the adaptive features every sentence; defaults to 'no'; if -DOCSTART- marks are present, choose 'docstart'.
	 */
	String clearFeatures = "no";
	
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLexer() {
		return lexer;
	}
	public void setLexer(String lexer) {
		this.lexer = lexer;
	}
	public DictTag getDictTag() {
		return dictTag;
	}
	public void setDictTag(DictTag dictTag) {
		this.dictTag = dictTag;
	}
	public String getDictPath() {
		return dictPath;
	}
	public void setDictPath(String dictPath) {
		this.dictPath = dictPath;
	}
	public String getClearFeatures() {
		return clearFeatures;
	}
	public void setClearFeatures(String clearFeatures) {
		this.clearFeatures = clearFeatures;
	}
	
}
