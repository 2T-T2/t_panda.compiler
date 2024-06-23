package t_panda.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * コンパイラ
 */
public class TPCompiler {
    private final JavaCompiler compiler;

    /**
     * 指定されたコンパイラで初期化
     * @param compiler コンパイラ
     */
    public TPCompiler(JavaCompiler compiler) {
        this.compiler = compiler;
    }
    /**
     * システムコンパイラ({@link ToolProvider#getSystemJavaCompiler()})で初期化
     * @see ToolProvider#getSystemJavaCompiler()
     */
    public TPCompiler() {
        this(ToolProvider.getSystemJavaCompiler());
    }

    /**
     * Returns the source versions of the Java programming language supported by this tool.
     * @see JavaCompiler#getSourceVersions()
     * @return a set of supported source versions
     */
    public Set<SourceVersion> getSourceVersions() {
        return this.compiler.getSourceVersions();
    }

    /**
     * コンパイラのバージョンを取得します。
     * @return コンパイラのバージョン
     */
    public Optional<String> getVersion() {
        String verStr;
        try (
            ByteArrayOutputStream bArrOutStrm = new ByteArrayOutputStream();
            ByteArrayInputStream bArrInStrm = new ByteArrayInputStream(new byte[] {});
        ) {
            this.compiler.run(bArrInStrm, bArrOutStrm, bArrOutStrm, "-version");
            verStr = new String(bArrOutStrm.toByteArray());
        } catch (Exception e) {
            verStr = null;
        }
        return Optional.ofNullable(verStr);
    }

    /**
     * コンパイルタスクを生成します
     * @return 生成されたコンパイルタスク
     */
    public CompileTask createTask() {
        return new CompileTask(this.compiler);
    }
}
