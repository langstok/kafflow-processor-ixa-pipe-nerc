package com.langstok.nlp.ixanerc;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.google.common.io.Files;

import eus.ixa.ixa.pipe.nerc.Annotate;
import eus.ixa.ixa.pipe.nerc.CLI;
import ixa.kaflib.KAFDocument;


@Service
@EnableConfigurationProperties(NercProperties.class)
public class IxaNercService {

	private final static Logger LOGGER = Logger.getLogger(IxaNercService.class);

	@Autowired
	NercProperties properties;

	private final String version = CLI.class.getPackage().getImplementationVersion();
	private final String commit = CLI.class.getPackage().getSpecificationVersion();

	private Annotate annotator;


	public KAFDocument transform(KAFDocument document){
		LOGGER.info("IXANERC started, publicId: " 
				+ document.getPublic().publicId + ", uri " + document.getPublic().uri);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			LOGGER.info("Tokenize: " + document.getFileDesc().title);
			document = annotate(document);
		} catch (IOException e) {
			LOGGER.error("IOException for document " + document.getPublic().publicId, e);
		} catch (JDOMException e) {
			LOGGER.error("JDOMException for document " + document.getPublic().publicId , e);
		}
		stopWatch.stop();
		LOGGER.info("IXANERC finished in time:" 
				+ stopWatch.getTotalTimeMillis() + " ms for publicId: " + document.getPublic().publicId);
		return document;
	}

	public final KAFDocument annotate(KAFDocument kaf) throws IOException, JDOMException	{

		KAFDocument.LinguisticProcessor newLp = kaf.addLinguisticProcessor("entities",
				new StringBuilder().append("ixa-pipe-nerc-").append(Files.getNameWithoutExtension(properties.model)).toString(),
				new StringBuilder().append(this.version).append("-").append(this.commit).toString());
		newLp.setBeginTimestamp();
		annotator.annotateNEs(kaf);
		newLp.setEndTimestamp();

		return kaf;
	}
	
	@PostConstruct
	private void init() throws IOException{
		String model = properties.getModel();
		String lexer = properties.getLexer();
		String dictTag = String.valueOf(properties.getDictTag());
		String dictPath = properties.getDictPath();
		String clearFeatures = properties.getClearFeatures();
		String lang = String.valueOf(properties.getLanguage());
		
		Properties properties = setAnnotateProperties(model, lang, lexer, dictTag, dictPath, clearFeatures);
		this.annotator = new Annotate(properties);
	}



	private Properties setAnnotateProperties(String model, String language, String lexer, String dictTag,
			String dictPath, String clearFeatures)
	{
		Properties annotateProperties = new Properties();
		annotateProperties.setProperty("model", model);
		annotateProperties.setProperty("language", language);
		annotateProperties.setProperty("ruleBasedOption", lexer);
		annotateProperties.setProperty("dictTag", dictTag);
		annotateProperties.setProperty("dictPath", dictPath);
		annotateProperties.setProperty("clearFeatures", clearFeatures);
		return annotateProperties;
	}

}
