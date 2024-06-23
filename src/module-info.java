/**
 * Javaコンパイラを操作する
 * <pre>
 * <code>
 * TPCompiler compiler = new TPCompiler(ToolProvider.getSystemJavaCompiler());
 * CompileTask task = compiler.createTask();
 * final String pkgName    = "pkg.dynamic";
 * final String fqcnSample = pkgName + ".Sample";
 * final String srcSample  = new StringBuilder()
 *     .append("package ").append(pkgName).append(";").append("\n")
 *     .append("public class Sample implements Comparator {").append("\n")
 *     .append("    @Override").append("\n")
 *     .append("    public int compare(String str1, String str2){").append("\n")
 *     .append("        return str1.compareTo(str2);").append("\n")
 *     .append("    }").append("\n")
 *     .append("}").append("\n")
 *     .append("").append("\n")
 * .toString();
 * CompileResult result = task.addCompileTarget(fqcnSample , srcSample)  // ソース指定
 *     .addOption("-p", "lib")                    // モジュールパス指定
 *     .run();
 * if( !result.isSuccess() ) {
 *     throw Exception("コンパイルエラー\n" + result.getOutMessage() + "\n" + result.getErrMessage() );
 * }
 * Comparator comparator = result.getCompiledClassAs(fqcnSample);
 * List&lt;String&gt; strList = new ArrayList&lt;String&gt;();
 * strList.add("bbb");
 * strList.add("aaa");
 * strList.add("ccc");
 * strList.sort(comparator);
 * </code>
 * </pre>
 */
module t_panda.compiler {
    requires transitive java.compiler;
    opens t_panda.compiler;
    exports t_panda.compiler;
}
