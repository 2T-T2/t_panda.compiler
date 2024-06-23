# t_panda.compiler
javax.tools.JavaCompilerのラッパー。動的コンパイルに使います。

# ビルド
## 必要フォルダ
out → classファイルの出力先<br>
dst → jarファイルの出力先<br>
## コマンド
```bat
build.bat
```

# 使い方(サンプル)
```java
 TPCompiler compiler = new TPCompiler(ToolProvider.getSystemJavaCompiler());
 CompileTask task = compiler.createTask();
 final String pkgName    = "pkg.dynamic";
 final String fqcnSample = pkgName + ".Sample";
 final String srcSample  = new StringBuilder()
     .append("package ").append(pkgName).append(";").append("\n")
     .append("public class Sample implements Comparator {").append("\n")
     .append("    @Override").append("\n")
     .append("    public int compare(String str1, String str2){").append("\n")
     .append("        return str1.compareTo(str2);").append("\n")
     .append("    }").append("\n")
     .append("}").append("\n")
     .append("").append("\n")
 .toString();
 CompileResult result = task.addCompileTarget(fqcnSample , srcSample)  // ソース指定
     .addOption("-p", "lib")                    // モジュールパス指定
     .run();
 if( !result.isSuccess() ) {
     throw Exception("コンパイルエラー\n" + result.getOutMessage() + "\n" + result.getErrMessage() );
 }
 // コンパイル結果のクラスを取得
 Comparator comparator = result.getCompiledClassAs(fqcnSample);
 List<String> strList = new ArrayList<String>();
 strList.add("bbb");
 strList.add("aaa");
 strList.add("ccc");
 strList.sort(comparator);
```
