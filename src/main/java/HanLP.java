import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.io.*;

public class HanLP {

    /**
     * 分类文件夹路径
     * --|_语料分类
     * ----|_军事
     * ------|_1.txt
     * ------|_2.txt
     * ----|_体育
     * ------|_1.txt
     * ------|_2.txt
     */
    final static String CATEGORY_FOLDER_PATH = "";

    public static void main(String[] args) throws IOException {
        // 获取分类模型
        NaiveBayesModel model = trainOrLoadModel(CATEGORY_FOLDER_PATH);
        IClassifier classifier = new NaiveBayesClassifier(model);
        // 需要分类的内容
        String text = "坦克";
        System.out.println("结果：" + classifier.classify(text) + "，分析内容：" + text);
    }

    /**
     * 训练分类模型
     *
     * @param categoryFolderPath 分类文件夹路径
     * @return
     * @throws IOException
     */
    private static NaiveBayesModel trainOrLoadModel(String categoryFolderPath) throws IOException {
        // 分类模型文件
        File categoryModelFile = new File("classification-model.ser");
        // 模型文件路径
        String modelPath = categoryModelFile.getAbsolutePath();
        if (categoryModelFile.exists()) {
            System.out.println("已找到训练分类模型：" + modelPath);
            System.out.println("如需重新训练请删除：" + modelPath);
            System.out.println("正在分类");
            NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(categoryModelFile.getAbsolutePath());
            return model;
        } else {
            File categoryFolder = new File(categoryFolderPath);
            if (!categoryFolder.exists() || !categoryFolder.isDirectory()) {
                System.err.println("分类文件夹里没有文本分类语料");
                return null;
            } else {
                // 找到分类文件夹然后训练模型
                IClassifier classifier = new NaiveBayesClassifier();
                classifier.train(categoryFolderPath);
                NaiveBayesModel model = (NaiveBayesModel) classifier.getModel();
                IOUtil.saveObjectTo(model, modelPath);
                return model;
            }
        }
    }
}
