package clearNLP;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;

public class ClearNLP {
	final String language = AbstractReader.LANG_EN;
	AbstractComponent tagger ,parser,identifier, classifier ,labeler;
	
	public ClearNLP(String modelType){
		try {
			tagger     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
			parser     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
			identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
			classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);			
			labeler    = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		} catch (IOException e) {
			System.out.println("Error during CLearNLP initialisation");
			e.printStackTrace();
		}		
	}
	
	
	public void dependencyTreeParser(List<String> sentence, BufferedWriter bufferedWriter) {
		try {		
			DEPTree tree = NLPGetter.toDEPTree(sentence);
			tagger.process(tree);
			parser.process(tree);
			identifier.process(tree);
			classifier.process(tree);
			labeler.process(tree);
			bufferedWriter.write(tree.toStringSRL());
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			bufferedWriter.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
