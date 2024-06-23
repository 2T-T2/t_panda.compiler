package t_panda.compiler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;

import t_panda.compiler.internal.ClassFileManager;
import t_panda.compiler.internal.DefaultCompileErrorListener;
import t_panda.compiler.internal.SourceFileObject;

/**
 * コンパイルタスク
 */
public class CompileTask {
    private final Set<SourceFileObject> compileTargets;
    private final List<String> opts;
    private final JavaCompiler complier; 
    private final List<String> classses;

    CompileTask(JavaCompiler compiler) {
        this.complier = compiler;
        this.compileTargets = new HashSet<>();
        this.opts = new ArrayList<>();
        this.classses = new ArrayList<>();
    }

    /**
     * アノテーションクラスを名称で追加します
     * @param annotationClassName アノテーションクラスの名称
     * @return 自身
     */
    public CompileTask addAnnotation(String annotationClassName) {
        this.classses.add(annotationClassName);
        return this;
    }
    /**
     * コンパイル対象を追加します
     * @param fqcn パッケージ名を付けて指定したクラス名の書き方のことを、完全修飾クラス名（Fully Qualified Class Name：FQCN）あるいは完全限定名（fully qualified name：FQN）
     * @param code コンパイル対象コード
     * @return 自身
     */
    public CompileTask addCompileTarget(String fqcn, String code) {
        this.compileTargets.add(new SourceFileObject(fqcn, code));
        return this;
    }
    /**
     * コンパイル対象を追加します。クラスfqcnはコードから自動判別します。
     * @param code コンパイル対象コード
     * @return 自身
     */
    public CompileTask addCompileTarget(String code) {
        Pattern clsPattern = Pattern.compile("class (.+)[ \n{]*");
        Matcher clsMatcher = clsPattern.matcher(code);
        if(!clsMatcher.find())
            new IllegalArgumentException("指定されたコードからクラスを検出できませんでした。");

        Pattern pkgPattern = Pattern.compile("^package (.+);");
        Matcher pkgMatcher = pkgPattern.matcher(code);

        StringBuilder fqcn = new StringBuilder();
        if(pkgMatcher.find()) {
            fqcn.append(pkgMatcher.group(1));
            fqcn.append('.');
        }
        fqcn.append(clsMatcher.group(1).split(" ")[0].replace("{", "").trim());

        return addCompileTarget(fqcn.toString(), code);
    }

    /**
     * 指定されたオプションを追加します
     * @param opt 追加するオプション
     * @return 自身
     */
    public CompileTask addOption(String opt) {
        int argNum = this.complier.isSupportedOption(opt);
        if(argNum == -1)
            new IllegalArgumentException("指定されたオプションはサポートされていません。");
        if(argNum != 0)
            new IllegalArgumentException("指定されたオプションは引数が必要です。");

        this.opts.add(opt);
        return this;
    }

    /**
     * 指定されたオプションを追加します
     * @param optName 追加するオプション名
     * @param optArg 追加するオプション引数
     * @return 自身
     */
    public CompileTask addOption(String optName, String optArg) {
        int argNum = this.complier.isSupportedOption(optName);
        if(argNum == -1)
            new IllegalArgumentException("指定されたオプションはサポートされていません。");
        if(argNum == 0)
            new IllegalArgumentException("指定されたオプションは引数を指定できません。");

        this.opts.add(optName);
        this.opts.add(optArg);
        return this;
    }

    /**
     * コンパイルを実行します。
     * @return コンパイルの結果
     */
    public CompileResult run() {
        CompileResult result = new CompileResult();
        DefaultCompileErrorListener listener = new DefaultCompileErrorListener(result.getErr());
        ClassFileManager classFileManager = new ClassFileManager(this.complier, listener);
        result.setSuccess(
            complier.getTask(
                new PrintWriter(result.getOut()),
                classFileManager,
                listener,
                opts,
                classses,
                compileTargets
            ).call()
        );
        result.setClassLoader(classFileManager.getClassLoader(null));
        return result;
    }

}
