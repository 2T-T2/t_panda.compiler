package t_panda.compiler.internal;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

public class DefaultCompileErrorListener implements DiagnosticListener<JavaFileObject> {
    private final OutputStream errStrm;

    public DefaultCompileErrorListener(OutputStream errStrm) {
        this.errStrm = errStrm;
    }

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        PrintStream writer = new PrintStream(this.errStrm);
        writer.println(diagnostic.toString());
    }
    
}
