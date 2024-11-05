package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.model.PerceptronCWSInstance;
import com.hankcs.model.PerceptronNERInstance;
import com.hankcs.model.PerceptronPOSInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: NLP分析器
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class HanLPNLPAnalyzer extends Analyzer {

    private static final Logger logger = LogManager.getLogger(HanLPNLPAnalyzer.class);

    /**
     * 分词配置
     */
    private final Configuration configuration;

    public HanLPNLPAnalyzer(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(
                TokenizerBuilder.tokenizer(
                        AccessController.doPrivileged((PrivilegedAction<Segment>) () -> {
                            try {
                                return new PerceptronLexicalAnalyzer(
                                        PerceptronCWSInstance.getInstance().getLinearModel(),
                                        PerceptronPOSInstance.getInstance().getLinearModel(),
                                        PerceptronNERInstance.getInstance().getLinearModel()
                                );
                            } catch (Exception e) {
                                logger.warn("can not use nlp analyzer, provider default");
                                return HanLP.newSegment();
                            }
                        }), configuration));
    }
}
