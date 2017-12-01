package edu.buaa.test;

import java.io.IOException;

import jeasy.analysis.MMAnalyzer;

public class Test {

	public static void main(String[] args) throws Exception {
		String str = "原标题：书记、市长爬窗户上下班的背后9日，《人民日报》刊发《“有了全面从严治党，我们才起死回生”——对一基层单位全面从严治党的样本分析》一文。该文讲述了吉林省四平市科学技术研究院20余年来，由盛转衰、由衰到乱、再由乱到治的生死起伏。文章披露";

		// 建立分词对象
		MMAnalyzer mm = new MMAnalyzer();
		// 执行分词操作
		String result = mm.segment(str, "|");

		System.out.println(result);

	}

}
