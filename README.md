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
package t_panda.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import t_panda.compiler.*;

public class App {
    public static void main(String[] args) {
        TPCompiler compiler = new TPCompiler();
        CompileTask task = compiler.createTask();
        final String pkgName    = "pkg.dynamic";
        final String fqcnSample = pkgName + ".Sample";
        final String srcSample  = new StringBuilder()
            .append("package ").append(pkgName).append(";").append("\n")
            .append("import ").append("java.util.Comparator;").append("\n")
            .append("public class Sample implements Comparator<String> {").append("\n")
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
            System.out.println(result.getOutMessage());
            System.err.println(result.getErrMessage());
            return;
        }

        try {
            // コンパイル結果のクラスを取得
            Class<Comparator<String>> comparator = result.getCompiledClassAs(fqcnSample);
            List<String> strList = new ArrayList<String>();
            strList.add("bbb");
            strList.add("aaa");
            strList.add("ccc");
            strList.sort(comparator.getConstructor().newInstance());
            System.out.println(strList.toString());

        } catch (InstantiationException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } 
    }
}
```
